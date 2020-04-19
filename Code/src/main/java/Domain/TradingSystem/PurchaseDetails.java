package Domain.TradingSystem;

import java.time.LocalDateTime;
import java.util.Map;

public class PurchaseDetails {

    private int id;
    private LocalDateTime dateTime;
    private User user;
    private Map<Integer, Integer> products;
    private double price;

    public PurchaseDetails(int id, User user, Map<Integer, Integer> products, double price) {
        this.dateTime = LocalDateTime.now();
        this.id = id;
        this.user = user;
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

    public Map<Integer, Integer> getProducts() {
        return products;
    }

    public double getPrice() {
        return price;
    }
}
