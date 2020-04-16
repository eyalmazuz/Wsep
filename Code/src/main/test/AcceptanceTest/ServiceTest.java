package AcceptanceTest;
import AcceptanceTest.Data.*;
import Service.Bridge;
import junit.framework.TestCase;


public abstract class ServiceTest extends TestCase {

    Bridge bridge;

    public void setUp(){
        this.bridge = Driver.getBridge();
        this.setUpUsers();
    }

    //TODO FIX THIS SHIT
    private void setUpUsers() {
//        for(String[] userData : Database.Users){
//            register(userData[0], userData[1]);
//        }
//        login("chika", "12345");
//        int sid_1 = openStore();
//        addProdcut(1, sid_1, 5);
//        addProdcut(2, sid_1, 5);
//        appointManager(sid_1, "dia");
//        appointOwner(sid_1, "kanan");
//        logout();
//
//        login("dia", "12345");
//        appointManager(sid_1, "ruby");
//        logout();
//
//        login("hanamaru", "12345");
//        int sid_2 = openStore();
//        addProdcut(2, sid_2, 10);

    }

    private void clearDatabase(){

    }

    public boolean login (String username , String password){
        return this.bridge.login(username, password);
    }

    public int register(String username, String password){
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

    public boolean buyCart(String[][] cart){
        return bridge.buyCart(cart);
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

    public boolean updateItemDiscount(int storeId, int itemID, int discount){ return bridge.updateItemDiscount(storeId, itemID, discount); }

    public String[][] searchProducts(int id, String category, String keyword, Integer productRating, Integer storeRating, Integer priceFrom, Integer priceTo){
        return this.bridge.searchProducts(id, category, keyword, productRating, storeRating, priceFrom, priceTo); }


    public String[][] viewCart(){
        return this.bridge.viewCart();
    }

    public String[][] getAllInfo(){
        return this.bridge.getAllInfo();
    }

    public String[][] viewPurchaseHistory(){
        return bridge.viewPurchaseHistory();
    }

    public String[][] searchUserHistory(String username) { return this.bridge.searchUserHistory(username);}

    public String[][] searchStoreHistory(int storeId) { return this.bridge.searchStoreHistory(storeId);}

    public String[][] viewShopHistory(int storeId){ return bridge.viewShopHistory(storeId); }

    public boolean setupSystem(String suppyConfig, String paymentConfig) { return bridge.setupSystem(suppyConfig, paymentConfig); }


}
