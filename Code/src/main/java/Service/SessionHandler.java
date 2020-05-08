package Service;


import DTOs.ActionResultDTO;
import DTOs.IntActionResultDto;
import Domain.TradingSystem.System;

public class SessionHandler {

    private System system = System.getInstance();

    public SessionHandler(){

    }

    public ActionResultDTO setup(String supplyConfig, String paymentConfig) {
        return system.setup(supplyConfig, paymentConfig);
    }

    public IntActionResultDto startSession() {
        return system.startSession();
    }
}
