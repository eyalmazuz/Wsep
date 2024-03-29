package Service;

public interface Bridge {

    boolean setupSystem(String supplyConfig, String paymentConfig,String path);

    boolean login(int sessionId, String username, String password);

    int register(int sessionId, String username, String password);

    String getAllInfo(int sessionId);

    String viewCart(int sessionId);

    String searchProducts(int sessionId, String productName, String category, String[] keywords, int productRating, int storeRating, int priceFrom, int priceTo);

    boolean addToCart(int sessionId, int storeId, int productId, Integer amount);

    boolean updateAmount(int sessionId, int storeId, int productId, int amount);

    boolean deleteItemInCart(int sessionId, int storeId, int productId);

    boolean clearCart(int sessionId);

    boolean buyCart(int sessionId, String cardNumber, String cardMonth, String cardYear, String cardHolder,
                    String cardCcv, String cardId, String buyerName, String address, String city, String country, String zip);

    boolean logout(int sessionId);

    int openStore(int sessionId);

    String viewPurchaseHistory(int sessionId);

    String searchUserHistory(int sessionId, int userId);

    boolean addProductToStore(boolean flag, int sessionId, int productId, int storeId, int amount);

    boolean editProduct(boolean flag, int sessionId, int storeId, int productId, String productInfo);

    boolean deleteProduct(boolean flag, int sessionId, int storeId, int productId);

    boolean appointManager(int sessionId, int storeId, int userId);

    boolean appointOwner(int sessionId, int storeId, int userId);

    boolean removeManager(int sessionId, int storeId, int userId);

    boolean editManagerOptions(int sessionId, int storeId, int userId, String option);

    String viewShopHistory(int sessionId, int storeId);

    String getStoreHistory(int sessionId, int storeId);

    int startSession();

    void addProductInfo(int sessionId, int id, String name, String category, double basePrice);

    boolean changeBuyingPolicy(int sessionId, boolean flag, int storeId, String newPolicy);

    boolean removeOwner(int sessionId, int storeId, int userId);
}
