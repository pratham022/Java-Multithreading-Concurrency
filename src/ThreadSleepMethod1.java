public class ThreadSleepMethod1 extends Thread {
    public void run() {
        for (int i = 0; i < 5; i++) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println(e);
            }
            System.out.println(i);
        }
    }

    public static void main(String[] args) {
        ThreadSleepMethod1 obj1 = new ThreadSleepMethod1();
        ThreadSleepMethod1 obj2 = new ThreadSleepMethod1();

        obj1.start();
        obj2.start();
    }
}
