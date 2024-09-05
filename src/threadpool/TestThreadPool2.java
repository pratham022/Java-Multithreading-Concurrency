package threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestThreadPool2 {
    public static void main(String[] args) {
        Runnable r1 = new TestTask("Task 1");
        Runnable r2 = new TestTask("Task 2");
        Runnable r3 = new TestTask("Task 3");
        Runnable r4 = new TestTask("Task 4");
        Runnable r5 = new TestTask("Task 5");

        ExecutorService executor = Executors.newFixedThreadPool(3);
        executor.execute(r1);
        executor.execute(r2);
        executor.execute(r3);
        executor.execute(r4);
        executor.execute(r5);

        executor.shutdown();

    }
}
