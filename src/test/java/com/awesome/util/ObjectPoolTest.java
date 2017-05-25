package com.awesome.util;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ObjectPoolTest {

    @Test
    public void normalWorkflow() {
        AtomicInteger counter = new AtomicInteger(0);
        ObjectPool<Integer> pool =
            new ObjectPool<>(() -> counter.incrementAndGet());
        int item1 = pool.get();
        int item2 = pool.get();
        assertEquals(1, item1);
        assertEquals(2, item2);
        pool.release(item1);
        assertEquals(item1, pool.get().intValue());
    }

    @Test(expected = IllegalStateException.class)
    public void whenUnknownReleasedThrow() {
        AtomicInteger counter = new AtomicInteger(0);
        ObjectPool<Integer> pool =
            new ObjectPool<>(() -> counter.incrementAndGet());
        Integer item = pool.get();
        pool.release(item);
        pool.release(2);
    }

}
