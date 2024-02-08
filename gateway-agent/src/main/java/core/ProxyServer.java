package core;
import config.EnvironmentConfig;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
public class ProxyServer {
    private final int port;
    private final ProxyHandler proxyHandler;
    private final EnvironmentConfig config;

    private Bootstrap bootstrapTestEnv;
    private Bootstrap bootstrapOrigTar;

    public ProxyServer(int port, ProxyHandler proxyHandler, Bootstrap bootstrapTestEnv, Bootstrap bootstrapOrigTar,EnvironmentConfig config) {
        this.port = port;
        this.proxyHandler = proxyHandler;
        this.bootstrapTestEnv = bootstrapTestEnv;
        this.bootstrapOrigTar = bootstrapOrigTar;
        this.config = config;
    }

    public void start() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            // 在这里，我们不立即创建 EnvironmentBehavior 实例
                            // 而是将 Bootstrap 实例和配置传递给 ProxyHandler
                            ch.pipeline().addLast(new ProxyHandler(bootstrapTestEnv, bootstrapOrigTar, config));
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            // 绑定并开始接受传入的连接
            ChannelFuture bindFuture = bootstrap.bind(port).sync();
            bindFuture.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        ApplicationContext context = new AnnotationConfigApplicationContext("config");
        EnvironmentConfig config = context.getBean(EnvironmentConfig.class);

        // 创建EnvironmentBehavior实例
        EnvironmentBehavior testEnvBehavior = new ProjectEnvironmentBehavior(config, null, null);
        EnvironmentBehavior origTarEnvBehavior = new TestEnvironmentBehavior(config);

        // 创建ChannelHandler实例
        ChannelHandler testEnvChannelHandler = new ProjectEnvironmentProxyHandler(testEnvBehavior);
        ChannelHandler origTarEnvChannelHandler = new TestEnvironmentProxyHandler(origTarEnvBehavior);

        // 使用创建好的ChannelHandler来创建Bootstrap实例
        Bootstrap bootstrapTestEnv = config.createBootstrapForTestEnv(testEnvChannelHandler);
        Bootstrap bootstrapOrigTar = config.createBootstrapForOrigTarEnv(origTarEnvChannelHandler);
        ChannelFuture testEnvChannelFuture = bootstrapTestEnv.connect();
        ChannelFuture origTarChannelFuture = bootstrapOrigTar.connect();
        Channel testEnvironmentChannel = testEnvChannelFuture.sync().channel();
        Channel originalTargetChannel = origTarChannelFuture.sync().channel();

        ProxyHandler handler;
        if ("project".equals(config.getEnvironment())) {
            EnvironmentBehavior behavior = new ProjectEnvironmentBehavior(config, testEnvironmentChannel, originalTargetChannel);
            handler = new ProxyHandler(bootstrapTestEnv, bootstrapOrigTar, config);
        } else if ("test".equals(config.getEnvironment())) {
            EnvironmentBehavior behavior = new TestEnvironmentBehavior(config);
            handler = new ProxyHandler(bootstrapTestEnv, bootstrapOrigTar, config);
        } else {
            throw new IllegalArgumentException("Invalid environment: " + config.getEnvironment());
        }

        int port = config.getProxyServerPort();

        new ProxyServer(port, handler, bootstrapTestEnv, bootstrapOrigTar,config).start();
    }
}
