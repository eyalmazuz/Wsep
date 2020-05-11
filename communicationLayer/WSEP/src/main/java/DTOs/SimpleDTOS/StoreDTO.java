package DTOs.SimpleDTOS;

import java.util.List;

public class StoreDTO {
    int storeId;
    String buyingPolicy;
    String discountPolicy;
    List<ProductInStoreDTO> products;

    public StoreDTO(int storeId, String buyingPolicy, String discountPolicy, List<ProductInStoreDTO> products) {
        this.storeId = storeId;
        this.buyingPolicy = buyingPolicy;
        this.discountPolicy = discountPolicy;
        this.products = products;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getBuyingPolicy() {
        return buyingPolicy;
    }

    public void setBuyingPolicy(String buyingPolicy) {
        this.buyingPolicy = buyingPolicy;
    }

    public String getDiscountPolicy() {
        return discountPolicy;
    }

    public void setDiscountPolicy(String discountPolicy) {
        discountPolicy = discountPolicy;
    }

    public List<ProductInStoreDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductInStoreDTO> products) {
        this.products = products;
    }

    public String toString() {
        String info =
                "Store ID: " + storeId +
                        "\nBuying policy: " + buyingPolicy +
                        "\nDiscount policy: " + discountPolicy+
                        "\nProducts:\n\n";

        for (ProductInStoreDTO product: products) {
            info += product.toString() + "\n";
        }

        return info;
    }
}
