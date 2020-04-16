package Service;

public interface Bridge {

    boolean setupSystem(String supplyConfig, String paymentConfig);

    boolean login (String username , String password);

    boolean register(String username, String password);

    String[][] getAllInfo();

    String[][] viewCart();

    String[][] searchProducts(int id, String category, String keyword, Integer productRating, Integer storeRating, Integer priceFrom, Integer priceTo);

    boolean addToCart(int productId, Integer amount);

    boolean updateAmount(int productId, int amount);

    boolean deleteItemInCart(int productId);

    boolean clearCart();

    boolean buyCart(String[][] cart);

    boolean logout();

    boolean openStore();

    String[][] viewPurchaseHistory();

    String[][] searchUserHistory(String username);

    String[][] searchStoreHistory(int storeId);

    boolean addProduct(int productId, int storeId, int amount);

    boolean editProduct(int storeId, int productId, String productInfo);

    boolean deleteProduct(int storeId, int productId);

    boolean appointManager(int storeId, String username);

    boolean appointOwner(int storeId, String username);

    boolean removeManager(int storeId, String username);

    boolean editManagerOptions(int storeId, int userId,int adminId, String option);

    String[][] viewShopHistory(int storeId);

    boolean updateItemDiscount(int storeId, int itemID, int discount);
}
