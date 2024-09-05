package threadpool;

public class WorkerThread implements Runnable {
    private String message;
    public WorkerThread(String message) {
        this.message = message;
    }
    @Override
    public void run() {
        System.out.println(String.format("%s [Start] Message: %s", Thread.currentThread().getName(), message));
        processMessage();
        System.out.println(String.format("%s [End] Message: %s", Thread.currentThread().getName(), message));
    }
    private void processMessage() {
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            System.out.println(e);
        }
    }
}
