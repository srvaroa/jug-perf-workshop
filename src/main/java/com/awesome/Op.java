package com.awesome;

import org.HdrHistogram.*;

public class Op {
    public final Histogram histogram;

    volatile long start = 0L;

    public Op(Histogram h) {
        histogram = h;
    }

    public void reset() {
        start = 0L;
        histogram.reset();
    }

    public void enter() {
        if (start == 0)
            start = System.nanoTime();
    }

    public void exit() {
        long end = System.nanoTime();
        histogram.recordValue(end - start);
        start = end;
    }
}
