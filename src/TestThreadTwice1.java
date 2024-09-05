public class TestThreadTwice1 extends Thread {
    public void run() {
        System.out.println("Thread is running");
    }

    public static void main(String[] args) {
        TestThreadTwice1 obj = new TestThreadTwice1();
        obj.start();

        /**
         * After starting a thread, it can never be started again.
         * If you does so, an IllegalThreadStateException is thrown.
         * In such case, thread will run once but for second time, it will throw exception.
         */
        obj.start();
    }
}
