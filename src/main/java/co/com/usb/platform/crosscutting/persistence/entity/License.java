
package co.com.usb.platform.crosscutting.persistence.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;

/**
 * License
 *
 * @author USB
 * @version 1.0
 * @since 2020-11-14
 *
 */
@Document(collection = "licenses")
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class License implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @Field("_id")
  @JsonProperty("_id")
  private String id;
  @Field("serial_number")
  @JsonProperty("serial_number")
  private Integer serialNumber;
  @Field("certificate")
  @JsonProperty("certificate")
  private String certificate;
  @Field("creation_date")
  @JsonProperty("creation_date")
  private Date creationDate;
  @Field("expiration_date")
  @JsonProperty("expiration_days")
  private Integer expirationDays;
  @Field("company")
  @JsonProperty("company")
  @NotEmpty
  private String company;
  @Field("software_name")
  @JsonProperty("software_name")
  @NotEmpty
  private String softwareName;
  @Field("license_type")
  @JsonProperty("license_type")
  @NotEmpty
  private String licenseType;
  @Field("license_status")
  @JsonProperty("license_status")
  private boolean licenseStatus;

}
