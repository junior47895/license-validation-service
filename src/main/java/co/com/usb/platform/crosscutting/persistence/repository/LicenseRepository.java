package co.com.usb.platform.crosscutting.persistence.repository;

import co.com.usb.platform.crosscutting.persistence.entity.License;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * LicenseRepository
 *
 * @author USB
 * @version 1.0
 * @since 2020-11-14
 *
 */

@Repository
public interface LicenseRepository extends MongoRepository<License, String> {

  License findBySerialNumber(int serialNumber);

}

