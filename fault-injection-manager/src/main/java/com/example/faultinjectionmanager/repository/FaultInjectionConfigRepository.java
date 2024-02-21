package com.example.faultinjectionmanager.repository;

import com.example.faultinjectionmanager.entity.FaultInjectionConfigEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface FaultInjectionConfigRepository extends ElasticsearchRepository<FaultInjectionConfigEntity,Long> {
    //提供对FaultInjectionConfigEntity实体进行操作的接口 定义特定的查询方法

    //根据故障注入是否启用来查询配置
    List<FaultInjectionConfigEntity> findByEnabled(boolean enabled);
    //根据故障注入百分比查询配置
    List<FaultInjectionConfigEntity> findByInjectionPercentage(int injectionPercentage);
    //查找故障注入宝粉笔在指定范围内的配置
    List<FaultInjectionConfigEntity> findByInjectionPercentageBetween(int startPercentage,int endPercentage);
}
