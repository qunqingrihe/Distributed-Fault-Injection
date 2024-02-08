package api;
import config.EnvironmentConfig;
import core.FaultInjector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

    @RestController
    @RequestMapping("/api/fault-injection")
    public class FaultInjectionController {
        private final EnvironmentConfig environmentConfig;

        @Autowired
        public FaultInjectionController(EnvironmentConfig environmentConfig) {
            this.environmentConfig = environmentConfig;
        }

        // 获取当前的故障注入器配置
        @GetMapping
        public FaultInjector getFaultInjector() {
            return environmentConfig.getFaultInjector();
        }

        // 更新故障注入器配置
        @PostMapping
        public ResponseEntity<?> updateFaultInjector(@RequestBody FaultInjector newFaultInjector) {
            // 这里可以添加逻辑来验证newFaultInjector的属性
            // 例 可以检查故障类型和规则是否有效
            // 更新配置
            environmentConfig.setFaultInjector(newFaultInjector);
            return ResponseEntity.ok("Fault injector updated");
        }

    }
    //定义了两个端点：
//GET /api/fault-injection：获取当前的故障注入器配置。
//POST /api/fault-injection：更新故障注入器配置
//它接受一个新的FaultInjector对象，并更新EnvironmentConfig中的相应字段。
