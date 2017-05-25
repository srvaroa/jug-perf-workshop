package com.awesome.ex0;

import com.awesome.*;
import java.util.concurrent.locks.LockSupport;

public class Ex0 extends BaseExamplePeriod {

    public Ex0(long runtime) {
        super(runtime);
    }

    @Override
    protected void operation() {
        LockSupport.parkNanos(1000 * 100);
    }

    public static void main(final String args[]) {
        final long runtime = args.length > 0 ? Long.parseLong(args[0]) : BaseExamplePeriod.DEFAULT_RUN_TIME_MSEC;
        Ex0 ex = new Ex0(runtime);
        ex.run();
        ex.printResults();
    }

}
