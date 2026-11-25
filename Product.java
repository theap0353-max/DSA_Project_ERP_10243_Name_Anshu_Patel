/**
 * Product: stores product ID, name, price and stock quantity.
 */
public class Product {
    private String productId;
    private String productName;
    private double price;
    private int quantity;

    public Product(String productId, String productName, double price, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
    }

    public String getProductId()   { return productId; }
    public String getProductName() { return productName; }
    public double getPrice()       { return price; }
    public int getQuantity()       { return quantity; }

    public void setProductName(String productName) { this.productName = productName; }
    public void setPrice(double price)             { this.price = price; }
    public void setQuantity(int quantity)          { this.quantity = quantity; }

    /** Reduces stock only if enough quantity is available. */
    public boolean reduceStock(int qty) {
        if (qty <= 0 || qty > quantity) {
            return false;
        }
        quantity -= qty;
        return true;
    }

    /** Adds stock back (used when a transaction is undone). */
    public void increaseStock(int qty) {
        if (qty > 0) {
            quantity += qty;
        }
    }

    @Override
    public String toString() {
        return String.format("%-6s | %-16s | Rs %8.2f | Stock %4d",
                productId, productName, price, quantity);
    }
}
