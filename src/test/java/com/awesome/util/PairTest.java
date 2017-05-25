package com.awesome.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PairTest {

    @Test
    public void testCreation() {
        final Pair<Integer, Integer> p = new Pair<>(1, 2);
        assertEquals(1, p.left().intValue());
        assertEquals(2, p.right().intValue());
    }

    @Test
    public void testReset() {
        final Pair<Integer, Integer> p = new Pair<>(1, 2);
        p.reset(3, 4);
        assertEquals(3, p.left().intValue());
        assertEquals(4, p.right().intValue());
    }

}
