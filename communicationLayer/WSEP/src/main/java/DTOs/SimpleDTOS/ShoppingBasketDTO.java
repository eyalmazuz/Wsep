package DTOs.SimpleDTOS;



import java.util.List;


public class ShoppingBasketDTO {
    private int storeId;
    private List<SimpProductAmountDTO> productsAndAmounts;

    public ShoppingBasketDTO(int storeId, List<SimpProductAmountDTO> productsAndAmounts) {

        this.storeId = storeId;
        this.productsAndAmounts = productsAndAmounts;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public List<SimpProductAmountDTO> getProductsAndAmounts() {
        return productsAndAmounts;
    }

    public void setProductsAndAmounts(List<SimpProductAmountDTO> productsAndAmounts) {
        this.productsAndAmounts = productsAndAmounts;
    }

    @Override
    public String toString() {
        String output = "";
        for (SimpProductAmountDTO productId : productsAndAmounts) {
            int amount = productId.getAmount();
            output = "Product Name: " + productId.getName() + ", amount: " + amount + "\n"+output;
        }
        return output;
    }



}
