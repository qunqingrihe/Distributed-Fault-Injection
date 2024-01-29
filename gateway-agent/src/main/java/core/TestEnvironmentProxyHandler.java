package core;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class TestEnvironmentProxyHandler extends ChannelInboundHandlerAdapter {

    private final EnvironmentBehavior behavior;

    public TestEnvironmentProxyHandler(EnvironmentBehavior behavior) {
        this.behavior = behavior;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        behavior.handle(ctx, msg);
    }
}
