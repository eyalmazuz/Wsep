package Domain.TradingSystem;

public class System {

    private User currentUser;

    private boolean addProductToStore(int storeId, int productId,int ammount){
       return currentUser.addProductToStore(storeId,productId,ammount);
    }
}
