package com.example.faultinjectionmanager.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class FaultInjectLogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String status; //故障注入的状态
    private long responseTime; //响应时间
    private double throughPut; //吞吐量

    public FaultInjectLogEntity() {
    }

    public FaultInjectLogEntity(String status, long responseTime, double throughPut) {
        this.status = status;
        this.responseTime = responseTime;
        this.throughPut = throughPut;
    }

}
