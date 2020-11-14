package co.com.usb.platform.infrastructure.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * UsbApiApplication
 *
 * @author USB
 * @version 1.0
 * @since 2020-11-14
 *
 */

@SpringBootApplication(scanBasePackages = {
    "co.com.usb.platform.infrastructure",
    "co.com.usb.platform.modules",
    "co.com.usb.platform.crosscutting"})
@EnableMongoRepositories(basePackages = "co.com.usb.platform.crosscutting.persistence.repository")
@EntityScan(basePackages = "co.com.usb.platform.crosscutting.persistence.entity")
public class UsbApiApplication {

  public static void main(final String[] args) {
    SpringApplication.run(UsbApiApplication.class, args);
  }

}
