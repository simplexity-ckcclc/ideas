package com.ckcclc.kafka;

import com.tplink.cloud.common.mq.kafka.KafkaProducerBuilder;
import com.tplink.cloud.common.mq.kafka.config.Serializers;
import com.tplink.cloud.common.mq.kafka.core.CloudKafkaProducer;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by admin on 2017/1/17.
 */
public class Producer {

    public static final Logger logger = LoggerFactory.getLogger(Producer.class);

    public static void main(String[] args) {
        CloudKafkaProducer<Integer, String> kafkaProducer
                = KafkaProducerBuilder.builder()
                .withServer("192.168.8.128:9092")
                .withKeySerializer(Serializers.INTEGET)
                .withValueSerializer(Serializers.STRING)
                .build();
//        try {
//            for (int i = 0; i < 10; i++) {
//                JSONObject msg = new JSONObject();
//                msg.put("index", i);
//                kafkaProducer.send("kafka.example", i, msg.toString());
//                logger.info("send data:{}", msg);
//            }
//            kafkaProducer.flush();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        JSONObject msg = new JSONObject();
        msg.put("msgLevel", "HIGH");
        msg.put("msgType", "COMMON");
        msg.put("data", new JSONObject().put("key", "val"));
        try {
            kafkaProducer.send("kafka.example", msg.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        kafkaProducer.close();
    }

}
