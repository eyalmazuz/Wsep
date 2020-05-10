package DTOs.SimpleDTOS;

public class ProductInStoreDTO {
    int productId;
    String name;
    String category;
    int amount;
    String info;
    int storeId;

    public ProductInStoreDTO(int productId, String name, String category, int amount, String info, int storeId) {
        this.productId = productId;
        this.name = name;
        this.category = category;
        this.amount = amount;
        this.info = info;
        this.storeId = storeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }


    public String toString() {
        return "Store ID: " + getStoreId() + ", product ID: " + getProductId() + ", amount: " + getAmount() + ", info: " + getInfo();
    }
}
