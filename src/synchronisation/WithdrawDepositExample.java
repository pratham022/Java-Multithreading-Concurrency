package synchronisation;

class Customer {
    int amount = 10000;

    synchronized void withdrawAmount(int amount) {
        if (this.amount < amount) {
            System.out.println("Not sufficient balance in account. Wait until a sufficient amount is deposited. ");
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
        this.amount -= amount;
        System.out.println(amount + " Rs. Withdraw completed");
    }

    synchronized void depositAmount(int amount) {
        this.amount += amount;
        System.out.println(amount + " Rs. Amount deposited");
        notify();
    }
}
public class WithdrawDepositExample {
    public static void main(String[] args) {
        Customer c = new Customer();

        Thread t1 = new Thread() {
            public void run() {
                c.withdrawAmount(15000);
            }
        };
        t1.start();

        Thread t2 = new Thread() {
            public void run() {
                c.depositAmount(10000);
            }
        };
        t2.start();
    }
}
