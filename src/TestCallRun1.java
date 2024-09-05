public class TestCallRun1 extends Thread {
    public void run() {
        System.out.println("Thread is running");
    }

    public static void main(String[] args) {
        TestCallRun1 obj = new TestCallRun1();
        obj.run(); //works, but does not start a separate call stack
        obj.run(); // As we can see in the above program that there is no context-switching
                    // because here t1 and t2 will be treated as normal object not thread object.
    }
}
