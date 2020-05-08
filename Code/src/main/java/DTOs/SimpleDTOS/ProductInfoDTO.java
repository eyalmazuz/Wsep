package DTOs.SimpleDTOS;

public class ProductInfoDTO {
    private int id;
    private String name;
    private String category;
    private double rating;

    public ProductInfoDTO(int productId, String name, String category,double rating) {
        this.id = productId;
        this.name = name;
        this.category = category;
        this.rating = rating;
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

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Product: \n" +
                "id: " + id + "\n" +
                "name: " + name + '\n' +
                "category: " + category + '\n' +
                "rating: " + rating + "\n";
    }
}
