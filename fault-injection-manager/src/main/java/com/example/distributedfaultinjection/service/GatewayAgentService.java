package com.example.distributedfaultinjection.service;

import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;

@Service
public class GatewayAgentService {
    private static final String AGENT_START_COMMAND = "gateway-agent start";
    private static final String AGENT_STOP_COMMAND = "gateway-agent stop";
    private static final String AGENT_CONFIG_FILE = "/etc/gateway-agent/config.yaml";
    private  final Runtime runtime;
    public GatewayAgentService(){
        this.runtime=Runtime.getRuntime();
    }
    public void startAgent() throws IOException{
        Process process = runtime.exec(AGENT_START_COMMAND);
        // 等待进程启动完成
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            // 处理异常
        }
    }
    public void stopAgent() throws IOException{
        Process process = runtime.exec(AGENT_STOP_COMMAND);
        // 等待进程停止完成
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            // 处理异常
        }
    }
    public void updateAgentConfig(String config) throws IOException {
        // 将配置写入到配置文件
        try (FileWriter writer = new FileWriter(AGENT_CONFIG_FILE)) {
            writer.write(config);
        }
        // 重启 gateway-agent
        stopAgent();
        startAgent();
    }
}
