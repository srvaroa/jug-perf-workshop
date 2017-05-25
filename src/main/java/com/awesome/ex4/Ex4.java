package com.awesome.ex4;

import com.awesome.*;

public class Ex4 extends BaseExampleOnce {
    private static final int ITERATIONS = 50 * 1000 * 1000;
    private final UnaryOperation[] operations;
    private int numOperations = 1;

    public Ex4() {
        operations = new UnaryOperation[4];
        operations[0] = new DoubleOperation();
        operations[1] = new HalfOperation();
        operations[2] = new IncOperation();
        operations[3] = new DecOperation();
    }

    @Override
    protected void warmupCompleted() {
        numOperations = operations.length;
    }

    public interface UnaryOperation {
        long map(final long value);
    }

    public class IncOperation implements UnaryOperation {
        public long map(final long value) {
            return value + 1;
        }
    }

    public class DecOperation implements UnaryOperation {
        public long map(final long value) {
            return value - 1;
        }
    }

    public class DoubleOperation implements UnaryOperation {
        public long map(final long value) {
            return value * 2;
        }
    }

    public class HalfOperation implements UnaryOperation {
        public long map(final long value) {
            return value >> 1;
        }
    }

    private static long measureOp(final UnaryOperation operation, long value, Op op) {
        op.enter();
        value += operation.map(value);
        op.exit();
        return value;
    }

    @Override
    public final void operation(Op op) {
        long value = 5231;
        for (int i = 0; i < 2; i++) {
            System.out.println("Running loop " + (i + 1));
            for (int j = 0; j < numOperations; ++j) {
                UnaryOperation operation = operations[j];
                System.out.println(operation.getClass().getName());
                for (int k = 0; k < ITERATIONS; k++) {
                    value = measureOp(operation, value, op);
                }
            }
        }

        // Use value
        System.out.println("value = " + value);
    }

    public static void main(final String args[]) throws InterruptedException {
        Ex4 ex = new Ex4();
        ex.run();
        ex.printResults();
    }
}
