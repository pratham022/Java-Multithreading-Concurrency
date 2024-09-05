public class InitiateAThread2 implements Runnable {

    /**
     * Implement Runnable: The class InitiateAThread2 implements the Runnable interface.
     * The Runnable interface requires you to implement the run() method, which defines the code that should be executed in the thread.
     */

    @Override
    public void run() {
        System.out.println("Thread is running");
    }

    public static void main(String[] args) {
        InitiateAThread2 obj = new InitiateAThread2();

        /**
         * Create a Thread Object: In the main() method, you create an instance of InitiateAThread2 and pass it to a new Thread object.
         * This Thread object takes the Runnable instance as a parameter.
         */
        Thread t = new Thread(obj);
        t.start(); // // Start the new thread
    }
}
