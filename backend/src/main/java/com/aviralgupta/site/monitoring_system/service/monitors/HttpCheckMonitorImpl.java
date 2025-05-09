package com.aviralgupta.site.monitoring_system.service.monitors;


import com.aviralgupta.site.monitoring_system.util.enums.MonitorStatusEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class HttpCheckMonitorImpl extends AbstractMonitor {

    @Setter
    @Getter
    private boolean isHttps;
    private final RestTemplate restTemplate;
    private final int connectTimeout = 5000; // 5 seconds

    public HttpCheckMonitorImpl(Integer userId, String serverAddress) {
        super(userId, serverAddress);
        this.restTemplate = new RestTemplate();
    }

    public HttpCheckMonitorImpl(String monitorId, Integer userId, String serverAddress) {
        super(monitorId, userId, serverAddress);
        this.restTemplate = new RestTemplate();
    }

    @Override
    public MonitorStatusEnum run() {

        String url = "";

        if(isHttps)
            url = "https://" + getServerAddress();
        else
            url = "http://" + getServerAddress();

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            if(response.getStatusCode().is2xxSuccessful())
                return MonitorStatusEnum.HEALTHY;

            return MonitorStatusEnum.CRITICAL;
        } catch (Exception e) {
            return MonitorStatusEnum.CRITICAL;
        }
    }
}
