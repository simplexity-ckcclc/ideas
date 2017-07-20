package com.ckcclc.hadoop.secondarysort;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

/**
 * Created by ckcclc on 20/07/2017.
 */
public class SortPartitioner extends Partitioner<CompositeKey, Text> {

    @Override
    public int getPartition(CompositeKey compositeKey, Text text, int i) {
        return Math.abs(compositeKey.hashCode() % i);
    }
}
