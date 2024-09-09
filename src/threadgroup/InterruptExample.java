package threadgroup;

class ThreadNew2 extends Thread {
    ThreadNew2(String tName, ThreadGroup threadGroup) {
        super(threadGroup, tName);
        start();
    }
    public void run() {
        for (int i = 0; i < 1000; i++) {
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
        System.out.println(Thread.currentThread().getName() + " thread has finished executing");
    }
}
public class InterruptExample {

    public static void main(String[] args) {
        ThreadGroup tg = new ThreadGroup("Parent Group");
        ThreadGroup tg1 = new ThreadGroup(tg, "Child Group");

        ThreadNew2 th1 = new ThreadNew2("Thread 1", tg);
        System.out.println("Starting " + th1.getName());
        ThreadNew2 th2 = new ThreadNew2("Thread 2", tg);
        System.out.println("Starting " + th2.getName());

        tg.interrupt();
    }
}
