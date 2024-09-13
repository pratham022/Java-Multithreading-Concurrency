package commonly_asked_questions;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ClientWrapper extends Client {

    private final Object lock = new Object();

    private boolean isInitialized = false;
    private boolean isClosed = false;
    private boolean isRequestInProgress = false;

    @Override
    public void init() {
        synchronized (lock) {
            // Check if initialization is already in progress or completed
            if (isInitialized || isClosed) {
                throw new IllegalStateException("Client already initialized or closed");
            }
            // Simulate initialization logic
            System.out.println("Initializing...");
            try {
                Thread.sleep(1000); // Simulate delay
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Preserve interruption status
            }

            isInitialized = true;
            lock.notifyAll(); // Notify all waiting threads
            System.out.println("Initialization complete.");
        }
    }

    @Override
    public void request() {
        synchronized (lock) {
            // Wait for initialization to complete
            while (!isInitialized) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Preserve interruption status
                }
            }
            // Check if connection is closed or if request is in progress
            while (isRequestInProgress || isClosed) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Preserve interruption status
                }
            }

            isRequestInProgress = true;
            // Simulate request logic
            System.out.println("Processing request...");
            try {
                Thread.sleep(1000); // Simulate delay
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Preserve interruption status
            }

            isRequestInProgress = false;
            lock.notifyAll(); // Notify threads waiting for close
            System.out.println("Request processing complete.");
        }
    }

    @Override
    public void close() {
        synchronized (lock) {
            // Ensure no request is in progress
            while (isRequestInProgress) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Preserve interruption status
                }
            }
            // Check if connection is already closed
            if (isClosed) {
                throw new IllegalStateException("Client is already closed");
            }

            // Simulate close logic
            System.out.println("Closing...");
            try {
                Thread.sleep(1000); // Simulate delay
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Preserve interruption status
            }

            isClosed = true;
            lock.notifyAll(); // Notify threads waiting for request or close
            System.out.println("Client closed.");
        }
    }
}



abstract class Client {

    public abstract void init();

    public abstract void request();

    public abstract void close();
}

class ClientWrapperTest {

    public static void main(String[] args) {
        ClientWrapper clientWrapper = new ClientWrapper();

        // Thread for initializing
        Thread initThread = new Thread(() -> {
            try {
                clientWrapper.init();
            } catch (Exception e) {
                System.err.println("Init Exception: " + e.getMessage());
            }
        });

        // Threads for requests
        Thread requestThread1 = new Thread(() -> {
            try {
                clientWrapper.request();
            } catch (Exception e) {
                System.err.println("Request1 Exception: " + e.getMessage());
            }
        });

        Thread requestThread2 = new Thread(() -> {
            try {
                clientWrapper.request();
            } catch (Exception e) {
                System.err.println("Request2 Exception: " + e.getMessage());
            }
        });

        // Thread for closing
        Thread closeThread = new Thread(() -> {
            try {
                clientWrapper.close();
            } catch (Exception e) {
                System.err.println("Close Exception: " + e.getMessage());
            }
        });

        // Start threads
        initThread.start();
        try {
            // Wait for initialization to complete before starting requests
            initThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        requestThread1.start();
        requestThread2.start();
        try {
            // Wait for requests to complete before closing
            requestThread1.join();
            requestThread2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        closeThread.start();
        try {
            // Wait for closing to complete
            closeThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}




