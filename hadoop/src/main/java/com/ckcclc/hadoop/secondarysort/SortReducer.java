package com.ckcclc.hadoop.secondarysort;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * Created by ckcclc on 20/07/2017.
 */
public class SortReducer extends Reducer<CompositeKey, Text, Text, IntWritable> {

    @Override
    protected void reduce(CompositeKey key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

        Integer sum = 0;

        for (Text value : values) {
            String[] strings = value.toString().split(",");

            if (strings.length != 2) {
                throw new RuntimeException("wrong input format");
            }

            sum += Integer.valueOf(strings[1]);
        }

        context.write(new Text(key.getFirst()), new IntWritable(sum));
    }
}
