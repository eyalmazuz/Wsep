package Service;

import Domain.TradingSystem.Pair;
import Domain.TradingSystem.System;

public class DomainController {
    private Domain.TradingSystem.System system;

    public DomainController(){

        system = new System();
    }

    public int openStore(int sessionId) {
        return system.openStroe(sessionId);
    }

    public String getPurchaseHistory(int sessionId) {
        return system.getHistory(sessionId);
    }

    public boolean logout(int sessionId) {
        return system.logout(sessionId);
    }

    public boolean addProduct(int sessionId, int productId, int storeId, int amount) {
        return system.addProductToStore(sessionId, storeId,productId,amount);
    }

    public boolean deleteProduct(int sessionId, int storeId, int productId) {
        return system.deleteProductFromStore(sessionId, storeId,productId);
    }

    public boolean editManagerOptions(int sessionId, int storeId, int userId,  String option) {
        return system.setManagerDetalis(sessionId, userId,storeId,option);
    }

    public int register(int sessionId, String username, String password) {
        return system.register(sessionId, username, password);
    }

    public boolean login(int sessionId, String username, String password) {
        return system.login(sessionId, username, password);
    }

    public boolean setup(String supplyConfig, String paymentConfig) {
        system.setup(supplyConfig, paymentConfig);
        return true;
    }

    public String getAllInfo() {

        return system.viewStoreProductInfo();
    }

    public String searchProducts(int sessionId, int id, String category, String keyword, Pair<Integer, Integer> priceRange, Integer productRating, Integer storeRating) {
        return system.searchProducts(sessionId,"", category, new String[]{keyword}, priceRange, productRating, storeRating);
    }

    public boolean addToCart(int sessionId, int storeId, int productId, Integer amount) {
        return system.addToCart(sessionId, storeId, productId, amount);
    }

    public boolean updateAmount(int sessionId, int storeId, int productId, int amount) {
        return system.updateAmount(sessionId, storeId, productId, amount);
    }

    public boolean deleteItemInCart(int sessionId, int storeId, int productId) {
        return system.deleteItemInCart(sessionId, storeId, productId);
    }

    public boolean clearCart(int sessionId) {
        return system.clearCart(sessionId);
    }

    public boolean buyCart(int sessionId) {

        return system.buyCart(sessionId);
    }

    public boolean appointManager(int sessionId, int storeId, int userId) {
        return system.addStoreManager(sessionId, storeId, userId);
    }

    public boolean appointOwner(int sessionId, int storeId, int userId) {
        return system.addStoreOwner(sessionId, storeId, userId);
    }

    public boolean removeManager(int sessionId, int storeId, int userId) {
        return system.deleteManager(sessionId, storeId, userId);
    }

    public String viewCart(int sessionId) {
        return system.getCart(sessionId);
    }

    public boolean editProduct(int sessionId, int storeId, int productId, String productInfo) {
        return system.editProductInStore(sessionId, storeId, productId, productInfo);
    }

    public String viewShopHistory(int sessionId, int storeId) {
        return system.getStoreHistoryAsAdmin(sessionId, storeId);
    }

    public String viewUserHistory(int sessionId, int userId) {
        //TODO FIX THIS
        return system.getUserHistory(sessionId, userId);
    }

    public String getStoryHistory(int sessionId, int storeId) {
        return system.getStoreHistory(sessionId, storeId);
    }

    public int startSession() {
        return system.startSession();
    }
}
