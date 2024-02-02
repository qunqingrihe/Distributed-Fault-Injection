package config;

import core.FaultInjector;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
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
    public Bootstrap createBootstrapForTestEnv() {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline p = ch.pipeline();
                    }
                });

        // 这里设置你的服务器地址
        bootstrap.remoteAddress(getProxyServerAddress(), getProxyServerPort());

        return bootstrap;
    }

    public Bootstrap createBootstrapForOrigTarEnv() {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline p = ch.pipeline();
                    }
                });
        // 这里设置服务器地址
        bootstrap.remoteAddress(getProxyServerAddress(), getProxyServerPort());

        return bootstrap;
    }
}

