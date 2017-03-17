/**
 * Copyright (c) 2016, TP-Link Co.,Ltd.
 * Author: gaopengcheng <gaopengcheng@tp-link.net>
 * Created: 2016-11-26
 */
package com.ckcclc.kafka;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RejectedExecutionException;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tplink.cloud.common.config.constants.Keys;
import com.tplink.cloud.common.mq.kafka.KafkaConsumerBuilder;
import com.tplink.cloud.common.mq.kafka.config.AutoOffsetReset;
import com.tplink.cloud.common.mq.kafka.config.Deserializers;
import com.tplink.cloud.common.mq.kafka.core.CloudKafkaConsumer;
import com.tplink.cloud.kte.monitor.Monitor;
import com.tplink.cloud.kte.monitor.StatJmx;
import com.tplink.cloud.kte.task.LogFormationTask;
import com.tplink.cloud.kte.util.Config;
import com.tplink.cloud.kte.util.Constants;

public class PipeLine extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(PipeLine.class);
    private static final Config config = Config.INSTANCE;
    private static final StatJmx state = Monitor.INSTANCE.getState();
    private static final ForkJoinPool forkJoinPool = new ForkJoinPool();
    private static final int EXCEPTION_SLEEP_TIME = 3000; // 3 seconds

    private static final ThreadLocal<SimpleDateFormat> dateFormat = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
        }
    };

    private CloudKafkaConsumer<String, String> kafkaConsumer = null;
    private int pollTimeout = -1;
    private String topic;
    private String indexPrefix;
    private int threshold;

    public PipeLine(String topic) {
        this.topic = topic;
    }

    @Override
    public void run() {
        // 1. first initialize kafka consumer configuration
        try {
            initKafkaConsumer();
            logger.info("Kafka consumer configure initialized completed!");
        } catch (Exception e) {
            logger.error("Initialize kafka consumer error: ", e);
            return;
        }

        // 2. now back to business, start to pull messages from kafka and send to elasticsearch
        long count = 0;
        int period = config.getValue(Constants.KafkaKeys.KAFKA_DELAY_CHECK_PERIOD);
        long freshnessDate = config.getValue(Constants.Server.LOG_FRESHNESS_DATE_KEY);
        int taskThreshold = config.getValue(Constants.Server.TASK_REJECT_THRESHOLD);
        switchIndexPrefix();

        while (ServerState.INSTANCE.isRunning()) {
            try {
                ConsumerRecords<String, String> records = kafkaConsumer.poll(pollTimeout);

                // check whether it is timely to consume kafka messages
                if (count % period == 0 && records.iterator().hasNext()) {
                    String marker = records.iterator().next().value();
                    long delay = new Date().getTime() - getTime(marker);
                    state.getStats(topic + Constants.Monitor.KAFKA_CONSUME_DELAY)
                            .addRequest(delay * Constants.Server.TIME_FACTOR);

                    // protection from data flood, if message older than given threshold, just abandon it
                    if (delay > freshnessDate) {
                        logger.warn("Kafka batch: [batchSize={}, batchId={}, messageTime={}] abandoned.",
                                records.count(),
                                count,
                                dateFormat.get().format(new Date(getTime(marker))));
                        continue;
                    }
                }

                // if message is consumed timely, store into es, increase batch id
                count++;

                logger.debug("Kafka batch: [batchSize={}, batchId={}]", records.count(), count);

                for (TopicPartition partition : records.partitions()) {
                    LogFormationTask task = LogFormationTask.create(records.records(partition),
                            threshold, indexPrefix);

                    try {
                        if (forkJoinPool.getQueuedSubmissionCount() < taskThreshold) {
                            forkJoinPool.execute(task);
                        } else {
                            state.getStats(Constants.Monitor.KAFKA_FAILED_TASK)
                                    .addErrorRequest(Constants.Server.EXCEPTION_COST_TIME);
                            logger.warn("Task abandoned, sizeOfLogs={}", records.records(partition).size());
                        }
                    } catch (RejectedExecutionException e) {
                        logger.error("Log formation task rejected.", e);
                    }
                }
            } catch (Exception e) {
                // This case should never happen theoretically
                // Sleep 3 seconds, then continue; if this happen, it means something really bad happened,
                // catch only guarantee the program won't die;
                // When this happen, it may lead to data overstocked in kafka.
                state.getStats(Constants.Monitor.SERVER_FATAL).addErrorRequest(Constants.Server.EXCEPTION_COST_TIME);
                logger.error("Error occurs while transferring logs from kafka to es: ", e);
                try {
                    sleep(EXCEPTION_SLEEP_TIME);
                } catch (Exception ex) {
                    logger.error("Error occurs while trying to sleep the thread: ", ex);
                }
            }
        }

        kafkaConsumer.close();
    }

    /**
     * this method returns the time of the input log
     *
     * @param log format:"2016-12-01 15:43:38,959 | INFO  | main |..."
     * @return {@link Date#getTime()}
     */
    private long getTime(String log) {
        try {
            String time = log.split(Constants.Format.LOG_LINE_SEPARATOR, 2)[0];
            return dateFormat.get().parse(time).getTime();
        } catch (Exception e) {
            return new Date().getTime();
        }
    }

    private void switchIndexPrefix() {
        // only three topics are supported by now
        if (topic.contains(Constants.Server.CONNECTOR_KEYWORD)) {
            indexPrefix = Constants.ESKeys.CONNECTOR_INDEX_PREFIX;
        } else if (topic.contains(Constants.Server.APPSERVER_KEYWORD)) {
            indexPrefix = Constants.ESKeys.APPSERVER_INDEX_PREFIX;
        } else if (topic.contains(Constants.Server.APPSERVER_INTERNAL_KEYWORD)) {
            indexPrefix = Constants.ESKeys.APPSERVER_INTERNAL_INDEX_PREFIX;
        }
    }

    private void initKafkaConsumer() throws Exception {
        String brokers = config.getValue(Keys.KAFKA_ADDRESS);
        boolean isAutoCommitEnabled = config.getValue(Keys.KAFKA_CONSUMER_AUTO_COMMIT_ENABLED);
        int commitInterval = config.getValue(Keys.KAFKA_CONSUMER_AUTO_COMMIT_INTERVAL_MILLIS);
        int sessionTimeout = config.getValue(Keys.KAFKA_CONSUMER_SESSION_TIMEOUT_MILLIS);
        int maxFetchBytes = config.getValue(Keys.KAFKA_CONSUMER_MAX_PARTITION_FETCH_BYTES);
        int minFetchBytes = config.getValue(Constants.KafkaKeys.KAFKA_CONSUMER_FETCH_MIN_BYTES);

        pollTimeout = config.getValue(Constants.KafkaKeys.KAFKA_CONSUMER_POOL_TIMEOUT_MILLIS);

        logger.info("[{}={}]", Keys.KAFKA_ADDRESS, brokers);
        logger.info("[{}={}]", Constants.KafkaKeys.KAFKA_CONSUMER_GROUP_ID, topic); // use topic as group id
        logger.info("[{}={}]", Keys.KAFKA_CONSUMER_AUTO_COMMIT_ENABLED, isAutoCommitEnabled);
        logger.info("[{}={}]", Keys.KAFKA_CONSUMER_AUTO_COMMIT_INTERVAL_MILLIS, commitInterval);
        logger.info("[{}={}]", Keys.KAFKA_CONSUMER_SESSION_TIMEOUT_MILLIS, sessionTimeout);
        logger.info("[{}={}]", Keys.KAFKA_CONSUMER_MAX_PARTITION_FETCH_BYTES, maxFetchBytes);
        logger.info("[{}={}]", Constants.KafkaKeys.KAFKA_CONSUMER_FETCH_MIN_BYTES, minFetchBytes);
        logger.info("[{}={}]", Constants.KafkaKeys.KAFKA_CONSUMER_POOL_TIMEOUT_MILLIS, pollTimeout);

        kafkaConsumer = KafkaConsumerBuilder.builder().withBootstrapServers(brokers)
                .withGroupId(topic) // use topic as group id
                .withAutoCommit(isAutoCommitEnabled)
                .withKeyDeserializer(Deserializers.STRING)
                .withValueDeserializer(Deserializers.STRING)
                .withCommitIntervalMs(commitInterval)
                .withSessionimeoutMs(sessionTimeout)
                .withMaxPartitionFetchBytes(maxFetchBytes)
                .withAutoOffsetReset(AutoOffsetReset.LATEST)
                .withParam("fetch.min.bytes", String.valueOf(minFetchBytes))
                .build();

        // topic = config.getValue(Constants.KafkaKeys.KAFKA_CONSUMER_TOPIC);
        logger.info("[kafkaTopic={}]", topic);

        kafkaConsumer.subscribe(topic);

        threshold = config.getValue(Constants.Server.TASK_SPLIT_THRESHOLD);
    }
}
