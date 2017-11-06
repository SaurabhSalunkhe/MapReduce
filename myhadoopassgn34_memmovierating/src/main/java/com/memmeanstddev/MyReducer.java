package com.memmeanstddev;

import java.io.IOException;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.SortedMapWritable;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapreduce.Reducer;

public class MyReducer extends Reducer<LongWritable, SortedMapWritable, LongWritable, MemConsciousStdDevTuple> {

    private MemConsciousStdDevTuple cw = new MemConsciousStdDevTuple();
    private TreeMap<Double, Long> ratingCounts = new TreeMap<Double, Long>();

    @Override
    protected void reduce(LongWritable key, Iterable<SortedMapWritable> values, Context context) throws IOException, InterruptedException {
        double sum = 0;
        double totalrating = 0;
        ratingCounts.clear();
        cw.setMedian(0);
        cw.setStddev(0);;

        for (SortedMapWritable v : values) {
            for (Entry<WritableComparable, Writable> entry : v.entrySet()) {
                double rating = ((DoubleWritable) entry.getKey()).get();
                long cnt = ((LongWritable) entry.getValue()).get();
                totalrating += cnt;
                sum += rating * cnt;

                Long storedCount = ratingCounts.get(rating);
                if (storedCount == null) {
                    ratingCounts.put(rating, cnt);
                } else {
                    ratingCounts.put(rating, storedCount + cnt);
                }
            }
        }

        double medianIndex = (double) totalrating / 2L;
        double previousRatings = 0;
        double ratings = 0;
        double prevKey = 0;
        for (Entry<Double, Long> entry : ratingCounts.entrySet()) {
            ratings = previousRatings + entry.getValue();
            if (previousRatings <= medianIndex && medianIndex < ratings) {
                if (ratings % 2 == 0 && previousRatings == medianIndex) {
                    cw.setMedian((float) (entry.getKey() + prevKey) / 2.0f);
                } else {
                    cw.setMedian(entry.getKey());
                }
                break;
            }
            previousRatings = ratings;
            prevKey = entry.getKey();
        }

        // calculate standard deviation
        double mean = (double) sum / totalrating;

        float sumOfSquares = 0.0f;
        for (Entry<Double, Long> entry : ratingCounts.entrySet()) {
            sumOfSquares += (entry.getKey() - mean)
                    * (entry.getKey() - mean) * entry.getValue();
        }

        cw.setStddev((float) Math.sqrt(sumOfSquares / (totalrating - 1)));
        context.write(key, cw);
    }
}
