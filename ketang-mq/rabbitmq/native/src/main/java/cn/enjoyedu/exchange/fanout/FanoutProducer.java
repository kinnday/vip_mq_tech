package cn.enjoyedu.exchange.fanout;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 *@author Mark老师   享学课堂 https://enjoy.ke.qq.com
 *往期视频咨询芊芊老师  QQ：2130753077  VIP课程咨询 依娜老师  QQ：2470523467
 *类说明：
 */
// fanout是广播交换器，与路由建没有关系， 绑定了路由建就可以收到消息！！！
public class FanoutProducer {

    public final static String EXCHANGE_NAME = "fanout_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        /**
         * 创建连接连接到MabbitMQ
         */
        ConnectionFactory factory = new ConnectionFactory();
        // 设置MabbitMQ所在主机ip或者主机名
        factory.setHost("10.45.4.97");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("123456");

        // 创建一个连接
        Connection connection = factory.newConnection();

        // 创建一个信道
        Channel channel = connection.createChannel();
        // 指定转发
//        fxc-!!!! 改变路由器的类型为 fanout
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

        String queueName = "producer_create";
        channel.queueDeclare(queueName,false,false,
                false,null);
        channel.queueBind(queueName,EXCHANGE_NAME,"test");

        //所有日志严重性级别
        String[] severities={"error","info","warning"};
        for(int i=0;i<3;i++){
            String severity = severities[i%3];//每一次发送一条不同严重性的日志

            // 发送的消息
            String message = "Hello World_"+(i+1);
            //参数1：exchange name
            //参数2：routing key
            channel.basicPublish(EXCHANGE_NAME, severity,
                    null, message.getBytes());
            System.out.println(" [x] Sent '" + severity +"':'"
                    + message + "'");
        }
        // 关闭频道和连接
        channel.close();
        connection.close();
    }

}
