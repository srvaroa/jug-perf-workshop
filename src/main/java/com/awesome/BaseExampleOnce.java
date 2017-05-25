package com.awesome;

public abstract class BaseExampleOnce extends BaseExample {
    protected abstract void operation(Op op);

    @Override
    protected void measuredOp() {
        operation(op);
    }

    @Override
    protected void doRun() {
        measuredOp();
    }
}
