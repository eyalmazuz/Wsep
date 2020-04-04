package Service;

public interface Bridge {

    boolean login (String username , String password);

    boolean register(String username, String password);

    String getAllInfo();

    String viewCart();

    String searchProducts(String name, String category, String keyword, String filterOptions);

    boolean addToCart(String productName, Integer amount);

    boolean updateAmount(int amount);

    boolean deleteItemInCart(String productName);

    boolean clearCart();

    boolean buyCart(String user, String cart);

    boolean logout();

    boolean openStore();

    String viewPurchaseHistory();

    String searchUserHistory(String username);

    String searchStoreHistory(String StoreName);

    boolean addProduct(int id, int amount);

    boolean editProduct(int id, int amount);

    boolean deleteProduct(int id);

    boolean appointManager(String username);

    boolean appointOwner(String username);

    boolean removeManager(int id);

    boolean editManagerOptions(int id, int option);

    String viewShopHistory();

    boolean updateItemDiscount(int itemID, int discount);
}
