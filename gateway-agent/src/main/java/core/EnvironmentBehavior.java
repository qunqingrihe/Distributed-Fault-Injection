package core;
import config.EnvironmentConfig;
import io.netty.channel.*;

public interface EnvironmentBehavior {
    void handle(ChannelHandlerContext ctx, Object msg);
    void forwardToTestEnvironment(ChannelHandlerContext ctx, Object msg);
    void defaultBehavior(ChannelHandlerContext ctx, Object msg);
}

