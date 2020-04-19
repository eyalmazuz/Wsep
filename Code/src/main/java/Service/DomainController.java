package Service;

import Domain.TradingSystem.Pair;
import Domain.TradingSystem.System;

public class DomainController {
    private Domain.TradingSystem.System system;

    public DomainController(){
        system = System.getInstance();
    }

    public int openStore() {
        return system.openStroe();
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

    public String[][] getAllInfo() {
        //TODO FIX THIS CHANGE FROM STRING[][] TO STRING
        String info = system.viewStoreProductInfo();
        return null;
    }

    public String[][] searchProducts(int id, String category, String keyword, Pair<Integer, Integer> priceRange, Integer productRating, Integer storeRating) {
        //TODO FIX THIS PRODUCT NAME and FROM STRING[][] to STRING
        String products = system.searchProducts("", category, new String[]{keyword}, priceRange, productRating, storeRating);
        return null;
    }

    public boolean addToCart(int productId, Integer amount) {
        //TODO NO ADD TO CART FIX LATER
        return false;
    }

    public boolean updateAmount(int productId, int amount) {
        //TODO NO UPDATE AMOUNT
        return false;
    }

    public boolean deleteItemInCart(int productId) {
        //TODO NO DELETE
        return false;
    }

    public boolean clearCart() {
        //TODO NO CLEAR
        return false;
    }

    public boolean buyCart() {
        //TODO NO BUY
        return false;
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
}
