package com.example.distributedfaultinjection.service;

import org.springframework.stereotype.Service;

@Service
public class LogService {
    //加载日志系统的库
    //声明本地方法
    public String getLogs(){
        return "logs";
    }
    public void writeLog(String log) {
        // 调用日志系统的函数来写入日志
        // 例如writeLogNative(log);
    }
}
