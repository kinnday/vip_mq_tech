package cn.enjoyedu.exchange.direct;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 *@author Mark老师   享学课堂 https://enjoy.ke.qq.com
 *往期视频咨询芊芊老师  QQ：2130753077  VIP课程咨询 依娜老师  QQ：2470523467
 *类说明：普通的消费者
 */
public class NormalConsumer {

    public static void main(String[] argv)
            throws IOException, TimeoutException {
//      创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("10.45.4.97");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("123456");

        // 打开连接和创建频道，与发送端一样
        Connection connection = factory.newConnection();
//        创建信道  connection.createChannel();
        final Channel channel = connection.createChannel();
//        在信道中设置交换器
        channel.exchangeDeclare(DirectProducer.EXCHANGE_NAME,BuiltinExchangeType.DIRECT);
//        channel.exchangeDeclare(DirectProducer.EXCHANGE_NAME,    "direct");

//        /*声明一个队列*/   [消费者端实现]
        String queueName = "focuserror";
        channel.queueDeclare(queueName,false,false,
                false,null);

//        /*绑定，将队列和交换器通过路由键进行绑定*/  [消费者端实现]
//        String[] serverities = {"error","info","warning"};
        String routekey = "info";/*表示只关注error级别的日志消息*/
//        channel.queueBind(queueName,DirectProducer.EXCHANGE_NAME,routekey);
        channel.queueBind(queueName,DirectProducer.EXCHANGE_NAME,routekey);

        System.out.println("waiting for message........");

        /*声明了一个消费者*/
//        final Consumer consumer = new DefaultConsumer(channel){
//            @Override
//            public void handleDelivery(String consumerTag,
//                                       Envelope envelope,
//                                       AMQP.BasicProperties properties,
//                                       byte[] body) throws IOException {
//                String message = new String(body, "UTF-8");
//                System.out.println("Received["+envelope.getRoutingKey()
//                        +"]"+message);
//            }
//        };

//        fxc-声明一个消费者
        final Consumer consumer= new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) throws IOException {
//                super.handleDelivery(consumerTag, envelope, properties, body);
                String message = new String(body, "UTF-8");
                System.out.println("Received["+envelope.getRoutingKey()
                        +"]"+message);
            }
        };
        /*消费者正式开始在指定队列上消费消息*/
        channel.basicConsume(queueName,true,consumer);


    }

}
