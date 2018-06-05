package com.ckcclc.spark.structured;

import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.sql.*;
import org.apache.spark.sql.streaming.StreamingQuery;
import org.apache.spark.sql.streaming.StreamingQueryException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import scala.Tuple2;

import static org.apache.spark.sql.functions.col;

/**
 * Author:  huangyucong <huangyucong@sensetime.com>
 * Created: 27/04/2018
 */
public class StructuredStreaming {

    public static void main(String[] args) throws StreamingQueryException {
//        wordCount();
        wordCountWindow();
    }

    // nc -lk 9527
    private static void wordCount() throws StreamingQueryException {
        SparkSession sparkSession = SparkSession.builder().master("local").appName("word count").getOrCreate();

        Dataset<Row> ds = sparkSession.readStream()
                .format("socket")
                .option("host", "localhost")
                .option("port", "9527")
                .load();



        Dataset<Row> words = ds.as(Encoders.STRING())
                .flatMap((FlatMapFunction<String, String>) line -> Arrays.asList(line.split(" ")).iterator(), Encoders.STRING())
                .groupBy("value")
                .count();

        StreamingQuery query = words.writeStream()
                .format("console")
                .outputMode("complete")
                .start();

        query.awaitTermination();
    }

    // nc -lk 9527
    private static void wordCountWindow() throws StreamingQueryException {
        SparkSession sparkSession = SparkSession.builder().master("local").appName("word count").getOrCreate();

        Dataset<Row> ds = sparkSession.readStream()
                .format("socket")
                .option("host", "localhost")
                .option("port", "9527")
                .option("includeTimestamp", true)
                .load();

        Dataset<Row> lines = ds.as(Encoders.tuple(Encoders.STRING(), Encoders.TIMESTAMP()))
                .toDF("line", "timestamp");

        Dataset<Row> words = lines
                .flatMap((FlatMapFunction<Row, Tuple2<String, Timestamp>>) tuple -> {
                    List<Tuple2<String, Timestamp>> results = new ArrayList<>();
                    for (String word : ((String) tuple.get(0)).split(" ")) {
                        results.add(new Tuple2<>(word, (Timestamp) tuple.get(1)));
                    }
                    return results.iterator();
                }, Encoders.tuple(Encoders.STRING(), Encoders.TIMESTAMP()))
                .toDF("word", "timestamp");

        Dataset<Row> wordCounts = words
                .withWatermark("timestamp", "5 seconds")
                .groupBy(functions.window(col("timestamp"), "5 seconds", "3 seconds"),
                        words.col("word"))
                .count();

        StreamingQuery query = wordCounts.writeStream()
                .format("console")
                .outputMode("append")
                .option("truncate", false)
                .start();

        query.awaitTermination();
    }

}
