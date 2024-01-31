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
                        @SneakyThrows
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ChannelFuture testEnvChannelFuture = bootstrapTestEnv.connect();
                            ChannelFuture origTarChannelFuture = bootstrapOrigTar.connect();
                            Channel testEnvironmentChannel = testEnvChannelFuture.sync().channel();
                            Channel originalTargetChannel = origTarChannelFuture.sync().channel();
                            EnvironmentBehavior behavior = new ProjectEnvironmentBehavior(config,testEnvironmentChannel, originalTargetChannel);
                            ProxyHandler handler = new ProxyHandler(behavior);
                            ch.pipeline().addLast(handler);
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture bindFuture = bootstrap.bind(port);
            bindFuture.sync().addListener(future -> {
                if (future.isSuccess()) {
                    System.out.println("Server started successfully.");
                } else {
                    System.out.println("Server could not be started.");
                    future.cause().printStackTrace();
                }
            });

            bindFuture.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        ApplicationContext context = new AnnotationConfigApplicationContext("config");
        EnvironmentConfig config = context.getBean(EnvironmentConfig.class);
        Bootstrap bootstrapTestEnv = config.createBootstrapForTestEnv();
        Bootstrap bootstrapOrigTar = config.createBootstrapForOrigTarEnv();
        ChannelFuture testEnvChannelFuture = bootstrapTestEnv.connect();
        ChannelFuture origTarChannelFuture = bootstrapOrigTar.connect();
        Channel testEnvironmentChannel = testEnvChannelFuture.sync().channel();
        Channel originalTargetChannel = origTarChannelFuture.sync().channel();

        ProxyHandler handler;
        if ("project".equals(config.getEnvironment())) {
            EnvironmentBehavior behavior = new ProjectEnvironmentBehavior(config, testEnvironmentChannel, originalTargetChannel);
            handler = new ProxyHandler(behavior);
        } else if ("test".equals(config.getEnvironment())) {
            EnvironmentBehavior behavior = new TestEnvironmentBehavior(config);
            handler = new ProxyHandler(behavior);
        } else {
            throw new IllegalArgumentException("Invalid environment: " + config.getEnvironment());
        }

        int port = config.getProxyServerPort();

        new ProxyServer(port, handler, bootstrapTestEnv, bootstrapOrigTar,config).start();
    }
}
