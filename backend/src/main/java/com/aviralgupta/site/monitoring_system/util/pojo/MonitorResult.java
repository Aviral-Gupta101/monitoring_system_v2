package com.aviralgupta.site.monitoring_system.util.pojo;

import com.aviralgupta.site.monitoring_system.util.enums.MonitorStatusEnum;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NonNull;

@Data
public class MonitorResult {

    @NonNull
    @NotBlank
    private String monitorId;

    @NonNull
    @NotBlank
    private Integer userId;

    @NonNull
    @NotBlank
    private String serverAddress;

    @NonNull
    private MonitorStatusEnum status;

}
