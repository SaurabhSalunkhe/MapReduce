package com.avgstockprice;

import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Reducer;

public class MyReducer extends Reducer<IntWritable, CountAverageTuple, IntWritable, CountAverageTuple> {

	private CountAverageTuple result = new CountAverageTuple();
	
	public void reduce(IntWritable key, Iterable<CountAverageTuple> values, Context context) throws IOException, InterruptedException{
		float sum = 0;
		long count = 0;
		
		for (CountAverageTuple value : values) {
			sum += value.getCount() * value.getAverage();
			count += value.getCount();
		}
		
		result.setCount(count);
		result.setAverage(sum/count);

		context.write(key, result);
	}
}
