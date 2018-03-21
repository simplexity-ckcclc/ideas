package com.ckcclc.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.Metric;
import org.apache.kafka.common.MetricName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Properties;

/**
 * Created by admin on 2017/1/17.
 */
public class Producer {

    public static final Logger logger = LoggerFactory.getLogger(Producer.class);

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        KafkaProducer<String, String> producer = new KafkaProducer<>(props);

        Map<MetricName, ? extends Metric> metrics = producer.metrics();
        System.out.println(metrics);

        String topic = "SensefacePush";
        String data = "{\"platform\":\"SVP\",\"index\":\"xxx-12345677\",\"metaType\":\"NEW\",\"image\":{\"url\":\"http://xxx.jpg\",\"width\":789,\"height\":456},\"camera\":{\"id\":123,\"serial\":\"camera-serial\",\"regionId\":12312321,\"cameraId\":123456,\"name\":\"xxxx\",\"code\":\"xxxx\",\"type\":1,\"position\":{\"longitude\":123.2321,\"latitude\":123.12},\"address\":\"xxxx\",\"path\":\"xxxxx\",\"affiliation\":[1,2,3,4],\"group\":{\"id\":123,\"serial\":\"xxxx\",\"name\":\"xxxx\",\"zoomLevel\":12,\"position\":{\"longitude\":123.2321,\"latitude\":123.12},\"path\":\"xxxxx\"}},\"capturedTime\":1233898089,\"receivedTime\":1232323132,\"faceInfo\":{\"image\":{\"url\":\"http://xxx.jpg\",\"width\":789,\"height\":456},\"quality\":0.87,\"start\":{\"x\":123,\"y\":234},\"end\":{\"x\":123,\"y\":234},\"trackId\":123456,\"metaType\":\"NEW\",\"angle\":{\"yaw\":0.23,\"pitch\":0.23,\"roll\":0.23},\"ageStage\":\"AGEDNESS\",\"gender\":\"MALE\",\"attribute\":\"EMOTION_ANGER\"},\"taskInfos\":[{\"index\":\"xxxx\",\"id\":\"xxxx\",\"serial\":\"task-serial\",\"name\":\"xxxx\",\"metaType\":\"NEW\",\"detectType\":\"DETECT\",\"recognisedInfos\":{\"targetLibrary\":{\"name\":\"xxxx\",\"serial\":\"xxxx\",\"attibute\":1,\"type\":1,\"id\":1,\"isPrivate\":1,\"platformType\":1,\"ip\":\"172.18.0.109\",\"port\":12312},\"similars\":[{\"target\":{\"id\":123,\"name\":\"xxxx\",\"serial\":\"xxxx\",\"aliasname\":\"xxxx\",\"url\":\"xxxx\",\"identityId\":\"xxxx\",\"tarLibSerial\":\"xxxx\",\"tarLibName\":\"xxxx\",\"tarAttribute\":1,\"gender\":1,\"nationality\":\"xxxx\",\"nation\":\"xxxx\",\"snative\":\"xxxx\",\"birthday\":12321321312321,\"address\":\"xxxx\",\"level\":1,\"createTime\":12312312},\"score\":0.87}]},\"taskStatistics\":{\"id\":\"xxxx\",\"serial\":\"xxxx\",\"name\":\"xxxx\",\"detectCount\":120,\"alarmCount\":120,\"detectAllCount\":120,\"alarmAllCount\":120}}],\"cameraStatistics\":{\"id\":123456,\"serial\":\"xxxx\",\"name\":\"xxxx\",\"detectCount\":120,\"alarmCount\":120,\"detectAllCount\":120,\"alarmAllCount\":120},\"totalStatistics\":{\"allDetectCount\":123,\"detectCount\":123,\"allAlarmCount\":123,\"alarmCount\":123}}";


        while (true) {
            producer.send(new ProducerRecord<>(topic, data));
        }

//        producer.close();
    }

}
