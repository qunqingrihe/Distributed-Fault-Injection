package com.example.distributedfaultinjection.controller;

import com.example.distributedfaultinjection.service.FaultInjectionPackageService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("")
public class FaultInjectionPackageController {

    private final FaultInjectionPackageService faultInjectionPackageService;

    public FaultInjectionPackageController(FaultInjectionPackageService faultInjectionPackageService) {
        this.faultInjectionPackageService = faultInjectionPackageService;
    }

    @PostMapping("/start")
    public void startPackage() {
        faultInjectionPackageService.startPackage();
    }

    @PostMapping("/stop")
    public void stopPackage() {
        faultInjectionPackageService.stopPackage();
    }

    @PutMapping("/config")
    public void updateConfig(@RequestBody String config) {
        faultInjectionPackageService.updateConfig(config);
    }
}
