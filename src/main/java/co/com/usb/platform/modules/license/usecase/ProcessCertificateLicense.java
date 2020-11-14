package co.com.usb.platform.modules.license.usecase;

import co.com.usb.platform.crosscutting.persistence.entity.License;
import lombok.extern.log4j.Log4j2;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Component;
import sun.security.x509.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;

import static co.com.usb.platform.crosscutting.constants.Constants.RSA_FILE;

/**
 * ProcessCertificateLicense
 *
 * @author USB
 * @version 1.0
 * @since 2020-11-14
 */

@Log4j2
@Component
public class ProcessCertificateLicense {

    private static final long DAY_CONVERT = 86400000L;

    public String generateCertificate(final String dn,
                                      final KeyPair pair,
                                      final int days,
                                      final String algorithm,
                                      final int serialNumber,
                                      final Date creationDate)
            throws GeneralSecurityException, IOException {
        PrivateKey privatekey = pair.getPrivate();
        X509CertInfo info = new X509CertInfo();
        Date from = creationDate;
        Date to = new Date(from.getTime() + days * DAY_CONVERT);
        CertificateValidity interval = new CertificateValidity(from, to);
        X500Name owner = new X500Name(dn);

        info.set(X509CertInfo.VALIDITY, interval);
        info.set(X509CertInfo.SERIAL_NUMBER, new CertificateSerialNumber(serialNumber));
        info.set(X509CertInfo.SUBJECT, owner);
        info.set(X509CertInfo.ISSUER, owner);
        info.set(X509CertInfo.KEY, new CertificateX509Key(pair.getPublic()));
        info.set(X509CertInfo.VERSION, new CertificateVersion(CertificateVersion.V3));
        AlgorithmId algorithmId = new AlgorithmId(AlgorithmId.md5WithRSAEncryption_oid);
        info.set(X509CertInfo.ALGORITHM_ID, new CertificateAlgorithmId(algorithmId));

        // Sign the cert to identify the algorithm that's used.
        X509CertImpl certificate = new X509CertImpl(info);
        certificate.sign(privatekey, algorithm);

        // Update the algorith, and resign.
        algorithmId = (AlgorithmId) certificate.get(X509CertImpl.SIG_ALG);
        info.set(CertificateAlgorithmId.NAME + "." + CertificateAlgorithmId.ALGORITHM, algorithmId);
        certificate = new X509CertImpl(info);
        certificate.sign(privatekey, algorithm);
        String certificateEncode = new String(Base64.encodeBase64(certificate.getEncoded()));
        return certificateEncode;
    }

    public X509Certificate decodeCertificate(final License license)
            throws CertificateException {
        byte encodedCert[] = Base64.decodeBase64(license.getCertificate());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(encodedCert);
        CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
        X509Certificate cert = (X509Certificate) certFactory.generateCertificate(inputStream);
        return cert;
    }


    public void verifyCertificate(final License license, final String keyEncode)
            throws CertificateException,
            NoSuchAlgorithmException,
            InvalidKeyException,
            NoSuchProviderException,
            SignatureException,
            InvalidKeySpecException {

        X509Certificate certificate = decodeCertificate(license);

        byte[] byteKey = Base64.decodeBase64(keyEncode.getBytes());
        X509EncodedKeySpec x509publicKey = new X509EncodedKeySpec(byteKey);
        KeyFactory kf = KeyFactory.getInstance(RSA_FILE);

        PublicKey publicKey = kf.generatePublic(x509publicKey);
        certificate.checkValidity();
        certificate.verify(publicKey);
        log.info("Public key {}", publicKey);
        log.info("Certificate name {}", certificate.getSubjectDN().getName());
        log.info("Serial number {}", certificate.getSerialNumber());
    }
}
