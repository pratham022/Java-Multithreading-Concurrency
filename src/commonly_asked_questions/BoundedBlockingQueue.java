package commonly_asked_questions;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class BoundedBlockingQueue {
    int queue[];
    int capacity;
    int front, rear;
    int currSize;
    public BoundedBlockingQueue(int capacity) {
        this.capacity= capacity;
        queue = new int[capacity];
        front = -1;
        rear = -1;
        currSize = 0;
    }
    synchronized void enqueue(int elem) throws InterruptedException {
        while (full()) {
            System.out.println("Queue is full. Cannot enqueue this elem. " + Thread.currentThread().getName() + " is waiting.");
            wait();
        }
        if (empty()) {
            front = 0;
            rear = 0;
        } else {
            rear = (rear + 1) % capacity;
        }
        queue[rear] = elem;
        currSize++;
        System.out.println(Thread.currentThread().getName() + " Enqueued " + elem + " into the queue.");
        notifyAll();
    }
    synchronized void deque() throws InterruptedException {
        while (empty()) {
            System.out.println("Queue is empty. Cannot dequeue. " + Thread.currentThread().getName() + " is waiting.");
            wait();
        }
        if (front == rear) {
            front = -1;
            rear = -1;
        } else {
            front = (front + 1) % capacity;
        }
        currSize--;
        System.out.println(Thread.currentThread().getName() + " Dequeued element from queue.");
        notifyAll();
    }
    boolean full() {
        if ((rear + 1) % capacity == front) {
            return true;
        } else {
            return false;
        }
    }
    boolean empty() {
        if (front == -1) return true;
        else return false;
    }
    int size() {
        System.out.println("Queue size is: " + currSize);
        return currSize;
    }
}

class Solution {
    public static void main(String[] args) throws InterruptedException {
        int producerThreads = 2;
        int consumerThreads = 2;
        int capacity = 2;
        BoundedBlockingQueue boundedBlockingQueue = new BoundedBlockingQueue(capacity);

        List<Thread> producerThreadList = new ArrayList<>();
        List<Thread> consumerThreadList = new ArrayList<>();

        for (int i = 0; i < producerThreads; i++) {
            Thread producerThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        boundedBlockingQueue.enqueue(1);
                        boundedBlockingQueue.enqueue(2);
                        boundedBlockingQueue.enqueue(3);
                        boundedBlockingQueue.enqueue(4);
                        boundedBlockingQueue.enqueue(5);
                    } catch (InterruptedException e) {
                        System.out.println(e);
                    }

                }
            });
            producerThreadList.add(producerThread);
            producerThread.setName("Producer - " + i);
            producerThread.start();
        }

        for (int i = 0; i < consumerThreads; i++) {
            Thread consumerThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        boundedBlockingQueue.deque();
                        boundedBlockingQueue.deque();
                        boundedBlockingQueue.deque();
                        boundedBlockingQueue.deque();
                    } catch (InterruptedException e) {
                        System.out.println(e);
                    }

                }
            });
            consumerThreadList.add(consumerThread);
            consumerThread.setName("Consumer - " + i);
            consumerThread.start();
        }
        for (int i = 0; i < producerThreads; i++) {
            producerThreadList.get(i).join();
        }
        for (int i = 0; i < consumerThreads; i++) {
            consumerThreadList.get(i).join();
        }
        boundedBlockingQueue.size();
    }
}



/**
 1188. Design Bounded Blocking Queue
 Implement a thread safe bounded blocking queue that has the following methods:

 BoundedBlockingQueue(int capacity) The constructor initializes the queue with a maximum capacity.
 void enqueue(int element) Adds an element to the front of the queue. If the queue is full, the calling thread is blocked until the queue is no longer full.
 int dequeue() Returns the element at the rear of the queue and removes it. If the queue is empty, the calling thread is blocked until the queue is no longer empty.
 int size() Returns the number of elements currently in the queue.
 Your implementation will be tested using multiple threads at the same time. Each thread will either be a producer thread that only makes calls to the enqueue method or a consumer thread that only makes calls to the dequeue method. The size method will be called after every test case.

 Please do not use built-in implementations of bounded blocking queue as this will not be accepted in an interview.



 Example 1:

 Input:
 1
 1
 ["BoundedBlockingQueue","enqueue","dequeue","dequeue","enqueue","enqueue","enqueue","enqueue","dequeue"]
 [[2],[1],[],[],[0],[2],[3],[4],[]]

 Output:
 [1,0,2,2]

 Explanation:
 Number of producer threads = 1
 Number of consumer threads = 1

 BoundedBlockingQueue queue = new BoundedBlockingQueue(2);   // initialize the queue with capacity = 2.

 queue.enqueue(1);   // The producer thread enqueues 1 to the queue.
 queue.dequeue();    // The consumer thread calls dequeue and returns 1 from the queue.
 queue.dequeue();    // Since the queue is empty, the consumer thread is blocked.
 queue.enqueue(0);   // The producer thread enqueues 0 to the queue. The consumer thread is unblocked and returns 0 from the queue.
 queue.enqueue(2);   // The producer thread enqueues 2 to the queue.
 queue.enqueue(3);   // The producer thread enqueues 3 to the queue.
 queue.enqueue(4);   // The producer thread is blocked because the queue's capacity (2) is reached.
 queue.dequeue();    // The consumer thread returns 2 from the queue. The producer thread is unblocked and enqueues 4 to the queue.
 queue.size();       // 2 elements remaining in the queue. size() is always called at the end of each test case.


 Example 2:

 Input:
 3
 4
 ["BoundedBlockingQueue","enqueue","enqueue","enqueue","dequeue","dequeue","dequeue","enqueue"]
 [[3],[1],[0],[2],[],[],[],[3]]

 Output:
 [1,0,2,1]

 Explanation:
 Number of producer threads = 3
 Number of consumer threads = 4

 BoundedBlockingQueue queue = new BoundedBlockingQueue(3);   // initialize the queue with capacity = 3.

 queue.enqueue(1);   // Producer thread P1 enqueues 1 to the queue.
 queue.enqueue(0);   // Producer thread P2 enqueues 0 to the queue.
 queue.enqueue(2);   // Producer thread P3 enqueues 2 to the queue.
 queue.dequeue();    // Consumer thread C1 calls dequeue.
 queue.dequeue();    // Consumer thread C2 calls dequeue.
 queue.dequeue();    // Consumer thread C3 calls dequeue.
 queue.enqueue(3);   // One of the producer threads enqueues 3 to the queue.
 queue.size();       // 1 element remaining in the queue.

 Since the number of threads for producer/consumer is greater than 1, we do not know how the threads will be scheduled in the operating system, even though the input seems to imply the ordering. Therefore, any of the output [1,0,2] or [1,2,0] or [0,1,2] or [0,2,1] or [2,0,1] or [2,1,0] will be accepted.

 **/