package co.com.usb.platform.modules.license.usecase;


import co.com.usb.platform.crosscutting.persistence.entity.License;
import co.com.usb.platform.crosscutting.persistence.model.LicenseDTO;
import co.com.usb.platform.crosscutting.utils.CheckUtil;
import co.com.usb.platform.crosscutting.utils.UUIDUtils;
import co.com.usb.platform.modules.license.dataproviders.LicenseDataProvider;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static co.com.usb.platform.crosscutting.constants.Constants.*;
import static co.com.usb.platform.crosscutting.messages.LicenseMessages.*;

/**
 * ProcessLicense
 *
 * @author USB
 * @version 1.0
 * @since 2020-11-14
 */

@Log4j2
@Component
@AllArgsConstructor
public class ProcessLicense {

    private LicenseDataProvider licenseDataProvider;
    private ProcessKeyCertificate processKeyCertificate;
    private ProcessCertificateLicense processCertificateLicense;

    public LicenseDTO licenseValidation(final int serialNumber,
                                        final MultipartFile certificateKey)
            throws Exception {

        License license = licenseDataProvider.getLicense(serialNumber);
        if (!CheckUtil.checkNull(license)) {
            throw new Exception(LICENCE_NOT_FOUND);
        }
        if (!license.isLicenseStatus()) {
            throw new Exception(LICENSE_STATUS_FALSE);
        }

        String keyEncode = extractMultipartContent(certificateKey);
        processCertificateLicense.verifyCertificate(license, keyEncode);
        return licenseDTOConverter(license);

    }

    public LicenseDTO findBySerialNumber(final int serialNumber)
            throws Exception {

        LicenseDTO licenseDTO = licenseDTOConverter(
                licenseDataProvider.getLicense(serialNumber));
        if (!CheckUtil.checkNull(licenseDTO)) {
            throw new Exception(LICENCE_NOT_FOUND);
        }
        return licenseDTO;

    }

    public String extractMultipartContent(final MultipartFile multipartFile) throws IOException {
        BufferedReader br;
        String result = "";
        String line;
        InputStream is = multipartFile.getInputStream();
        br = new BufferedReader(new InputStreamReader(is));
        while ((line = br.readLine()) != null) {
            if (!line.contains(HEADER_PUBLIC_KEY)
                    && !line.contains(FOOTER_PUBLIC_KEY)) {
                result += line + "\n";
            }
        }
        log.info("Key encode: \n {}", result);
        return result;
    }


    public LicenseDTO saveLicenseWithCertificate(final LicenseDTO licenseDTO) throws Exception {
        Date creationDate = new Date();
        KeyPair keyPair = processKeyCertificate.generateKeyPair();
        String keyEncoded = processKeyCertificate.encodePublicKey(keyPair);
        String keyEncodeFile = processKeyCertificate.generatePublicKeyFile(keyEncoded);
        License license = licenseConverter(licenseDTO);
        String distinguishedName = "CN=LICENSE, O=" + license.getCompany() + ", C=" + license.getSoftwareName();
        license.setId(UUIDUtils.randomUUID());
        license.setCreationDate(new Date());
        license.setCertificate(processCertificateLicense.generateCertificate(distinguishedName,
                keyPair,
                license.getExpirationDays(),
                ALGORITHM,
                license.getSerialNumber(),
                creationDate));
        licenseDataProvider.save(license);
        licenseDTO.setPublicKeyEncode(keyEncodeFile);
        return licenseDTO;
    }


    public LicenseDTO updateLicenseBasicData(final LicenseDTO licenseDTO,
                                             final int serialCode) throws Exception {

        log.info("Update basic data for id: \n {}", serialCode);
        License license = licenseDataProvider.getLicense(serialCode);

        if (!CheckUtil.checkNull(license)) {
            throw new Exception(LICENCE_NOT_FOUND);
        }

        license.setLicenseStatus(licenseDTO.isLicenseStatus());
        license.setLicenseType(licenseDTO.getLicenseType());
        log.info("license response {}", licenseDTO.toString());
        log.info("license modified {}", license.toString());
        licenseDataProvider.save(license);

        return licenseDTO;
    }

    public List<LicenseDTO> findAllLicenses() throws Exception {

        List<License> licenses = licenseDataProvider.findAll();

        if (!CheckUtil.checkNull(licenses)) {
            throw new Exception(LICENCES_NOT_FOUND);
        }
        List<LicenseDTO> licenseDTOs = new ArrayList<>();
        for (License item : licenses) {
            licenseDTOs.add(licenseDTOConverter(item));
        }
        return licenseDTOs;
    }


    public void deleteLicense(final int serialNumber) throws Exception {

        log.info("removing license for id: \n {}", serialNumber);
        licenseDataProvider.delete(serialNumber);

    }

    public License licenseConverter(final LicenseDTO licenseDTO) {
        License license = new License();
        return license.builder()
                .company(licenseDTO.getCompany())
                .creationDate(licenseDTO.getCreationDate())
                .expirationDays(licenseDTO.getExpirationDays())
                .softwareName(licenseDTO.getSoftwareName())
                .licenseType(licenseDTO.getLicenseType())
                .serialNumber(licenseDTO.getSerialNumber())
                .licenseStatus(licenseDTO.isLicenseStatus())
                .build();
    }

    public LicenseDTO licenseDTOConverter(final License license) {
        LicenseDTO licenseDTO = new LicenseDTO();
        return licenseDTO.builder()
                .company(license.getCompany())
                .creationDate(license.getCreationDate())
                .expirationDays(license.getExpirationDays())
                .softwareName(license.getSoftwareName())
                .licenseType(license.getLicenseType())
                .serialNumber(license.getSerialNumber())
                .licenseStatus(license.isLicenseStatus())
                .build();
    }

}
