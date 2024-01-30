package core;

import config.EnvironmentConfig;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Random;

public class ProjectEnvironmentBehavior implements EnvironmentBehavior {

    private final EnvironmentConfig config;
    private final Random random = new Random();

    public ProjectEnvironmentBehavior(EnvironmentConfig config) {
        this.config = config;
    }

    @Override
    public void handle(ChannelHandlerContext ctx, Object msg) {

        double divertPercentage = config.getDivertPercentage();
        if (random.nextDouble() < divertPercentage) {
            // 使用一个方法将请求转发到测试环境
            forwardToTestEnvironment(ctx, msg);
        } else {
            // 默认行为，如直接进行代理
            defaultBehavior(ctx, msg);
        }
    }

    @Override
    public void forwardToTestEnvironment(ChannelHandlerContext ctx, Object msg) {

    }

    @Override
    public void defaultBehavior(ChannelHandlerContext ctx, Object msg) {

    }
}
