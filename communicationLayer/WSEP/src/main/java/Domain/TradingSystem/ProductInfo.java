package Domain.TradingSystem;

public class ProductInfo {
    private int id;
    private String name;
    private String category;
    private double rating;
    private double defaultPrice;

    public ProductInfo(int productId, String name, String category) {
        this.id = productId;
        this.name = name;
        this.category = category;
        this.rating = 2.5;
        this.defaultPrice = 5;
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
