package threadgroup;

public class ThreadGroupDemo implements Runnable {

    @Override
    public void run() {
        System.out.println("Thread running");
    }
    public static void main(String[] args) {
        ThreadGroupDemo runnable = new ThreadGroupDemo();
        ThreadGroup threadGroup = new ThreadGroup("Parent Thread Group");
        Thread t1 = new Thread(threadGroup, runnable, "Thread 1");
        t1.start();
        Thread t2 = new Thread(threadGroup, runnable, "Thread 2");
        t2.start();
        Thread t3 = new Thread(threadGroup, runnable, "Thread 3");
        t3.start();

        System.out.println("Thread Group Name: " + threadGroup.getName());
        threadGroup.list();

    }
}
