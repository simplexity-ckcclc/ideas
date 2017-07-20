package com.ckcclc.hadoop.secondarysort;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * Created by ckcclc on 20/07/2017.
 */
public class Main extends Configured implements Tool {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {

        int status = ToolRunner.run(new Main(), args);
        System.exit(status);

    }

    @Override
    public int run(String[] strings) throws Exception {
        Configuration configuration = getConf();
        Job job = Job.getInstance(configuration, "SecondarySort");
        job.setJarByClass(Main.class);

        job.setInputFormatClass(FileInputFormat.class);
        job.setOutputFormatClass(FileOutputFormat.class);

        FileInputFormat.addInputPath(job, new Path(strings[0]));
        FileOutputFormat.setOutputPath(job, new Path(strings[1]));

        job.setMapperClass(SortMapper.class);
        job.setMapOutputKeyClass(CompositeKey.class);
        job.setMapOutputValueClass(Text.class);

        job.setReducerClass(SortReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);

        job.setPartitionerClass(SortPartitioner.class);
        job.setGroupingComparatorClass(SortGroupingComparator.class);

        boolean successful = job.waitForCompletion(true);
        return successful ? 0 : 1;
    }
}
