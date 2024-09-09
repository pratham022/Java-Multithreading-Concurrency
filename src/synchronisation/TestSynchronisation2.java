package synchronisation;

class MyThread3 extends Thread {
    Table t;
    MyThread3(Table t) {
        this.t = t;
    }
    public void run() {
        t.printTable(100);
    }
}
class MyThread4 extends Thread {
    Table t;
    MyThread4(Table t) {
        this.t = t;
    }
    public void run() {
        t.printTable(1000);
    }
}
public class TestSynchronisation2 {
    public static void main(String[] args) {
        Table obj1 = new Table();
        Table obj2 = new Table();

        MyThread1 t1 = new MyThread1(obj1);
        MyThread2 t2 = new MyThread2(obj1);
        MyThread3 t3 = new MyThread3(obj2);
        MyThread4 t4 = new MyThread4(obj2);
        t1.start();
        t2.start();
        t3.start();
        t4.start();
    }
}
