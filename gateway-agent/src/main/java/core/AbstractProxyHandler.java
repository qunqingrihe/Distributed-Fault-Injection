package core;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public abstract class AbstractProxyHandler extends ChannelInboundHandlerAdapter {

    // 在子类中实现的方法，用于处理代理服务器收到的请求
    protected abstract void forwardAndModifyTraffic(Object msg);

    // 在子类中实现的方法，用于处理管理指令
    protected abstract void executeManagementCommand(String command);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 收到请求时，由子类处理
        forwardAndModifyTraffic(msg);
    }

    public void receiveCommand(String command) {
        // 收到管理指令时，由子类处理
        executeManagementCommand(command);
    }
}

