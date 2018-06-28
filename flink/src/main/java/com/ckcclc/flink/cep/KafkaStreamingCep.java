package com.ckcclc.flink.cep;

import com.alibaba.fastjson.JSON;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Author:  huangyucong <huangyucong@sensetime.com>
 * Created: 13/06/2018
 */
public class KafkaStreamingCep {

    private static final Logger logger = LoggerFactory.getLogger(KafkaStreamingCep.class);

    private static final double TEMPERATURE_THRESHOLD = 30.0;

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "localhost:9092");
        properties.setProperty("group.id", "flink-cep");

        FlinkKafkaConsumer011<String> consumer = new FlinkKafkaConsumer011<>("flink",
                new SimpleStringSchema(), properties);
        DataStream<String> source = env.addSource(consumer);
        DataStream<TemperatureEvent> events = source
                .map(string -> JSON.parseObject(string, TemperatureEvent.class))
                .keyBy("rackID");

        events.print().setParallelism(1);

        Pattern<TemperatureEvent, ?> pattern = Pattern.<TemperatureEvent>begin("First Event")
            .subtype(TemperatureEvent.class)
            .where(new SimpleCondition<TemperatureEvent>() {
                @Override
                public boolean filter(TemperatureEvent event) {
                    return event.getTemperature() > TEMPERATURE_THRESHOLD;
                }
            }).next("Second Event")
            .subtype(TemperatureEvent.class)
            .where(new SimpleCondition<TemperatureEvent>() {
                @Override
                public boolean filter(TemperatureEvent event) {
                    return event.getTemperature() > TEMPERATURE_THRESHOLD;
                }
            }).within(Time.seconds(10));

        PatternStream<TemperatureEvent> patternStream = CEP.pattern(events, pattern);
        DataStream<TemperatureWarning> warnings = patternStream.select(
            new PatternSelectFunction<TemperatureEvent, TemperatureWarning>() {
                @Override
                public TemperatureWarning select(Map<String, List<TemperatureEvent>> pattern) throws Exception {
                    TemperatureEvent first = pattern.get("First Event").get(0);
                    TemperatureEvent second = pattern.get("Second Event").get(0);
                    return new TemperatureWarning(first.getRackID(),
                            (first.getTemperature() + second.getTemperature()) / 2);
                }
            }
        );

        warnings.print().setParallelism(1);

        env.execute("Kafka CEP");
    }

}
