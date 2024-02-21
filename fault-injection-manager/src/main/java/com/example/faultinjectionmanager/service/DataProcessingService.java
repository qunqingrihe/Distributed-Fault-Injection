package com.example.faultinjectionmanager.service;

import com.example.faultinjectionmanager.entity.FaultInjectLogEntity;
import com.example.faultinjectionmanager.other.DataObject;
import com.example.faultinjectionmanager.repository.RedisConfigRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;


@Service
public class DataProcessingService {
    private static final Logger logger= LoggerFactory.getLogger(DataProcessingService.class);

    //注入redisTemplate与redis进行交互
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    //有一个Repository或DAO用于数据库操作
    @Autowired
    private RedisConfigRepository repository;

    public void processData(DataObject dataObject){
        //这里处理数据逻辑
        //解析状态和性能数据，存储到数据库，或进行一些分析
        //还可将数据存储带redis中，或更新已有的记录

        //从Redis中读取数据
        ValueOperations<String,Object> valueOps= redisTemplate.opsForValue();;
        //使用dataObjectKey作为键名
        DataObject redisData=(DataObject) valueOps.get("dataObjectKey");

        //对读取的数据进行处理
        if(redisData!=null){
            //解析状态和性能数据
            String status=redisData.getStatus();
            DataObject.PerformanceData performance=redisData.getPerformance();
            if(performance!=null){
                long responseTime= performance.getResponseTime();;
                double throughPut=performance.getThroughput();
                logger.info("从redis中处理的数据：状态={},响应时间={},CPU使用率={}",status,responseTime,throughPut);

                //进行数据分析

                //将数据存储到数据库
                FaultInjectLogEntity entity=new FaultInjectLogEntity(status,responseTime,throughPut);
                repository.save(entity);

                //处理结果更新会redis，执行更新操作
                valueOps.set("dataObjectKey",redisData);
            }else{
                logger.warn("性能数据为空");
            }
        }else{
            logger.warn("从redis中读取的数据为空");
        }

    }
}
