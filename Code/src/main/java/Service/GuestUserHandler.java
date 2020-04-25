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
            return s.register(sessionId,username, password);
        }
        return -1;
    }

    public boolean addProductToCart(int sessionId, int storeId, int productId, int amount) {
        return s.addToCart(sessionId, storeId, productId, amount);
    }

    public boolean editProductInCart(int sessionId, int storeId, int productId, int amount) {
        return s.updateAmount(sessionId, storeId, productId, amount);
    }

    public boolean removeProductInCart(int sessionId, int storeId, int productId) {
        return s.deleteItemInCart(sessionId, storeId, productId);
    }

    public boolean clearCart(int sessionId) {
        return s.clearCart(sessionId);
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

    public String searchProducts(int sessionId, String productName, String categoryName, String[] keywords, int minItemRating, int minStoreRating) {
        return s.searchProducts(sessionId, productName, categoryName, keywords, minItemRating, minStoreRating);
    }

    public String viewStoreProductInfo() {
        return s.viewStoreProductInfo();
    }


    public String viewCart(int sessionId) {
        return s.getCart(sessionId);
    }
}
