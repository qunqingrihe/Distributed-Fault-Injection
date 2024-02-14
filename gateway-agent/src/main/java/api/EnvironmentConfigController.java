package api;

import DTO.TrafficRatioUpdateRequest;
import config.EnvironmentConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/config")
public class EnvironmentConfigController {

    private final EnvironmentConfig environmentConfig;

    @Autowired
    public EnvironmentConfigController(EnvironmentConfig environmentConfig) {
        this.environmentConfig = environmentConfig;
    }

    // 获取当前的分流比例
    @GetMapping("/divertPercentage")
    public ResponseEntity<Double> getDivertPercentage() {
        double currentPercentage = environmentConfig.getDivertPercentage();
        return ResponseEntity.ok(currentPercentage);
    }

    // 更新分流比例
    @PostMapping("/divertPercentage")
    public ResponseEntity<?> updateDivertPercentage(@RequestBody TrafficRatioUpdateRequest updateRequest) {
        double newPercentage = updateRequest.getRatio();

        // 验证新的百分比值是否有效
        if (newPercentage < 0 || newPercentage > 1) {
            return ResponseEntity.badRequest().body("Percentage must be between 0 and 1");
        }

        // 更新配置中的百分比值
        environmentConfig.setDivertPercentage(newPercentage);

        // 返回更新后的百分比值
        return ResponseEntity.ok("Divert percentage updated to " + newPercentage);
    }
}
