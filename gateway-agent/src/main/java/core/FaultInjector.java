package core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import event.*;




public class FaultInjector {

    // 模拟流量注入
    public void injectTrafficFault() {

        // 调用JNI方法来发送错误数据包
        // 假设有一个nativeLib库中的nativeMethod方法用于发送错误包
        PacketInjector.injectFaultyPacket();
    }

    // 模拟启停测试
    public void injectKillFault() {
        try {
            // 调用shell脚本来杀死服务器进程
            // 假设shell脚本在 /path/to/kill_script.sh
            Runtime.getRuntime().exec("/path/to/kill_script.sh");
        } catch (IOException e) {
            // 处理异常
            e.printStackTrace();
        }
    }

    // 模拟参数故障
    public void injectConfigurationFault(String configFilePath, String newConfig) {
        try {
            // 修改配置文件
            Files.write(Paths.get(configFilePath), newConfig.getBytes());
            // 重启Spring服务
            // 这里假设有一个restartSpringService的shell脚本
            Runtime.getRuntime().exec("/path/to/restart_spring_service.sh");
        } catch (IOException e) {
            // 处理异常
            e.printStackTrace();
        }
    }

    // 加载本地库，这里需要替换为实际的库名
    static {
        System.loadLibrary("nativeLib");
    }

    // 声明native方法，这里需要替换为实际的方法签名
    private native void nativeMethod();
}
