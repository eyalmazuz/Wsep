package Domain.TradingSystem;

import Domain.TradingSystem.UserState;

public class Subscriber implements UserState {

    public boolean addProductToStore(Store store, int productId, int ammount) {
        store.addProduct(productId,ammount);
        return true;
    }

}
