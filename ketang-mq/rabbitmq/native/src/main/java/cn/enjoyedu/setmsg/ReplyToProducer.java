package cn.enjoyedu.setmsg;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

/**
 *@author Mark老师   享学课堂 https://enjoy.ke.qq.com
 *往期视频咨询芊芊老师  QQ：2130753077  VIP课程咨询 依娜老师  QQ：2470523467
 *类说明：消息的属性的控制
 */
public class ReplyToProducer {

    public final static String EXCHANGE_NAME = "replyto";

    public static void main(String[] args)
            throws IOException, TimeoutException {
        /* 创建连接,连接到RabbitMQ*/
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("10.45.4.97");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("123456");
        Connection connection = connectionFactory.newConnection();

        /*创建信道*/
        Channel channel = connection.createChannel();
        /*创建持久化交换器*/
        channel.exchangeDeclare(EXCHANGE_NAME,"direct",false);

        //响应QueueName ，消费者将会把要返回的信息发送到该Queue -匿名队列
        String responseQueue = channel.queueDeclare().getQueue();
        //消息的唯一id
        String msgId = UUID.randomUUID().toString();
//       建造者模式-生成消息的属性集
        AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
//                指定-回复消息的队列
                .replyTo(responseQueue)
                .messageId(msgId)
                .build();

        /*声明了一个消费者*/
        final Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag,
                                       Envelope envelope,
                                       AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("Received["+envelope.getRoutingKey()
                        +"]"+message);
            }
        };
        /*消费者正式开始在指定队列上消费消息*/
//        request-response 模式
        channel.basicConsume(responseQueue,true,consumer);

        String msg = "Hellol,RabbitMq";
//        fxc-生产消息时携带属性
        channel.basicPublish(EXCHANGE_NAME,"error",
                properties,
                msg.getBytes());
        System.out.println("Sent error:"+msg);
    }

}
