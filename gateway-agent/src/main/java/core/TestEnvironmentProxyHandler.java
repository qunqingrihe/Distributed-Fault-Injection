package core;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelHandlerContext;
import config.EnvironmentConfig;

public class TestEnvironmentProxyHandler extends ProxyHandler {

    private final EnvironmentBehavior behavior;

    // 修改构造函数以接收 Bootstrap 实例和 EnvironmentConfig 实例
    public TestEnvironmentProxyHandler(Bootstrap bootstrapTestEnv,
                                       Bootstrap bootstrapOrigTar,
                                       EnvironmentConfig config,
                                       EnvironmentBehavior behavior) {
        super(bootstrapTestEnv, bootstrapOrigTar, config); // 调用父类的构造函数
        this.behavior = behavior;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 这里，您可以根据需要调用 behavior.handle 来处理消息
        behavior.handle(ctx, msg);
    }

    // 如果需要，您可以添加其他方法来支持 TestEnvironmentProxyHandler 的功能
}

