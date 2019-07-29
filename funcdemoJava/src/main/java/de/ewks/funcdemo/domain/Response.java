package de.ewks.funcdemo.domain;

import com.google.common.util.concurrent.AtomicDouble;

import java.util.concurrent.atomic.AtomicInteger;

public class Response {
    private double max = Double.MIN_VALUE;
    private double min = Double.MAX_VALUE;
    private double avg =0;
    private final AtomicDouble avgAtom = new AtomicDouble(0);
    private final AtomicInteger sum = new AtomicInteger(0);

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        if (max > this.max) {
            this.max = max;
        }
    }

    public double getAvg() {
        return avg;
    }

    public void setAvg(double avg) {
        this.avg = avg;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        if (min < this.min) {
            this.min = min;
        }
    }

    public void setAvgAtom(double newAvg) {
        sum.incrementAndGet();
        avgAtom.addAndGet(newAvg);
    }

    public Response calcAvgAndGet() {
        this.avg = (this.avgAtom.get() / this.sum.get());
        return this;
    }


    public Response add(double toCheck) {
        this.setMax(toCheck);
        this.setMin(toCheck);
        this.setAvgAtom(toCheck);
        return this;
    }

    public Response add(Response toCheck) {
        this.setMax(toCheck.max);
        this.setMin(toCheck.min);
        this.sum.addAndGet(toCheck.sum.get());
        this.avgAtom.addAndGet(toCheck.avgAtom.get());
        return this;
    }
}
