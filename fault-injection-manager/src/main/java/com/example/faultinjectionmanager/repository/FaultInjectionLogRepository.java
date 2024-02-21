package com.example.faultinjectionmanager.repository;

import com.example.faultinjectionmanager.entity.FaultInjectLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

//可自定义查询方法
public interface FaultInjectionLogRepository extends ElasticsearchRepository<FaultInjectLog,String> {

    //提供对FaultInjectionLogEntity实体进行操作的接口

    //根据故障类型查询
    List<FaultInjectLog> faultByFaultType(String faultType);

    //使用Query注解自定义查询
    @Query("")
    List<FaultInjectLog> searchByFaultType(String faultType);

    //分页和排序
    Page<FaultInjectLog> findByFaultType(String faultType, Pageable pageable);

}
