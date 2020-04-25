package Service;

import Domain.TradingSystem.*;
import Domain.TradingSystem.System;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuestUserHandler {
    System s = System.getInstance();

    public boolean login(int sessionId , String username, String password) {
        //check if guest - userHandler
        if (s.isGuest(sessionId)){
            int subId = s.getSubscriber(username, password);
            if(subId != -1){
                s.setState(sessionId, subId);
                s.mergeCartWithSubscriber(sessionId);
                return true;
            }
            return false;
        }
        return false;
    }

    public int register(int sessionId, String username, String password) {
        if (s.isGuest(sessionId)) {
            return s.register(username, password);
        }
        return -1;
    }

    public boolean purchaseCart(int sessionId) {
        double totalPrice = s.buyCart(sessionId);
        if (totalPrice > -1) {
            if (s.confirmPurchase(sessionId, totalPrice)) {
                return s.requestConfirmedPurchase(sessionId);
            }
        }
        return false;
    }

    public String viewStoreProductInfo() {
        return s.viewStoreProductInfo();
    }

    public String searchProducts(int sessionId, String productName, String categoryName, String[] keywords, Pair<Integer, Integer> priceRange, int minItemRating, int minStoreRating) {
        User u = s.getUser(sessionId);
        List<ProductInStore> allProducts = new ArrayList<>();
        List<ProductInStore> filteredProducts = new ArrayList<>();

        List<Store> stores = s.getStores();
        Map<Integer, String> productNames = s.getProductNames();
        Map<Integer, String> productCategories = s.getProductCategories();
        Map<Integer, Integer> productRatings = s.getProductRatings();


        // only consider the stores above the minStoreRating
        for (Store store: stores)
            if (store.getRating() >= minStoreRating) allProducts.addAll(store.getProducts());

        for (ProductInStore pis: allProducts) {
            if (productName != null)
                if (productNames.get(pis.getId()).equals(productName)) {
                    filteredProducts.add(pis);
                    continue;
                }
            if (categoryName != null)
                if (productCategories.get(pis.getId()).equals(categoryName)) {
                    filteredProducts.add(pis);
                    continue;
                }
            if (priceRange != null) {
                double price = pis.getPrice(u);
                if (price >= priceRange.getFirst() && price <= priceRange.getSecond()) {
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
            if (keywords != null) {
                for (String keyword : keywords) {
                    if (productInfo.contains(keyword)) {
                        results += productInfo + "\n---------------------------------\n";
                        break;
                    }
                }
            }
            else results += productInfo + "\n---------------------------------\n";
        }

        return results;


    }
}
