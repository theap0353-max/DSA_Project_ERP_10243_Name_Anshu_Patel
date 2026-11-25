import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

/**
 * BillingCounter: uses a QUEUE (FIFO) for waiting customers and a
 * STACK (LIFO) for completed transactions.
 */
public class BillingCounter {
    private static final double DISCOUNT_RATE = 0.05; // 5%

    private final Inventory inventory;
    private final Queue<Customer> waitingCustomers = new LinkedList<>();
    private final Stack<Transaction> history = new Stack<>();
    private Customer currentCustomer = null; // customer being served
    private int nextCustomerNumber = 1;

    public BillingCounter(Inventory inventory) {
        this.inventory = inventory;
    }

    /** Adds a customer at the rear of the queue. */
    public boolean enqueueCustomer(String name) {
        if (name == null || name.trim().isEmpty()) {
            System.out.println("Customer name cannot be blank.");
            return false;
        }
        Customer c = new Customer("C" + nextCustomerNumber++, name.trim());
        waitingCustomers.offer(c);
        System.out.println("Added to queue: " + c + "  (waiting: " + waitingCustomers.size() + ")");
        return true;
    }

    /** Removes the customer at the front of the queue. */
    public void serveNextCustomer() {
        if (currentCustomer != null) {
            System.out.println("Finish billing " + currentCustomer + " first (menu option 5).");
            return;
        }
        Customer c = waitingCustomers.poll();
        if (c == null) {
            System.out.println("The customer queue is empty.");
            return;
        }
        currentCustomer = c;
        System.out.println("Now serving: " + c);
        if (!waitingCustomers.isEmpty()) {
            System.out.println("Next in line: " + waitingCustomers.peek());
        }
    }

    /** Builds a bill for the customer currently being served. */
    public void generateBill(Scanner sc) {
        if (currentCustomer == null) {
            System.out.println("No customer is being served. Use option 4 first.");
            return;
        }
        Transaction bill = new Transaction(currentCustomer);
        System.out.println("Billing: " + currentCustomer);
        System.out.println("Enter product ID and quantity. Type DONE as the ID to finish.");

        while (true) {
            System.out.print("Product ID: ");
            String id = sc.nextLine().trim();
            if (id.equalsIgnoreCase("DONE")) {
                break;
            }
            if (id.isEmpty()) {
                System.out.println("Product ID cannot be blank.");
                continue;
            }
            Product p = inventory.findProduct(id);          // HashMap lookup
            if (p == null) {
                System.out.println("Product not found. Enter a valid ID.");
                continue;
            }
            System.out.print("Quantity (" + p.getProductName() + ", stock " + p.getQuantity() + "): ");
            int qty;
            try {
                qty = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid quantity. Enter a whole number.");
                continue;
            }
            if (qty <= 0) {
                System.out.println("Quantity must be positive.");
                continue;
            }
            int alreadyOnBill = bill.quantityOf(p.getProductId());
            if (qty + alreadyOnBill > p.getQuantity()) {
                System.out.println("Insufficient stock; item not added.");
                continue;
            }
            bill.addItem(p, qty);
            System.out.printf("Added %d x %s = Rs %.2f%n", qty, p.getProductName(), p.getPrice() * qty);
        }

        if (bill.getItems().isEmpty()) {
            System.out.println("No items added. Bill cancelled.");
            return;
        }

        bill.calculate(DISCOUNT_RATE);
        bill.printReceipt(DISCOUNT_RATE);

        System.out.print("Confirm sale? (Y/N): ");
        String answer = sc.nextLine().trim();
        if (!answer.equalsIgnoreCase("Y")) {
            System.out.println("Sale cancelled. Stock unchanged.");
            return;
        }
        for (Transaction.Item it : bill.getItems()) {
            inventory.updateStock(it.getProduct().getProductId(), it.getQuantity());
        }
        pushTransaction(bill);
        System.out.println("Sale completed. Stock updated.");
        currentCustomer = null;
    }

    /** Pushes a completed transaction onto the history stack. */
    public void pushTransaction(Transaction t) {
        history.push(t);
    }

    /** Shows the latest transaction (top of the stack) without removing it. */
    public void viewRecentTransaction() {
        if (history.isEmpty()) {
            System.out.println("No transactions yet.");
            return;
        }
        System.out.println("Most recent transaction (" + history.size() + " stored):");
        history.peek().printReceipt(DISCOUNT_RATE);
    }

    /** Pops the latest transaction and puts its items back into stock. */
    public void undoLastTransaction(Scanner sc) {
        if (history.isEmpty()) {
            System.out.println("Nothing to undo.");
            return;
        }
        Transaction t = history.peek();
        System.out.printf("Undo bill of %s (Rs %.2f)? (Y/N): ", t.getCustomer(), t.getTotal());
        if (!sc.nextLine().trim().equalsIgnoreCase("Y")) {
            System.out.println("Undo cancelled.");
            return;
        }
        history.pop();
        for (Transaction.Item it : t.getItems()) {
            it.getProduct().increaseStock(it.getQuantity());
        }
        System.out.println("Transaction undone. Stock restored.");
    }
}
