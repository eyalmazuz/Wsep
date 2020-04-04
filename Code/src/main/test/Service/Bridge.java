package Service;

public interface Bridge {

    boolean login (String username , String password);

    boolean register(String username, String password);

    String getAllInfo();

    String searchProducts(String name, String category, String keyword, String filterOptions);

    boolean addToCart(String productName, Integer amount);
}
