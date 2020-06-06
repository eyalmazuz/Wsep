package Domain.TradingSystem;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.types.DateTimeType;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@DatabaseTable (tableName = "purchaseDetails")
public class PurchaseDetails {

    @DatabaseField (id = true)
    private int id;

    @DatabaseField (dataType = DataType.TIME_STAMP)
    private Timestamp dateTime;

    private User user;

    @DatabaseField
    private int subscriberId = -1;

    @DatabaseField (foreign = true)
    private Store store;

    @DatabaseField (dataType = DataType.SERIALIZABLE)
    private HashMap<ProductInfo, Integer> products;

    @DatabaseField
    private double price;

    public static int nextPurchaseId = 0;

    public PurchaseDetails () {}

    public PurchaseDetails(User user, Store store, HashMap<ProductInfo, Integer> products, double price) {
        this.dateTime = new Timestamp(java.lang.System.currentTimeMillis());
        this.id = nextPurchaseId;
        nextPurchaseId++;
        this.user = user;
        if (user.getState() instanceof Subscriber) subscriberId = ((Subscriber) user.getState()).getId();
        this.store = store;
        this.products = products;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public LocalDateTime getTime() {
        return dateTime.toLocalDateTime();
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
            output += product.getKey().getName() + "\n" +
                    "Amount: " + String.valueOf(product.getValue()) + "\n";

        }
        output += "Price: " + String.valueOf(price) + "\n";
        return output;
    }

    public boolean equals(Object other) {
        if (other instanceof PurchaseDetails) return ((PurchaseDetails) other).getId() == id;
        return false;
    }
}
