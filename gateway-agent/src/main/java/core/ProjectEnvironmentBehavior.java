package core;

import config.EnvironmentConfig;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import java.text.MessageFormat;
import java.util.Random;

public class ProjectEnvironmentBehavior implements EnvironmentBehavior {

    private final EnvironmentConfig config;
    private final Random random = new Random();
    private Channel testEnvironmentChannel;
    private Channel originalTargetChannel;

    public ProjectEnvironmentBehavior(
            EnvironmentConfig config,
            Channel testEnvironmentChannel,
            Channel originalTargetChannel
    ) {
        this.config = config;
        this.testEnvironmentChannel = testEnvironmentChannel;
        this.originalTargetChannel = originalTargetChannel;
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

    public void closeChannels() {
        if (testEnvironmentChannel != null) {
            testEnvironmentChannel.close();
        }
        if (originalTargetChannel != null) {
            originalTargetChannel.close();
        }
    }

    @Override
    public void forwardToTestEnvironment(ChannelHandlerContext ctx, Object msg) {
        if(testEnvironmentChannel != null && testEnvironmentChannel.isActive()) {
            System.out.println(MessageFormat.format("Forwarding the message to the test environment: {0}", msg.toString()));
            ChannelFuture future = testEnvironmentChannel.writeAndFlush(msg);
            future.addListener((ChannelFutureListener) channelFuture -> {
                if (!channelFuture.isSuccess()) {
                    System.err.println("Failed to forward the message to the test environment due to: " +
                            channelFuture.cause());
                    defaultBehavior(ctx, msg);
                }
            });
        } else {
            System.err.println("Test environment channel is not available.");
            defaultBehavior(ctx, msg);
        }
    }

    @Override
    public void defaultBehavior(ChannelHandlerContext ctx, Object msg) {
        if (originalTargetChannel != null && originalTargetChannel.isActive()) {
            System.out.println(MessageFormat.format("Default behavior: Passing the message along: {0}", msg.toString()));
            ChannelFuture future = originalTargetChannel.writeAndFlush(msg);
            future.addListener((ChannelFutureListener) channelFuture -> {
                if (!channelFuture.isSuccess()) {
                    System.err.println("Failed to send the message to the original target due to: " +
                            channelFuture.cause());
                }
            });
        } else {
            System.err.println("Original target channel is not available.");
        }
    }

    @Override
    public void receiveCommand(String command) {
        // 解析命令字符串，提取出新的分流比例
        String[] parts = command.split("=");
        String key = parts[0];
        double value = Double.parseDouble(parts[1]);

        if ("divertPercentage".equals(key)) {
            // 将新的分流比例设置到config中
            config.setDivertPercentage(value);
        } else {
            System.err.println("Received unknown command: " + command);
        }
    }



    public void setTestEnvironmentChannel(Channel testEnvironmentChannel) {
        this.testEnvironmentChannel = testEnvironmentChannel;
    }



    public void setOriginalTargetChannel(Channel originalTargetChannel) {
        this.originalTargetChannel = originalTargetChannel;
    }
}
