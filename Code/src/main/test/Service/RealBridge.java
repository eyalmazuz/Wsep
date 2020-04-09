package Service;

import AcceptanceTest.Data.*;

import java.util.ArrayList;
import java.util.List;

public class RealBridge implements Bridge {

    public boolean login(String username, String password) {
        return false;
    }

    public boolean register(String username, String password) {
        return false;
    }

    public ArrayList<Store> getAllInfo() {
        return null;
    }

    public List<Product> searchProducts(String name, String category, String keyword, FilterOption filterOptions) {
        return null;
    }

    public boolean addToCart(int productId, Integer amount) {
        return false;
    }

    public boolean updateAmount(int productId, int amount) {
        return false;
    }

    public boolean deleteItemInCart(int productId) {
        return false;
    }

    public boolean clearCart() {
        return false;
    }

    public boolean buyCart(String user, ShoppingCart cart) {
        return false;
    }

    public ShoppingCart viewCart(){
        return null;
    }

    public boolean logout(){ return false; }

    public boolean openStore() { return false;}

    public List<History> viewPurchaseHistory(){ return null; }

    public List<History> searchUserHistory(String username){ return null;}

    public List<History> searchStoreHistory(String storeName){ return null;}

    public boolean addProduct(int id, int amount) { return false ;}

    public boolean editProduct(int id, int amount) { return false ;}

    public boolean deleteProduct(int id) { return false ;}

    public boolean appointManager(int storeId, String username) { return false ;}

    public boolean appointOwner(int storeId, String username) { return false ;}

    public boolean removeManager(int storeId, String username) { return false ;}

    public boolean editManagerOptions(int storeId, int userId,int adminId, String option){ return false;}

    public boolean updateItemDiscount(int storeId, int itemID, int discount){ return false;}

    public List<History> viewShopHistory(){ return null; }
}
