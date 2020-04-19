package Service;

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
}
