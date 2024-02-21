package com.example.faultinjectionmanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class DataStorageService {
    private final StringRedisTemplate stringRedisTemplate;

    @Autowired
    public DataStorageService(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    //将数据保存到redis中
    public void saveData(String key,String value){
        stringRedisTemplate.opsForValue().set(key,value);
    }
    //从redis中获取数据
    public String getData(String key){
        return stringRedisTemplate.opsForValue().get(key);
    }
}
