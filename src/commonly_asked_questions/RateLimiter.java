package commonly_asked_questions;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class RateLimiter {
    private final Queue<Request> requestQueue;
    private final int capacity;  // Maximum number of requests allowed
    private final long expireDuration;  // Time after which requests expire
    private final ReentrantLock lock = new ReentrantLock();  // Lock for concurrency control

    public RateLimiter(int capacity, long expireDuration) {
        this.requestQueue = new LinkedList<>();
        this.capacity = capacity;
        this.expireDuration = expireDuration;  // Time in milliseconds
    }

    // Thread-safe version of canAccept
    public boolean canAccept() {
        lock.lock();  // Lock the section of code that modifies shared resources
        try {
            cleanUpExpiredRequests();  // Remove expired requests
            if (requestQueue.size() < capacity) {
                // Add the request to the queue with the current timestamp
                requestQueue.offer(new Request(System.currentTimeMillis()));
                return true;
            }
            return false;
        } finally {
            lock.unlock();  // Unlock after the critical section
        }
    }

    // Remove expired requests from the queue
    private void cleanUpExpiredRequests() {
        long currentTime = System.currentTimeMillis();
        // Check if any requests have expired
        while (!requestQueue.isEmpty() && (currentTime - requestQueue.peek().timestamp >= expireDuration)) {
            requestQueue.poll();  // Remove expired requests from the front of the queue
        }
    }

    // Nested Request class to store request timestamps
    private static class Request {
        long timestamp;  // Timestamp when the request was made

        public Request(long timestamp) {
            this.timestamp = timestamp;
        }
    }

    // Main method to simulate multithreaded rate limiting
    public static void main(String[] args) throws InterruptedException {
        RateLimiter rateLimiter = new RateLimiter(3, 5000);  // Capacity of 3, expiration time of 5 seconds

        // Create multiple threads to test concurrency
        Thread t1 = new Thread(() -> testRateLimiter(rateLimiter, "Thread-1"));
        Thread t2 = new Thread(() -> testRateLimiter(rateLimiter, "Thread-2"));
        Thread t3 = new Thread(() -> testRateLimiter(rateLimiter, "Thread-3"));
        Thread t4 = new Thread(() -> testRateLimiter(rateLimiter, "Thread-4"));

        t1.start();
        t2.start();
        t3.start();
        t4.start();

        // Join the threads to ensure they complete before exiting main
        t1.join();
        t2.join();
        t3.join();
        t4.join();
    }

    // Method to test RateLimiter with a specific thread name
    private static void testRateLimiter(RateLimiter rateLimiter, String threadName) {
        for (int i = 0; i < 5; i++) {
            if (rateLimiter.canAccept()) {
                System.out.println(threadName + " - Request accepted at " + System.currentTimeMillis());
            } else {
                System.out.println(threadName + " - Request rejected at " + System.currentTimeMillis());
            }
            try {
                Thread.sleep(1000);  // Wait 1 second between requests
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}




