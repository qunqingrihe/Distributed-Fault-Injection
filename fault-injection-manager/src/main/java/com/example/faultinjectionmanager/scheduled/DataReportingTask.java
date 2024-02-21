package com.example.faultinjectionmanager.scheduled;

import com.example.faultinjectionmanager.other.DataObject;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class DataReportingTask {
    private final RestTemplate restTemplate=new RestTemplate();
    private final String dataEndpointUrl=""; //定义数据上报的终端URL

    @Scheduled(fixedDelay = 10000)
    public void reportData(){
        //构造数据对象
        DataObject dataObject = new DataObject();
        //设置状态和性能数据
        dataObject.setStatus("OK");
        DataObject.PerformanceData performance=new DataObject.PerformanceData();
        performance.setResponseTime(100);
        performance.setThroughput(50.5);
        dataObject.setPerformance(performance);

        //发送数据到web管理系统
        restTemplate.postForObject(dataEndpointUrl,dataObject,String.class);
    }
}
