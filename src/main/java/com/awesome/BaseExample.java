package com.awesome;

import org.HdrHistogram.Histogram;

import java.io.FileNotFoundException;
import java.io.PrintStream;

public abstract class BaseExample implements Runnable {
    protected Histogram histogram = new Histogram(60000000000L, 3);
    protected Op op = new Op(histogram);

    static long WARMUP_TIME_MSEC = 3000;

    protected void start() {
    }

    protected void stop() throws InterruptedException {
    }

    protected void warmupCompleted() {
    }

    @Override
    public void run() {
        start();

        long startTime = System.currentTimeMillis();
        long now;

        System.out.println("Warming up " + getClass().getSimpleName());

        do {
            measuredOp();
            now = System.currentTimeMillis();
        } while (now - startTime < WARMUP_TIME_MSEC);

        op.reset();
        warmupCompleted();

        System.out.println("Running " + getClass().getSimpleName());

        doRun();

        try {
            stop();
        } catch (InterruptedException e) {
        }
    }

    protected abstract void measuredOp();
    protected abstract void doRun();

    public void printResults() {
        long timestamp = System.currentTimeMillis() / 1000L;
        String filename = "build/" + getClass().getSimpleName() + "-" + timestamp + ".hist";
        try (PrintStream histoStream = new PrintStream(filename)) {
            histogram.outputPercentileDistribution(histoStream, 1000.0);
            System.out.println("Latencies in usec recorded to " + filename);
        } catch (FileNotFoundException e) {
            System.err.println(filename + ": Failed to create histogram file");
            e.printStackTrace();
        }
    }
}
