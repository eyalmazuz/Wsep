package Domain.TradingSystem;

import DTOs.ActionResultDTO;
import DTOs.DoubleActionResultDTO;
import DTOs.ResultCode;
import DataAccess.DAOManager;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.HashMap;
import java.util.Map;

@DatabaseTable(tableName = "shoppingBaskets")
public class ShoppingBasket {

    @DatabaseField (generatedId = true)
    private int id;

    @DatabaseField (foreign = true)
    private ShoppingCart cart;

    @DatabaseField (foreign = true)
    private Store store;

    @DatabaseField (dataType = DataType.SERIALIZABLE)
    private HashMap<ProductInfo, Integer> products = new HashMap<>();

    public ShoppingBasket() {}

    public ShoppingBasket(ShoppingCart cart, Store store) {
        this.cart = cart;
        this.store = store;
    }

    public ShoppingBasket(Store store) {
        this.store = store;
    }

    // usecases 2.6, 2.7

    public void addProduct(ProductInfo product, int amount, boolean updateDatabase) {
        products.put(product, products.getOrDefault(product, 0) + amount);
        if (updateDatabase) DAOManager.updateShoppingBasket(this);
    }

    public Store getStore() {
        return store;
    }

    public int getStoreId() {
        return store.getId();
    }

    public ActionResultDTO editProduct(ProductInfo product, int newAmount, boolean updateDatabase) {
        if (!products.containsKey(product)) return new ActionResultDTO(ResultCode.ERROR_CART_MODIFICATION, "Basket does not have this product.");
        products.put(product, newAmount);
        if (updateDatabase) DAOManager.updateShoppingBasket(this);;
        return new ActionResultDTO(ResultCode.SUCCESS, null);
    }

    public ActionResultDTO removeProduct(ProductInfo product, boolean updateDatabase) {
        if (!products.containsKey(product)) return new ActionResultDTO(ResultCode.ERROR_CART_MODIFICATION, "Basket does not have this product.");
        products.remove(product);
        if (updateDatabase) DAOManager.updateShoppingBasket(this);
        return new ActionResultDTO(ResultCode.SUCCESS, null);
    }

    // usecase 2.8.1
    public ActionResultDTO checkBuyingPolicy(User user, ShoppingBasket basket) {
        return store.checkPurchaseValidity(user, basket);
    }

    public DoubleActionResultDTO getTotalPrice(User user) {
        return store.getPrice(user, products);
    }

    public PurchaseDetails savePurchase(User user) {
        return store.savePurchase(user, products);
    }

    public void cancelPurchase(PurchaseDetails purchaseDetails) {
        store.cancelPurchase(purchaseDetails);
    }

    public Map<ProductInfo, Integer> getProducts() {
        return products;
    }

    public void merge(ShoppingBasket otherBasket) {
        for (ProductInfo product : otherBasket.products.keySet()) {
            int amount = otherBasket.getProducts().get(product);
            products.put(product, products.getOrDefault(product, 0) + amount);
        }
    }

    @Override
    public String toString() {
        String output = "";
        for (ProductInfo product : products.keySet()) {
            int amount = products.get(product);
            output += "Product ID: " + product.getId() + ", amount: " + amount + "\n";
        }
        return output;
    }

    public boolean checkStoreSupplies() {
        for (ProductInfo product : products.keySet()) {
            if (store.getProductAmount(product.getId()) < products.get(product)) return false;
        }
        return true;
    }

    public boolean updateStoreSupplies() {
        for (ProductInfo product : products.keySet()) {
            if (! store.setProductAmount(product.getId(), store.getProductAmount(product.getId()) - products.get(product)))
                return false;
        }
        return true;
    }
    public void restoreStoreSupplies() {
        for (ProductInfo product : products.keySet()) {
            store.setProductAmount(product.getId(), store.getProductAmount(product.getId()) + products.get(product));
        }
    }


    public double getProductPrice(int productId) {
        return store.getProductPrice(productId);
    }

    public void setProducts(HashMap<ProductInfo, Integer> products) {
        this.products = products;
    }
}
