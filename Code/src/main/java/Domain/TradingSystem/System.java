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
    }

    public void addStore (){
        Store store = new Store();
        stores.add(store);
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

    // UseCase 4.3
    public List <User> getAvailableUsersToOwn(int storeId) throws Exception {
        //TODO:Add logger call
        List <User> owners = new LinkedList<User>(); //update store owners list
        if (currentUser.getState().hasOwnerPermission()){
            for (Store store: stores) {
                if (store.getId()==storeId)
                    owners = store.getOwners();
            }
            if (owners == null)
                return null;
            List <Subscriber> ownersSubs = new LinkedList<Subscriber>();
            for (User owner: owners){
                ownersSubs.add((Subscriber) owner.getState());
            }
            userHandler.getAvailableUsersToOwn(ownersSubs); // return only available subs
        }
        else
            throw new Exception("No permission");
    }

    public boolean addStoreOwner (int storeId, User newOwner) {
        //TODO:Add logger call
        for (Store store : stores) {
            if (store.getId() == storeId) {
                store.addOwner(newOwner, currentUser);
                return true;
            }
        }
        return false;
    }
}
