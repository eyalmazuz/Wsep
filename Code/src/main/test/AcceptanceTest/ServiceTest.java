package AcceptanceTest;
import AcceptanceTest.Data.*;
import Service.Bridge;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

public abstract class ServiceTest extends TestCase {

    Bridge bridge;

    public void setUp(){
        this.bridge = Driver.getBridge();
        this.setUpUsers();
        this.setUpStores();
    }

    private void setUpStores() {
        Store store = new Store();
        store.owner = new User("hanamaru", "123456");
        store.items.add(new Product(1, 200, "Phone", 5 ));
        store.items.add(new Product(2, 100, "Phone",5));
        Database.Stores.add(store);
        store = new Store();
        store.owner = new User("chika", "12345");
        store.items.add(new Product(1, 200, "Phone", 5 ));
        store.items.add(new Product(3, 50, "Food",10));
        Database.Stores.add(store);
        Database.hcart.items.put(1, 5);
    }

    private void setUpUsers() {
        for(String[] userData : Database.Users){
            register(userData[0], userData[1]);
        }
    }

    private void clearDatabase(){

    }

    public boolean login (String username , String password){
        return this.bridge.login(username, password);
    }

    public boolean register(String username, String password){
        return this.bridge.register(username, password);
    }

    public boolean addToCart(int productId, Integer amount){
        return this.bridge.addToCart(productId, amount);
    }

    public boolean updateAmount(int productId, int amount){
        return bridge.updateAmount(productId, amount);
    }

    public boolean deleteItemInCart(int productId){
        return bridge.deleteItemInCart(productId);
    }

    public boolean clearCart(){
        return bridge.clearCart();
    }

    public boolean buyCart(String user, ShoppingCart cart){
        return bridge.buyCart(user, cart);
    }

    public boolean logout(){ return bridge.logout(); }

    public boolean openStore(){ return bridge.openStore(); }

    public boolean addProdcut(int id, int amount) { return bridge.addProduct(id, amount); }

    public boolean editProduct(int id, int amount) { return bridge.editProduct(id, amount); }

    public boolean deleteProduct(int id) { return bridge.deleteProduct(id); }

    public boolean appointManager(int storeId, String username) { return bridge.appointManager(storeId, username); }

    public boolean appointOwner(int storeId, String username) { return bridge.appointOwner(storeId, username); }

    public boolean removeManager(int storeId, String username) { return bridge.removeManager(storeId, username); }

    public boolean editManagerOptions(int storeId, int userId,int adminId, String option){ return bridge.editManagerOptions(storeId, userId, adminId, option); }

    public boolean updateItemDiscount(int storeId, int itemID, int discount){ return bridge.updateItemDiscount(storeId, itemID, discount); }

    public List<Product> searchProducts(String name, String category, String keyword, FilterOption option){
        return this.bridge.searchProducts(name, category, keyword, option); }


    public ShoppingCart viewCart(){
        return this.bridge.viewCart();
    }

    public ArrayList<Store> getAllInfo(){
        return this.bridge.getAllInfo();
    }

    public List<History> viewPurchaseHistory(){
        return bridge.viewPurchaseHistory();
    }

    public List<History> searchUserHistory(String username) { return this.bridge.searchUserHistory(username);}

    public List<History> searchStoreHistory(String storeName) { return this.bridge.searchStoreHistory(storeName);}

    public List<History> viewShopHistory(){ return bridge.viewShopHistory(); }


}
