package commonly_asked_questions;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class TaskScheduler {
    private final Queue<Runnable> taskQueue = new LinkedList<>();
    private final Lock lock = new ReentrantLock();
    private final Condition hasTask = lock.newCondition();
    private final Condition allTasksDone = lock.newCondition();
    private int pendingTasks = 0;
    private volatile boolean stop = false;

    public TaskScheduler(int numThreads) {
        // Create worker threads
        for (int i = 0; i < numThreads; i++) {
            new Thread(new Worker()).start();
        }
    }

    // Non-blocking Schedule method to add tasks to the queue
    public void schedule(Runnable task) {
        lock.lock();
        try {
            taskQueue.add(task);
            pendingTasks++;
            hasTask.signal();  // Notify a worker that a new task is available
        } finally {
            lock.unlock();
        }
    }

    // Blocking call that waits for all tasks to complete
    public void waitUntilCompletion() throws InterruptedException {
        lock.lock();
        try {
            while (pendingTasks > 0) {
                allTasksDone.await();  // Wait until all tasks are done
            }
        } finally {
            lock.unlock();
        }
    }

    // Stops the worker threads gracefully
    public void shutdown() {
        lock.lock();
        try {
            stop = true;
            hasTask.signalAll();  // Wake up all workers to terminate
        } finally {
            lock.unlock();
        }
    }

    // Worker class to process tasks from the queue
    private class Worker implements Runnable {
        public void run() {
            while (true) {
                Runnable task;
                lock.lock();
                try {
                    while (taskQueue.isEmpty() && !stop) {
                        hasTask.await();  // Wait for a task to become available
                    }

                    if (stop && taskQueue.isEmpty()) {
                        break;  // Stop the thread if no more tasks and shutdown is triggered
                    }

                    task = taskQueue.poll();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                } finally {
                    lock.unlock();
                }

                if (task != null) {
                    task.run();

                    lock.lock();
                    try {
                        pendingTasks--;
                        if (pendingTasks == 0) {
                            allTasksDone.signal();  // Signal that all tasks are done
                        }
                    } finally {
                        lock.unlock();
                    }
                }
            }
        }
    }
}

class TaskSchedulerDemo {
    public static void main(String[] args) throws InterruptedException {
        // Create the scheduler with 3 worker threads
        TaskScheduler scheduler = new TaskScheduler(3);

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

