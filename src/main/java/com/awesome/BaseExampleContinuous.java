package com.awesome;

public abstract class BaseExampleContinuous extends BaseExample {
    protected abstract void operation();

    @Override
    protected void measuredOp() {
        op.enter();
        operation();
        op.exit();
    }

    @Override
    protected void doRun() {
        do {
            measuredOp();
        } while (true);
    }
}
