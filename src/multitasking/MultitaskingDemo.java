package multitasking;

class SimpleTask1 extends Thread {
    public void run() {
        System.out.println("Task One");
    }
}
class SimpleTask2 extends Thread {
    public void run() {
        System.out.println("Task Two");
    }
}
public class MultitaskingDemo {
    public static void main(String[] args) {
        SimpleTask1 t1 = new SimpleTask1();
        SimpleTask2 t2 = new SimpleTask2();

        t1.start();
        t2.start();
    }
}
