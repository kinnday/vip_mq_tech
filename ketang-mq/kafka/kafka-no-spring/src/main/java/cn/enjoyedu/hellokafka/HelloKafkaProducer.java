package cn.enjoyedu.hellokafka;

import cn.enjoyedu.config.BusiConst;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * @author Mark老师   享学课堂 https://enjoy.ke.qq.com
 * 往期课程咨询芊芊老师  QQ：2130753077 VIP课程咨询 依娜老师  QQ：2133576719
 * 类说明：kafak生产者
 */
public class HelloKafkaProducer {

    public static void main(String[] args) {

        Properties properties = new Properties();
//      单机； 集群的时候 英文逗号分隔即可
        properties.put("bootstrap.servers","59.110.139.17:9092");
//        properties.put("bootstrap.servers","127.0.0.1:9092");
//      键的序列化，后面的值必须实现kaka的Serializer 接口
        properties.put("key.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
//      值的序列化，
        properties.put("value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
//      有了前面的三个参数，就可以创建生产者
        KafkaProducer<String,String> producer
                = new KafkaProducer<String, String>(properties);
        try {
            ProducerRecord<String,String> record;
            try {
//              创建消息： 主体+键+值
                record = new ProducerRecord<String,String>(BusiConst.HELLO_TOPIC,
                        "teacher02","lison");
                producer.send(record);
                System.out.println("message is sent.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } finally {
            producer.close();
        }
    }


}
