package com.meanstddev;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * @author saurabhsalunkhe
 *
 */
public class Driver {

	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			System.err.println("Usage: MaxSubmittedCharge <input path> <output path>");
			System.exit(-1);
		}

		Path inputPath = new Path(args[0]);
		Path outputDir = new Path(args[1]);

		// Create configuration
		Configuration conf = new Configuration(true);

		// Create job
		Job job = new Job(conf, "MeanStdDev");
		job.setJarByClass(Driver.class);

		// Setup MapReduce
		job.setMapperClass(MyMapper.class);
		job.setReducerClass(MyReducer.class);

		// Specify key / value

		job.setMapOutputKeyClass(IntWritable.class);
		job.setMapOutputValueClass(FloatWritable.class);

		job.setOutputKeyClass(IntWritable.class);
		job.setOutputValueClass(MedianStdDevTuple.class);
		// Input
		FileInputFormat.addInputPath(job, inputPath);
		job.setInputFormatClass(TextInputFormat.class);

		// Output
		FileOutputFormat.setOutputPath(job, outputDir);

		// Delete output if exists
		FileSystem hdfs = FileSystem.get(conf);
		if (hdfs.exists(outputDir))
			hdfs.delete(outputDir, true);

		// Execute job
		int code = job.waitForCompletion(true) ? 0 : 1;
		System.exit(code);
	}
}
