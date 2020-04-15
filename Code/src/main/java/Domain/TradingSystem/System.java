package Domain.TradingSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class System {

    private static System instance = null;

    private User currentUser;
    private SupplyHandler supplyHandler;
    private PaymentHandler paymentHandler;
    private UserHandler userHandler;

    private List<Store> stores = new ArrayList<>();

    private System(){
        userHandler = new UserHandler();
    }

    public static System getInstance(){
        if (instance == null) instance = new System();
        return instance;
    }

    //Usecase 1.1
    private void setSupply(String config){
        supplyHandler = new SupplyHandler(config);
    }

    private void setPayment(String config){
       paymentHandler = new PaymentHandler(config);
    }


    public void setup(String supplyConfig,String paymentConfig){
        userHandler.setAdmin();
        setSupply(supplyConfig);
        setPayment(paymentConfig);
        //TODO:Add Error handling.
    }

    public boolean addProductToStore(int storeId, int productId,int ammount){
        //TODO:Add logger call
        if(currentUser!=null) {
            return currentUser.addProductToStore(storeId, productId, ammount);
        }
        return false;
    }

    public boolean makePayment(User user, Map<Integer, Map<Integer, Integer>> storeProductsIds) {
        return paymentHandler.makePayment(user, storeProductsIds);
    }

    public void cancelPayment(User user, Map<Integer, Map<Integer, Integer>> storeProductsIds) {
        paymentHandler.cancelPayment(user, storeProductsIds);
    }

    public boolean requestSupply(User user, Map<Integer, Map<Integer, Integer>> storeProductsIds) {
        // check whether the stores have enough of the given products
        for (Integer storeId : storeProductsIds.keySet()) {
            for (Store store : stores) {
                if (store.getId() == storeId) {
                    Map<Integer, Integer> productAmounts = storeProductsIds.get(storeId);
                    for (Integer productId : productAmounts.keySet()) {
                        if (store.getProductAmount(productId) < productAmounts.get(productId)) {
                            return false;
                        }
                    }

                    break;
                }
            }
        }

        return supplyHandler.requestSupply(user, storeProductsIds);
    }

    public boolean register(String username, String password) {
        if (!currentUser.isGuest()) return false;
        return userHandler.register(username, password);
    }

}
