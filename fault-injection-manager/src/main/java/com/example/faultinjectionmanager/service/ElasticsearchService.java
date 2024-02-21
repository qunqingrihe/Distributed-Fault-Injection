package com.example.faultinjectionmanager.service;

import com.example.faultinjectionmanager.entity.FaultInjectLog;
import com.example.faultinjectionmanager.repository.FaultInjectionLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ElasticsearchService {
    @Autowired
    private FaultInjectionLogRepository faultInjectionLogRepository;

    private final DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

//    public void recordFaultInjection(String faultType,String targetNode,String description,String faultParameters,String startTime,String endTime){
//        FaultInjectLog log=new FaultInjectLog();
//        log.setFaultType(faultType);
//        log.setTargetNode(targetNode);
//        log.setDescription(description);
//        log.setFaultParameters(faultParameters);
//        log.setStartTime(startTime);
//        log.setEndTime(endTime);
//
//        faultInjectionLogRepository.save(log);
//    }

    //将故障注入日志记录到elasticsearch中
    public void recordFaultInjection(FaultInjectLog faultInjectLog){
        faultInjectLog.setStartTime(faultInjectLog.getStartTime().formatted(formatter));
        faultInjectLog.setEndTime(faultInjectLog.getEndTime().formatted(formatter));

        faultInjectionLogRepository.save(faultInjectLog);
    }
    public List<FaultInjectLog> findByFaultType(String faultType){
        //使用repository提供的方法获取数据
        return faultInjectionLogRepository.faultByFaultType(faultType);
    }
}
