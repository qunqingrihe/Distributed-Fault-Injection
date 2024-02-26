package core;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class CommandQueueManager  {
    private final static String QUEUE_NAME = "command_queue";

    public static void startCommandProducer(String command) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            channel.basicPublish("", QUEUE_NAME, null, command.getBytes(StandardCharsets.UTF_8));
            System.out.println(" [x] Sent: '" + command + "'");
        }
    }

    public static void startCommandConsumer() throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String command = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.println(" [x] Received: '" + command + "'");
                // 在这里执行命令或将命令放入缓存区
            };

            channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {
            });

            // 等待一段时间，保持程序运行
            Thread.sleep(5000);
        }
    }
}

