package DTOs.SimpleDTOS;

import Domain.Util.Pair;

import java.util.Map;

public class ShoppingBasketDTO {
    private int storeId;
    private Map<Pair<Integer,String>,Integer> productsAndAmounts;

    public ShoppingBasketDTO(int storeId, Map<Pair<Integer, String>, Integer> productsAndAmounts) {

        this.storeId = storeId;
        this.productsAndAmounts = productsAndAmounts;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public Map<Pair<Integer, String>, Integer> getProductsAndAmounts() {
        return productsAndAmounts;
    }

    public void setProductsAndAmounts(Map<Pair<Integer, String>, Integer> productsAndAmounts) {
        this.productsAndAmounts = productsAndAmounts;
    }

    @Override
    public String toString() {
        String output = "";
        for (Pair<Integer,String> productId : productsAndAmounts.keySet()) {
            int amount = productsAndAmounts.get(productId);
            output = "Product Name: " + productId.getSecond() + ", amount: " + amount + "\n"+output;
        }
        return output;
    }



}
