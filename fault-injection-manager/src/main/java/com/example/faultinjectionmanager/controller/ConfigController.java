package com.example.faultinjectionmanager.controller;

import com.example.faultinjectionmanager.entity.FaultInjectionConfigEntity;
import com.example.faultinjectionmanager.service.EnvironmentConfigService;
import com.example.faultinjectionmanager.service.FaultInjectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/config")
public class  ConfigController {
    @Autowired
    private EnvironmentConfigService environmentConfigService;
    @Autowired
    private FaultInjectionService faultInjectionService;

    @PutMapping("/divertPercentage")
    public ResponseEntity<String> updateDivertPercentage(@RequestParam int divertPercentage){
        //更新流量分流百分比的接口
        environmentConfigService.updateDivertPercentage(divertPercentage);
        return ResponseEntity.ok("Divert percentage update successfully");
    }

    @PutMapping("/faultInjection")
    //                                                              此处是模拟故障注入配置类
    public ResponseEntity<String> updateFaultInjection(FaultInjectionConfigEntity faultInjectionConfig){
        //更新故障注入设置的接口
        faultInjectionService.updateFaultInjection(faultInjectionConfig);
        return ResponseEntity.ok("Fault injection settings updated successfully");
    }
}
