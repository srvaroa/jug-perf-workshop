package com.awesome.util;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Supplier;

/**
 * THREAD UNSAFE object pool, meant to be used by a single thread.
 */
public class ObjectPool<T> {

    private final Set<T> available;
    private final Set<T> elsewhere;
    private final Supplier<T> supplier;

    public ObjectPool(Supplier<T> supplier) {
        this.supplier = supplier;
        this.available = new HashSet<>();
        this.elsewhere = new HashSet<>();
    }

    public T get() {
        if (available.isEmpty()) {
            available.add(supplier.get());
        }
        Iterator<T> it = available.iterator();
        T item = it.next();
        it.remove();
        if (!elsewhere.add(item)) {
            throw new IllegalStateException("Item reserved twice!");
        }
        return item;
    }

    public void release(T t) {
        if (!elsewhere.remove(t))
            throw new IllegalStateException("Unknown item released");
        if (!available.add(t))
            throw new IllegalStateException("Item released twice!");
    }

}
