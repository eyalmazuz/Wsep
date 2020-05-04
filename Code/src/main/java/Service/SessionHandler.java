package Service;


import Domain.TradingSystem.Store;
import Domain.TradingSystem.System;

public class SessionHandler {

    private System system = System.getInstance();

    public SessionHandler(){

    }

    public boolean setup(String supplyConfig, String paymentConfig) {
        return system.setup(supplyConfig, paymentConfig);
    }

    public int startSession() {
        return system.startSession();
    }
}
