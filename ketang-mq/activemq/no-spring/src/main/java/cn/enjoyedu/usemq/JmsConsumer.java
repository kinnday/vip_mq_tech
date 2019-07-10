package cn.enjoyedu.usemq;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 *@author Mark老师   享学课堂 https://enjoy.ke.qq.com
 *往期视频咨询芊芊老师  QQ：2130753077  VIP课程咨询 依娜老师QQ：2133576719
 *类说明：消费者端-同步接收
 */
public class JmsConsumer {
    /*默认连接用户名*/
    private static final String USERNAME
            = ActiveMQConnection.DEFAULT_USER;
    /* 默认连接密码*/
    private static final String PASSWORD
            = ActiveMQConnection.DEFAULT_PASSWORD;
    /* 默认连接地址*/
    private static final String BROKEURL
            = ActiveMQConnection.DEFAULT_BROKER_URL;

    public static void main(String[] args) {
        /* 1 连接工厂*/
        ConnectionFactory connectionFactory;
        /* 2 连接*/
        Connection connection = null;
        /* 3 会话*/
        Session session;
        /* 4 消息的目的地*/
        Destination destination;
        /* 5 消息的消费者*/
        MessageConsumer messageConsumer;

        /* 实例化连接工厂*/
        connectionFactory
                = new ActiveMQConnectionFactory(USERNAME,PASSWORD,BROKEURL);

        try {
            /* 通过连接工厂获取连接*/
            connection = connectionFactory.createConnection();
            /* 启动连接*/
            connection.start();
            /* 创建session*/
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            /* 创建一个名为HelloWorld消息队列*/
            destination = session.createTopic("HelloActiveMq");
//            destination = session.createQueue("HelloActiveMqQueue");
            /* 6 创建消息消费者*/
            messageConsumer = session.createConsumer(destination);
//          消息接收有两种方式： 阻塞方式； 异步方式
            Message message;
            while((message = messageConsumer.receive())!=null){
                System.out.println(((TextMessage)message).getText());
            }

        } catch (JMSException e) {
            e.printStackTrace();
        }finally {
            if(connection!=null){
                try {
                    connection.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
