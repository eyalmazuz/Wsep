package Service;

public interface Bridge {

    boolean setupSystem(String supplyConfig, String paymentConfig);

    boolean login (String username , String password);

    int register(String username, String password);

    String[][] getAllInfo();

    String[][] viewCart();

    String[][] searchProducts(int id, String category, String keyword, Integer productRating, Integer storeRating, Integer priceFrom, Integer priceTo);

    void addToCart(int storeId, int productId, Integer amount);

    void updateAmount(int storeId, int productId, int amount);

    void deleteItemInCart(int storeId, int productId);

    void clearCart();

    void buyCart(String[][] cart);

    boolean logout();

    int openStore();

    String[][] viewPurchaseHistory();

    String[][] searchUserHistory(String username);

    String[][] searchStoreHistory(int storeId);

    boolean addProduct(int productId, int storeId, int amount);

    boolean editProduct(int storeId, int productId, String productInfo);

    boolean deleteProduct(int storeId, int productId);

    boolean appointManager(int storeId, int userId);

    boolean appointOwner(int storeId, int userId);

    boolean removeManager(int storeId, int userId);

    boolean editManagerOptions(int storeId, int userId, String option);

    String[][] viewShopHistory(int storeId);

    boolean updateItemDiscount(int storeId, int itemID, int discount);
}
