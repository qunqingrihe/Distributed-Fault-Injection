package core;

import config.EnvironmentConfig;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;

public class TestEnvironmentBehavior implements EnvironmentBehavior {

    private final EnvironmentConfig config;
    private final FaultInjector faultInjector;
    private final Channel testEnvironmentChannel;

    public TestEnvironmentBehavior(EnvironmentConfig config, Channel testEnvironmentChannel) {
        this.config = config;
        this.faultInjector = config.getFaultInjector(); // 获取故障注入器实例
        this.testEnvironmentChannel = testEnvironmentChannel; // 注入测试环境的 Channel
    }

    @Override
    public void handle(ChannelHandlerContext ctx, Object msg) {
        // 检查是否启用了故障注入
        if (config.isFaultInjectorEnabled()) {
            // 这里可以根据msg的类型和内容决定调用FaultInjector的哪个方法
            // 例如，如果msg是某种类型的请求，我们可能会想要注入流量故障
            if (msg instanceof FullHttpRequest) {
                // 模拟流量故障
                faultInjector.injectTrafficFault();
            }
            // 其他类型的故障注入可以在这里添加
        }
        // 继续处理消息，或者转发到测试环境
        forwardToTestEnvironment(ctx, msg);
    }

    @Override
    public void forwardToTestEnvironment(ChannelHandlerContext ctx, Object msg) {
        if (testEnvironmentChannel.isActive()) {
            testEnvironmentChannel.writeAndFlush(msg);
        } else {
            // 处理通道不活跃的情况
            System.out.println("Test environment channel is not active.");
        }
    }

    @Override
    public void defaultBehavior(ChannelHandlerContext ctx, Object msg) {
        // 默认行为，比如直接回复请求或者转发到下一个处理器
        // 这里可以直接调用 ctx.fireChannelRead(msg); 来转发到下一个ChannelHandler
        ctx.fireChannelRead(msg);
    }

    @Override
    public void receiveCommand(String command) {
        // 根据命令调整FaultInjector的行为
        // 例如，可以根据命令启用或禁用FaultInjector的某些方法
        // 这里需要实现命令解析和处理逻辑
        // 例如，如果命令是"injectTrafficFault"，那么我们可以调用faultInjector.injectTrafficFault()
    }
}
