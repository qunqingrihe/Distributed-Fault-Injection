package com.example.faultinjectionmanager.scheduled;

import com.example.faultinjectionmanager.other.DataObject;
import com.example.faultinjectionmanager.service.DataUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DataUploadTask {
    @Autowired
    private DataUploadService dataUploadService;

    @Scheduled(fixedRate = 60000)
    public void uploadData(){
        //定期上传数据的任务，每隔60秒执行1次
        DataObject dataObject=createOrGetDataObject();
        dataUploadService.uploadData(dataObject);
    }

    private DataObject createOrGetDataObject(){
        DataObject dataObject=new DataObject();
        dataObject.setStatus("正常");

        DataObject.PerformanceData performanceData=new DataObject.PerformanceData();
        performanceData.setResponseTime(100);
        performanceData.setThroughput(50.0);

        dataObject.setPerformance(performanceData);
        return dataObject;
    }
}
