package com.ckcclc.kafka;

import com.tplink.cloud.common.mq.kafka.KafkaConsumerBuilder;
import com.tplink.cloud.common.mq.kafka.config.Deserializers;
import com.tplink.cloud.common.mq.kafka.core.CloudKafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by admin on 2017/2/13.
 */
public class Consumer {

    public static final Logger logger = LoggerFactory.getLogger(Consumer.class);

    public static void main(String[] args) {
        CloudKafkaConsumer<String, String> kafkaConsumer = null;
        try {
            kafkaConsumer = KafkaConsumerBuilder.builder().withBootstrapServers("192.168.8.128:9092")
                    .withGroupId("ckcclc")
                    .withAutoCommit(true)
                    .withKeyDeserializer(Deserializers.STRING)
                    .withValueDeserializer(Deserializers.STRING)
                    .withCommitIntervalMs(5000)
                    .withSessionimeoutMs(30000)
                    .build();
        } catch (Exception e) {
            logger.error("exception caught", e);
        }

        kafkaConsumer.subscribe("test", "kafka.example");
        logger.info("consumer init");

        while (true) {
            ConsumerRecords<String, String> records = kafkaConsumer.poll(1000);
            for (ConsumerRecord<String, String> record : records) {
                logger.info("record:{}", record.toString());
            }
        }
    }


}
