package AcceptanceTest;
import AcceptanceTest.Data.*;
import Domain.TradingSystem.System;
import Service.Bridge;
import junit.framework.TestCase;


public abstract class ServiceTest extends TestCase {

    Bridge bridge;

    public void setUp(){
        this.bridge = Driver.getBridge();
        this.setupSystem("Mock Config", "Mock Config");
        this.setUpUsers();
    }

    private void setUpUsers() {
        for(String[] userData : Database.Users){
            int userId = register(userData[0], userData[1]);
            Database.userToId.put(userData[0], userId);
        }
        login("chika", "12345");
        int sid_1 = openStore();
        Database.userToStore.put("chika", sid_1);
        addProdcut(1, sid_1, 5);
        addProdcut(2, sid_1, 5);
        appointManager(sid_1, Database.userToId.get("dia"));
        appointOwner(sid_1, Database.userToId.get("kanan"));
        logout();

        login("dia", "12345");
        appointManager(sid_1, Database.userToId.get("ruby"));
        logout();

        login("hanamaru", "12345");
        int sid_2 = openStore();
        Database.userToStore.put("hanamaru", sid_2);
        addProdcut(2, sid_2, 10);
        logout();

        Database.Stores = "Store ID: " + String.valueOf(Database.userToStore.get("chika")) + "\n" +
                "Buying policy: \n" +
                "Discount policy: \n" +
                "Products:\n" +
                "\n" +
                "Product ID: 1, amount: 5\n" +
                "Product ID: 2, amount: 5\n" +
                "\n" +
                "--------------------------\n" +
                "Store ID: " + String.valueOf(Database.userToStore.get("hanamaru")) + "\n" +
                "Buying policy: \n" +
                "Discount policy: \n" +
                "Products:\n" +
                "\n" +
                "Product ID: 2, amount: 10\n" +
                "\n" +
                "--------------------------\n";
    }

    private void clearDatabase(){

    }

    public boolean login (String username , String password){
        return this.bridge.login(username, password);
    }

    public int register(String username, String password){
        return this.bridge.register(username, password);
    }

    public boolean addToCart(int storeId, int productId, Integer amount){
        return this.bridge.addToCart(storeId, productId, amount);
    }

    public boolean updateAmount(int storeId, int productId, int amount){
        return bridge.updateAmount(storeId, productId, amount);
    }

    public boolean deleteItemInCart(int storeId, int productId){
        return bridge.deleteItemInCart(storeId, productId);
    }

    public boolean clearCart(){
        return bridge.clearCart();
    }

    public boolean buyCart(){
        return bridge.buyCart();
    }

    public boolean logout(){ return bridge.logout(); }

    public int openStore(){ return bridge.openStore(); }

    public boolean addProdcut(int productId, int storeId, int amount) { return bridge.addProduct(productId, storeId, amount); }

    public boolean editProduct(int storeId, int productId, String productInfo) { return bridge.editProduct(storeId, productId, productInfo); }

    public boolean deleteProduct(int storeId, int productId) { return bridge.deleteProduct(storeId, productId); }

    public boolean appointManager(int storeId, int userId) { return bridge.appointManager(storeId, userId); }

    public boolean appointOwner(int storeId, int userId) { return bridge.appointOwner(storeId, userId); }

    public boolean removeManager(int storeId, int userId) { return bridge.removeManager(storeId, userId); }

    public boolean editManagerOptions(int storeId, int userId, String option){ return bridge.editManagerOptions(storeId, userId, option); }

    public String searchProducts(int id, String category, String keyword, int productRating, int storeRating, int priceFrom, int priceTo){
        return this.bridge.searchProducts(id, category, keyword, productRating, storeRating, priceFrom, priceTo); }


    public String viewCart(){
        return this.bridge.viewCart();
    }

    public String getAllInfo(){
        return this.bridge.getAllInfo();
    }

    public String viewPurchaseHistory(){
        return bridge.viewPurchaseHistory();
    }

    public String searchUserHistory(int userId) { return this.bridge.searchUserHistory(userId);}

    public String searchStoreHistory(int storeId) { return this.bridge.searchStoreHistory(storeId);}

    public String getStoreHistory(int storeId) { return this.bridge.getStoreHistory(storeId); }

    public String viewShopHistory(int storeId){ return bridge.viewShopHistory(storeId); }

    public boolean setupSystem(String suppyConfig, String paymentConfig) { return bridge.setupSystem(suppyConfig, paymentConfig); }


}
