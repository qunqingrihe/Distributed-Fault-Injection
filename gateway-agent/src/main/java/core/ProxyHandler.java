package core;

import config.EnvironmentConfig;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ProxyHandler extends ChannelInboundHandlerAdapter {
    private final Bootstrap bootstrapTestEnv;
    private final Bootstrap bootstrapOrigTar;
    private final EnvironmentConfig config;
    private EnvironmentBehavior behavior;

    public ProxyHandler(Bootstrap bootstrapTestEnv, Bootstrap bootstrapOrigTar, EnvironmentConfig config) {
        this.bootstrapTestEnv = bootstrapTestEnv;
        this.bootstrapOrigTar = bootstrapOrigTar;
        this.config = config;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 当通道激活时，创建EnvironmentBehavior实例
        if (behavior == null) {
            // 这里假设EnvironmentBehavior的构造函数可以接受null作为Channel参数，并在稍后进行设置
            behavior = new ProjectEnvironmentBehavior(config, null, null);
        }
        // 如果需要，可以在这里连接到测试环境和原始目标环境
        // 这里可能需要处理连接的异步性质
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 如果EnvironmentBehavior尚未创建，则首先创建它
        if (behavior == null) {
            channelActive(ctx); // 确保behavior被创建
        }
        // 将消息传递给EnvironmentBehavior处理
        behavior.handle(ctx, msg);
    }
    private EnvironmentBehavior createEnvironmentBehavior(Channel channel) {
        // 根据配置和Channel创建相应的EnvironmentBehavior实例
        if ("project".equals(config.getEnvironment())) {
            return new ProjectEnvironmentBehavior(config, channel, null); // 针对项目环境
        } else if ("test".equals(config.getEnvironment())) {
            return new TestEnvironmentBehavior(config); // 针对测试环境
        } else {
            throw new IllegalArgumentException("Unknown environment type: " + config.getEnvironment());
        }
    }
}
