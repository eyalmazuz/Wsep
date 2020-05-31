package AcceptanceTest;

import AcceptanceTest.Data.Database;
import DataAccess.DAOManager;
import Service.Bridge;
import junit.framework.TestCase;


public abstract class ServiceTest extends TestCase {

    Bridge bridge;


    public void setUp(){
        this.bridge = Driver.getBridge();

        Database.sessionId = startSession();

        this.setupSystem("Mock Config", "Mock Config","");
        DAOManager.clearDatabase();
        if(Driver.flag) {

            this.setUpUsers();
            login(Database.sessionId, "admin", "admin");
            addProductInfo(Database.sessionId, 1, "UO", "KB", 10);
            addProductInfo(Database.sessionId, 2, "Famichiki", "Food", 10);
            logout(Database.sessionId);
            Driver.flag = false;
        }

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

    public boolean buyCart(int sessionId, String paymentDetails){
        return bridge.buyCart(sessionId, paymentDetails);
    }

    public boolean logout(int sessionId){ return bridge.logout(sessionId); }

    public int openStore(int sessionId){ return bridge.openStore(sessionId); }

    public boolean addProdcut(boolean flag, int sessionId, int productId, int storeId, int amount) { return bridge.addProduct(flag, sessionId, productId, storeId, amount); }

    public boolean editProduct(boolean flag, int sessionId, int storeId, int productId, String productInfo) { return bridge.editProduct(flag, sessionId, storeId, productId, productInfo); }

    public boolean deleteProduct(boolean flag, int sessionId, int storeId, int productId) { return bridge.deleteProduct(flag, sessionId, storeId, productId); }

    public boolean appointManager(int sessionId, int storeId, int userId) { return bridge.appointManager(sessionId, storeId, userId); }

    public boolean appointOwner(int sessionId, int storeId, int userId) { return bridge.appointOwner(sessionId, storeId, userId); }

    public boolean removeManager(int sessionId, int storeId, int userId) { return bridge.removeManager(sessionId, storeId, userId); }

    public boolean removeOwner(int sessionId, int storeId, int userId) { return bridge.removeOwner(sessionId, storeId, userId); }

    public boolean editManagerOptions(int sessionId, int storeId, int userId, String option){ return bridge.editManagerOptions(sessionId, storeId, userId, option); }

    public String searchProducts(int sessionId, String productName, String category, String[] keywords, int productRating, int storeRating, int priceFrom, int priceTo){
        return this.bridge.searchProducts(sessionId, productName, category, keywords, productRating, storeRating, priceFrom, priceTo); }


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

    public String getStoreHistory(int sessionId, int storeId) { return this.bridge.getStoreHistory(sessionId, storeId); }

    public String viewShopHistory(int sessionId, int storeId){ return bridge.viewShopHistory(sessionId, storeId); }

    public boolean setupSystem(String suppyConfig, String paymentConfig,String path) { return bridge.setupSystem(suppyConfig, paymentConfig,path); }

    public int startSession() { return this.bridge.startSession(); }

    public void addProductInfo(int sessionId, int id, String name, String category, double basePrice) { this.bridge.addProductInfo(sessionId, id, name, category, basePrice);}

    public boolean changeBuyingPolicy(int sessionId, boolean flag, int storeId, String newPolicy) { return this.bridge.changeBuyingPolicy(sessionId, flag, storeId, newPolicy); }
}
