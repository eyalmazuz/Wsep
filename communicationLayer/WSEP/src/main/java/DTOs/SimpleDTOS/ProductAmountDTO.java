package DTOs.SimpleDTOS;

public class ProductAmountDTO {
    private ProductInfoDTO productInfo;
    private int amount;
    private double price;

    public ProductAmountDTO(ProductInfoDTO productInfo, int amount, double price) {
        this.productInfo = productInfo;
        this.amount = amount;
        this.price = price;
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

    public double getPrice(){ return price;}

    public void setPrice(double price){ this.price = price;}
}
