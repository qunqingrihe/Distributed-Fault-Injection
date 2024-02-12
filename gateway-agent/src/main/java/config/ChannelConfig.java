package config;

import core.ProjectEnvironmentProxyHandler;
import core.TestEnvironmentProxyHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChannelConfig {

    @Bean(name = "testEnvironmentChannel")
    public Channel testEnvironmentChannel(EnvironmentConfig config, ProjectEnvironmentProxyHandler proxyHandler) {
        Bootstrap bootstrapTestEnv = config.createBootstrap(config.getProxyServerAddress(), config.getProxyServerPort(), proxyHandler);
        return bootstrapTestEnv.connect().channel();
    }

    @Bean(name = "originalTargetChannel")
    public Channel originalTargetChannel(EnvironmentConfig config, ProjectEnvironmentProxyHandler proxyHandler) {
        Bootstrap bootstrapOrigTar = config.createBootstrap(config.getOrigTargetAddress(), config.getOrigTargetPort(), proxyHandler);
        return bootstrapOrigTar.connect().channel();
    }
}

