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
}
