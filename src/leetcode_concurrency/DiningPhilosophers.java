package leetcode_concurrency;

public class DiningPhilosophers {
    private boolean forks[];

    public DiningPhilosophers() {
        forks = new boolean[5];
        for (int i = 0; i < 5; i++) forks[i] = true;
    }

    synchronized public void wantsToEat(int philosopher, Runnable pickLeftFork, Runnable pickRightFork,
                           Runnable eat, Runnable putLeftFork, Runnable putRightFork) throws InterruptedException {
        int leftFork = philosopher;
        int rightFork = (philosopher + 1) % 5;

        while (!forks[leftFork] || !forks[rightFork]) {
            wait();
        }

        forks[leftFork] = false;
        forks[rightFork] = false;
        pickLeftFork.run();
        pickRightFork.run();
        eat.run();
        putLeftFork.run();
        putRightFork.run();
        forks[leftFork] = true;
        forks[rightFork] = true;
        notifyAll();
    }

    public static void main(String[] args) {
        DiningPhilosophers diningPhilosophers = new DiningPhilosophers();
        Runnable pickLeftFork = new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + " picked left fork");
            }
        };
        Runnable pickRightFork = new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + " picked right fork");
            }
        };
        Runnable eat = new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + " is eating");
            }
        };
        Runnable putLeftFork = new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + " put left fork");
            }
        };
        Runnable putRightFork = new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + " put right fork");
            }
        };

        for (int i = 0; i < 5; i++) {
            final int philosopher = i;
            Thread philosopherThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        diningPhilosophers.wantsToEat(philosopher, pickLeftFork, pickRightFork, eat, putLeftFork, putRightFork);
                    } catch (InterruptedException e) {
                        System.out.println(e);
                    }
                }
            }, "Philosopher " + philosopher);
            philosopherThread.start();
        }
    }
}
