package com.example.faultinjectionmanager.service;

import com.example.faultinjectionmanager.entity.EnvironmentConfigEntity;
import com.example.faultinjectionmanager.repository.EnvironmentConfigRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EnvironmentConfigService {
    private static final Logger logger= LoggerFactory.getLogger(ElasticsearchService.class);
    @Autowired
    private EnvironmentConfigRepository configRepository;

    //实现更新分流比例的逻辑
    @Transactional
    public void updateDivertPercentage(int divertPercentage){
        logger.info("Updating divert percentage to {}",divertPercentage);
        //更新分流比例的代码
        if(divertPercentage<0||divertPercentage>100){
            logger.error("Invalid divert percentage:{}.It must be between 0 and 100",divertPercentage);
            throw new IllegalArgumentException("Divert percentage must be between 0 and 100");
        }

//        EnvironmentConfigEntity config=configRepository.findAll().stream().findFirst()
//                .orElseThrow(()->new RuntimeException("Environment config not found"));
        List<EnvironmentConfigEntity> configList= (List<EnvironmentConfigEntity>) configRepository.findAll();
        try{
            if(!configList.isEmpty()){
                EnvironmentConfigEntity config =configList.get(0);
                //更新分流比例
                config.setDivertPercentage(divertPercentage);

                configRepository.save(config);
            }else{
                System.out.println("配置列表为空");
            }
        }catch (IndexOutOfBoundsException e){
            System.err.println("索引越界异常："+e.getMessage());
        }

        logger.info("Successfully updated divert percentage to {}",divertPercentage);
    }
}
