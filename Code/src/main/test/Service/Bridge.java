package Service;

public interface Bridge {

    boolean setupSystem(String supplyConfig, String paymentConfig);

    boolean login (String username , String password);

    int register(String username, String password);

    String getAllInfo();

    String viewCart();

    String searchProducts(int id, String category, String keyword, int productRating, int storeRating, int priceFrom, int priceTo);

    boolean addToCart(int storeId, int productId, Integer amount);

    boolean updateAmount(int storeId, int productId, int amount);

    boolean deleteItemInCart(int storeId, int productId);

    boolean clearCart();

    boolean buyCart();

    boolean logout();

    int openStore();

    String viewPurchaseHistory();

    String searchUserHistory(int userId);

    String searchStoreHistory(int storeId);

    boolean addProduct(int productId, int storeId, int amount);

    boolean editProduct(int storeId, int productId, String productInfo);

    boolean deleteProduct(int storeId, int productId);

    boolean appointManager(int storeId, int userId);

    boolean appointOwner(int storeId, int userId);

    boolean removeManager(int storeId, int userId);

    boolean editManagerOptions(int storeId, int userId, String option);

    String viewShopHistory(int storeId);

    String getStoreHistory(int storeId);


}
