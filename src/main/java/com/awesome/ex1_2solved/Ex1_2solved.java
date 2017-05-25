package com.awesome.ex1_2solved;

import com.awesome.BaseExamplePeriod;
import com.awesome.util.ObjectPool;
import com.awesome.util.Pair;

import java.util.ArrayList;
import java.util.List;

/*
 * Before fixing anything below, try these:
 *
 * -XX:+PrintStringTableStatistics
 * -XX:+UseStringDeduplication
 */
public class Ex1_2solved extends BaseExamplePeriod {

    private static int SIZE = 1000000;
    private final List<Pair<String, String>> pairs = new ArrayList<>(SIZE);
    private final ObjectPool<Pair<String, String>> pool = new ObjectPool<>(Pair::new);

    private Integer counter = 0;

    private Ex1_2solved(long runtime) {
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
        Pair<String, String> pair = pool.get();
        pair.reset("k-" + k, "v-" + v);
        return pair;
    }

    public static void main(final String args[]) throws InterruptedException {
        final long runtime = args.length > 1 ? Long.parseLong(args[1]) : BaseExamplePeriod.DEFAULT_RUN_TIME_MSEC;
        Ex1_2solved ex = new Ex1_2solved(runtime);
        ex.run();
        ex.printResults();
    }

}
