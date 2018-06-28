package com.ckcclc.flink;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.Kafka010JsonTableSink;
import org.apache.flink.streaming.connectors.kafka.Kafka011JsonTableSource;
import org.apache.flink.streaming.connectors.kafka.KafkaTableSource;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.TableSchema;
import org.apache.flink.table.api.Types;
import org.apache.flink.table.api.java.StreamTableEnvironment;

import java.util.Properties;

/**
 * Author:  huangyucong <huangyucong@sensetime.com>
 * Created: 13/06/2018
 */
public class KafkaTableCep {

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();
        StreamTableEnvironment tableEnvironment = TableEnvironment.getTableEnvironment(env);

        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "localhost:9092");
        properties.setProperty("group.id", "flink-cep");

        KafkaTableSource source = Kafka011JsonTableSource.builder()
                .forTopic("flink")
                .withKafkaProperties(properties)
                .withSchema(TableSchema.builder()
                        .field("rackID", Types.INT())
                        .field("containerID", Types.INT())
                        .field("temperature", Types.DOUBLE())
                        .build())
                .build();

        tableEnvironment.registerTableSource("monitor", source);
        Table monitor = tableEnvironment.scan("monitor");
//        Table temp = monitor.filter("rackID == 1").groupBy("containerID").select("containerID, temperature.sum AS temp");
        Table temp = monitor.filter("rackID == 1").select("temperature AS temp");

        Properties props = new Properties();
        props.setProperty("bootstrap.servers", "localhost:9092");
        temp.writeToSink(new Kafka010JsonTableSink(
                "temp",                // Kafka topic to write to
                props));                  // Properties to configure the producer);

        env.execute();
    }


}
