package core;

import com.rabbitmq.client.*;
import io.netty.channel.Channel;
import reactor.netty.Connection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class CommandQueueManager {
    private final static String QUEUE_NAME = "command_queue";

    private static void startCommandProducer(String command) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try (Connection connection = (Connection) factory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            channel.basicPublish("", QUEUE_NAME, null, command.getBytes(StandardCharsets.UTF_8));
            System.out.println(" [x] Sent: '" + command + "'");
        }
    }

    private static void startCommandConsumer() throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String command = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.println(" [x] Received: '" + command + "'");
                FaultInjector faultInjector = new FaultInjector
                // 使用反射调用方法
                invokeMethod(faultInjector,command);
            };

            channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {
            });

            // 等待一段时间，保持程序运行
            Thread.sleep(5000);
        }
    }
    private static void invokeMethod(Object target, String methodName) {
        try {
            // 获取方法对象
            Method method = target.getClass().getMethod(methodName);

            // 调用方法
            method.invoke(target);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
