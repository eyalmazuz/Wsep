package Domain.TradingSystem;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class System {

    private static System instance = null;

    private User currentUser;
    private SupplyHandler supplyHandler;
    private PaymentHandler paymentHandler;
    private UserHandler userHandler;

    private List<Store> stores = new ArrayList<>();
    private Map<Integer, String> productNames = new HashMap<>();
    private Map<Integer, String> productCategories = new HashMap<>();
    private Map<Integer, Integer> productRatings = new HashMap<>();

    private System(){
        userHandler = new UserHandler();
    }

    public static System getInstance(){
        if (instance == null) instance = new System();
        return instance;
    }

    //Usecase 1.1
    private void setSupply(String config){
        supplyHandler = new SupplyHandler(config);
    }

    private void setPayment(String config){
       paymentHandler = new PaymentHandler(config);
    }


    public void setup(String supplyConfig,String paymentConfig){
        userHandler.setAdmin();
        setSupply(supplyConfig);
        setPayment(paymentConfig);
        //TODO:Add Error handling.
    }

    public boolean addProductToStore(int storeId, int productId,int ammount){
        //TODO:Add logger call
        if(currentUser!=null) {
            return currentUser.addProductToStore(storeId, productId, ammount);
        }
        return false;
    }

    public boolean makePayment(User user, Map<Integer, Map<Integer, Integer>> storeProductsIds) {
        return paymentHandler.makePayment(user, storeProductsIds);
    }

    public void cancelPayment(User user, Map<Integer, Map<Integer, Integer>> storeProductsIds) {
        paymentHandler.cancelPayment(user, storeProductsIds);
    }

    public boolean requestSupply(User user, Map<Integer, Map<Integer, Integer>> storeProductsIds) {
        // check whether the stores have enough of the given products
        for (Integer storeId : storeProductsIds.keySet()) {
            for (Store store : stores) {
                if (store.getId() == storeId) {
                    Map<Integer, Integer> productAmounts = storeProductsIds.get(storeId);
                    for (Integer productId : productAmounts.keySet()) {
                        if (store.getProductAmount(productId) < productAmounts.get(productId)) {
                            return false;
                        }
                    }

                    break;
                }
            }
        }

        return supplyHandler.requestSupply(user, storeProductsIds);
    }

    public int register(String username, String password) {
        if (!currentUser.isGuest()) return -1;
        return userHandler.register(username, password);
    }

    public boolean login(String username, String password) {
        if (!currentUser.isGuest()) return false;

        User userToLogin = userHandler.getSubscriberUser(username, password);

        if (userToLogin != null) {
            ShoppingCart subscriberCart = userToLogin.getShoppingCart();
            subscriberCart.merge(currentUser.getShoppingCart());
            currentUser = userToLogin;
            return true;
        }

        return false;
    }

    public String viewStoreProductInfo() {
        String info = "";
        for (Store store: stores) {
            info += store.toString() + "\n--------------------------\n";
        }

        return info;
    }

    public String searchProducts(String productName, String categoryName, String[] keywords, Pair<Integer, Integer> priceRange, int minItemRating, int minStoreRating) {
        List<ProductInStore> allProducts = new ArrayList<>();
        List<ProductInStore> filteredProducts = new ArrayList<>();


        for (Store store: stores)
            if (store.getRating() >= minStoreRating) allProducts.addAll(store.getProducts());

        for (ProductInStore pis: allProducts) {
            if (productName != null)
                if (productNames.get(pis.getId()).equals(productName)) {
                    filteredProducts.add(pis);
                    continue;
                }
            if (categoryName != null)
                if (productCategories.get(pis.getId()).equals(productName)) {
                    filteredProducts.add(pis);
                    continue;
                }
            if (priceRange != null) {
                double price = pis.getPrice(currentUser);
                if (price >= priceRange.getKey() && price <= priceRange.getValue()) {
                    filteredProducts.add(pis);
                    continue;
                }
            }
            if (minItemRating != -1) {
                if (productRatings.get(pis.getId()) >= minItemRating) {
                    filteredProducts.add(pis);
                }
            }
        }

        String results = "Results:\n\n";
        for (ProductInStore pis: filteredProducts) {
            String productInfo = pis.toString();
            for (String keyword: keywords) {
                if (productInfo.contains(keyword)) {
                    results += productInfo + "\n---------------------------------\n";
                    break;
                }
            }
        }


        return results;
    }

}
