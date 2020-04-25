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
            return s.register(username, password);
        }
        return -1;
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


}
