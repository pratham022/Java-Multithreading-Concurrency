public class InitiateAThread3 {
    public static void main(String[] args) {
        Thread t = new Thread("Thread 1");
        t.start();

        String threadName =  t.getName();
        System.out.println(String.format("%s is running", threadName));
    }
}
