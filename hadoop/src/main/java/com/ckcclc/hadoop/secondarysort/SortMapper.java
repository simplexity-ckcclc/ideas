package com.ckcclc.hadoop.secondarysort;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by ckcclc on 20/07/2017.
 */
public class SortMapper extends Mapper<LongWritable, Text, CompositeKey, Text> {

    private static final Logger logger = LoggerFactory.getLogger(SortMapper.class);

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

        String[] strings = value.toString().split(",");

        if (strings.length != 2) {
            logger.error("wrong input format, size:{}, text:{}", strings.length, value.toString());
            System.out.println("wrong input format, size:" + strings.length + ", text:" + value.toString());
            throw new RuntimeException("wrong input format");
        }

        context.write(new CompositeKey(strings[0], strings[1]), value);
    }
}
