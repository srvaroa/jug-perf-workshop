package com.awesome;

public abstract class BaseExamplePeriod extends BaseExample {
    public static long DEFAULT_RUN_TIME_MSEC = 20000;
    private final long runtime;

    public BaseExamplePeriod() {
        this(DEFAULT_RUN_TIME_MSEC);
    }

    public BaseExamplePeriod(long runtime) {
        this.runtime = runtime;
    }

    protected abstract void operation();

    @Override
    protected void measuredOp() {
        op.enter();
        operation();
        op.exit();
    }

    @Override
    protected void doRun() {
        long startTime = System.currentTimeMillis();
        long now;
        do {
            measuredOp();
            now = System.currentTimeMillis();
        } while (now - startTime < runtime);
    }
}
