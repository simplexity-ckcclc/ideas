package com.ckcclc.hadoop.wordcount;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * Created by ckcclc on 04/09/2017.
 */
public class Main extends Configured implements Tool {

    public static void main(String[] args) throws Exception {
        int status = ToolRunner.run(new Main(), args);
        System.exit(status);
    }


    @Override
    public int run(String[] strings) throws Exception {
        Configuration configuration = getConf();
        Job job = Job.getInstance(configuration, "wordcount");
        job.setJarByClass(Main.class);

        job.setInputFormatClass(TextInputFormat.class);
        FileInputFormat.addInputPath(job, new Path(strings[0]));

        job.setOutputFormatClass(TextOutputFormat.class);
        FileOutputFormat.setOutputPath(job, new Path(strings[1]));

        job.setMapperClass(CountMapper.class);
        job.setCombinerClass(CountReducer.class);
        job.setReducerClass(CountReducer.class);

        boolean status = job.waitForCompletion(true);
        return status ? 0 : 1;
    }
}
