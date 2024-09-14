package commonly_asked_questions;

import java.util.LinkedList;
import java.util.Queue;

class TaskSchedulerDemo {
    private final Queue<Runnable> taskQueue = new LinkedList<>();
    private final Object lock = new Object();
    private int pendingTasks = 0;
    private volatile boolean stop = false;

    public TaskSchedulerDemo(int numThreads) {
        // Create worker threads
        for (int i = 0; i < numThreads; i++) {
            new Thread(new Worker()).start();
        }
    }

    // Non-blocking Schedule method to add tasks to the queue
    public void schedule(Runnable task) {
        synchronized (lock) {
            taskQueue.add(task);
            pendingTasks++;
            lock.notify();  // Notify a worker that a new task is available
        }
    }

    // Blocking call that waits for all tasks to complete
    public void waitUntilCompletion() throws InterruptedException {
        synchronized (lock) {
            while (pendingTasks > 0) {
                lock.wait();  // Wait until all tasks are done
            }
        }
    }

    // Stops the worker threads gracefully
    public void shutdown() {
        synchronized (lock) {
            stop = true;
            lock.notifyAll();  // Wake up all workers to terminate
        }
    }

    // Worker class to process tasks from the queue
    private class Worker implements Runnable {
        public void run() {
            while (true) {
                Runnable task;
                synchronized (lock) {
                    while (taskQueue.isEmpty() && !stop) {
                        try {
                            lock.wait();  // Wait for a task to become available
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            break;
                        }
                    }

                    if (stop && taskQueue.isEmpty()) {
                        break;  // Stop the thread if no more tasks and shutdown is triggered
                    }

                    task = taskQueue.poll();
                }

                if (task != null) {
                    task.run();

                    synchronized (lock) {
                        pendingTasks--;
                        if (pendingTasks == 0) {
                            lock.notify();  // Signal that all tasks are done
                        }
                    }
                }
            }
        }
    }
}

public class TaskSchedulerUsingWaitNotify {
    public static void main(String[] args) throws InterruptedException {
        // Create the scheduler with 3 worker threads
        TaskSchedulerUsingWaitNotify scheduler = new TaskSchedulerUsingWaitNotify(3);

        // Schedule some tasks
        for (int i = 0; i < 10; i++) {
            final int taskNum = i;
            scheduler.schedule(() -> {
                System.out.println("Executing task " + taskNum + " on thread " + Thread.currentThread().getName());
                try {
                    Thread.sleep(500);  // Simulate work
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        // Wait for all tasks to complete
        scheduler.waitUntilCompletion();
        System.out.println("All tasks completed.");

        // Shutdown the scheduler
        scheduler.shutdown();
    }
}

