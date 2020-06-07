package Domain.TradingSystem;

import DataAccess.DAOManager;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "productsInStores")
public class ProductInStore {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = false, foreign = true)
    private ProductInfo productInfo;

    @DatabaseField
    private int amount;

    @DatabaseField
    private String info;

    @DatabaseField(foreign = true)
    private Store store;

    @DatabaseField
    private double price;

    public ProductInStore() {}

    public ProductInStore(ProductInfo productInfo, int amount, Store store) {
        this.productInfo = productInfo;
        this.amount = amount;
        this.store = store;
        this.price = productInfo.getDefaultPrice();
    }

    public int getProductInfoId() {
        return productInfo.getId();
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
        DAOManager.updateProductInStore(this);
    }

    public void addAmount(int amount) {
        this.amount += amount;
        DAOManager.updateProductInStore(this);
    }

    public String toString() {
        return "Store ID: " + store.getId() + ", product ID: " + productInfo.getId() + ", amount: " + amount + ", info: " + info;
    }

    public String getInfo (){
        return this.info;
    }
    public void editInfo(String newInfo){
        this.info=newInfo;
        DAOManager.updateProductInStore(this);
    }

    public ProductInfo getProductInfo() {
        return productInfo;
    }

    public Store getStore() {
        return store;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
        DAOManager.updateProductInStore(this);
    }
}
