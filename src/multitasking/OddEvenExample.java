package multitasking;

public class OddEvenExample {
    int counter = 1;
    static int NUM;
    public void displayOddNumber() {
        synchronized (this) {
            while (counter < NUM) {
                while (counter % 2 == 0) {
                    // counter is even, wait for even thread
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        System.out.println(e);
                    }

                }
                System.out.println(counter);
                counter++;
                // notify the other thread
                notify();
            }
        }
    }
    public void displayEvenNumber() {
        synchronized (this) {
            while (counter < NUM) {
                while (counter % 2 == 1) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        System.out.println(e);
                    }
                }
                System.out.println(counter);
                counter++;
                notify();
            }
        }
    }

    public static void main(String[] args) {
        NUM = 20;

        OddEvenExample oe = new OddEvenExample();

        Thread th1 = new Thread(new Runnable() {
            @Override
            public void run() {
                oe.displayOddNumber();
            }
        });

        Thread th2 = new Thread(new Runnable() {
            @Override
            public void run() {
                oe.displayEvenNumber();
            }
        });

        th1.start();
        th2.start();
    }
}
