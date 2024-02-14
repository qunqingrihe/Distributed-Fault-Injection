package config;
import core.EnvironmentBehavior;
import core.ProjectEnvironmentBehavior;
import core.ProjectEnvironmentProxyHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NettyConfiguration {
    private final EnvironmentConfig environmentConfig;

    public NettyConfiguration(EnvironmentConfig environmentConfig) {
        this.environmentConfig = environmentConfig;
    }

    private Bootstrap createBootstrap(String host, int port, ChannelHandler channelHandler) {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        if (channelHandler != null) {
                            pipeline.addLast(channelHandler);
                        }
                    }
                })
                .option(ChannelOption.SO_KEEPALIVE, true);
        b.remoteAddress(host, port);
        return b;
    }

    @Bean
    public Bootstrap bootstrapTestEnv() {
        return createBootstrap(
                environmentConfig.getProxyServerAddress(),
                environmentConfig.getProxyServerPort(),
                null // 在这里不设置 handler，稍后在 proxyHandler 中设置
        );
    }

    @Bean
    public Bootstrap bootstrapOrigTar() {
        return createBootstrap(
                environmentConfig.getOrigTargetAddress(),
                environmentConfig.getOrigTargetPort(),
                null // 在这里不设置 handler，稍后在 proxyHandler 中设置
        );
    }

    @Bean
    public ProjectEnvironmentProxyHandler projectEnvironmentProxyHandler(
            @Qualifier("bootstrapTestEnv") Bootstrap bootstrapTestEnv,
            @Qualifier("bootstrapOrigTar") Bootstrap bootstrapOrigTar) {
        // 创建 ProjectEnvironmentProxyHandler 实例
        ProjectEnvironmentProxyHandler proxyHandler = new ProjectEnvironmentProxyHandler(
                bootstrapTestEnv,
                bootstrapOrigTar,
                environmentConfig,
                null, // 这里暂时不设置 testEnvironmentChannel
                null  // 这里暂时不设置 originalTargetChannel
        );

        // 设置 ChannelHandler 到 Bootstrap 实例
        bootstrapTestEnv.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) {
                ch.pipeline().addLast(proxyHandler);
            }
        });

        bootstrapOrigTar.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) {
                ch.pipeline().addLast(proxyHandler);
            }
        });

        return proxyHandler;
    }
}


