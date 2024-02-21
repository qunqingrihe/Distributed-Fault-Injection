package com.example.faultinjectionmanager.job;

import com.example.faultinjectionmanager.service.FaultInjectionService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FaultInjectionJob implements Job {
    @Autowired
    private FaultInjectionService faultInjectionService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String faultType=context.getJobDetail().getJobDataMap().getString("faultType");
        //根据故障注入类型执行相应的故障注入逻辑
        switch (faultType){
            case "flow_injection":
                faultInjectionService.injectFlow_injection();
                break;
            case "start_stop_test":
                faultInjectionService.injectStart_stop_test();
                break;
            case "parameter_modification":
                faultInjectionService.injectParameter_modification();
                break;
            default:
                //其他处理或错误处理
        }
    }
}
