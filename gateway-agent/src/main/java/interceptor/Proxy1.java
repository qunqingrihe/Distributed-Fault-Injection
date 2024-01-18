package interceptor;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class Proxy1 {
    private final int port;
    // 构造函数，接收一个端口号
    public Proxy1(int port) {
        this.port = port;
    }

    // 启动代理服务器
    public void start() {
        // 创建两个EventLoopGroup，bossGroup用于接收连接，workerGroup用于处理连接
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            // 创建一个ServerBootstrap
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup) // 设置EventLoopGroup
                    .channel(NioServerSocketChannel.class) // 设置用于接收连接的Channel
                    .handler(new LoggingHandler(LogLevel.INFO)) // 设置日志处理器
                    .childHandler(new ChannelInitializer<Channel>() { // 设置用于处理连接的处理器
                        @Override
                        protected void initChannel(Channel ch) {
                            ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO)); // 添加日志处理器
                            ch.pipeline().addLast(new ProxyFrontendHandler()); // 添加前端处理器
                        }
                    });
            // 绑定端口并同步等待成功
            ChannelFuture f = b.bind(port).sync();
            // 等待服务器Socket关闭
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 关闭EventLoopGroup
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    // 前端处理器，处理从客户端接收到的数据，并将数据发送到后端服务器
    class ProxyFrontendHandler extends ChannelInboundHandlerAdapter {
        private final String remoteHost = "remote_host"; // 后端服务器的地址
        private final int remotePort = 12345; // 后端服务器的端口
        private Channel outboundChannel; // 与后端服务器的连接

        @Override
        public void channelActive(ChannelHandlerContext ctx) {
            // 当与客户端的连接建立时，创建一个与后端服务器的连接
            final Channel inboundChannel = ctx.channel();

            // 创建一个Bootstrap
            Bootstrap b = new Bootstrap();
            b.group(inboundChannel.eventLoop()) // 使用与客户端相同的EventLoop
                    .channel(ctx.channel().getClass()) // 使用与客户端相同的Channel类
                    .handler(new ChannelInitializer<Channel>() { // 设置用于处理连接的处理器
                        @Override
                        protected void initChannel(Channel ch) {
                            ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO)); // 添加日志处理器
                            ch.pipeline().addLast(new ProxyBackendHandler(inboundChannel)); // 添加后端处理器
                        }
                    })
                    .option(ChannelOption.AUTO_READ, false); // 设置不自动读取数据
            ChannelFuture f = b.connect(remoteHost, remotePort); // 连接到后端服务器
            outboundChannel = f.channel(); // 获取与后端服务器的连接
            f.addListener((ChannelFutureListener) future -> {
                if (future.isSuccess()) {
                    // 连接成功，开始读取数据
                    inboundChannel.read();
                } else {
                    // 连接失败，关闭与客户端的连接
                    inboundChannel.close();
                }
            });
        }
        @Override
        public void channelRead(final ChannelHandlerContext ctx, Object msg) {
            // 当读取到数据时，将数据写入到与后端服务器的连接
            if (outboundChannel.isActive()) {
                outboundChannel.writeAndFlush(msg).addListener((ChannelFutureListener) future -> {
                    if (future.isSuccess()) {
                        // 数据写入成功，继续读取数据
                        ctx.channel().read();
                    } else {
                        // 数据写入失败，关闭与客户端的连接
                        future.channel().close();
                    }
                });
            }
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) {
            // 当与客户端的连接关闭时，关闭与后端服务器的连接
            if (outboundChannel != null) {
                closeOnFlush(outboundChannel);
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            // 当出现异常时，打印异常并关闭与客户端的连接
            cause.printStackTrace();
            closeOnFlush(ctx.channel());
        }

        // 关闭Channel，并在关闭前刷新出站缓冲区
        static void closeOnFlush(Channel ch) {
            if (ch.isActive()) {
                ch.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
            }
        }
    }

    // 后端处理器，处理从后端服务器接收到的数据，并将数据发送到客户端
    class ProxyBackendHandler extends ChannelInboundHandlerAdapter {
        private final Channel inboundChannel; // 与客户端的连接

        ProxyBackendHandler(Channel inboundChannel) {
            this.inboundChannel = inboundChannel;
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) {
            // 当与后端服务器的连接建立时，开始读取数据
            ctx.read();
        }

        @Override
        public void channelRead(final ChannelHandlerContext ctx, Object msg) {
            // 当读取到数据时，将数据写入到与客户端的连接
            inboundChannel.writeAndFlush(msg).addListener((ChannelFutureListener) future -> {
                if (future.isSuccess()) {
                    // 数据写入成功，继续读取数据
                    ctx.channel().read();
                } else {
                    // 数据写入失败，关闭与后端服务器的连接
                    future.channel().close();
                }
            });
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) {
            // 当与后端服务器的连接关闭时，关闭与客户端的连接
            ProxyFrontendHandler.closeOnFlush(inboundChannel);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            // 当出现异常时，打印异常并关闭与后端服务器的连接
            cause.printStackTrace();
            ProxyFrontendHandler.closeOnFlush(ctx.channel());
        }
    }

    // 主函数，创建并启动代理服务器
    public static void main(String[] args) {
        new Proxy1( 8080).start();
    }
}





















