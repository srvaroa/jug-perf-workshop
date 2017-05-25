package com.awesome.util;

/**
 * A pair of values that can be resetted.
 *
 * Thread UNSAFE.
 */
public class Pair<LEFT, RIGHT> {

    private LEFT left;
    private RIGHT right;

    public Pair() {
    }

    public Pair(LEFT left, RIGHT right) {
        reset(left, right);
    }

    public void reset(LEFT left, RIGHT right) {
        this.left = left;
        this.right = right;
    }

    public LEFT left() {
        return left;
    }

    public RIGHT right() {
        return right;
    }
}
