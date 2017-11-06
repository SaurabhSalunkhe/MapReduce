package com.memmeanstddev;

import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.SortedMapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class MyMapper extends Mapper<LongWritable, Text, LongWritable, SortedMapWritable> {

	private LongWritable outmovieId = new LongWritable();
	private LongWritable count = new LongWritable(1);
	private SortedMapWritable cwo = new SortedMapWritable();
	private DoubleWritable rating = new DoubleWritable();

	protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

		try {
			String[] str = value.toString().split("::");
			outmovieId.set(Long.parseLong(str[1]));
			rating.set(Double.parseDouble(str[2]));

			cwo.put(rating, count);
			context.write(outmovieId, cwo);

		} catch (Exception e) {
		}
	}

}
