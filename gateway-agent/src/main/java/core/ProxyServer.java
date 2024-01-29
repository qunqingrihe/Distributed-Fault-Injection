package core;
import config.EnvironmentConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
public class ProxyServer {
    private final int port;
    private final ProxyHandler proxyHandler;

    public ProxyServer(int port, ProxyHandler proxyHandler) {
        this.port = port;
        this.proxyHandler = proxyHandler;
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
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(proxyHandler);
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            Channel channel = bootstrap.bind(port).sync().channel();
            channel.closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        ApplicationContext context = new AnnotationConfigApplicationContext("config");
        EnvironmentConfig config = context.getBean(EnvironmentConfig.class);

        ProxyHandler handler;
        if ("project".equals(config.getEnvironment())) {
            handler = new ProxyHandler(new ProjectEnvironmentBehavior(config));
        } else if ("test".equals(config.getEnvironment())) {
            handler = new ProxyHandler(new TestEnvironmentBehavior(config));
        } else {
            throw new IllegalArgumentException("Invalid environment: " + config.getEnvironment());
        }

        int port = config.getProxyServerPort();

        new ProxyServer(port, handler).start();
    }
}
