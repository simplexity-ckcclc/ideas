/**
 * Author:  huangyucong <huangyucong@sensetime.com>
 * Created: 18-3-19
 */

package com.ckcclc.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class Consumer implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(Consumer.class);

    private KafkaConsumer<Integer, String> consumer;
    private String topic;

    Consumer(String brokers, String topic, String groupId) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.IntegerDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");

        consumer = new KafkaConsumer<>(props);
        this.topic = topic;
    }

    @Override
    public void run() {
        while (true) {
            doWork();
        }
    }

    private void doWork() {
        consumer.subscribe(Collections.singletonList(this.topic));
        ConsumerRecords<Integer, String> records = consumer.poll(1000);
        for (ConsumerRecord<Integer, String> record : records) {
            logger.info("Topic:{} received message: ({},{}) at offset:{} ",
                    topic, record.key(), record.value(), record.offset());
        }
    }

    public static void main(String[] args) {
        String groupId = "ckcclcConsumer";
//        String brokers = args[0];
        String brokers = "172.18.0.119:10209";
        String topics = "SensefacePush";
//        String topics = args[1];

        List<String> topicList = Arrays.asList(topics.split(","));

        for (String topic : topicList) {
            Thread thread = new Thread(new Consumer(brokers, topic, groupId), "consumer-thread-" + topic);
            thread.start();
            logger.info("start consume topic:{}", topic);
        }

        while (true) {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
