package threadgroup;

public class ActiveCountExample implements Runnable {

    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
    }

    public static void main(String[] args) {
        ThreadGroup threadGroup = new ThreadGroup("Parent Thread Group");

        ActiveCountExample runnable = new ActiveCountExample();
        Thread t1 = new Thread(threadGroup, runnable, "first");
        Thread t2 = new Thread(threadGroup, runnable, "second");
        t1.start();
        t2.start();
        System.out.println("Active count of threads: " + threadGroup.activeCount());

    }
}
