package com.example.faultinjectionmanager.entity;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;

//使用lombok的Getter & Setter
@Setter
@Getter
@Document(indexName = "fault_injection_log")
public class  FaultInjectLog {
    @Id
    private String id;

    private String faultType; //故障类型
    private String targetNode; //故障节点
    private String description; //故障描述
    private String faultParameters; //故障参数
    private String status; //故障注入的状态
    private String startTime; //故障开始时间
    private String endTime; //故障结束时间

}
