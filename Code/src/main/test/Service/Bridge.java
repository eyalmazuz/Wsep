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
}
