package com.ckcclc.hadoop.secondarysort;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

/**
 * Created by ckcclc on 20/07/2017.
 */
public class SortGroupingComparator extends WritableComparator {

    @Override
    public int compare(WritableComparable a, WritableComparable b) {
        CompositeKey keyA = (CompositeKey) a;
        CompositeKey keyB = (CompositeKey) b;

        return keyA.getFirst().compareTo(keyB.getFirst());
    }
}
