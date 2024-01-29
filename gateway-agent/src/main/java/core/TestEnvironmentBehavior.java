package core;

import config.EnvironmentConfig;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class TestEnvironmentBehavior implements EnvironmentBehavior {

    private final EnvironmentConfig config;

    public TestEnvironmentBehavior(EnvironmentConfig config) {
        this.config = config;
    }

    @Override
    public void handle(ChannelHandlerContext ctx, Object msg) {
        FaultInjector faultInjector = config.getFaultInjector();
    }
}
