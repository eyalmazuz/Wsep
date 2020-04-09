package Domain.TradingSystem;

import java.util.LinkedList;
import java.util.List;

public class System {

    private static System instance = new System();

    private System(){
        userHandler = new UserHandler();
    }

    public static System getInstance(){
        return instance;
    }

    private User currentUser;
    private SupplyHandler supplyHandler;
    private PaymentHandler paymentHandler;
    private UserHandler userHandler;
    private List<Store> stores = new LinkedList<Store>();

    //Usecase 1.1
    private void setSupply(String config){
        supplyHandler = new SupplyHandler(config);
    }

    private void setPayment(String config){
       paymentHandler = new PaymentHandler(config);
    }

    public void setup(String supplyConfig,String paymentConfig){
        //TODO:Add logger call
        userHandler.setAdmin();
        setSupply(supplyConfig);
        setPayment(paymentConfig);
        //TODO:Add Error handling.
        currentUser = new User();
    }

    public void addStore (){
        Store store = new Store();
        stores.add(store);
    }

    //Usecase 3.1
    public boolean logout(){
        return currentUser.logout();
    }

    //Usecase 3.2

    public boolean openStroe(){
        Store newStore  = currentUser.openStore();
        if (newStore != null){
            stores.add(newStore);
        }
        return true;
    }


    //Usecase 3.7
    public String getHistory(){
        return currentUser.getHistory();
    }


    // UseCase 4.1.1
    public boolean addProductToStore(int storeId, int productId,int ammount){
        //TODO:Add logger call
        if(currentUser!=null) {
            return currentUser.addProductToStore(storeId, productId, ammount);
        }
        return false;
    }

    //UseCase 4.1.2
    public boolean editProductInStore(int storeId, int productId,String newInfo){
        //TODO:Add logger call
        if(currentUser!=null) {
            return currentUser.editProductInStore(storeId, productId, newInfo);
        }
        return false;
    }

    //UseCase 4.1.3
    public boolean deleteProductFromStore(int storeId, int productId){
        //TODO:Add logger call
        if(currentUser!=null) {
            return currentUser.deleteProductFromStore(storeId, productId);
        }
        return false;
    }

    //UseCase 4.3

    // UseCase 4.3

    /**
     *
     * @param storeId
     * @return - list of available user id's
     *           if there is no premission returns null.
     */
    public List <Integer> getAvailableUsersToOwn(int storeId) {
        //TODO:Add logger call
        List <Subscriber> owners = new LinkedList<Subscriber>(); //update store owners list
        if (currentUser.getState().hasOwnerPermission(storeId)){
            for (Store store: stores) {
                if (store.getId()==storeId)
                    owners = store.getOwners();
            }

            return userHandler.getAvailableUsersToOwn(owners); // return only available subs
        }
        else
            return null;
    }

    public boolean addStoreOwner (int storeId, int userId) {
        //TODO:Add logger call
        Subscriber newOwner = userHandler.getUser(userId);
        for (Store store : stores) {
            if (store.getId() == storeId) {
                return currentUser.addOwner(store,newOwner);

            }
        }
        return false;
    }
}
