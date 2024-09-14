package commonly_asked_questions;

class CustomReadWriteLock {
    private int readers = 0;       // Number of active readers
    private int writers = 0;       // Number of active writers
    private int writeRequests = 0; // Number of write requests (waiting writers)

    public synchronized void lockRead() throws InterruptedException {
        while (writers > 0 || writeRequests > 0) {
            // If there's an active writer or pending writers, block readers
            wait();
        }
        readers++;
    }

    public synchronized void unlockRead() {
        readers--;
        if (readers == 0) {
            // Notify writers if no active readers
            notifyAll();
        }
    }

    public synchronized void lockWrite() throws InterruptedException {
        writeRequests++;
        while (readers > 0 || writers > 0) {
            // Wait if there are active readers or writers
            wait();
        }
        writeRequests--;
        writers++;
    }

    public synchronized void unlockWrite() {
        writers--;
        // Notify all waiting threads (readers or writers)
        notifyAll();
    }
}

