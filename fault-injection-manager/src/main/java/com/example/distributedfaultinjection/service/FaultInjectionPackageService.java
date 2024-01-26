package com.example.distributedfaultinjection.service;

import org.springframework.stereotype.Service;

@Service
public class FaultInjectionPackageService {
    static {
        System.loadLibrary("FaultInjectionPackage"); // 加载C库
    }

    // 声明本地方法
    private native void startPackageNative();
    private native void stopPackageNative();
    private native void updateConfigNative(String config);

    public void startPackage() {
        startPackageNative(); // 调用C函数来启动故障注入包
    }

    public void stopPackage() {
        stopPackageNative(); // 调用C函数来停止故障注入包
    }

    public void updateConfig(String config) {
        updateConfigNative(config); // 调用C函数来更新配置
    }
}
