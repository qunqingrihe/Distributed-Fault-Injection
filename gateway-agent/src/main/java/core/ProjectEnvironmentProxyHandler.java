package core;

import event.TrafficRatioUpdatedEvent;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.springframework.context.event.EventListener;

import java.util.concurrent.ConcurrentHashMap;

public class ProjectEnvironmentProxyHandler extends ChannelInboundHandlerAdapter {

    private final EnvironmentBehavior behavior;
    private final ConcurrentHashMap<String, Double> trafficRatios = new ConcurrentHashMap<>();
    public ProjectEnvironmentProxyHandler(EnvironmentBehavior behavior) {
        this.behavior = behavior;
    }

    public void updateTrafficRatio(String serviceType, double ratio) {
        trafficRatios.put(serviceType, ratio);
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 判断消息类型
        if (msg instanceof String) {
            String message = (String) msg;
            if (message.startsWith("COMMAND:")) {
                // 如果消息是一个管理指令，去掉"COMMAND:"前缀，然后处理
                String command = message.substring("COMMAND:".length());
            } else {
                // 否则，将消息视为一个请求，交给behavior处理
                behavior.handle(ctx, msg);
            }
        } else {
            // 如果消息不是一个字符串，直接忽略
            System.err.println("Received a message of unknown type: " + msg.getClass());
        }
    }
    @EventListener
    public void onTrafficRatioUpdated(TrafficRatioUpdatedEvent event) {
        updateTrafficRatio(event.getServiceType(), event.getRatio());
    }

}


