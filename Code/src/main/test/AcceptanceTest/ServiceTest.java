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
        Database.sessionId = startSession();
        this.setUpUsers();
    }

    private void setUpUsers() {
        for(String[] userData : Database.Users){
            int userId = register(Database.sessionId, userData[0], userData[1]);
            Database.userToId.put(userData[0], userId);
        }



    }

    private void clearDatabase(){

    }

    public boolean login (int sessionId, String username , String password){
        return this.bridge.login(sessionId, username, password);
    }

    public int register(int sessionId, String username, String password){
        return this.bridge.register(sessionId, username, password);
    }

    public boolean addToCart(int sessionId, int storeId, int productId, Integer amount){
        return this.bridge.addToCart(sessionId, storeId, productId, amount);
    }

    public boolean updateAmount(int sessionId, int storeId, int productId, int amount){
        return bridge.updateAmount(sessionId, storeId, productId, amount);
    }

    public boolean deleteItemInCart(int sessionId, int storeId, int productId){
        return bridge.deleteItemInCart(sessionId, storeId, productId);
    }

    public boolean clearCart(int sessionId){
        return bridge.clearCart(sessionId);
    }

    public boolean buyCart(int sessionId){
        return bridge.buyCart(sessionId);
    }

    public boolean logout(int sessionId){ return bridge.logout(sessionId); }

    public int openStore(int sessionId){ return bridge.openStore(sessionId); }

    public boolean addProdcut(int sessionId, int productId, int storeId, int amount) { return bridge.addProduct(sessionId, productId, storeId, amount); }

    public boolean editProduct(int sessionId, int storeId, int productId, String productInfo) { return bridge.editProduct(sessionId, storeId, productId, productInfo); }

    public boolean deleteProduct(int sessionId, int storeId, int productId) { return bridge.deleteProduct(sessionId, storeId, productId); }

    public boolean appointManager(int sessionId, int storeId, int userId) { return bridge.appointManager(sessionId, storeId, userId); }

    public boolean appointOwner(int sessionId, int storeId, int userId) { return bridge.appointOwner(sessionId, storeId, userId); }

    public boolean removeManager(int sessionId, int storeId, int userId) { return bridge.removeManager(sessionId, storeId, userId); }

    public boolean editManagerOptions(int sessionId, int storeId, int userId, String option){ return bridge.editManagerOptions(sessionId, storeId, userId, option); }

    public String searchProducts(int sessionId, int id, String category, String keyword, int productRating, int storeRating, int priceFrom, int priceTo){
        return this.bridge.searchProducts(sessionId, id, category, keyword, productRating, storeRating, priceFrom, priceTo); }


    public String viewCart(int sessionId){
        return this.bridge.viewCart(sessionId);
    }

    public String getAllInfo(int sessionId){
        return this.bridge.getAllInfo(sessionId);
    }

    public String viewPurchaseHistory(int sessionId){
        return bridge.viewPurchaseHistory(sessionId);
    }

    public String searchUserHistory(int sessionId, int userId) { return this.bridge.searchUserHistory(sessionId, userId);}

    public String searchStoreHistory(int sessionId, int storeId) { return this.bridge.searchStoreHistory(sessionId, storeId);}

    public String getStoreHistory(int sessionId, int storeId) { return this.bridge.getStoreHistory(sessionId, storeId); }

    public String viewShopHistory(int sessionId, int storeId){ return bridge.viewShopHistory(sessionId, storeId); }

    public boolean setupSystem(String suppyConfig, String paymentConfig) { return bridge.setupSystem(suppyConfig, paymentConfig); }

    public int startSession() { return this.bridge.startSession(); }
}
