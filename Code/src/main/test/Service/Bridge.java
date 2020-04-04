package Service;

import AcceptanceTest.Data.History;
import AcceptanceTest.Data.Product;
import AcceptanceTest.Data.ShoppingCart;
import AcceptanceTest.Data.Store;

import java.util.ArrayList;
import java.util.List;

public interface Bridge {

    boolean login (String username , String password);

    boolean register(String username, String password);

    ArrayList<Store> getAllInfo();

    ShoppingCart viewCart();

    List<Product> searchProducts(String name, String category, String keyword, String filterOptions);

    boolean addToCart(String productName, Integer amount);

    boolean updateAmount(int amount);

    boolean deleteItemInCart(String productName);

    boolean clearCart();

    boolean buyCart(String user, String cart);

    boolean logout();

    boolean openStore();

    List<History> viewPurchaseHistory();

    List<History> searchUserHistory(String username);

    List<History> searchStoreHistory(String StoreName);

    boolean addProduct(int id, int amount);

    boolean editProduct(int id, int amount);

    boolean deleteProduct(int id);

    boolean appointManager(String username);

    boolean appointOwner(String username);

    boolean removeManager(int id);

    boolean editManagerOptions(int id, int option);

    List<History> viewShopHistory();

    boolean updateItemDiscount(int itemID, int discount);
}
