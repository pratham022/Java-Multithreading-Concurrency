public class InitiateAThread extends Thread {

    // extend the Thread class
    /**
     * used to perform action for a thread
     */
    public void run() {
        System.out.println("thread is running");
    }

    /**
     * start() method is used to start the execution of the thread. JVM calls the run() method of Thread
     * @param args
     */
    public static void main(String[] args) {
        InitiateAThread obj = new InitiateAThread();
        obj.start();
    }
}
