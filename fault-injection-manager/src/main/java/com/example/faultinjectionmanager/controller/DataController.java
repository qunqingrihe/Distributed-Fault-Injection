package com.example.faultinjectionmanager.controller;

import com.example.faultinjectionmanager.other.DataObject;
import com.example.faultinjectionmanager.service.DataProcessingService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataController {
    private final DataProcessingService dataProcessingService;

    public DataController(DataProcessingService dataProcessingService) {
        this.dataProcessingService = dataProcessingService;
    }

    @PostMapping("/api/data")
    public void receiveData(@RequestBody DataObject dataObject){
        //接收并处理来自前端的数据接口
        dataProcessingService.processData(dataObject);
    }
}
