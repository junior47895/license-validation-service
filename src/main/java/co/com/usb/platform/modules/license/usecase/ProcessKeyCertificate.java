package co.com.usb.platform.modules.license.usecase;

import co.com.usb.platform.crosscutting.utils.UUIDUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.camel.util.IOHelper;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static co.com.usb.platform.crosscutting.constants.Constants.*;

/**
 * ProcessKeyCertificate
 *
 * @author USB
 * @version 1.0
 * @since 2020-11-14
 *
 */
@Log4j2
@Component
public class ProcessKeyCertificate {



    public KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA_FILE);
        keyPairGenerator.initialize(FILE_SIZE);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return keyPair;
    }

    public String encodePublicKey(final KeyPair keyPair) {
        PublicKey pub = keyPair.getPublic();
        BASE64Encoder encoder = new BASE64Encoder();
        String keyEncoded = encoder.encode(pub.getEncoded());
        log.info("key encode generate");
        return keyEncoded;
    }

    public String generatePublicKeyFile(final String keyEncoded) throws IOException {
        log.info("Creating file with public key");
        StringBuilder keyBuilder = new StringBuilder();
        keyBuilder.append(HEADER_PUBLIC_KEY.concat("\n"));
        keyBuilder.append(keyEncoded);
        keyBuilder.append("\n".concat(FOOTER_PUBLIC_KEY).concat("\n"));
        log.info("key {}", keyBuilder.toString());
        String keyFileName = UUIDUtils.randomUUID();
        byte[] encoded = Base64Utils.encode(compressPublicKeyFile(keyBuilder.toString(),
                keyFileName.concat(FILE_KEY_EXTENSION)));

        return new String(encoded);
    }


    private byte[] compressPublicKeyFile(final String keyString,
                                         final String keyFileName)
            throws IOException {
        log.info("Compressing file with public key");
        ByteArrayInputStream byteArrayInputStream =
                new ByteArrayInputStream((keyString.getBytes()));
        ByteArrayOutputStream byteArrayOutputStream =
                new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(byteArrayOutputStream);
        zos.putNextEntry(new ZipEntry(keyFileName));
        IOHelper.copy(byteArrayInputStream, zos);
        IOHelper.close(byteArrayInputStream, zos);
        return byteArrayOutputStream.toByteArray();
    }
}
