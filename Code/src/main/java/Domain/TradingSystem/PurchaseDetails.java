package Domain.TradingSystem;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class PurchaseDetails {

    private int id;
    private LocalDateTime dateTime;
    private User user;
    private Store store;

    private Map<ProductInfo, Integer> products;
    private double price;

    public PurchaseDetails(int id, User user, Store store, Map<ProductInfo, Integer> products, double price) {
        this.dateTime = LocalDateTime.now();
        this.id = id;
        this.user = user;
        this.store = store;
        this.products = products;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public LocalDateTime getTime() {
        return dateTime;
    }

    public User getUser() {
        return user;
    }

    public Map<ProductInfo, Integer> getProducts() {
        return products;
    }

    public Store getStore() {
        return store;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        String output = "";
        for (Map.Entry<ProductInfo, Integer> product : products.entrySet()) {
            output += product.getKey() + "\n" +
                    "Amount: " + String.valueOf(product.getValue()) + "\n";

        }
        output += "Price: " + String.valueOf(price) + "\n";
        return output;
    }
}
