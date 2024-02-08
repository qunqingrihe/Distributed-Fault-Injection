package config;

import core.EnvironmentBehavior;
import core.ProjectEnvironmentBehavior;
import core.ProjectEnvironmentProxyHandler;
import io.netty.channel.Channel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NettyConfiguration {
    private  Channel testEnvironmentChannel = null;
    private  Channel originalTargetChannel = null;

    @Bean
    public EnvironmentConfig environmentConfig() {
        return new EnvironmentConfig();
    }

    @Bean
    public EnvironmentBehavior environmentBehavior(EnvironmentConfig environmentConfig) {
        // 创建并返回ProjectEnvironmentBehavior的实例
        return new ProjectEnvironmentBehavior(environmentConfig, null, originalTargetChannel);
    }

    @Bean
    public ProjectEnvironmentProxyHandler projectEnvironmentProxyHandler(EnvironmentBehavior environmentBehavior) {
        // 使用EnvironmentBehavior实例来创建ProjectEnvironmentProxyHandler
        return new ProjectEnvironmentProxyHandler(environmentBehavior);
    }
}
