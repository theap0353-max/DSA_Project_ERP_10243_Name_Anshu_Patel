import java.util.Scanner;

/**
 * Supermarket Billing System - menu-driven console application.
 * Data structures: Array, HashMap, Queue, Stack.
 */
public class Main {

    private static int readChoice(Scanner sc) {
        try {
            return Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // 1-2. Initialise structures and load sample products
        Inventory inventory = new Inventory();
        inventory.addProduct("P101", "Rice 5kg", 320, 20);
        inventory.addProduct("P102", "Cooking Oil 1L", 145, 30);
        inventory.addProduct("P103", "Sugar 1kg", 48, 25);
        inventory.addProduct("P104", "Tea 250g", 110, 15);
        BillingCounter counter = new BillingCounter(inventory);

        int choice;
        do {
            System.out.println();
            System.out.println("========== SUPERMARKET BILLING SYSTEM ==========");
            System.out.println("1. Display Products");
            System.out.println("2. Search Product by ID");
            System.out.println("3. Add Customer to Queue");
            System.out.println("4. Serve Next Customer");
            System.out.println("5. Generate Customer Bill");
            System.out.println("6. View Recent Transaction");
            System.out.println("7. Undo Latest Eligible Transaction");
            System.out.println("8. Exit");
            System.out.print("Enter your choice: ");
            choice = readChoice(sc);

            switch (choice) {
                case 1:
                    inventory.displayProducts();
                    break;
                case 2:
                    System.out.print("Enter product ID: ");
                    Product p = inventory.findProduct(sc.nextLine());
                    if (p == null) {
                        System.out.println("Product not found. Enter a valid ID.");
                    } else {
                        System.out.println("Product found - " + p);
                    }
                    break;
                case 3:
                    System.out.print("Enter customer name: ");
                    counter.enqueueCustomer(sc.nextLine());
                    break;
                case 4:
                    counter.serveNextCustomer();
                    break;
                case 5:
                    counter.generateBill(sc);
                    break;
                case 6:
                    counter.viewRecentTransaction();
                    break;
                case 7:
                    counter.undoLastTransaction(sc);
                    break;
                case 8:
                    System.out.println("Thank you for using the Supermarket Billing System.");
                    break;
                default:
                    System.out.println("Invalid option. Please enter a number from 1 to 8.");
            }
        } while (choice != 8);

        sc.close();
    }
}
