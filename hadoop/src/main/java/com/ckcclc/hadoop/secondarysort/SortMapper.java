package com.ckcclc.hadoop.secondarysort;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * Created by ckcclc on 20/07/2017.
 */
public class SortMapper extends Mapper<LongWritable, Text, CompositeKey, Text> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

        String[] strings = value.toString().split("\t");

        if (strings.length != 2) {
            throw new RuntimeException("wrong input format");
        }

        context.write(new CompositeKey(strings[0], strings[1]), value);
    }
}
