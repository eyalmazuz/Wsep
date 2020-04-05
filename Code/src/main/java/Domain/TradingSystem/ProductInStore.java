package Domain.TradingSystem;

public class ProductInStore {

    private ProductInfo productInfo;
    private int amount;
    private String info;

    public ProductInStore(int productId, int amount) {

     this.productInfo = new ProductInfo(productId);
     this.amount = amount;
    }

    public int getId() {
        return productInfo.getId();
    }

    public void addAmount(int amount) {
        this.amount += amount;
    }

    public String getInfo (){
        return this.info;
    }
    public void editInfo(String newInfo){
        this.info=info;
    }
}
