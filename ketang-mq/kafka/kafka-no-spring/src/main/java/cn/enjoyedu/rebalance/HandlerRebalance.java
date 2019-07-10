package cn.enjoyedu.rebalance;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Mark老师   享学课堂 https://enjoy.ke.qq.com
 * 往期课程咨询芊芊老师  QQ：2130753077 VIP课程咨询 依娜老师  QQ：2133576719
 * 类说明：再均衡监听器
 */
public class HandlerRebalance {

    /*模拟一个保存分区偏移量的数据库表*/
    public final static ConcurrentHashMap<TopicPartition,Long>
            partitionOffsetMap = new ConcurrentHashMap<TopicPartition,Long>();

    private final Map<TopicPartition, OffsetAndMetadata> currOffsets;
    private final KafkaConsumer<String,String> consumer;

    public HandlerRebalance(Map<TopicPartition, OffsetAndMetadata> currOffsets,
                            KafkaConsumer<String, String> consumer) {
        this.currOffsets = currOffsets;
        this.consumer = consumer;
    }

    public void onPartitionsRevoked(
            Collection<TopicPartition> partitions) {
        final String id = Thread.currentThread().getId()+"";
        System.out.println(id+"-onPartitionsRevoked参数值为："+partitions);
        System.out.println(id+"-服务器准备分区再均衡，提交偏移量。当前偏移量为："
                +currOffsets);
        //TODO
        System.out.println("分区偏移量表中："+partitionOffsetMap);
    }

    public void onPartitionsAssigned(
            Collection<TopicPartition> partitions) {
        final String id = Thread.currentThread().getId()+"";
        System.out.println(id+"-再均衡完成，onPartitionsAssigned参数值为："+partitions);
        System.out.println("分区偏移量表中："+partitionOffsetMap);
        //TODO

    }
}
