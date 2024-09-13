package commonly_asked_questions;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class LRUCache {
    class Node {
        int key;
        int val;
        Node prev;
        Node next;

        Node(int key, int val) {
            this.key = key;
            this.val = val;
        }
    }

    private final Node head = new Node(-1, -1);
    private final Node tail = new Node(-1, -1);
    private final int cap;
    private final Map<Integer, Node> cache = new HashMap<>();
    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();

    public LRUCache(int capacity) {
        cap = capacity;
        head.next = tail;
        tail.prev = head;
    }

    private void addNode(Node newNode) {
        Node temp = head.next;
        newNode.next = temp;
        newNode.prev = head;
        head.next = newNode;
        temp.prev = newNode;
    }

    private void deleteNode(Node delNode) {
        Node prev = delNode.prev;
        Node next = delNode.next;
        prev.next = next;
        next.prev = prev;
    }

    public int get(int key) {
        rwLock.readLock().lock();
        try {
            if (cache.containsKey(key)) {
                Node node = cache.get(key);
                int value = node.val;

                // Move the accessed node to the front (most recently used)
                deleteNode(node);
                addNode(node);

                return value;
            }
            return -1;
        } finally {
            rwLock.readLock().unlock();
        }
    }

    public void put(int key, int value) {
        rwLock.writeLock().lock();
        try {
            if (cache.containsKey(key)) {
                Node node = cache.get(key);
                deleteNode(node);
                cache.remove(key);
            } else if (cache.size() == cap) {
                // Remove the least recently used node
                Node lru = tail.prev;
                cache.remove(lru.key);
                deleteNode(lru);
            }

            // Add the new node
            Node newNode = new Node(key, value);
            addNode(newNode);
            cache.put(key, newNode);
        } finally {
            rwLock.writeLock().unlock();
        }
    }
}

class LRUCacheTest {
    public static void main(String[] args) {
        final LRUCache cache = new LRUCache(5);

        // Creating write threads
        Thread writer1 = new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                cache.put(i, i * 10);
                System.out.println("Writer1: Put (" + i + ", " + (i * 10) + ")");
                try {
                    Thread.sleep(100); // Simulate some delay
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        Thread writer2 = new Thread(() -> {
            for (int i = 11; i <= 20; i++) {
                cache.put(i, i * 10);
                System.out.println("Writer2: Put (" + i + ", " + (i * 10) + ")");
                try {
                    Thread.sleep(100); // Simulate some delay
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        // Creating read threads
        Thread reader1 = new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                int value = cache.get(i);
                System.out.println("Reader1: Get (" + i + ") = " + value);
                try {
                    Thread.sleep(50); // Simulate some delay
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        Thread reader2 = new Thread(() -> {
            for (int i = 5; i <= 15; i++) {
                int value = cache.get(i);
                System.out.println("Reader2: Get (" + i + ") = " + value);
                try {
                    Thread.sleep(50); // Simulate some delay
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        // Start threads
        writer1.start();
        writer2.start();
        reader1.start();
        reader2.start();

        // Wait for threads to finish
        try {
            writer1.join();
            writer2.join();
            reader1.join();
            reader2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

