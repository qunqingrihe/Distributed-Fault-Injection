package monitoring;

import config.EnvironmentConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import DTO.StatusData;

@Component
public class MonitoringTask {
    private final WebClient webClient;
    private final EnvironmentConfig environmentConfig;

    @Autowired
    public MonitoringTask(EnvironmentConfig environmentConfig, WebClient.Builder webClientBuilder) {
        this.environmentConfig = environmentConfig;
        this.webClient = webClientBuilder.baseUrl(environmentConfig.getManagementSystemEndpoint()).build();
    }

    @Scheduled(fixedRateString = "${monitoring.reportRate}") // 使用配置文件中的值
    public void reportData() {
        StatusData statusData = collectStatusData();

        webClient.post()
                .uri("/report") // 假设管理系统的上报API路径是 "/report"
                .bodyValue(statusData)
                .retrieve()
                .bodyToMono(Void.class)
                .subscribe(); // 订阅结果，非阻塞执行
    }

    private StatusData collectStatusData() {
        // 实现数据收集逻辑
        return new StatusData(
                environmentConfig.getName(),
                environmentConfig.getProxyServerAddress(),
                environmentConfig.getProxyServerPort(),
                environmentConfig.isFaultInjectorEnabled(),
                environmentConfig.getDivertPercentage()
        );
    }
}
