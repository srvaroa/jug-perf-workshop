package com.awesome.ex3_0solved;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.awesome.BaseExamplePeriod;

public class Ex3_0solved extends BaseExamplePeriod {
    ByteBuffer buf = ByteBuffer.allocateDirect(256).order(ByteOrder.nativeOrder());

    public Ex3_0solved(long runtime) {
        super(runtime);
        serialize(new ObjectThatsSerialized(102, false, 9102, new double[] { 0.5d, 0.6d, 345.1d, 1392.4d, }), buf);
        buf.flip();
    }

    public static class ObjectWithFlyweight {
        private static final int SIZE_OF_BOOLEAN = 1;
        private static final int SIZE_OF_INT = 4;
        private static final int SIZE_OF_LONG = 8;
        private static final int SIZE_OF_DOUBLE = 8;

        public static long someId(final ByteBuffer bf) {
            return bf.getLong(0);
        }

        public static void setSomeId(ByteBuffer bf, long someId) {
            bf.putLong(0, someId);
        }

        public static boolean someFlag(final ByteBuffer bf) {
            return 0 != bf.get(SIZE_OF_LONG); 
        }

        public static void setSomeFlag(ByteBuffer bf, boolean flag) {
            bf.put(SIZE_OF_LONG, (byte)(flag ? 1 : 0));
        }

        public static int someNumber(final ByteBuffer bf) {
            return bf.getInt(SIZE_OF_LONG + SIZE_OF_BOOLEAN);
        }

        public static void setSomeNumber(ByteBuffer bf, int someNumber) {
            bf.putInt(SIZE_OF_LONG + SIZE_OF_BOOLEAN, someNumber);
        }

        public static int listSize(final ByteBuffer bf) {
            return bf.getInt(SIZE_OF_LONG + SIZE_OF_BOOLEAN + SIZE_OF_INT);
        }

        public static double elementAt(final ByteBuffer bf, int pos) {
            return bf.getDouble(SIZE_OF_LONG + SIZE_OF_BOOLEAN + SIZE_OF_INT + SIZE_OF_INT + (SIZE_OF_DOUBLE * pos));
        }

        public static void setElementAt(final ByteBuffer bf, int pos, double element) {
            bf.putDouble(SIZE_OF_LONG + SIZE_OF_BOOLEAN + SIZE_OF_INT + SIZE_OF_INT + (SIZE_OF_DOUBLE * pos), element);
        }

        public static void changeMe(ByteBuffer bf) {
            int s = listSize(bf);
            setSomeNumber(bf, someNumber(bf) + s);
            setSomeFlag(bf, s > 2);
            if (s > 0) {
                setElementAt(bf, s - 1, elementAt(bf, s - 1) * elementAt(bf, 0));
            }
        }
    }

    public static class ObjectThatsSerialized {
        private long someId;
        private boolean someFlag;
        private int someNumber;
        private double[] someList;

        public ObjectThatsSerialized(long someId, boolean someFlag, int someNumber, double[] someList) {
            this.someId = someId;
            this.someFlag = someFlag;
            this.someNumber = someNumber;
            this.someList = someList;
        }

        public void changeMe() {
            int s = someList.length;
            someNumber += s;
            someFlag = s > 2;
            if (s > 0) {
                someList[s - 1] *= someList[0];
            }
        }
    }

    public static void serialize(final ObjectThatsSerialized o, ByteBuffer bf) {
        bf.putLong(o.someId);
        bf.put((byte)(o.someFlag ? 1 : 0));
        bf.putInt(o.someNumber);
        bf.putInt(o.someList.length);
        for (final double d : o.someList) {
            bf.putDouble(d);
        }
    }

    public static ObjectThatsSerialized deserialize(final ByteBuffer bf) {
        final long someId = bf.getLong();
        final boolean someFlag = 0 != bf.get();
        final int someNumber = bf.getInt();

        final int listSize = bf.getInt();
        final double[] someList = new double[listSize];
        for (int i = 0; i < listSize; i++) {
            someList[i] = bf.getDouble();
        }

        return new ObjectThatsSerialized(someId, someFlag, someNumber, someList);
    }

    @Override
    public final void operation() {
        ObjectWithFlyweight.changeMe(buf);
    }

    void print() {
        System.out.println(ObjectWithFlyweight.someNumber(buf));
    }

    public static void main(final String args[]) throws InterruptedException {
        final long runtime = args.length > 0 ? Long.parseLong(args[0]) : BaseExamplePeriod.DEFAULT_RUN_TIME_MSEC;
        Ex3_0solved ex = new Ex3_0solved(runtime);
        ex.run();
        ex.printResults();
        ex.print();
    }
}
