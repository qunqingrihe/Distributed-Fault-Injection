package com.example.faultinjectionmanager.repository;

import com.example.faultinjectionmanager.entity.EnvironmentConfigEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface EnvironmentConfigRepository extends ElasticsearchRepository<EnvironmentConfigEntity,Long> {
    //提供对EnvironmentConfigEntity实体进行操作的方法，一些基本的CRUD操作
    EnvironmentConfigEntity findTopByOrderByIdAsc();
    //根据分流比例查询环境配置
    List<EnvironmentConfigEntity> findByDivertPercentage(int divertPercentage);
    //查找分流比例在指定范围内的环境配置
    List<EnvironmentConfigEntity> findByDivertPercentageBetween(int startPercentage,int endPercentage);
}
