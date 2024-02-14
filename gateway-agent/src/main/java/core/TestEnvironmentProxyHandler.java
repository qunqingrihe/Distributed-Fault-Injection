package core;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelHandlerContext;
import config.EnvironmentConfig;

public class TestEnvironmentProxyHandler extends ProxyHandler {

    private final EnvironmentConfig config;
    private final FaultInjector faultInjector;

    public TestEnvironmentProxyHandler(Bootstrap bootstrapTestEnv,
                                       Bootstrap bootstrapOrigTar,
                                       EnvironmentConfig config) {
        super(bootstrapTestEnv, bootstrapOrigTar, config);
        this.config = config;
        this.faultInjector = config.getFaultInjector();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 检查是否启用了故障注入
        if (config.isFaultInjectorEnabled()) {
            // 根据消息内容决定是否注入故障
            faultInjector.injectTrafficFault();
        } else {
            // 如果故障注入没有启用，进行默认行为
            super.channelRead(ctx, msg);
        }
    }
    protected void executeManagementCommand(String command) {
        String[] commandParts = command.split("=");
        if (commandParts.length != 2) {
            System.err.println("Invalid command format.");
            return;
        }

        String commandType = commandParts[0];
        String commandValue = commandParts[1];

        switch (commandType) {
            case "enableFaultInjection":
                // 启用故障注入
                config.setFaultInjectorEnabled(true);
                System.out.println("Fault injection enabled.");
                break;
            case "disableFaultInjection":
                // 禁用故障注入
                config.setFaultInjectorEnabled(false);
                System.out.println("Fault injection disabled.");
                break;
            case "updateFaultInjectionSettings":
                // 更新故障注入设置
                String[] settingParts = commandValue.split(",");
                for (String setting : settingParts) {
                    String[] settingValues = setting.split(":");
                    if ("traffic".equals(settingValues[0])) {
                        // 更新流量故障注入设置
                        // 这里需要根据实际情况来实现更新逻辑
                    } else if ("kill".equals(settingValues[0])) {
                        // 更新启停测试故障注入设置
                        // 这里需要根据实际情况来实现更新逻辑
                    } else if ("config".equals(settingValues[0])) {
                        // 更新配置故障注入设置
                        String configFilePath = settingValues[1];
                        String newConfig = settingValues[2];
                        faultInjector.injectConfigurationFault(configFilePath, newConfig);
                        System.out.println("Configuration fault injection settings updated.");
                    } else {
                        System.err.println("Unknown setting type: " + settingValues[0]);
                    }
                }
                break;
            default:
                System.err.println("Unknown command type: " + commandType);
                break;
        }
    }

}

