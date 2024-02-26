package com.example.faultinjectionmanager.controller;

import com.example.faultinjectionmanager.entity.FaultInjectLog;
import com.example.faultinjectionmanager.service.ElasticsearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/elasticsearch")
public class ElasticsearchController {
    @Autowired
    private ElasticsearchService elasticsearchService;

    public ElasticsearchController(ElasticsearchService elasticsearchService){
        this.elasticsearchService=elasticsearchService;
    }

    @PostMapping("/record")
    public ResponseEntity<String> recordFaultInjection(@RequestBody FaultInjectLog faultInjectLog){
       //记录故障注入日志到Elasticsearch的接口
        elasticsearchService.recordFaultInjection(faultInjectLog);
        return ResponseEntity.ok("Fault injection log recorded successfully");
    }

    @GetMapping("/logs")
    public ResponseEntity<List<FaultInjectLog>> getLogsByFaultType(@RequestParam String faultType){

        List<FaultInjectLog> logs=elasticsearchService.findByFaultType(faultType);
        return ResponseEntity.ok(logs);
    }
}
