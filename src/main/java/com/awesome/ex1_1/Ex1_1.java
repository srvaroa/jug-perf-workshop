package com.awesome.ex1_1;

import com.awesome.BaseExamplePeriod;
import com.awesome.util.Pair;

import java.util.LinkedList;
import java.util.List;

/*
 * Before fixing anything below, try these:
 *
 * -XX:+PrintStringTableStatistics
 * -XX:+UseStringDeduplication
 */
public class Ex1_1 extends BaseExamplePeriod {

    private static int SIZE = 1000000;
    private final List<Pair<String, String>> pairs = new LinkedList<>();

    private Integer counter = 0;

    private Ex1_1(long runtime) {
        super(runtime);
    }

    @Override
    public void start() {
        while (pairs.size() < SIZE) {
            pairs.add(next());
        }
    }

    @Override
    public final void operation() {
        // after we reach the desired listSize, just add and remove
        pairs.add(next());
        pairs.remove(0);
    }

    @Override
    public final void warmupCompleted() {
        System.out.println("List has: " + pairs.size());
    }

    private Pair<String, String> next() {
        int k = counter++ % SIZE;
        int v = counter++ % SIZE;
        return new Pair<>("k-" + k, "v-" + v);
    }

    public static void main(final String args[]) throws InterruptedException {
        final long runtime = args.length > 0 ? Long.parseLong(args[0]) : BaseExamplePeriod.DEFAULT_RUN_TIME_MSEC;
        Ex1_1 ex = new Ex1_1(runtime);
        ex.run();
        ex.printResults();
    }

}

