package config;

import communication.DefaultCommandReceiver;
import core.EnvironmentBehavior;
import core.FaultInjector;
import core.ProjectEnvironmentProxyHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@RefreshScope //动态配置
@Configuration
@ConfigurationProperties(prefix ="environment")//配置文件
public class EnvironmentConfig {
    @Getter
    private String name; // 环境名称
    @Getter
    private String proxyServerAddress; // 代理服务器地址
    @Getter
    private int proxyServerPort; // 代理服务器端口
    @Getter
    private boolean faultInjectorEnabled; // 是否启动故障注入器
    @Getter
    private double divertPercentage; // 流量重定向比例
    @Getter
    private FaultInjector faultInjector; // 故障注入器实例
    private EnvironmentBehavior environmentBehavior; // 新增属性
    @Getter
    private String origTargetAddress;
    @Getter
    private int origTargetPort;

    public void setDivertPercentage(double divertPercentage) {
        this.divertPercentage = divertPercentage;
    }

    public void setFaultInjector(FaultInjector faultInjector) {
        this.faultInjector = faultInjector;
    }

    public String getEnvironment() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProxyServerAddress(String proxyServerAddress) {
        this.proxyServerAddress = proxyServerAddress;
    }

    public void setProxyServerPort(int proxyServerPort) {
        this.proxyServerPort = proxyServerPort;
    }

    public void setFaultInjectorEnabled(boolean faultInjectorEnabled) {
        this.faultInjectorEnabled = faultInjectorEnabled;
    }

    public void setOrigTargetAddress(String origTargetAddress) {
        this.origTargetAddress = origTargetAddress;
    }

    public void setOrigTargetPort(int origTargetPort) {
        this.origTargetPort = origTargetPort;
    }
    // 一个私有的辅助方法，用于创建Bootstrap实例
    private Bootstrap createBootstrap(String host, int port, ChannelHandler channelHandler) {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(channelHandler); // 将传入的 ChannelHandler 添加到 pipeline
                    }
                })
                .option(ChannelOption.SO_KEEPALIVE, true);
        b.remoteAddress(host, port);
        return b;
    }
    @Bean
    public ChannelHandler channelHandler(EnvironmentBehavior environmentBehavior) {
        // 创建 ProjectEnvironmentProxyHandler 实例时传入 EnvironmentConfig
        return new ProjectEnvironmentProxyHandler(environmentBehavior);
    }
    @Bean
    public Bootstrap createBootstrapForTestEnv(ChannelHandler channelHandler) {
        return createBootstrap(proxyServerAddress, proxyServerPort, channelHandler);
    }

    @Bean
    public Bootstrap createBootstrapForOrigTarEnv(ChannelHandler channelHandler) {
        return createBootstrap(origTargetAddress, origTargetPort, channelHandler);
    }
}

