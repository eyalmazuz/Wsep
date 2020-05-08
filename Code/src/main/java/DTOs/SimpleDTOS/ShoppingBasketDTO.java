package DTOs.SimpleDTOS;

import java.util.Map;

public class ShoppingBasketDTO {
    private int storeId;
    private Map<String,Integer> productsAndAmounts;

    public ShoppingBasketDTO( int storeId, Map<String, Integer> productsAndAmounts) {

        this.storeId = storeId;
        this.productsAndAmounts = productsAndAmounts;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public Map<String, Integer> getProductsAndAmounts() {
        return productsAndAmounts;
    }

    public void setProductsAndAmounts(Map<String, Integer> productsAndAmounts) {
        this.productsAndAmounts = productsAndAmounts;
    }


        @Override
    public String toString() {
        String output = "";
        for (String productId : productsAndAmounts.keySet()) {
            int amount = productsAndAmounts.get(productId);
            output += "Product Name: " + productId + ", amount: " + amount + "\n";
        }
        return output;
    }



}
