package Service;

import Domain.TradingSystem.System;
import Domain.TradingSystem.UserHandler;

public class GuestUserHandler {
    System s = System.getInstance();

    public boolean login(int sessionId , String username, String password){
        //check if guest - userHandler
        if(username == null || password == null || username.equals("") || password.equals("")) return false;
        if(s.isGuest(sessionId)){
            int subId = s.getSubscriber(username,password);
            if(subId != -1){
                s.setState(sessionId,subId);
                s.mergeCartWithSubscriber(sessionId);
                return true;
            }
            return false;
        }
        return false;

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
