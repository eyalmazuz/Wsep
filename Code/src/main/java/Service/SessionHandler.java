package Service;


import Domain.TradingSystem.Store;
import Domain.TradingSystem.System;

public class SessionHandler {
    private Domain.TradingSystem.System system;

    public SessionHandler(){

        system = new System();
    }

    public boolean setup(String supplyConfig, String paymentConfig) {
        return system.setup(supplyConfig, paymentConfig);
    }

    public int startSession() {
        return system.startSession();
    }
}
