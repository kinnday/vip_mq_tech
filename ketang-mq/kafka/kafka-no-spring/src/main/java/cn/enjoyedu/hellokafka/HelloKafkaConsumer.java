package cn.enjoyedu.hellokafka;

import cn.enjoyedu.config.BusiConst;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Collections;
import java.util.Properties;

/**
 * @author Mark老师   享学课堂 https://enjoy.ke.qq.com
 * 往期课程咨询芊芊老师  QQ：2130753077 VIP课程咨询 依娜老师  QQ：2133576719
 * 类说明：
 */
public class HelloKafkaConsumer {

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put("bootstrap.servers","59.110.139.17:9092");
//        properties.put("bootstrap.servers","127.0.0.1:9092");
//      键值的反序列化，必须实现kafka的Deserializer 接口
        properties.put("key.deserializer",
                StringDeserializer.class);
        properties.put("value.deserializer",
                StringDeserializer.class);
//      配置消费者-群组
        properties.put(ConsumerConfig.GROUP_ID_CONFIG,"test");
//      创建消费者实例
        KafkaConsumer<String,String> consumer
                = new KafkaConsumer<String, String>(properties);
        try {
//          消费者订阅主题
            consumer.subscribe(Collections.singletonList(BusiConst.HELLO_TOPIC));
            while(true){
//              kafka 只有拉取模式，没有主动推送模式！！！
//              500毫秒拉取一次
                ConsumerRecords<String, String> records = consumer.poll(500);
                for(ConsumerRecord<String, String> record:records){
                    System.out.println(String.format("topic:%s,分区：%d,偏移量：%d," +
                            "key:%s,value:%s",record.topic(),record.partition(),
                            record.offset(),record.key(),record.value()));
                    //do my work
                }
            }
        } finally {
            consumer.close();

        }

    }




}
