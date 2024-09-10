package leetcode_concurrency;

class Foo {

    boolean firstPrinted;
    boolean secondPrinted;
    public Foo() {
        firstPrinted = false;
        secondPrinted = false;
    }

    public void first(Runnable printFirst) throws InterruptedException {

         synchronized (this) {
            // printFirst.run() outputs "first". Do not change or remove this line.
            printFirst.run();
            firstPrinted = true;
            notifyAll();
         }
    }

    public void second(Runnable printSecond) throws InterruptedException {

         synchronized (this) {
            while (!firstPrinted) {
                wait();
            }
            // printSecond.run() outputs "second". Do not change or remove this line.
            printSecond.run();
            secondPrinted = true;
            notifyAll();
         }
    }

    public void third(Runnable printThird) throws InterruptedException {

         synchronized (this) {
            while (!secondPrinted) {
                wait();
            }
            // printThird.run() outputs "third". Do not change or remove this line.
            printThird.run();
         }
    }
}

public class PrintInOrder {
    public static void main(String[] args) throws InterruptedException {
        Foo foo = new Foo();
        foo.first(new Runnable() {
            @Override
            public void run() {
                System.out.println("first");
            }
        });
        foo.second(new Runnable() {
            @Override
            public void run() {
                System.out.println("second");
            }
        });
        foo.third(new Runnable() {
            @Override
            public void run() {
                System.out.println("third");
            }
        });
    }
}
