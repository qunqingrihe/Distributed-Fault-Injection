package monitoring;

import config.EnvironmentConfig;
import DTO.StatusData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class EnvironmentStatusReporter {
    private final EnvironmentConfig environmentConfig;
    private final WebClient webClient;

    @Autowired
    public EnvironmentStatusReporter(EnvironmentConfig environmentConfig, WebClient.Builder webClientBuilder) {
        this.environmentConfig = environmentConfig;
        this.webClient = webClientBuilder.baseUrl("http://monitoring-system-endpoint.com").build();
    }

    @Scheduled(fixedRate = 60000) // 每60秒执行一次
    public void reportStatus() {
        StatusData statusData = new StatusData(
                environmentConfig.getName(),
                environmentConfig.getProxyServerAddress(),
                environmentConfig.getProxyServerPort(),
                environmentConfig.isFaultInjectorEnabled(),
                environmentConfig.getDivertPercentage()
        );

        // 使用WebClient上报statusData到监控系统
        webClient.post()
                .uri("/api/report") // 监控系统的上报API
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(statusData)
                .retrieve() // 发起请求并检索响应
                .bodyToMono(Void.class) // 将响应体转换为Mono<Void>
                .subscribe(
                        result -> System.out.println("Status reported successfully"),
                        error -> System.err.println("Error reporting status: " + error.getMessage())
                );
    }
}
