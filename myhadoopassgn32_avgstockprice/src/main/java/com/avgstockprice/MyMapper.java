package com.avgstockprice;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class MyMapper extends Mapper<Object, Text, IntWritable, CountAverageTuple> {

	private IntWritable outYear = new IntWritable();
	private CountAverageTuple outCoutAverage = new CountAverageTuple();

	public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
		SimpleDateFormat frmt = new SimpleDateFormat("yyyy-mm-dd");
		String[] fields = value.toString().split(",");
		if (fields[0].contains("NYSE")) {

			try {
				String strDate = fields[2];
				Date creationDate = frmt.parse(strDate);

				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
				outYear.set(Integer.parseInt((dateFormat.format(creationDate))));
				outCoutAverage.setAverage(Double.parseDouble(fields[8]));
				outCoutAverage.setCount(1);

				context.write(outYear, outCoutAverage);
			} catch (ParseException e) {
			}

		}
	}
}
