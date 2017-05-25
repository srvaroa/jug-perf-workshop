package com.awesome.ex1_0;

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
public class Ex1_0 extends BaseExamplePeriod {

    private static int SIZE = 1000000;
    private final List<Pair<String, String>> pairs = new LinkedList<>();

    private Integer counter = 0;

    private Ex1_0(long runtime) {
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
        Integer k = counter++ % SIZE;
        Integer v = counter++ % SIZE;
        return new Pair<>(new String("k-" + k), new String("v-" + v));
    }

    public static void main(final String args[]) throws InterruptedException {
        final long runtime = args.length > 0 ? Long.parseLong(args[0]) : BaseExamplePeriod.DEFAULT_RUN_TIME_MSEC;
        Ex1_0 ex = new Ex1_0(runtime);
        ex.run();
        ex.printResults();
    }

}
