package com.meanstddev;

import java.io.IOException;

import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class MyMapper extends Mapper<Object, Text,IntWritable, FloatWritable> {
	
	private IntWritable outMovieId = new IntWritable();
	private FloatWritable outMovieRating = new FloatWritable();
	
	public void map(Object key, Text value, Context context) throws IOException, InterruptedException{
		
		String fields[] = value.toString().split("::");

		outMovieId.set(Integer.parseInt(fields[1]));
		outMovieRating.set(Float.parseFloat(fields[2]));

        context.write(outMovieId, outMovieRating);
		
	}

}
