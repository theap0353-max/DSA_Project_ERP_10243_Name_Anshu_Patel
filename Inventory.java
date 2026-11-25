import java.util.HashMap;

/**
 * Inventory: products are stored in an ARRAY and indexed in a HASHMAP.
 *  - Array   : display all products          -> O(N)
 *  - HashMap : find product by unique ID     -> O(1) average
 */
public class Inventory {
    private static final int CAPACITY = 50;

    private final Product[] products = new Product[CAPACITY];
    private final HashMap<String, Product> productMap = new HashMap<>();
    private int count = 0;

    /** Inserts into the array and the HashMap. Returns false if invalid. */
    public boolean addProduct(String id, String name, double price, int qty) {
        if (id == null || id.trim().isEmpty() || name == null || name.trim().isEmpty()) {
            return false;
        }
        if (price <= 0 || qty < 0 || count >= CAPACITY) {
            return false;
        }
        id = id.trim().toUpperCase();
        if (productMap.containsKey(id)) {
            return false; // IDs must be unique
        }
        Product p = new Product(id, name.trim(), price, qty);
        products[count++] = p;
        productMap.put(id, p);
        return true;
    }

    /** HashMap lookup by product ID. Returns null if not found. */
    public Product findProduct(String id) {
        if (id == null || id.trim().isEmpty()) {
            return null;
        }
        return productMap.get(id.trim().toUpperCase());
    }

    /** Validates and reduces stock of a product. */
    public boolean updateStock(String id, int qty) {
        Product p = findProduct(id);
        return p != null && p.reduceStock(qty);
    }

    /** Traverses the array and prints every product. */
    public void displayProducts() {
        System.out.println();
        System.out.println("---------------- PRODUCT CATALOGUE ----------------");
        if (count == 0) {
            System.out.println("No products available.");
        }
        for (int i = 0; i < count; i++) {
            System.out.println(products[i]);
        }
        System.out.println("---------------------------------------------------");
    }
}
