package com.example.faultinjectionmanager.controller;

import com.example.faultinjectionmanager.service.FaultInjectionService;
import core.FaultInjector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//此类调用故障注入类的方法
@RestController
@RequestMapping("/api/fault")
public class FaultInjectionController {
//    @Autowired
//    private FaultInjectionService faultInjectionService;
    private FaultInjector faultInjector;

    //private FaultInjector faultInjector;

    @GetMapping("/management/status")
    public String getSystemStatus(){
        //获取系统状态的逻辑
        return "System Status: OK";
    }

//    @GetMapping("/inject-flow-injection")
    @GetMapping("/inject/flow")
    public String injectFlowInjection(){
        //模拟流量注入故障的逻辑
//        faultInjectionService.injectFlow_injection();
        //触发流量注入故障的接口
        faultInjector.injectTrafficFault();
        return "Flow Injection fault injected successfully";
    }

//    @GetMapping("/inject-start-stop-test")
    @GetMapping("inject/start")
    public String injectStartStopTest(){
        //模拟启停测试故障注入的逻辑
//        faultInjectionService.injectStart_stop_test();
        //触发启停测试故障注入的接口
        faultInjector.injectKillFault();
        return "Start Stop Test fault injected successfully";
    }

//    @GetMapping("/inject-parameter-modification")
    @GetMapping("/inject/parameter")
    public String injectParameterModification(){
        //模拟参数错误故障注入的逻辑
//        faultInjectionService.injectParameter_modification();
        //触发参数修改故障注入的接口
        faultInjector.injectConfigurationFault("","");
        return "Parameter Modification fault injected successfully";
    }
}