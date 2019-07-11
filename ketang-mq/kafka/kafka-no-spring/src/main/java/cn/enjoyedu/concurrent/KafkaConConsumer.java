package cn.enjoyedu.concurrent;

import cn.enjoyedu.config.BusiConst;
import cn.enjoyedu.config.KafkaConst;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Mark老师   享学课堂 https://enjoy.ke.qq.com
 * 往期课程咨询芊芊老师  QQ：2130753077 VIP课程咨询 依娜老师  QQ：2133576719
 * 类说明：多线程下正确的使用消费者
 */
public class KafkaConConsumer {

//  首先定义一个线程池
    private static ExecutorService executorService
            = Executors.newFixedThreadPool(
                    BusiConst.CONCURRENT_PARTITIONS_COUNT);

    private static class ConsumerWorker implements Runnable{

        private KafkaConsumer<String,String> consumer;

        //fxc
        public ConsumerWorker(KafkaConsumer<String, String> consumer) {
            this.consumer = consumer;
        }

        @Override
        public void run() {
            final String id = Thread.currentThread().getId()
                    +"-"+System.identityHashCode(consumer);
            try {
                while(true){
                    ConsumerRecords<String, String> records
                            = consumer.poll(500);
                    for(ConsumerRecord<String, String> record:records){
                        System.out.println(id+"|"+String.format(
                                "主题：%s，分区：%d，偏移量：%d，" +
                                        "key：%s，value：%s",
                                record.topic(),record.partition(),
                                record.offset(),record.key(),record.value()));
                        //do our work
                    }
                }
            } finally {
                consumer.close();
            }
        }
    }

    public static void main(String[] args) {
        /*消费配置的实例*/
        Map<String, Object> config
                = KafkaConst.consumerConfigMap("concurrent",
                StringDeserializer.class,
                StringDeserializer.class);

        for(int i = 0; i<BusiConst.CONCURRENT_PARTITIONS_COUNT; i++){
            //TODO
            KafkaConsumer<String,String> consumer
                    = new KafkaConsumer<String, String>(config);
            consumer.subscribe(Collections.singletonList(BusiConst.CONCURRENT_USER_INFO_TOPIC) );
            executorService.submit(new ConsumerWorker(consumer));
        }
//        executorService.shutdown();
    }




}
