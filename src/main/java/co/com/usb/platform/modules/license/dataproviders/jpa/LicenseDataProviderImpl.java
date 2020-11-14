
package co.com.usb.platform.modules.license.dataproviders.jpa;

import co.com.usb.platform.crosscutting.persistence.entity.License;
import co.com.usb.platform.crosscutting.persistence.repository.LicenseRepository;
import co.com.usb.platform.crosscutting.utils.CheckUtil;
import co.com.usb.platform.modules.license.dataproviders.LicenseDataProvider;
import com.mongodb.MongoException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.mongodb.UncategorizedMongoDbException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;

/**
 * LicenseDataProviderImpl
 *
 * @author USB
 * @version 1.0
 * @since 2020-11-14
 */
@Log4j2
@Service
@AllArgsConstructor
public class LicenseDataProviderImpl implements LicenseDataProvider {

    private LicenseRepository repository;

    private Validator validator;

    @Override
    public License getLicense(final int serialCode) {
        try {

            License license = repository.findBySerialNumber(serialCode);
            return license;

        } catch (UncategorizedMongoDbException | MongoException ex) {
            log.error("error searching order : {}", ex);
            return null;
        }
    }


    @Override
    @Transactional
    public License save(final License license) throws Exception {
        try {

            if (!CheckUtil.checkNull(license)) {
                throw new Exception("The license is null");
            }

            return repository.save(license);

        } catch (UncategorizedMongoDbException | MongoException ex) {
            log.error("error searching order : {}", ex);
            return null;
        }
    }

    @Override
    public List<License> findAll() {
        try {

            List<License> licenses = repository.findAll();
            return licenses;

        } catch (UncategorizedMongoDbException | MongoException ex) {
            log.error("error searching license : {}", ex);
            return null;
        }
    }

    @Override
    @Transactional()
    public void delete(final int serialNumber) {
        try {
            License license = repository.findBySerialNumber(serialNumber);
            repository.deleteById(license.getId());
        } catch (UncategorizedMongoDbException | MongoException ex) {
            log.error("error deleting license : {}", ex);
        }
    }


    @Override
    public void validate(final License license) throws ConstraintViolationException {

        Set<ConstraintViolation<License>> constraintViolations = validator.validate(license);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }

    }
}
