package core;
import config.EnvironmentConfig;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
public class ProxyServer {
    private final int port;
    private final ProxyHandler proxyHandler;
    private final EnvironmentConfig config;

    private Bootstrap bootstrapTestEnv;
    private Bootstrap bootstrapOrigTar;
    private final ChannelInboundHandler httpServerHandler;


    public ProxyServer(int port, ProxyHandler proxyHandler, Bootstrap bootstrapTestEnv, Bootstrap bootstrapOrigTar,EnvironmentConfig config, ChannelInboundHandler httpServerHandler) {
        this.port = port;
        this.proxyHandler = proxyHandler;
        this.bootstrapTestEnv = bootstrapTestEnv;
        this.bootstrapOrigTar = bootstrapOrigTar;
        this.config = config;
        this.httpServerHandler = httpServerHandler;
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
                            ch.pipeline().addLast(new HttpServerCodec());
                            ch.pipeline().addLast(new HttpObjectAggregator(65536));
                            ch.pipeline().addLast(httpServerHandler);
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

            // 创建Bootstrap实例
            Bootstrap bootstrapTestEnv = config.createBootstrapForTestEnv(null); // 这里传入null作为ChannelHandler，稍后设置
            Bootstrap bootstrapOrigTar = config.createBootstrapForOrigTarEnv(null); // 同上

            // 连接到测试环境和原始目标环境
            ChannelFuture testEnvChannelFuture = bootstrapTestEnv.connect();
            ChannelFuture origTarChannelFuture = bootstrapOrigTar.connect();

            // 确保连接完成
            Channel testEnvironmentChannel = testEnvChannelFuture.sync().channel();
            Channel originalTargetChannel = origTarChannelFuture.sync().channel();
            // 创建ProxyHandler实例，它将会根据环境配置的不同来决定具体的行为
            ProxyHandler handler;
            EnvironmentBehavior testEnvBehavior = new ProjectEnvironmentBehavior(config, testEnvironmentChannel, originalTargetChannel);
            EnvironmentBehavior origTarBehavior = new ProjectEnvironmentBehavior(config, testEnvironmentChannel, originalTargetChannel);
            if ("project".equals(config.getEnvironment())) {
                handler = new ProjectEnvironmentProxyHandler(bootstrapTestEnv, bootstrapOrigTar, config, testEnvironmentChannel, originalTargetChannel);
            } else if ("test".equals(config.getEnvironment())) {
                // 这里需要传入所有必要的参数来创建TestEnvironmentProxyHandler实例
                handler = new TestEnvironmentProxyHandler(bootstrapTestEnv, bootstrapOrigTar, config);
            } else {
                throw new IllegalArgumentException("Invalid environment: " + config.getEnvironment());
            }

            // 使用创建好的ChannelHandler来更新Bootstrap实例
            bootstrapTestEnv.handler(handler);
            bootstrapOrigTar.handler(handler);

            FaultInjector faultInjector = new FaultInjector(); // 使用适当的构造函数
            int port = config.getProxyServerPort();
            ProjectEnvironmentProxyHandler proxyHandler = new ProjectEnvironmentProxyHandler(bootstrapTestEnv, bootstrapOrigTar, config, testEnvironmentChannel, originalTargetChannel);
            ChannelInboundHandler httpServerHandler = new HttpServerHandler(proxyHandler,faultInjector, config);

            // 创建并启动代理服务器
            new ProxyServer(port, handler, bootstrapTestEnv, bootstrapOrigTar, config, httpServerHandler).start();
        }

    }
