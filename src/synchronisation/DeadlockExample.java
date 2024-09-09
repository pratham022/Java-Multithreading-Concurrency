package synchronisation;

public class DeadlockExample {
    public static void main(String[] args) {
        final String resource1 = "Resource 1";
        final String resource2 = "Resource 2";

        Thread t1 = new Thread() {
            public void run() {
                synchronized (resource1) {
                    System.out.println("Resource 1 locked by Thread 1");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        System.out.println(e);
                    }
                }

                synchronized (resource2) {
                    System.out.println("Resource 2 locked by Thread 1");
                }
            }
        };

        Thread t2 = new Thread() {
            public void run() {
                synchronized (resource2) {
                    System.out.println("Resource 2 locked by Thread 2");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        System.out.println(e);
                    }
                }
                synchronized (resource1) {
                    System.out.println("Resource 1 locked by Thread 2");
                }
            }
        };

        t1.start();
        t2.start();
    }
}
