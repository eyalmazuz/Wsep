package AcceptanceTest;
import Service.Bridge;
import junit.framework.TestCase;

public abstract class ServiceTest extends TestCase {

    Bridge bridge;

    public void setUp(){
        this.bridge = Driver.getBridge();
        this.setUpUsers();

    }

    private void setUpUsers() {
        this.bridge.register("bob", "1234");
        this.bridge.register("danny", "password");
    }

    public boolean login (String username , String password){
        return this.bridge.login(username, password);
    }

    public boolean register(String username, String password){
        return this.bridge.register(username, password);
    }

    public String searchProducts(String name, String category, String keyword, String filterOptions){
        return this.bridge.searchProducts(name, category, keyword, filterOptions);
    }

    public boolean addToCart(String productName, Integer amount){
        return this.bridge.addToCart(productName, amount);
    }

    public String viewCart(){
        return this.bridge.viewCart();
    }

    public String getAllInfo(){
        return this.bridge.getAllInfo();
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

    public String viewPurchaseHistory(){
        return bridge.viewPurchaseHistory();
    }

    public String searchUserHistory(String username) { return this.bridge.searchUserHistory(username);}

    public String searchStoreHistory(String storeName) { return this.bridge.searchStoreHistory(storeName);}

    public boolean addProdcut(int id, int amount) { return bridge.addProduct(id, amount); }

    public boolean editProduct(int id, int amount) { return bridge.editProduct(id, amount); }

    public boolean deleteProduct(int id) { return bridge.deleteProduct(id); }

    public boolean appointManager(String username) { return bridge.appointManager(username); }

    public boolean appointOwner(String username) { return bridge.appointOwner(username); }

    public boolean removeManager(int id) { return bridge.removeManager(id); }

    public boolean editManagerOptions(int id, int option){ return bridge.editManagerOptions(id, option); }

    public String viewShopHistory(){ return bridge.viewShopHistory(); }

    public boolean updateItemDiscount(int itemID, int discount){ return bridge.updateItemDiscount(itemID, discount); }

}
