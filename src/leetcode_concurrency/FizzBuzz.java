package leetcode_concurrency;

import java.util.function.IntConsumer;

class FizzBuzz {
    private int n;
    int i = 1;

    public FizzBuzz(int n) {
        this.n = n;
    }

    // printFizz.run() outputs "fizz".
    public void fizz(Runnable printFizz) throws InterruptedException {
        while (true) {
            synchronized(this) {
                if (i > n) {
                    notifyAll();
                    break;
                }
                if (i % 3 == 0 && i % 5 != 0) {
                    printFizz.run();
                    i++;
                    notifyAll();
                } else {
                    wait();
                }
            }
        }
    }

    // printBuzz.run() outputs "buzz".
    public void buzz(Runnable printBuzz) throws InterruptedException {
        while (true) {
            synchronized(this) {
                if (i > n) {
                    notifyAll();
                    break;
                }
                if (i % 3 != 0 && i % 5 == 0) {
                    printBuzz.run();
                    i++;
                    notifyAll();
                } else {
                    wait();
                }
            }
        }
    }

    // printFizzBuzz.run() outputs "fizzbuzz".
    public void fizzbuzz(Runnable printFizzBuzz) throws InterruptedException {
        while (true) {
            synchronized(this) {
                if (i > n) {
                    notifyAll();
                    break;
                }
                if (i % 3 == 0 && i % 5 == 0) {
                    printFizzBuzz.run();
                    i++;
                    notifyAll();
                } else {
                    wait();
                }
            }
        }
    }

    // printNumber.accept(x) outputs "x", where x is an integer.
    public void number(IntConsumer printNumber) throws InterruptedException {
        while (true) {
            synchronized(this) {
                if (i > n) {
                    notifyAll();
                    break;
                }
                if (i % 3 != 0 && i % 5 != 0) {
                    printNumber.accept(i);
                    i++;
                    notifyAll();
                } else {
                    wait();
                }
            }
        }
    }
}
