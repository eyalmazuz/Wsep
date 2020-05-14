package DTOs.SimpleDTOS;

public class ProductAmountDTO {
    private ProductInfoDTO productInfo;
    private int amount;

    public ProductAmountDTO(ProductInfoDTO productInfo, int amount) {
        this.productInfo = productInfo;
        this.amount = amount;
    }

    public ProductInfoDTO getProductInfo() {
        return productInfo;
    }

    public void setProductInfo(ProductInfoDTO productInfo) {
        this.productInfo = productInfo;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
