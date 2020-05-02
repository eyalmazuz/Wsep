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

    // 2.6, 2.7

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

    public boolean setPaymentDetails(int sessionId, String paymentDetails) {
        return s.setPaymentDetails(sessionId, paymentDetails);
    }

    // 2.8.1, 2.8.2
    public boolean requestPurchase(int sessionId) {
        if (s.isCartEmpty(sessionId)) return false;
        if (!s.checkBuyingPolicy(sessionId)) return false;
        double price = s.checkSuppliesAndGetPrice(sessionId);
        if (price < 0) return false;
        // pass price to user confirmation
        return true;
    }

    // 2.8.3, 2.8.4
    public boolean confirmPurchase(int sessionId, String paymentDetails) {
        if (!s.makePayment(sessionId, paymentDetails)) return false;
        s.savePurchaseHistory(sessionId);
        s.saveOngoingPurchaseForUser(sessionId);
        if (s.updateStoreSupplies(sessionId))
            s.emptyCart(sessionId);
        else{
            s.requestRefund(sessionId);
            s.restoreHistories(sessionId);
            s.removeOngoingPurchase(sessionId);
            return false;
        }

        if (!s.requestSupply(sessionId)) {
            s.requestRefund(sessionId);
            s.restoreSupplies(sessionId);
            s.restoreHistories(sessionId);
            s.restoreCart(sessionId);
            s.removeOngoingPurchase(sessionId);
            return false;
        }

        s.removeOngoingPurchase(sessionId);

        return true;
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
