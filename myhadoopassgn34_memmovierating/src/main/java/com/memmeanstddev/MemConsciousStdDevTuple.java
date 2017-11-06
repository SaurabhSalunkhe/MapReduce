package com.memmeanstddev;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

public class MemConsciousStdDevTuple implements Writable {

    private double median;
    private double stddev;

    public double getMedian() {
        return median;
    }

    public void setMedian(double median) {
        this.median = median;
    }

    public double getStddev() {
        return stddev;
    }

    public void setStddev(double stddev) {
        this.stddev = stddev;
    }

    public void write(DataOutput d) throws IOException {
        d.writeDouble(median);
        d.writeDouble(stddev);
    }

    public void readFields(DataInput di) throws IOException {
        median = di.readDouble();
        stddev = di.readDouble();

    }

    public String toString() {
        return (String.valueOf(this.median) + "\t" + String.valueOf(this.stddev));

    }
}
