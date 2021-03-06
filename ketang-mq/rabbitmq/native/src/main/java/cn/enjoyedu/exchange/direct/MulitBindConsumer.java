package cn.enjoyedu.exchange.direct;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 *@author Mark老师   享学课堂 https://enjoy.ke.qq.com
 *往期视频咨询芊芊老师  QQ：2130753077  VIP课程咨询 依娜老师  QQ：2470523467
 *类说明：队列和交换器的多重绑定
 */
//fxc-  队列和交换器的多重绑定；
public class MulitBindConsumer {

    public static void main(String[] argv) throws IOException,
            InterruptedException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("10.45.4.97");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("123456");

        // 打开连接和创建频道，与发送端一样
        Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();
        channel.exchangeDeclare(DirectProducer.EXCHANGE_NAME,
                "direct");

        //声明一个随机队列
//        fxc-使用随机队列！！！
        String queueName = channel.queueDeclare().getQueue();

        /*队列绑定到交换器上时，是允许绑定多个路由键的，也就是多重绑定*/
//        fxc-队列绑定对个路由建
        String[] severities={"error","info","warning"};
        for(String serverity:severities){
            channel.queueBind(queueName,DirectProducer.EXCHANGE_NAME,
                    serverity);
        }
        System.out.println(" [*] Waiting for messages:");

        // 创建队列消费者
        final Consumer consumerA = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag,
                                       Envelope envelope,
                                       AMQP.BasicProperties
                                               properties,
                                       byte[] body)
                    throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" Received "
                        + envelope.getRoutingKey() + ":'" + message
                        + "'");
            }
        };
        channel.basicConsume(queueName, true, consumerA);
    }
}
