package core;
import config.EnvironmentConfig;
import io.netty.channel.*;

public interface EnvironmentBehavior {
    void handle(ChannelHandlerContext ctx, Object msg);
}

