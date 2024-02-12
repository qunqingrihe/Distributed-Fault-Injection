package core;

import config.EnvironmentConfig;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import java.text.MessageFormat;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

public class ProjectEnvironmentProxyHandler extends ProxyHandler {

    private final EnvironmentConfig config;
    private final Random random = new Random();
    private final AtomicReference<Double> divertPercentage = new AtomicReference<>(0.0);
    private Channel testEnvironmentChannel;
    private Channel originalTargetChannel;
    private double trafficRatio; // 分流比例

    public ProjectEnvironmentProxyHandler(
            Bootstrap bootstrapTestEnv,
            Bootstrap bootstrapOrigTar,
            EnvironmentConfig config,
            Channel testEnvironmentChannel,
            Channel originalTargetChannel
    ) {
        super(bootstrapTestEnv, bootstrapOrigTar, config);
        this.config = config;
        this.testEnvironmentChannel = testEnvironmentChannel;
        this.originalTargetChannel = originalTargetChannel;
    }


    protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
        // 更新逻辑以使用原子引用的值
        double currentDivertPercentage = divertPercentage.get();
        if (random.nextDouble() < currentDivertPercentage) {
            // Forward the request to the test environment
            forwardToTestEnvironment(ctx, msg);
        } else {
            // Perform the default behavior, such as proxying directly
            defaultBehavior(ctx, msg);
        }
    }
    public void setBootstrapTestEnv(Bootstrap bootstrapTestEnv) {
        // 设置测试环境的 Bootstrap 实例
        this.bootstrapTestEnv = bootstrapTestEnv;
    }

    // 假设这是一个新的方法，用于设置原始目标环境的 Bootstrap 实例
    public void setBootstrapOrigTar(Bootstrap bootstrapOrigTar) {
        // 设置原始目标环境的 Bootstrap 实例
        this.bootstrapOrigTar = bootstrapOrigTar;
    }
    // 添加一个新方法来处理接收到的命令
    public void receiveCommand(String command) {
        // 命令格式: "divertPercentage=0.5"
        String[] parts = command.split("=");
        if (parts.length == 2 && "divertPercentage".equals(parts[0])) {
            try {
                double newPercentage = Double.parseDouble(parts[1]);
                // 使用原子操作更新分流比例
                divertPercentage.set(newPercentage);
                System.out.println("Divert percentage updated to " + newPercentage);
            } catch (NumberFormatException e) {
                System.err.println("Invalid divert percentage value: " + parts[1]);
            }
        } else {
            System.err.println("Invalid command format: " + command);
        }
    }

    private void forwardToTestEnvironment(ChannelHandlerContext ctx, Object msg) {
        if (testEnvironmentChannel != null && testEnvironmentChannel.isActive()) {
            System.out.println(MessageFormat.format("Forwarding the message to the test environment: {0}", msg.toString()));
            testEnvironmentChannel.writeAndFlush(msg).addListener((ChannelFutureListener) future -> {
                if (!future.isSuccess()) {
                    System.err.println("Failed to forward the message to the test environment: " + future.cause());
                    // Fallback to default behavior if forwarding fails
                    defaultBehavior(ctx, msg);
                }
            });
        } else {
            System.err.println("Test environment channel is not available.");
            // Fallback to default behavior if test environment channel is not active
            defaultBehavior(ctx, msg);
        }
    }

    private void defaultBehavior(ChannelHandlerContext ctx, Object msg) {
        if (originalTargetChannel != null && originalTargetChannel.isActive()) {
            System.out.println(MessageFormat.format("Default behavior: Passing the message along: {0}", msg.toString()));
            originalTargetChannel.writeAndFlush(msg).addListener((ChannelFutureListener) future -> {
                if (!future.isSuccess()) {
                    System.err.println("Failed to send the message to the original target: " + future.cause());
                }
            });
        } else {
            System.err.println("Original target channel is not available.");
            // Handle the scenario when the original target channel is not available
            // This could involve closing the connection or alerting the user
            ctx.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Handle exceptions that occur during read and write operations
        System.err.println("Exception occurred: " + cause.getMessage());
        cause.printStackTrace();
        ctx.close();
    }

    public void closeChannels() {
        // Close the channels when they are no longer needed
        if (testEnvironmentChannel != null) {
            testEnvironmentChannel.close();
        }
        if (originalTargetChannel != null) {
            originalTargetChannel.close();
        }
    }
    public Channel getTestEnvironmentChannel() {
        // 连接到测试环境并返回Channel
        return bootstrapTestEnv.connect().syncUninterruptibly().channel();
    }

    public Channel getOriginalTargetChannel() {
        // 连接到原始目标环境并返回Channel
        return bootstrapOrigTar.connect().syncUninterruptibly().channel();
    }
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 当连接建立时，初始化 Channel 实例
        if (this.testEnvironmentChannel == null) {
            this.testEnvironmentChannel = bootstrapTestEnv.connect().syncUninterruptibly().channel();
        }
        if (this.originalTargetChannel == null) {
            this.originalTargetChannel = bootstrapOrigTar.connect().syncUninterruptibly().channel();
        }
        super.channelActive(ctx);
    }
    /**
     * 更新分流比例。
     * @param serviceType 服务类型
     * @param ratio 新的分流比例
     */
    public synchronized void updateTrafficRatio(String serviceType, double ratio) {
        // 根据服务类型和比例更新代理的行为
        // 这里只是一个示例，具体实现取决于您的业务逻辑
        if ("test".equals(serviceType)) {
            this.trafficRatio = ratio;
            // 更新代理行为，例如修改代理规则，重新配置代理等
            // ...
        } else {
            throw new IllegalArgumentException("Unsupported service type: " + serviceType);
        }
    }
}
