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
    /**
     * open new store
     * @return the new store id
     */
    public int openStroe(){
        Store newStore  = currentUser.openStore();
        if (newStore != null){
            stores.add(newStore);
            return newStore.getId();
      }
        return -1;
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
        if (newOwner == null)
            return false;
        for (Store store : stores) {
            if (store.getId() == storeId) {
                return currentUser.addOwner(store,newOwner);

            }
        }
        return false;
    }


    /**
     *  Use Case 4.5
     * @param storeId
     * @return - list of available user id's
     *           if there is no premission returns null.
     */
    public List <Integer> getAvailableUsersToManage(int storeId) {
        //TODO:Add logger call
        List <Subscriber> managers = new LinkedList<Subscriber>(); //update store owners list
        if (currentUser.getState().hasOwnerPermission(storeId)){
            for (Store store: stores) {
                if (store.getId()==storeId)
                    managers = store.getManagers();
            }

            return userHandler.getAvailableUsersToOwn(managers); // return only available subs
        }
        else
            return null;
    }

    public boolean addStoreManager (int storeId, int userId) {
        //TODO:Add logger call
        Subscriber newManager = userHandler.getUser(userId);
        if (newManager == null)
            return false;
        for (Store store : stores) {
            if (store.getId() == storeId) {
                return currentUser.addManager(store,newManager);

            }
        }
        return false;
    }

    /**
     * Delete manager that was granted by the current user.
     * @param storeId
     * @param userId
     * @return
     */
    public boolean deleteManager (int storeId, int userId) {
        //TODO:Add logger call
        Subscriber managerToDelete = userHandler.getUser(userId);
        if (managerToDelete == null)
            return false;
        for (Store store : stores) {
            if (store.getId() == storeId) {
                return currentUser.deleteManager(store,managerToDelete);
            }
        }
        return false;
    }

    /**
     * Use Case 4.6.1
     * get all managers that were granted by the current user.
     * @param storeId
     * @return list of users ID's. If there is no permission, returns null.
     * If there are'nt users - return empty list.
     */
    public List <Integer> getManagersOfCurUser(int storeId){
        //TODO:Add logger call
        if (currentUser.getState().hasOwnerPermission(storeId)){
            return userHandler.getManagersOfCurUser(storeId,
                    ((Subscriber)currentUser.getState()).getId());
        }
        else
            return null;

}

    /**
     *
     * @param managerId
     * @param storeId
     * @return
     */
    public String getManagerDetails(int managerId, int storeId){
        if (currentUser.getState().hasOwnerPermission(storeId)) {
            return userHandler.getManagerDetails(managerId, storeId);
        }
        return null;
    }

    /**
     * set manager details :)
     * @param managerId
     * @param storeId
     * @param details
     * @return
     */
    public boolean setManagerDetalis(int managerId, int storeId, String details){
        Subscriber manager = userHandler.getUser(managerId);
        if (manager == null)
            return false;
        for (Store store : stores) {
            if (store.getId() == storeId) {
                return currentUser.getState().editPermission(manager, store, details);
            }
        }
        return false;
    }

    public String getStoreHistory(int storeId){
        return currentUser.getStoreHistory(storeId);
    }













}
