package net.alanwei.tools.impl;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import net.alanwei.tools.Configurations;
import net.alanwei.tools.Util;
import net.alanwei.tools.inter.IClipboardWatcher;
import net.alanwei.tools.inter.ILocalClipboard;
import net.alanwei.tools.inter.INetworkClipboard;
import net.alanwei.tools.models.LocalClipboardData;
import net.alanwei.tools.models.NetworkClipboardData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class RabbitMqClipboard implements INetworkClipboard {

    @Autowired
    private ILocalClipboard localClipboard;
    @Autowired
    private IClipboardWatcher clipboardWatcher;

    private NetworkClipboardData latestNetworkClipboardData = LocalClipboardData.invalid().toNetworkClipboard();
    private boolean hasWatched = false;
    private Channel pubChannel;

    //TODO 把网络剪贴板内容设置到本地剪贴板时, 要判断网络剪切板是否是最新的

    public RabbitMqClipboard() {
        new Thread(() -> {
            this.initSubChannel();
            this.watchLocalClipboard();
        }).start();

    }


    @Override
    public void publish(NetworkClipboardData clipboard) {
        //TODO 支持正则表达式过滤, 当匹配时, 不传送某些剪贴板内容到网络上.
        try {
            this.pubChannel.exchangeDeclare("/clipboard", "topic");
            String routingKey = "sys.default";
            String json = Util.toJson(clipboard);
            Util.log("publish: " + json);
            this.pubChannel.basicPublish("/clipboard", routingKey, null, json.getBytes("UTF-8"));
        } catch (Throwable ex) {
            Util.log("publish error: " + ex.toString());
            this.initPubChannel();
        }
    }

    private Channel getChannel() {
        try {
            String uri = Configurations.getMqUri();
            ConnectionFactory factory = new ConnectionFactory();
            factory.setUri(uri);
            Connection connection = factory.newConnection();
            return connection.createChannel();
        } catch (Throwable ex) {
        }
        return null;
    }

    private void initPubChannel() {
        this.pubChannel = this.getChannel();
    }

    private synchronized void watchLocalClipboard() {
        if (hasWatched) {
            return;
        }
        hasWatched = true;

        this.clipboardWatcher.watch(clipboardData -> {
            NetworkClipboardData networkClipboardData = clipboardData.toNetworkClipboard();
            if (!Objects.equals(networkClipboardData.getBase64Data(), this.latestNetworkClipboardData.getBase64Data())) {
                this.publish(networkClipboardData);
            }
            return 1;
        });
    }

    private void initSubChannel() {
        Channel subChannel = this.getChannel();
        try {
            subChannel.exchangeDeclare("/clipboard", "topic");
            String queueName = subChannel.queueDeclare().getQueue();
            subChannel.queueBind(queueName, "/clipboard", "sys.*");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String json = new String(delivery.getBody(), "UTF-8");
                Util.log("receive: " + json);
                NetworkClipboardData networkClipboardData = Util.parseJson(json, NetworkClipboardData.class);

                if (Objects.equals(networkClipboardData.getSourceId(), Configurations.LOCAL_HOST_SOURCE_ID)) {
                    Util.log("from self, drop.");
                    return;
                }
                LocalClipboardData localClipboardData = networkClipboardData.toLocalClipboard();
                localClipboard.set(localClipboardData);

                this.latestNetworkClipboardData = networkClipboardData;
            };
            subChannel.basicConsume(queueName, true, deliverCallback, tag -> {
            });
        } catch (Throwable ex) {
            Util.log("receive: " + ex.getMessage());
            try {
                Thread.sleep(1000 * 2);
                this.initSubChannel();
            } catch (Throwable sle) {
            }
        }
    }
}
