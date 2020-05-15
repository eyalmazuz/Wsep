package Service;

import DTOs.*;
import Domain.TradingSystem.System;


public class GuestUserHandler {

    System s = System.getInstance();

    public ActionResultDTO login(int sessionId , String username, String password) {
        //check if guest - userHandler
        if (s.isGuest(sessionId)){
            int subId = s.getSubscriber(username, password);
            if(subId != -1){
                s.setState(sessionId, subId);
                return new ActionResultDTO(ResultCode.SUCCESS, "Login successful.");
            }
            return new ActionResultDTO(ResultCode.ERROR_LOGIN, "No such username.");
        }
        return new ActionResultDTO(ResultCode.ERROR_LOGIN, "User already logged in.");
    }

    public IntActionResultDto register(int sessionId, String username, String password) {
        if (s.isGuest(sessionId)) {
            return s.register(sessionId,username, password);
        }
         return new IntActionResultDto(ResultCode.ERROR_REGISTER,"Only Guest can register",-1);
    }

    // 2.6, 2.7

    public ActionResultDTO addProductToCart(int sessionId, int storeId, int productId, int amount) {
        return s.addToCart(sessionId, storeId, productId, amount);
    }

    public ActionResultDTO editProductInCart(int sessionId, int storeId, int productId, int amount) {
        return s.updateAmount(sessionId, storeId, productId, amount);
    }

    public ActionResultDTO removeProductInCart(int sessionId, int storeId, int productId) {
        return s.deleteItemInCart(sessionId, storeId, productId);
    }

    public ActionResultDTO clearCart(int sessionId) {
        return s.clearCart(sessionId);
    }

    // 2.8.1, 2.8.2
    public ActionResultDTO requestPurchase(int sessionId) {
        if (s.isCartEmpty(sessionId)) return new ActionResultDTO(ResultCode.ERROR_PURCHASE, "Cart is empty.");
        ActionResultDTO result = s.checkBuyingPolicy(sessionId);
        if (result.getResultCode() != ResultCode.SUCCESS) return result;
        double price = s.checkSuppliesAndGetPrice(sessionId).getPrice();
        if (price < 0) return new ActionResultDTO(ResultCode.ERROR_PURCHASE, "Missing supplies.");
        // pass price to user confirmation
        return new ActionResultDTO(ResultCode.SUCCESS, "Request approved. Please confirm the price: " + price);
    }

    // 2.8.3, 2.8.4
    public ActionResultDTO confirmPurchase(int sessionId, String paymentDetails) {
        ActionResultDTO result = s.makePayment(sessionId, paymentDetails);
        if (result.getResultCode() != ResultCode.SUCCESS) return result;
        s.savePurchaseHistory(sessionId);
        s.saveOngoingPurchaseForUser(sessionId);

        if (s.updateStoreSupplies(sessionId)) {

            s.emptyCart(sessionId);
        }
        else {
            s.requestRefund(sessionId);
            s.restoreHistories(sessionId);
            s.removeOngoingPurchase(sessionId);
            return new ActionResultDTO(ResultCode.ERROR_PURCHASE, "Could not make purchase due to a sync problem.");
        }

        if (!s.requestSupply(sessionId)) {
            s.requestRefund(sessionId);
            s.restoreSupplies(sessionId);
            s.restoreHistories(sessionId);
            s.restoreCart(sessionId);
            s.removeOngoingPurchase(sessionId);
            return new ActionResultDTO(ResultCode.ERROR_PURCHASE, "Supply system could not deliver products. State restored.");
        }

        s.removeOngoingPurchase(sessionId);

        return new ActionResultDTO(ResultCode.SUCCESS, "Purchase successful.");
    }


    public ProductsActionResultDTO searchProducts(int sessionId, String productName, String categoryName, String[] keywords, int minItemRating, int minStoreRating) {
        return s.searchProducts(sessionId, productName, categoryName, keywords, minItemRating, minStoreRating);
    }

    public StoreActionResultDTO viewStoreProductInfo() {
        return s.viewStoreProductInfo();
    }


    public ShoppingCartDTO viewCart(int sessionId) {
        return s.getCart(sessionId);
    }
/*
    public ShoppingCartDTO getDemoCart() {
        Map<String,Integer > demo  = new HashMap<>();
        demo.put("Banana",5);
        demo.put("Carrot",3);
        ShoppingBasketDTO basketDemo = new ShoppingBasketDTO(5,demo);
        ShoppingBasketDTO basketDemo2 = new ShoppingBasketDTO(6,demo);
        List<ShoppingBasketDTO> lst = new LinkedList<>();
        lst.add(basketDemo);
        lst.add(basketDemo2);
        return new ShoppingCartDTO(ResultCode.SUCCESS,"test",lst);
        }
*/
}

