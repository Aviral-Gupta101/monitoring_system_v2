package com.aviralgupta.site.monitoring_system.dto;

import com.aviralgupta.site.monitoring_system.util.enums.MonitorTypeEnum;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.validator.routines.DomainValidator;
import org.apache.commons.validator.routines.InetAddressValidator;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonitorDto {

    private String id;

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @NotBlank
    private String serverAddress;

    @NotNull
    @Min(value = 10)
    private Integer ScheduleInterval;

    private MonitorTypeEnum type;

    @Min(value = 1, message = "Min value of port is 1")
    @Max(value = 65535, message = "Min value of port is 65535")
    private Integer port;

    private Boolean https;

    @AssertTrue(message = "serverAddress must be a valid IPv4 address or domain name")
    public boolean isServerAddressValid() {
        if (serverAddress == null || serverAddress.isBlank()) return true; // let @NotBlank handle this

        InetAddressValidator ipValidator = InetAddressValidator.getInstance();
        DomainValidator domainValidator = DomainValidator.getInstance(true); // allow localhost, etc.

        return ipValidator.isValidInet4Address(serverAddress) || domainValidator.isValid(serverAddress);
    }

    @AssertTrue(message = "Port number is required")
    public boolean isPortRequired() {

        if (type == MonitorTypeEnum.PORT_CHECK) {
            return port != null;
        }
        return true;
    }

    @AssertTrue(message = "https field is required")
    public boolean isHttpsRequired() {

        if (type == MonitorTypeEnum.HTTP_CHECK) {
            return https != null;
        }
        return true;
    }

}
