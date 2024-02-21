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
public class FaultInjectionConfigEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean enabled; //表示故障注入是否启用
    private int injectionPercentage; //故障注入百分比

    public FaultInjectionConfigEntity() {
    }

    public FaultInjectionConfigEntity(Long id, boolean enabled, int injectionPercentage) {
        this.id = id;
        this.enabled = enabled;
        this.injectionPercentage = injectionPercentage;
    }

}
