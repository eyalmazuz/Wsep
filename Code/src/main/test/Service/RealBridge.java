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
}
