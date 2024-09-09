package threadgroup;

class ThreadNew extends Thread {
    ThreadNew(String tName, ThreadGroup tg) {
        super(tg, tName);
        start();
    }
    public void run() {
        for (int i = 0; i < 100; i++) {
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
    }
}
public class DestroyExample {
    public static void main(String[] args) throws InterruptedException {
        ThreadGroup tg1 = new ThreadGroup("Parent Group");
        ThreadGroup tg2 = new ThreadGroup(tg1, "Child Group");

        ThreadNew th1 = new ThreadNew("the first", tg1);
        System.out.println("Starting the first thread");

        ThreadNew th2 = new ThreadNew("the second", tg1);
        System.out.println("Staring the second thread");

        th1.join();
        th2.join();

        tg2.destroy();
        System.out.println(tg2.getName() + " is destroyed");
        tg1.destroy();
        System.out.println(tg1.getName() + " is destroyed");

    }
}
