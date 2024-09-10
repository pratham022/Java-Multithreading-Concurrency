package leetcode_concurrency;

class FooBar {
    private int n;
    private boolean fooPrinted;
    public FooBar(int n) {
        this.n = n;
        fooPrinted = false;
    }
    public void foo(Runnable printFoo) throws InterruptedException {
        synchronized (this) {
            for (int i = 0; i < n; i++) {
                while (fooPrinted) {
                    wait();
                }
                printFoo.run();
                fooPrinted = true;
                notify();
            }
        }
    }

    public void bar(Runnable printBar) throws InterruptedException {
        synchronized (this) {
            for (int i = 0; i < n; i++) {
                while (!fooPrinted) {
                    wait();
                }
                printBar.run();
                fooPrinted = false;
                notify();
            }
        }
    }
}

class ThreadFoo implements Runnable {
    private FooBar fooBar;
    public ThreadFoo(FooBar fooBar) {
        this.fooBar = fooBar;
    }
    @Override
    public void run() {
        try {
            fooBar.foo(() -> System.out.print("foo"));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

class ThreadBar implements Runnable {
    private FooBar fooBar;
    public ThreadBar(FooBar fooBar) {
        this.fooBar = fooBar;
    }
    @Override
    public void run() {
        try {
            // fooBar.bar(() -> System.out.print("bar"));
            fooBar.bar(new Runnable() {
                @Override
                public void run() {
                    System.out.print("bar");
                }
            });
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

public class PrintFoobarAlternately {
    public static void main(String[] args) throws InterruptedException {
        int n = 3;
        FooBar fooBar = new FooBar(n);
        Thread threadFoo = new Thread(new ThreadFoo(fooBar));
        Thread threadBar = new Thread(new ThreadBar(fooBar));
        threadFoo.start();
        threadBar.start();
    }
}
