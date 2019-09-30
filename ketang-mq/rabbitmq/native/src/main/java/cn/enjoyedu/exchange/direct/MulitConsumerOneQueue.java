package cn.enjoyedu.exchange.direct;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 *@author Mark老师   享学课堂 https://enjoy.ke.qq.com
 *往期视频咨询芊芊老师  QQ：2130753077  VIP课程咨询 依娜老师  QQ：2470523467
 *类说明：一个队列多个消费者，则会表现出消息在消费者之间的轮询发送。
 */
//fxc- 多个消费者-对应一个队列
// 三个消费者 轮询消费！！！！
public class MulitConsumerOneQueue {

    private static class ConsumerWorker implements Runnable{
        final Connection connection;
        final String queueName;

//        两个入参： 连接和同一个队列名称！！！
        public ConsumerWorker(Connection connection,String queueName) {
            this.connection = connection;
            this.queueName = queueName;
        }

        public void run() {
            try {
                /*创建一个信道，意味着每个线程单独一个信道*/
                final Channel channel = connection.createChannel();
                channel.exchangeDeclare(DirectProducer.EXCHANGE_NAME,
                        "direct");
                /*声明一个队列,rabbitmq，如果队列已存在，不会重复创建*/
                channel.queueDeclare(queueName,
                        false,false,
                        false,null);
                //消费者名字，打印输出用
                final String consumerName
                        =  Thread.currentThread().getName();

                //所有日志严重性级别
                String[] severities={"error","info","warning"};
                for (String severity : severities) {
                    //关注所有级别的日志（多重绑定）
                    channel.queueBind(queueName,
                            DirectProducer.EXCHANGE_NAME, severity);
                }
                System.out.println(" ["+consumerName+"] Waiting for messages:");

                // 创建队列消费者
                final Consumer consumerA = new DefaultConsumer(channel) {
                    @Override
                    public void handleDelivery(String consumerTag,
                                               Envelope envelope,
                                               AMQP.BasicProperties
                                                       properties,
                                               byte[] body)
                            throws IOException {
                        String message =
                                new String(body, "UTF-8");
                        System.out.println(consumerName
                                +" Received "  + envelope.getRoutingKey()
                                + ":'" + message + "'");
                    }
                };
                channel.basicConsume(queueName, true, consumerA);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] argv) throws IOException,
            InterruptedException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("10.45.4.97");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("123456");

        // 打开连接和创建频道，与发送端一样
        Connection connection = factory.newConnection();

        //3个线程，线程之间共享队列,一个队列多个消费者
        String queueName = "focusAll";
        for(int i=0;i<3;i++){
            /*将队列名作为参数，传递给每个线程*/
            Thread worker =new Thread(new ConsumerWorker(connection,queueName));
            worker.start();
        }

    }
}
