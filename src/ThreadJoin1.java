public class ThreadJoin1 extends Thread {
    public void run() {
        for (int i = 0; i < 3; i++) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println(e);
            }
            System.out.println(Thread.currentThread().getName() + " is running and i is " + i);
        }
    }

    public static void main(String[] args) {
        ThreadJoin1 obj1 = new ThreadJoin1();
        ThreadJoin1 obj2 = new ThreadJoin1();
        obj1.start();
        obj2.start();

        try {
            obj1.join();
            obj2.join();
        } catch (InterruptedException e) {
            System.out.println(e);
        }

        System.out.println("All threads finished execution");
    }
}
