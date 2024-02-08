package api;

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
    public double getDivertPercentage() {
        return environmentConfig.getDivertPercentage();
    }

    // 更新分流比例
    @PostMapping("/divertPercentage")
    public ResponseEntity<?> updateDivertPercentage(@RequestBody double newPercentage) {
        if (newPercentage < 0 || newPercentage > 1) {
            return ResponseEntity.badRequest().body("Percentage must be between 0 and 1");
        }
        environmentConfig.setDivertPercentage(newPercentage);
        return ResponseEntity.ok("Divert percentage updated to " + newPercentage);
    }
}
