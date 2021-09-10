package com.eshop.cache.kafka;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * * kafka消费者
 * 2、编写业务逻辑
 *
 * （1）两种服务会发送来数据变更消息：商品信息服务，商品店铺信息服务，每个消息都包含服务名以及商品id
 *
 * （2）接收到消息之后，根据商品id到对应的服务拉取数据，这一步，我们采取简化的模拟方式，就是在代码里面写死，会获取到什么数据，不去实际再写其他的服务去调用了
 *
 * （3）商品信息：id，名称，价格，图片列表，商品规格，售后信息，颜色，尺寸
 *
 * （4）商品店铺信息：其他维度，用这个维度模拟出来缓存数据维度化拆分，id，店铺名称，店铺等级，店铺好评率
 *
 * （5）分别拉取到了数据之后，将数据组织成json串，然后分别存储到ehcache中，和redis缓存中
 *
 * @author: RenLiLi
 * @date: 2021/9/9 14:11
 */
public class KafkaConsumer implements Runnable {

    private ConsumerConnector consumerConnector;
    private String topic;

    public KafkaConsumer(String topic) {
        this.consumerConnector = Consumer.createJavaConsumerConnector(
                createConsumerConfig());
        this.topic = topic;
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        Map<String, Integer> topicCountMap = new HashMap<>();
        topicCountMap.put(topic, 1);

        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap =
                consumerConnector.createMessageStreams(topicCountMap);
        List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(topic);

        for (KafkaStream stream : streams) {
            new Thread(new KafkaMessageProcessor(stream)).start();
        }
    }

    /**
     * 创建kafka cosumer config
     * @return
     */
    private static ConsumerConfig createConsumerConfig() {
        Properties props = new Properties();
        props.put("zookeeper.connect", "192.168.31.187:2181,192.168.31.19:2181,192.168.31.227:2181");
        props.put("group.id", "eshop-cache-group");
        props.put("zookeeper.session.timeout.ms", "40000");
        props.put("zookeeper.sync.time.ms", "200");
        props.put("auto.commit.interval.ms", "1000");
        return new ConsumerConfig(props);
    }
}
