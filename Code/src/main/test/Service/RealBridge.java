package Service;

public class RealBridge implements Bridge {

    public boolean login(String username, String password) {
        return false;
    }

    public boolean register(String username, String password) {
        return false;
    }

    public String getAllInfo() {
        return null;
    }

    public String searchProducts(String name, String category, String keyword, String filterOptions) {
        return null;
    }

    public boolean addToCart(String productName, Integer amount) {
        return false;
    }

    public boolean updateAmount(int amount) {
        return false;
    }

    public boolean deleteItemInCart(String productName) {
        return false;
    }

    public boolean clearCart() {
        return false;
    }

    public boolean buyCart(String user, String cart) {
        return false;
    }

    public String viewCart(){
        return null;
    }

    public boolean logout(){ return false; }

    public boolean openStore() { return false;}

    public String viewPurchaseHistory(){ return null; }

    public String searchUserHistory(String username){ return null;}

    public String searchStoreHistory(String storeName){ return null;}

    public boolean addProduct(int id, int amount) { return false ;}

    public boolean editProduct(int id, int amount) { return false ;}

    public boolean deleteProduct(int id) { return false ;}

    public boolean appointManager(String username) { return false ;}

    public boolean appointOwner(String username) { return false ;}

    public boolean removeManager(int id) { return false ;}

    public boolean editManagerOptions(int id, int option){ return false;}

    public boolean updateItemDiscount(int itemID, int discount){ return false;}

    public String viewShopHistory(){ return null; }
}
