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
public class EnvironmentConfigEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int divertPercentage; //分流比例

    public EnvironmentConfigEntity() {
    }

    public EnvironmentConfigEntity(Long id, int divertPercentage) {
        this.id = id;
        this.divertPercentage = divertPercentage;
    }
}
