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
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@RefreshScope //动态配置
@Configuration
@ConfigurationProperties(prefix ="environment")//配置文件
public class EnvironmentConfig {
    private String name; // 环境名称
    private String proxyServerAddress; // 代理服务器地址
    private int proxyServerPort; // 代理服务器端口
    private boolean faultInjectorEnabled; // 是否启动故障注入器
    private double divertPercentage; // 流量重定向比例
    private FaultInjector faultInjector; // 故障注入器实例
    private EnvironmentBehavior environmentBehavior; // 新增属性
    private String origTargetAddress;
    private int origTargetPort;

    public double getDivertPercentage() {
        return divertPercentage;
    }

    public void setDivertPercentage(double divertPercentage) {
        this.divertPercentage = divertPercentage;
    }

    public FaultInjector getFaultInjector() {
        return faultInjector;
    }

    public void setFaultInjector(FaultInjector faultInjector) {
        this.faultInjector = faultInjector;
    }

    public String getEnvironment() {
        return name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProxyServerAddress() {
        return proxyServerAddress;
    }

    public void setProxyServerAddress(String proxyServerAddress) {
        this.proxyServerAddress = proxyServerAddress;
    }

    public int getProxyServerPort() {
        return proxyServerPort;
    }

    public void setProxyServerPort(int proxyServerPort) {
        this.proxyServerPort = proxyServerPort;
    }

    public boolean isFaultInjectorEnabled() {
        return faultInjectorEnabled;
    }

    public void setFaultInjectorEnabled(boolean faultInjectorEnabled) {
        this.faultInjectorEnabled = faultInjectorEnabled;
    }
    public String getOrigTargetAddress() {
        return origTargetAddress;
    }

    public void setOrigTargetAddress(String origTargetAddress) {
        this.origTargetAddress = origTargetAddress;
    }

    public int getOrigTargetPort() {
        return origTargetPort;
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
    public Bootstrap createBootstrapForTestEnv(ChannelHandler channelHandler) {
        return createBootstrap(proxyServerAddress, proxyServerPort, channelHandler);
    }

    @Bean
    public Bootstrap createBootstrapForOrigTarEnv(ChannelHandler channelHandler) {
        return createBootstrap(origTargetAddress, origTargetPort, channelHandler);
    }
}

