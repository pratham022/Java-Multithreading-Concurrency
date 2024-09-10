package leetcode_concurrency;

class H2O {

    int h20Counter;
    public H2O() {
        h20Counter = 0;
    }

    public void hydrogen(Runnable releaseHydrogen) throws InterruptedException {

        synchronized(this) {
            while (h20Counter > 1) {
                wait();
            }
            // releaseHydrogen.run() outputs "H". Do not change or remove this line.
            releaseHydrogen.run();
            h20Counter = (h20Counter + 1) % 3;
            notifyAll();
        }
    }

    public void oxygen(Runnable releaseOxygen) throws InterruptedException {

        synchronized(this) {
            while (h20Counter < 2) {
                wait();
            }
            // releaseOxygen.run() outputs "O". Do not change or remove this line.
            releaseOxygen.run();
            h20Counter = (h20Counter + 1) % 3;
            notifyAll();
        }
    }
}
