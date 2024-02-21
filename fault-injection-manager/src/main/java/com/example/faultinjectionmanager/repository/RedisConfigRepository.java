package com.example.faultinjectionmanager.repository;

import com.example.faultinjectionmanager.entity.FaultInjectLogEntity;
import com.example.faultinjectionmanager.entity.FaultInjectionConfigEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface RedisConfigRepository extends ElasticsearchRepository<FaultInjectLogEntity,String> {
    //根据状态查询故障注入日志
    List<FaultInjectLogEntity> findByStatus(String status);

    //根据响应时间范围查询故障注入日志
    List<FaultInjectLogEntity> findByResponseBetween(long startTime,long endTime);

    //根据吞吐量范围查询
    List<FaultInjectionConfigEntity> findByThroughPutBeween(double minThroughPut,double maxThroughPut);
}
