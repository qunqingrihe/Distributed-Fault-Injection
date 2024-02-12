package core;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;

public class HttpServerHandler extends SimpleChannelInboundHandler<Object> {

    private final ProjectEnvironmentProxyHandler proxyHandler;

    public HttpServerHandler(ProjectEnvironmentProxyHandler proxyHandler) {
        this.proxyHandler = proxyHandler;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof FullHttpRequest) {
            handleHttpRequest(ctx, (FullHttpRequest) msg);
        } else if (msg instanceof WebSocketFrame) {
            handleWebSocketFrame(ctx, (WebSocketFrame) msg);
        }
    }

    private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
        // Handle WebSocket frame
    }

    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {
        // Handle a bad request.
        if (!req.decoderResult().isSuccess()) {
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }

        // Allow only GET methods.
        if (req.method() != HttpMethod.GET) {
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN));
            return;
        }

        // Check for WebSocket upgrade request and handle it.
        if ("websocket".equalsIgnoreCase(req.headers().get(HttpHeaderNames.UPGRADE.toString()))) {
            ctx.pipeline().replace(this, "websocketHandler", new WebSocketServerProtocolHandler("/ws"));
            ctx.pipeline().addLast(new WebSocketFrameHandler(proxyHandler));
            ctx.fireChannelRead(req.retain());
        } else {
            // Handle normal HTTP request
            // You can process other requests here and send back HTTP responses
            // ...
        }
    }

    private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, FullHttpResponse res) {
        // Generate an error page if response status code is not OK (200).
        if (res.status().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(res.status().toString(), CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
            HttpUtil.setContentLength(res, res.content().readableBytes());
        }

        // Send the response and close the connection if necessary.
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        if (!HttpUtil.isKeepAlive(req) || res.status().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }

    // ...（其他方法）
}
class WebSocketFrameHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    private final ProjectEnvironmentProxyHandler proxyHandler;

    public WebSocketFrameHandler(ProjectEnvironmentProxyHandler proxyHandler) {
        this.proxyHandler = proxyHandler;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) {
        /// 处理文本帧
        if (frame instanceof TextWebSocketFrame textFrame) {
            String request = textFrame.text();
            System.out.printf("%s received %s%n", ctx.channel(), request);
            proxyHandler.receiveCommand(request);
            ctx.channel().writeAndFlush(new TextWebSocketFrame("Command received and applied"));
        }
        // 处理二进制帧
        else if (frame instanceof BinaryWebSocketFrame binaryFrame) {
            // 处理二进制数据
            System.out.println("Received binary data");
        }
        // 处理关闭帧
        else if (frame instanceof CloseWebSocketFrame closeFrame) {
            System.out.println("Received close frame");
            // 通常不需要手动关闭WebSocket连接，因为Netty会帮你处理但如果需要在关闭前做一些处理，可以在这里添加
        }
        // 处理Ping帧
        else if (frame instanceof PingWebSocketFrame pingFrame) {
            System.out.println("Received ping");
            // Pong帧通常用于响应Ping帧
            ctx.channel().writeAndFlush(new PongWebSocketFrame(pingFrame.content().retain()));
        }
        // 处理Pong帧
        else if (frame instanceof PongWebSocketFrame) {
            // Pong帧
            System.out.println("Received pong");
        }
        else {
            throw new UnsupportedOperationException("Unsupported frame type: " + frame.getClass().getName());
        }
    }
}

