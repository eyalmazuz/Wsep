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
        Database.Stores.add(new Store());
        Database.Stores.add(new Store());
        Database.Stores.add(new Store());
        Database.Stores.add(new Store());
    }

    private void setUpUsers() {
        Database.Users.add(new User("Danny", "123456"));
        Database.Users.add(new User("Chika", "chika"));
        Database.Users.add(new User("Ruby", "password"));
        Database.Users.add(new User("Kanan", "654321"));
    }

    private void clearDatabase(){
        Database.Users.clear();
        Database.Stores.clear();

    }

    public boolean login (String username , String password){
        return this.bridge.login(username, password);
    }

    public boolean register(String username, String password){
        return this.bridge.register(username, password);
    }

    public boolean addToCart(String productName, Integer amount){
        return this.bridge.addToCart(productName, amount);
    }

    public boolean updateAmount(int amount){
        return bridge.updateAmount(amount);
    }

    public boolean deleteItemInCart(String productName){
        return bridge.deleteItemInCart(productName);
    }

    public boolean clearCart(){
        return bridge.clearCart();
    }

    public boolean buyCart(String user, String cart){
        return bridge.buyCart(user, cart);
    }

    public boolean logout(){ return bridge.logout(); }

    public boolean openStore(){ return bridge.openStore(); }

    public boolean addProdcut(int id, int amount) { return bridge.addProduct(id, amount); }

    public boolean editProduct(int id, int amount) { return bridge.editProduct(id, amount); }

    public boolean deleteProduct(int id) { return bridge.deleteProduct(id); }

    public boolean appointManager(String username) { return bridge.appointManager(username); }

    public boolean appointOwner(String username) { return bridge.appointOwner(username); }

    public boolean removeManager(int id) { return bridge.removeManager(id); }

    public boolean editManagerOptions(int id, int option){ return bridge.editManagerOptions(id, option); }

    public boolean updateItemDiscount(int itemID, int discount){ return bridge.updateItemDiscount(itemID, discount); }

    public List<Product> searchProducts(String name, String category, String keyword, String filterOptions){
        return this.bridge.searchProducts(name, category, keyword, filterOptions); }


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
