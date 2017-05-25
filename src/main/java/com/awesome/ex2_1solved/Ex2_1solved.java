package com.awesome.ex2_1solved;

import com.awesome.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Ex2_1solved extends BaseExamplePeriod {

    int waitTicks = 20;

    long writtenItems = 0L;
    long readItems = 0L;
    protected AtomicInteger running = new AtomicInteger(0);
    protected BlockingQueue<Long> queue;
    protected Thread consumer;

    public Ex2_1solved(long runtime) {
        super(runtime);
        queue = new LinkedBlockingQueue<>();
        consumer = new Thread(this::consume);
    }

    protected void consume() {
        while (running.get() > 0) {
            try {
                for (int i = 0; i < waitTicks && queue.isEmpty(); i++)
                    ;

                long start = queue.take();
                histogram.recordValue(System.nanoTime() - start);
                readItems++;
            } catch (InterruptedException e) {
                return;
            }
        }
    }

    @Override
    protected void operation() {
        long start = System.nanoTime();
        try {
            queue.put(start);
            writtenItems++;
        } catch(InterruptedException e) {
        }
    }

    @Override
    protected void start() {
        if (running.compareAndSet(0, 1))
            consumer.start();
    }

    @Override
    protected void stop() throws InterruptedException {
        if (running.compareAndSet(1, 0)) {
            synchronized(queue) {
                consumer.interrupt();
            }
            consumer.join();
        }
        assert writtenItems - readItems - queue.size() == 0;
        System.out.println("Wrote " + writtenItems + " elements");
        System.out.println("Read " + readItems + " elements");
    }

    public static void main(final String args[]) {
        final long runtime = args.length > 0 ? Long.parseLong(args[0]) : BaseExamplePeriod.DEFAULT_RUN_TIME_MSEC;
        Ex2_1solved ex = new Ex2_1solved(runtime);
        ex.run();
        ex.printResults();
    }

}
