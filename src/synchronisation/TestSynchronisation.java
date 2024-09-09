package synchronisation;

class Table {
    // synchronized void printTable(int n) {
    synchronized static void printTable(int n) {
        // synchronized (this) {
            for (int i = 0; i < 5; i++) {
                System.out.println(n * i);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
            }
        // }
    }
}

class MyThread1 extends Thread {
    Table t;
    MyThread1(Table t) {
        this.t = t;
    }
    public void run() {
        t.printTable(1);
    }
}
class MyThread2 extends Thread {
    Table t;
    MyThread2(Table t) {
        this.t = t;
    }
    public void run() {
        t.printTable(10);
    }
}
public class TestSynchronisation {
    public static void main(String[] args) {
        Table t = new Table();
        MyThread1 t1 = new MyThread1(t);
        MyThread2 t2 = new MyThread2(t);
        t1.start();
        t2.start();
    }
}
