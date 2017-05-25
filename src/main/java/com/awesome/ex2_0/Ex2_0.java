package com.awesome.ex2_0;

import com.awesome.*;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

public class Ex2_0 extends BaseExamplePeriod {

    long writtenItems = 0L;
    long readItems = 0L;
    AtomicInteger running = new AtomicInteger(0);
    Queue<Long> queue;
    Thread consumer;

    public Ex2_0(long runtime) {
        super(runtime);
        queue = new LinkedList<>();
        consumer = new Thread(this::consume);
    }

    void consume() {
        while (true) {
            synchronized(queue) {
                if (running.get() == 0)
                    return;

                if (queue.isEmpty()) {
                    try {
                        queue.wait();
                    } catch (InterruptedException e) {
                        return;
                    }
                }

                if (queue.isEmpty())
                    continue;

                long start = queue.poll();
                histogram.recordValue(System.nanoTime() - start);
                readItems++;
            }
        }
    }

    @Override
    protected void operation() {
        long start = System.nanoTime();
        synchronized(queue) {
            queue.add(start);
            queue.notify();
        }
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
                queue.notify();
            }
            consumer.join();
        }
        assert writtenItems - readItems - queue.size() == 0;
    }

    public static void main(final String args[]) {
        final long runtime = args.length > 0 ? Long.parseLong(args[0]) : BaseExamplePeriod.DEFAULT_RUN_TIME_MSEC;
        Ex2_0 ex = new Ex2_0(runtime);
        ex.run();
        ex.printResults();
    }

}
