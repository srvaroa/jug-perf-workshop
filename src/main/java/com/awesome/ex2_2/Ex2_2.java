package com.awesome.ex2_2;

import com.awesome.BaseExamplePeriod;
import org.jctools.queues.SpmcArrayQueue;

import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

public class Ex2_2 extends BaseExamplePeriod {

    final static int CAPACITY = 512;

    long writtenItems = 0L;
    long readItems = 0L;
    protected AtomicInteger running = new AtomicInteger(0);
    protected Queue<Long> queue;
    protected Thread consumer;

    public Ex2_2(long runtime) {
        super(runtime);
        queue = new SpmcArrayQueue<>(CAPACITY);
        consumer = new Thread(this::consume);
    }

    protected void consume() {
        while (true) {

            // TODO

        }
    }

    @Override
    protected void operation() {

        // TODO

        writtenItems++;
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
        Ex2_2 ex = new Ex2_2(runtime);
        ex.run();
        ex.printResults();
    }

}
