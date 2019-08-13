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
import net.alanwei.tools.models.ClipboardType;
import net.alanwei.tools.models.LocalClipboardData;
import net.alanwei.tools.models.NetworkClipboardData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class RabbitMqClipboard implements INetworkClipboard {
    private int tryCount = 1;

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
//            this.latestNetworkClipboardData = clipboard;
            Util.log(String.format("publish: %s", clipboard));
            clipboard.setTimeStamp(System.currentTimeMillis() / 1000);
            clipboard.setSourceId(Configurations.LOCAL_HOST_SOURCE_ID);

            this.pubChannel.exchangeDeclare("/clipboard", "topic");
            String routingKey = "sys.default";
            String json = Util.toJson(clipboard);
            this.pubChannel.basicPublish("/clipboard", routingKey, null, json.getBytes("UTF-8"));
        } catch (Throwable ex) {
            Util.log("publish error: " + ex.toString());
            try {
                Thread.sleep(1000 * tryCount++);
            } catch (Throwable ex1) {
            }
            this.pubChannel = this.getChannel();
        }
    }

    private synchronized void watchLocalClipboard() {
        if (hasWatched) {
            return;
        }
        hasWatched = true;

        this.clipboardWatcher.watch(localClipboard -> {
            if (localClipboard.getData() != null && this.latestNetworkClipboardData != null) {

                LocalClipboardData latestClipboard = this.latestNetworkClipboardData.toLocalClipboard();
                String localHashCode = Util.calculateHash(localClipboard.getData()),
                        latestNetworkHashCode = Util.calculateHash(latestClipboard.getData());

                if (!Objects.equals(localHashCode, latestNetworkHashCode)) {
                    if (localClipboard.getType().equals(ClipboardType.String) || latestClipboard.getType().equals(ClipboardType.Invalid)) {
                        Util.log("watch not equal, local: " + localClipboard + ", network:" + this.latestNetworkClipboardData);
                        this.publish(localClipboard.toNetworkClipboard());
                    }
                    if (localClipboard.getType().equals(ClipboardType.Image) && localClipboard.getData().length != latestClipboard.getData().length) {
                        Util.log("watch not equal, local: " + localClipboard + ", network:" + this.latestNetworkClipboardData);
                        this.publish(localClipboard.toNetworkClipboard());
                    }
                }
            }
            Util.log("watch equal, local: " + localClipboard + ", network:" + this.latestNetworkClipboardData);
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
                NetworkClipboardData networkClipboardData = Util.parseJson(json, NetworkClipboardData.class);
                Util.log("receive: " + networkClipboardData);

                if (Objects.equals(networkClipboardData.getSourceId(), Configurations.LOCAL_HOST_SOURCE_ID)) {
                    Util.log("from self, drop.");
                    return;
                }
                this.latestNetworkClipboardData = networkClipboardData;
                Util.log("set latest: " + networkClipboardData);

                LocalClipboardData localClipboardData = networkClipboardData.toLocalClipboard();
                localClipboard.set(localClipboardData);
            };
            subChannel.basicConsume(queueName, true, deliverCallback, tag -> {
            });

            tryCount = 1;
        } catch (Throwable ex) {
            Util.log("receive error: " + ex.toString());
            try {
                Thread.sleep(1000 * tryCount++);
                this.initSubChannel();
            } catch (Throwable sle) {
            }
        }
    }


    private Channel getChannel() {
        String uri = Configurations.getMqUri();
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setUri(uri);
            Connection connection = factory.newConnection();
            return connection.createChannel();
        } catch (Throwable ex) {
            Util.log("rabbitmq(" + uri + ") connect error: " + ex.toString());
        }
        return null;
    }


}
