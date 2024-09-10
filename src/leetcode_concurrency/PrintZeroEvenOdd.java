package leetcode_concurrency;

import java.util.function.IntConsumer;

class ZeroEvenOdd {
    private int n;
    private int currNum = 1;
    private boolean zeroTurn = true;

    public ZeroEvenOdd(int n) {
        this.n = n;
    }

    // printNumber.accept(x) outputs "x", where x is an integer.
    public void zero(IntConsumer printNumber) throws InterruptedException {
        synchronized (this) {
            for (int i = 0; i < n; i++) {
                while (!zeroTurn) {
                    wait();
                }
                printNumber.accept(0);
                zeroTurn = false;
                notifyAll();
            }
        }
    }

    public void even(IntConsumer printNumber) throws InterruptedException {
        synchronized (this) {
            for (int i = 2; i <= n; i += 2) {
                while (zeroTurn || currNum % 2 != 0) {
                    wait();
                }
                printNumber.accept(currNum);
                currNum++;
                zeroTurn = true;
                notifyAll();
            }
        }
    }

    public void odd(IntConsumer printNumber) throws InterruptedException {
        synchronized (this) {
            for (int i = 1; i <= n; i += 2) {
                while (zeroTurn || currNum % 2 == 0) {
                    wait();
                }
                printNumber.accept(currNum);
                currNum++;
                zeroTurn = true;
                notifyAll();
            }
        }
    }
}

public class PrintZeroEvenOdd {
}
