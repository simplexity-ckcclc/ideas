package com.ckcclc.spark;

import com.google.common.collect.Sets;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import scala.Tuple2;

/**
 * Author:  huangyucong <huangyucong@sensetime.com>
 * Created: 21/04/2018
 */
public class SparkStreaming {

    private static final Logger logger = LoggerFactory.getLogger(SparkStreaming.class);

    public static void main(String[] args) throws InterruptedException {
        logger.info("Starting...");
        countWord();
//        countWordKafka();
    }

    // nc -lk 9527
    private static void countWord() throws InterruptedException {
        SparkConf sparkConf = new SparkConf().setAppName("count word").setMaster("local[2]");
        JavaSparkContext sc = new JavaSparkContext(sparkConf);
        sc.setLogLevel("WARN");
        JavaStreamingContext ssc = new JavaStreamingContext(sc, Durations.seconds(10L));

        JavaInputDStream<String> inputDStream = ssc.socketTextStream("localhost", 9527);

        JavaPairDStream<String, Integer> counts = inputDStream.flatMap(line -> Arrays.asList(line.split(" ")).iterator())
                .mapToPair(word -> new Tuple2<>(word, 1))
                .reduceByKey((x, y) -> x + y);

        counts.print();

        ssc.start();
        ssc.awaitTermination();
    }

    private static void countWordKafka() throws InterruptedException {
        SparkConf sparkConf = new SparkConf().setAppName("count word").setMaster("local[2]");
        JavaSparkContext sc = new JavaSparkContext(sparkConf);
        sc.setLogLevel("WARN");
        JavaStreamingContext ssc = new JavaStreamingContext(sc, Durations.seconds(10L));

        String topic = "spark";
        String brokers = "localhost:9092";
        Map<String, Object> kafkaParams = new HashMap<>();
        kafkaParams.put("metadata.broker.list", brokers) ;
        kafkaParams.put("bootstrap.servers", brokers);
        kafkaParams.put("group.id", "spark");
        kafkaParams.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        kafkaParams.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        kafkaParams.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        //Topic分区
        Map<TopicPartition, Long> offsets = new HashMap<>();
        offsets.put(new TopicPartition("spark", 0), 2L);

        //通过KafkaUtils.createDirectStream(...)获得kafka数据，kafka相关参数由kafkaParams指定
        JavaInputDStream<ConsumerRecord<Object,Object>> inputDStream = KafkaUtils.createDirectStream(
                ssc,
                LocationStrategies.PreferConsistent(),
                ConsumerStrategies.Subscribe(Sets.newHashSet(topic.split(",")), kafkaParams, offsets)
        );

        JavaPairDStream<String, Integer> counts = inputDStream.flatMap(line ->
                Arrays.asList((line.value().toString()).split(" ")).iterator())
                .mapToPair(word -> new Tuple2<>(word, 1))
                .reduceByKey((x, y) -> x + y);

        counts.print();

        ssc.start();
        ssc.awaitTermination();
    }



}
