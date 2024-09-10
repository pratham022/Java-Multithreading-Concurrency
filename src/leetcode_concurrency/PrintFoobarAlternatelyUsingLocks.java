package leetcode_concurrency;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
class FooBar2 {
    private int n;
    private final Lock lock = new ReentrantLock();
    private final Condition fooCondition = lock.newCondition();
    private final Condition barCondition = lock.newCondition();
    private boolean fooTurn = true;

    public FooBar2(int n) {
        this.n = n;
    }

    public void foo(Runnable printFoo) throws InterruptedException {
        lock.lock();
        try {
            for (int i = 0; i < n; i++) {
                while (!fooTurn) {
                    fooCondition.await();
                }
                // printFoo.run() outputs "foo". Do not change or remove this line.
                printFoo.run();
                fooTurn = false;
                barCondition.signal();
            }
        } finally {
            lock.unlock();
        }
    }

    public void bar(Runnable printBar) throws InterruptedException {
        lock.lock();
        try {
            for (int i = 0; i < n; i++) {
                while (fooTurn) {
                    barCondition.await();
                }
                // printBar.run() outputs "bar". Do not change or remove this line.
                printBar.run();
                fooTurn = true;
                fooCondition.signal();
            }
        } finally {
            lock.unlock();
        }
    }
}
public class PrintFoobarAlternatelyUsingLocks {
}
