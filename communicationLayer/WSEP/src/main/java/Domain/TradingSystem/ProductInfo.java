package Domain.TradingSystem;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable (tableName = "productInfos")
public class ProductInfo {

    @DatabaseField(id = true)
    private int id;

    @DatabaseField
    private String name;

    @DatabaseField
    private String category;

    @DatabaseField
    private double rating;

    @DatabaseField
    private double defaultPrice;

    public ProductInfo() {}

    public ProductInfo(int productId, String name, String category, double basePrice) {
        this.id = productId;
        this.name = name;
        this.category = category;
        this.rating = 2.5;
        this.defaultPrice = basePrice;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Product: \n" +
                "id: " + id + "\n" +
                "name: " + name + '\n' +
                "category: " + category + '\n' +
                "rating: " + rating + "\n";
    }

    public double getDefaultPrice() {
        return defaultPrice;
    }
}
