package Service;

import Domain.TradingSystem.Pair;
import Domain.TradingSystem.System;

public class DomainController {
    private Domain.TradingSystem.System system;

    public DomainController(){

        system = new System();
    }

    public int openStore() {
        return system.openStore();
    }

    public String getPurchaseHistory() {
        return system.getHistory();
    }

    public boolean logout() {
        return system.logout();
    }

    public boolean addProduct(int productId, int storeId, int amount) {
        return system.addProductToStore(storeId,productId,amount);
    }

    public boolean deleteProduct(int storeId, int productId) {
        return system.deleteProductFromStore(storeId,productId);
    }

    public boolean editManagerOptions(int storeId, int userId,  String option) {
        return system.setManagerDetalis(userId,storeId,option);
    }

    public int register(String username, String password) {
        return system.register(username, password);
    }

    public boolean login(String username, String password) {
        return system.login(username, password);
    }

    public boolean setup(String supplyConfig, String paymentConfig) {
        system.setup(supplyConfig, paymentConfig);
        return true;
    }

    public String getAllInfo() {

        return system.viewStoreProductInfo();
    }

    public String searchProducts(int id, String category, String keyword, Pair<Integer, Integer> priceRange, Integer productRating, Integer storeRating) {
        return system.searchProducts("", category, new String[]{keyword}, priceRange, productRating, storeRating);
    }

    public boolean addToCart(int storeId, int productId, Integer amount) {
        return system.addToCart(storeId, productId, amount);
    }

    public boolean updateAmount(int storeId, int productId, int amount) {
        return system.updateAmount(storeId, productId, amount);
    }

    public boolean deleteItemInCart(int storeId, int productId) {
        return system.deleteItemInCart(storeId, productId);
    }

    public boolean clearCart() {
        return system.clearCart();
    }

    public boolean buyCart() {

        return system.buyCart();
    }

    public boolean appointManager(int storeId, int userId) {
        return system.addStoreManager(storeId, userId);
    }

    public boolean appointOwner(int storeId, int userId) {
        return system.addStoreOwner(storeId, userId);
    }

    public boolean removeManager(int storeId, int userId) {
        return system.deleteManager(storeId, userId);
    }

    public String viewCart() {
        return system.getCart();
    }

    public boolean editProduct(int storeId, int productId, String productInfo) {
        return system.editProductInStore(storeId, productId, productInfo);
    }

    public String viewShopHistory(int storeId) {
        return system.getStoreHistoryAsAdmin(storeId);
    }

    public String viewUserHistory(int userId) {
        //TODO FIX THIS
        return system.getUserHistory(userId);
    }

    public String getStoryHistory(int storeId) {
        return system.getStoreHistory(storeId);
    }
}
