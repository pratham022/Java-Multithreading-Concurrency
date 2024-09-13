package commonly_asked_questions;

import java.util.ArrayList;
import java.util.List;

class Node {
    int data;
    Node next;
    public Node(int data) {
        this.data = data;
        next = null;
    }
}
public class BoundedBlockingStackUsingLinkedList {
    Node head;
    int capacity;
    int currSize;
    public BoundedBlockingStackUsingLinkedList(int capacity) {
        this.capacity = capacity;
        head = null;
        currSize = 0;
    }
    boolean empty() {
        return head == null;
    }

    boolean full() {
        return currSize == capacity;
    }

    synchronized void push(int elem) throws InterruptedException {
        while (full()) {
            System.out.println("Stack is full. Cannot push element. " + Thread.currentThread().getName() + " is waiting");
            wait(); // Wait until an element is popped
        }

        Node newNode = new Node(elem);
        newNode.next = head;
        head = newNode;
        currSize++;
        System.out.println(Thread.currentThread().getName() + " pushed " + elem + " into the stack.");
        notifyAll(); // Notify waiting consumers
    }

    synchronized int pop() throws InterruptedException {
        while (empty()) {
            System.out.println("Stack is empty. Cannot pop element. " + Thread.currentThread().getName() + " is waiting");
            wait(); // Wait until an element is pushed
        }

        Node temp = head;
        head = head.next;
        int val = temp.data;
        currSize--;
        System.out.println(Thread.currentThread().getName() + " popped " + val + " from the stack.");
        notifyAll(); // Notify waiting producers
        return val;
    }

    synchronized int size() {
        return currSize;
    }
}

class SolutionStack {
    public static void main(String[] args) throws InterruptedException {
        int producers = 2; // Number of producers
        int consumers = 2; // Number of consumers
        int capacity = 5;  // Stack capacity
        BoundedBlockingStackUsingLinkedList stack = new BoundedBlockingStackUsingLinkedList(capacity);

        List<Thread> producerThreadList = new ArrayList<>();
        List<Thread> consumerThreadList = new ArrayList<>();

        // Creating producer threads
        for (int i = 0; i < producers; i++) {
            int producerId = i;
            Thread producerThread = new Thread(() -> {
                try {
                    for (int j = 1; j <= 5; j++) { // Each producer pushes 5 different values
                        stack.push(j + producerId * 100); // Differentiate producer pushes
                    }
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
            });
            producerThread.setName("Producer-" + i);
            producerThreadList.add(producerThread);
            producerThread.start();
        }

        // Creating consumer threads
        for (int i = 0; i < consumers; i++) {
            Thread consumerThread = new Thread(() -> {
                try {
                    for (int j = 0; j < 5; j++) { // Each consumer pops 5 values
                        stack.pop();
                    }
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
            });
            consumerThread.setName("Consumer-" + i);
            consumerThreadList.add(consumerThread);
            consumerThread.start();
        }

        // Joining producer threads
        for (Thread t : producerThreadList) {
            t.join();
        }

        // Joining consumer threads
        for (Thread t : consumerThreadList) {
            t.join();
        }

        // Checking the final stack size
        System.out.println("Final Stack Size: " + stack.size());
    }
}
