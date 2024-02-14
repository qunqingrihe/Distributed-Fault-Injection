package core;

import config.EnvironmentConfig;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public class HttpServerHandler extends ChannelInboundHandlerAdapter {
    private final ProjectEnvironmentProxyHandler proxyHandler;
    private final FaultInjector faultInjector;
    private final EnvironmentConfig environmentConfig;

    public HttpServerHandler(ProjectEnvironmentProxyHandler proxyHandler,
                             FaultInjector faultInjector,
                             EnvironmentConfig environmentConfig) {
        this.proxyHandler = proxyHandler;
        this.faultInjector = faultInjector;
        this.environmentConfig = environmentConfig;
    }


    // WebSocketFrameHandler类处理WebSocket帧
class WebSocketFrameHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

        private final ProjectEnvironmentProxyHandler proxyHandler;
        private final FaultInjector faultInjector;

        // 构造函数接受ProjectEnvironmentProxyHandler和FaultInjector实例
        public WebSocketFrameHandler(ProjectEnvironmentProxyHandler proxyHandler, FaultInjector faultInjector) {
            this.proxyHandler = proxyHandler;
            this.faultInjector = faultInjector;
        }

        // 处理WebSocket帧
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) {
            // 处理文本帧
            if (frame instanceof TextWebSocketFrame textFrame) {
                String request = textFrame.text();
                System.out.printf("%s received %s%n", ctx.channel(), request);
                processCommand(ctx, request);
            }
            // 处理二进制帧
            else if (frame instanceof BinaryWebSocketFrame) {
                System.out.println("Received binary data");
            }
            // 处理关闭帧
            else if (frame instanceof CloseWebSocketFrame) {
                System.out.println("Received close frame");
                ctx.close();
            }
            // 处理Ping帧
            else if (frame instanceof PingWebSocketFrame pingFrame) {
                System.out.println("Received ping");
                ctx.channel().writeAndFlush(new PongWebSocketFrame(pingFrame.content().retain()));
            }
            // 处理Pong帧
            else if (frame instanceof PongWebSocketFrame) {
                System.out.println("Received pong");
            } else {
                throw new UnsupportedOperationException("Unsupported frame type: " + frame.getClass().getName());
            }
        }

        // 处理接收到的命令
        private void processCommand(ChannelHandlerContext ctx, String request) {
            // 分割命令和参数
            String[] parts = request.split(":", 2);
            if (parts.length == 2) {
                String command = parts[0];
                String argument = parts[1];

                switch (command) {
                    case "divertTraffic":
                        // 处理流量分流命令
                        proxyHandler.receiveCommand(argument);
                        ctx.channel().writeAndFlush(new TextWebSocketFrame("Traffic diversion updated"));
                        break;
                    case "injectTrafficFault":
                        // 处理流量注入故障命令
                        faultInjector.injectTrafficFault();
                        ctx.channel().writeAndFlush(new TextWebSocketFrame("Traffic fault injected"));
                        break;
                    case "injectKillFault":
                        // 处理启停测试故障命令
                        faultInjector.injectKillFault();
                        ctx.channel().writeAndFlush(new TextWebSocketFrame("Kill fault injected"));
                        break;
                    case "injectConfigurationFault":
                        // 处理配置故障命令
                        String[] configParts = argument.split("=", 2);
                        if (configParts.length == 2) {
                            String configFilePath = configParts[0];
                            String newConfig = configParts[1];
                            faultInjector.injectConfigurationFault(configFilePath, newConfig);
                            ctx.channel().writeAndFlush(new TextWebSocketFrame("Configuration fault injected"));
                        } else {
                            ctx.channel().writeAndFlush(new TextWebSocketFrame("Invalid configuration fault command format"));
                        }
                        break;
                    default:
                        ctx.channel().writeAndFlush(new TextWebSocketFrame("Unknown command"));
                        break;
                }
            } else {
                ctx.channel().writeAndFlush(new TextWebSocketFrame("Invalid command format"));
            }
        }

        // 处理异常
        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            cause.printStackTrace();
            ctx.close();
        }
    }
}



