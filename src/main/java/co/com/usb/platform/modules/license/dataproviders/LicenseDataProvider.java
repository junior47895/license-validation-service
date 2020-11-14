package co.com.usb.platform.modules.license.dataproviders;

import co.com.usb.platform.crosscutting.persistence.entity.License;

import java.util.List;

/**
 * LicenseDataProvider
 *
 * @author USB
 * @version 1.0
 * @since 2020-11-14
 *
 */

public interface LicenseDataProvider {

  License getLicense(int serialCode);

  License save(License license) throws Exception;

  void delete(int serialNumber) throws Exception;

  List<License> findAll() throws Exception;

  void validate(License license) throws Exception;
}
