package com.ckcclc.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.broadcast.Broadcast;

import java.util.Arrays;

import scala.Tuple2;

/**
 * Author:  huangyucong <huangyucong@sensetime.com>
 * Created: 14/04/2018
 */
public class Spark {

    public static void main(String[] args) {
//        join();
        joinBroadcast();
    }

    private static void countWord() {
        SparkConf sparkConf = new SparkConf().setAppName("word count");
        JavaSparkContext sc = new JavaSparkContext(sparkConf);
        JavaRDD<String> lines = sc.textFile("/Users/ckcclc/tmp/spark/wordcount");
        lines.flatMap(line -> Arrays.asList(line.split(" ")).iterator())
                .mapToPair(word -> new Tuple2<>(word, 1))
                .reduceByKey((x, y) -> x + y)
                .mapToPair(pair -> new Tuple2<>(pair._2, pair._1))
                .sortByKey(false)
                .mapToPair(pair -> new Tuple2<>(pair._2, pair._1))
                .collect()
                .forEach(pair -> System.out.println(pair._1 + ":" + pair._2));
    }

    private static void join() {
        SparkConf sparkConf = new SparkConf().setMaster("local").setAppName("join");
        JavaSparkContext sc = new JavaSparkContext(sparkConf);
        JavaPairRDD<String, String> jobs = sc.textFile("/Users/ckcclc/tmp/spark/jobs")
                .mapToPair(line -> {
                    String[] words = line.split(" ");
                    return new Tuple2<>(words[0], words[1]);
                }).cache();
        JavaPairRDD<String, String> salaries = sc.textFile("/Users/ckcclc/tmp/spark/salaries")
                .mapToPair(line -> {
                    String[] words = line.split(" ");
                    return new Tuple2<>(words[0], words[1]);
                }).cache();
        jobs.join(salaries).foreach(pair -> System.out.println(pair._1 + ":" + pair._2));
        jobs.leftOuterJoin(salaries).foreach(pair -> System.out.println(pair._1 + ":" + pair._2));
        jobs.rightOuterJoin(salaries).foreach(pair -> System.out.println(pair._1 + ":" + pair._2));

    }

    private static void joinBroadcast() {
        SparkConf sparkConf = new SparkConf().setMaster("local").setAppName("join broadcast");
        JavaSparkContext sc = new JavaSparkContext(sparkConf);
        JavaPairRDD<String, String> jobs = sc.textFile("/Users/ckcclc/tmp/spark/jobs")
                .mapToPair(line -> {
                    String[] words = line.split(" ");
                    return new Tuple2<>(words[0], words[1]);
                });
        Broadcast<JavaPairRDD<String, String>> jobsBroadcast = sc.broadcast(jobs);

        JavaPairRDD<String, String> salaries = sc.textFile("/Users/ckcclc/tmp/spark/salaries")
                .mapToPair(line -> {
                    String[] words = line.split(" ");
                    return new Tuple2<>(words[0], words[1]);
                }).cache();

        jobsBroadcast.value().join(salaries).foreach(pair -> System.out.println(pair._1 + ":" + pair._2));
        jobsBroadcast.value().leftOuterJoin(salaries).foreach(pair -> System.out.println(pair._1 + ":" + pair._2));
        jobsBroadcast.value().rightOuterJoin(salaries).foreach(pair -> System.out.println(pair._1 + ":" + pair._2));

    }

}
