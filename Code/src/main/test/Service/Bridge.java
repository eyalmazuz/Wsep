package Service;

import AcceptanceTest.Data.*;

import java.util.ArrayList;
import java.util.List;

public interface Bridge {

    boolean login (String username , String password);

    boolean register(String username, String password);

    ArrayList<Store> getAllInfo();

    ShoppingCart viewCart();

    List<Product> searchProducts(String name, String category, String keyword, FilterOption filterOptions);

    boolean addToCart(int productId, Integer amount);

    boolean updateAmount(int productId, int amount);

    boolean deleteItemInCart(int productId);

    boolean clearCart();

    boolean buyCart(String user, ShoppingCart cart);

    boolean logout();

    boolean openStore();

    List<History> viewPurchaseHistory();

    List<History> searchUserHistory(String username);

    List<History> searchStoreHistory(String StoreName);

    boolean addProduct(int id, int amount);

    boolean editProduct(int id, int amount);

    boolean deleteProduct(int id);

    boolean appointManager(int storeId, String username);

    boolean appointOwner(int storeId, String username);

    boolean removeManager(int storeId, String username);

    boolean editManagerOptions(int storeId, int userId,int adminId, String option);

    List<History> viewShopHistory();

    boolean updateItemDiscount(int storeId, int itemID, int discount);
}
