package com.example.faultinjectionmanager.scheduled;

import com.example.faultinjectionmanager.service.FaultInjectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

//定期执行故障注入任务
@Component
public class ScheduleFaultInjection {
    @Autowired
    private FaultInjectionService faultInjectionService;

    //定义三个定期执行的故障注入任务，每隔10秒尝试执行一次
    @Scheduled(fixedDelay = 10000)
    public void scheduleFlowInjection(){
        faultInjectionService.injectFlow_injection();
    }
    @Scheduled(fixedDelay = 10000)
    public void scheduleStartStopTest(){
        faultInjectionService.injectStart_stop_test();
    }
    @Scheduled(fixedDelay = 10000)
    public void scheduleParameterModification(){
        faultInjectionService.injectParameter_modification();
    }
}
