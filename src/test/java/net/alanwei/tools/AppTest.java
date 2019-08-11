package net.alanwei.tools;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import net.alanwei.tools.models.ClipboardType;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;

/**
 * Unit test for simple App.
 */
public class AppTest {
    //    @Test
    public void send() throws Throwable {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri("amqp://guest:guest@47.52.157.46:32771/%2f");
        try (Connection connection = factory.newConnection()) {
            Channel channel = connection.createChannel();
            channel.exchangeDeclare("/clipboard", "topic");
            String routingKey = "sys.default";
            channel.basicPublish("/clipboard", routingKey, null, "Tim".getBytes("UTF-8"));
        }
    }

    //    @Test
    public void subscribe() throws Throwable {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri("amqp://guest:guest@47.52.157.46:32771/%2f");
        try (Connection connection = factory.newConnection()) {
            Channel channel = connection.createChannel();

            channel.exchangeDeclare("/clipboard", "topic");
            String queueName = channel.queueDeclare().getQueue();
            channel.queueBind(queueName, "/clipboard", "sys.*");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                System.out.println(" [x] Received '" + message + "'");
            };
            channel.basicConsume(queueName, true, deliverCallback, tag -> {
            });

            Thread.sleep(1000 * 60 * 60);
        }
    }

    @Test
    public void hostName() {
        System.getProperties().stringPropertyNames().forEach(n -> System.out.println("Name: " + n + ", Value: " + System.getProperty(n)));
    }

    @Test
    public void addListener() throws Throwable {
        String b2 = Util.toBase64(new byte[]{10, 100});
        byte[] b1 = Util.fromBase64(b2);
    }

}
