package co.com.usb.platform.crosscutting.persistence.model;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * LicenseDTO
 *
 * @author USB
 * @version 1.0
 * @since 2020-11-14
 *
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LicenseDTO implements Serializable {

    @NotNull
    private Integer serialNumber;
    private Date creationDate;
    @NotNull
    private Integer expirationDays;
    @NotEmpty
    private String company;
    @NotEmpty
    private String softwareName;
    @NotEmpty
    private String licenseType;
    private boolean licenseStatus;
    private String publicKeyEncode;


}
