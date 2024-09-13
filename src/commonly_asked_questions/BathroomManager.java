package commonly_asked_questions;

import java.util.LinkedList;
import java.util.Queue;

public class BathroomManager {
    private static final int MAX_CAPACITY = 3;
    private static final int MAX_CONSECUTIVE_TURNS = 2;

    private final Object lock = new Object();
    private final Queue<Person> democratQueue = new LinkedList<>();
    private final Queue<Person> republicanQueue = new LinkedList<>();
    private int bathroomOccupancy = 0;
    private String currentParty = "None";  // "Democrat" or "Republican"
    private int consecutiveTurns = 0;

    public void enterAndLeave(Person person) {
        try {
            enterBathroom(person);
            // Simulate time spent in the bathroom
            Thread.sleep(1000);
            leaveBathroom(person);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void enterBathroom(Person person) throws InterruptedException {
        synchronized (lock) {
            // Add the person to their respective queue
            if (person.isDemocrat()) {
                democratQueue.add(person);
            } else {
                republicanQueue.add(person);
            }
            System.out.println(person.name + " wants to enter bathroom");

            // Wait until the bathroom has room for their party and conditions are met
            while ((!person.getParty().equals(currentParty) && !currentParty.equals("None")) ||
                    bathroomOccupancy >= MAX_CAPACITY ||
                    (consecutiveTurns >= MAX_CONSECUTIVE_TURNS && person.getParty().equals(currentParty))) {
                System.out.println(person.name + " is waiting.");
                lock.wait();  // Wait until conditions allow entering
            }

            // When conditions are met, pop the person from the queue and enter the bathroom
            if (currentParty.equals("None") || currentParty.equals(person.getParty())) {
                currentParty = person.getParty();
                bathroomOccupancy++;
                consecutiveTurns++;

                if (person.isDemocrat()) {
                    democratQueue.poll();  // Remove the person from the queue
                } else {
                    republicanQueue.poll();  // Remove the person from the queue
                }

                System.out.println(person.name + " entered the bathroom. Occupancy: " + bathroomOccupancy);
            }
        }
    }

    public void leaveBathroom(Person person) {
        synchronized (lock) {
            bathroomOccupancy--;
            System.out.println(person.name + " leaves bathroom. Occupancy: " + bathroomOccupancy);

            // Reset consecutive turns if the bathroom is empty
            if (bathroomOccupancy == 0) {
                consecutiveTurns = 0;
                System.out.println("Bathroom is now empty.");
            }

            // If max turns are reached, or bathroom is empty, switch parties if necessary
            if (bathroomOccupancy == 0 || consecutiveTurns >= MAX_CONSECUTIVE_TURNS) {
                if (currentParty.equals("Democrat") && !republicanQueue.isEmpty()) {
                    currentParty = "Republican";
                    consecutiveTurns = 0;  // Reset turn counter for new party
                } else if (currentParty.equals("Republican") && !democratQueue.isEmpty()) {
                    currentParty = "Democrat";
                    consecutiveTurns = 0;
                } else if (democratQueue.isEmpty() && republicanQueue.isEmpty()) {
                    currentParty = "None";  // No one is waiting
                }
            }

            // Notify waiting threads
            lock.notifyAll();
        }
    }

    public static class Person {
        private final String name;
        private final boolean isDemocrat;

        public Person(String name, boolean isDemocrat) {
            this.name = name;
            this.isDemocrat = isDemocrat;
        }

        public boolean isDemocrat() {
            return isDemocrat;
        }

        public String getParty() {
            return isDemocrat ? "Democrat" : "Republican";
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public static void main(String[] args) {
        BathroomManager manager = new BathroomManager();

        // Create example people
        Person alice = new Person("Alice", true);  // Democrat
        Person bob = new Person("Bob", true);      // Democrat
        Person charlie = new Person("Charlie", false);  // Republican
        Person dave = new Person("Dave", false);   // Republican
        Person eve = new Person("Eve", true);      // Democrat

        // Create threads to simulate people entering and leaving the bathroom
        new Thread(() -> manager.enterAndLeave(alice)).start();
        new Thread(() -> manager.enterAndLeave(bob)).start();
        new Thread(() -> manager.enterAndLeave(charlie)).start();
        new Thread(() -> manager.enterAndLeave(dave)).start();
        new Thread(() -> manager.enterAndLeave(eve)).start();
    }
}
