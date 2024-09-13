package commonly_asked_questions;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class WorkerInterface {
    private int taskCounter = 0; // Keeps track of active tasks
    private final Object lock = new Object(); // Lock object for synchronization

    // Submits a task to the worker
    public void submitWork(Runnable task) {
        synchronized (lock) {
            taskCounter++;  // Increment the task counter
        }

        // Run the task in a new thread
        new Thread(() -> {
            try {
                // Execute the task
                task.run();
            } finally {
                synchronized (lock) {
                    taskCounter--;  // Decrement the task counter when task completes
                    if (taskCounter == 0) {
                        lock.notifyAll(); // Notify waiting threads when all tasks are complete
                    }
                }
            }
        }).start();
    }

    // Blocks until all submitted tasks are complete
    public void blockUntilComplete() {
        synchronized (lock) {
            while (taskCounter > 0) {
                try {
                    lock.wait(); // Wait until taskCounter becomes 0
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Handle interruption
                }
            }
        }
    }
}

public class WorkerInterfaceDemo {
    public static void main(String[] args) {
        WorkerInterface worker = new WorkerInterface();  // Create a worker

        // Submit multiple tasks
        worker.submitWork(() -> {
            System.out.println("Task 1 running");
            try { Thread.sleep(1000); } catch (InterruptedException e) {}
            System.out.println("Task 1 completed");
        });

        worker.submitWork(() -> {
            System.out.println("Task 2 running");
            try { Thread.sleep(2000); } catch (InterruptedException e) {}
            System.out.println("Task 2 completed");
        });

        worker.submitWork(() -> {
            System.out.println("Task 3 running");
            try { Thread.sleep(500); } catch (InterruptedException e) {}
            System.out.println("Task 3 completed");
        });

        // Block the current thread until all tasks are complete
        System.out.println("Waiting for tasks to complete...");
        worker.blockUntilComplete();
        System.out.println("All tasks completed!");
    }
}


