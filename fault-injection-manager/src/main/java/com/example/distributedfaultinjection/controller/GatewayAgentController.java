package com.example.distributedfaultinjection.controller;

import interceptor.Proxy1;
import interceptor.Proxy2;

public class GatewayAgentController {
    private Proxy1 proxy1;
    private Proxy2 proxy2;

    public GatewayAgentController(int port) {
        this.proxy1 = new Proxy1(port);
        this.proxy2 = new Proxy2();
    }

    public void start() {
        proxy1.start();
        proxy2.start();
    }



    // 设置配置、获取状态和日志
}

