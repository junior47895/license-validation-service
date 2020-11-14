package co.com.usb.platform.modules.license.api;

import co.com.usb.platform.crosscutting.persistence.model.LicenseDTO;
import co.com.usb.platform.modules.license.usecase.ProcessLicense;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * Exposes remote rest api for feeding orders Dashboard
 *
 * @author Luis Carlos Cabal
 * @version 1.0
 * @since 2020-04-13
 */

@Log4j2
@RestController
@AllArgsConstructor
@RequestMapping("/v1")
@CrossOrigin("*")
public class LicenseWebApi {

    private ProcessLicense processLicense;

    @PostMapping("/validationLicense/{serialNumber}")
    public ResponseEntity<?> validationWithCertificateKey(
            final @PathVariable("serialNumber") int serialNumber,
            final @RequestParam("certificateKey") MultipartFile certificateKey) {

        Map<String, Object> response = new HashMap<>();

        try {
            log.info("Initialized license validation");
            return ResponseEntity.ok(processLicense.licenseValidation(
                    serialNumber, certificateKey));
        } catch (Exception e) {
            response.put("message", "Error when validating license: ".concat(e.getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{serialNumber}")
    public ResponseEntity<?> findBySerialNumber(
            final @PathVariable("serialNumber") int serialNumber) {

        Map<String, Object> response = new HashMap<>();

        try {
            return ResponseEntity.ok(processLicense.findBySerialNumber(serialNumber));
        } catch (Exception e) {
            response.put("message", "Error when validating license: "
                    .concat(e.getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping
    public ResponseEntity<?> save(final @Valid @RequestBody LicenseDTO licenseDTO)
            throws Exception {

        return new ResponseEntity<>(processLicense
                .saveLicenseWithCertificate(licenseDTO),
                HttpStatus.CREATED);
    }

    @PutMapping("/{serialNumber}")
    public ResponseEntity<?> update(final @Valid @RequestBody LicenseDTO licenseDTO,
                                    final @PathVariable int serialNumber) throws Exception {

        return new ResponseEntity<>(processLicense
                .updateLicenseBasicData(licenseDTO, serialNumber),
                HttpStatus.CREATED);
    }


    @GetMapping()
    public ResponseEntity<?> findAll() throws Exception {
        return ResponseEntity.ok().body(processLicense.findAllLicenses());
    }

    @DeleteMapping("/{serialNumber}")
    public ResponseEntity<?> delete(final @PathVariable("serialNumber")
                                            int serialNumber) throws Exception {
        processLicense.deleteLicense(serialNumber);
        return ResponseEntity.status(HttpStatus.OK).body("");
    }
}
