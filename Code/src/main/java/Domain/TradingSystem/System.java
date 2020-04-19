package Domain.TradingSystem;

import java.util.LinkedList;
import java.util.List;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class System {

    private static System instance = null;

    private User currentUser;
    private SupplyHandler supplyHandler;
    private PaymentHandler paymentHandler;
    private UserHandler userHandler;
    private List<Store> stores = new LinkedList<Store>();

        private Map<Integer, String> productNames = new HashMap<>();
    private Map<Integer, String> productCategories = new HashMap<>();
    private Map<Integer, Integer> productRatings = new HashMap<>();

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

    public int register(String username, String password) {
        if (!currentUser.isGuest()) return -1;
        return userHandler.register(username, password);
    }

    public boolean login(String username, String password) {
        if (!currentUser.isGuest()) return false;

        User userToLogin = userHandler.getSubscriberUser(username, password);

        if (userToLogin != null) {
            ShoppingCart subscriberCart = userToLogin.getShoppingCart();
            subscriberCart.merge(currentUser.getShoppingCart());
            currentUser = userToLogin;
            return true;
        }

        return false;
    }

    public String viewStoreProductInfo() {
        String info = "";
        for (Store store: stores) {
            info += store.toString() + "\n--------------------------\n";
        }

        return info;
    }

    public String searchProducts(String productName, String categoryName, String[] keywords, Pair<Integer, Integer> priceRange, int minItemRating, int minStoreRating) {
        List<ProductInStore> allProducts = new ArrayList<>();
        List<ProductInStore> filteredProducts = new ArrayList<>();


        for (Store store: stores)
            if (store.getRating() >= minStoreRating) allProducts.addAll(store.getProducts());

        for (ProductInStore pis: allProducts) {
            if (productName != null)
                if (productNames.get(pis.getId()).equals(productName)) {
                    filteredProducts.add(pis);
                    continue;
                }
            if (categoryName != null)
                if (productCategories.get(pis.getId()).equals(productName)) {
                    filteredProducts.add(pis);
                    continue;
                }
            if (priceRange != null) {
                double price = pis.getPrice(currentUser);
                if (price >= priceRange.getKey() && price <= priceRange.getValue()) {
                    filteredProducts.add(pis);
                    continue;
                }
            }
            if (minItemRating != -1) {
                if (productRatings.get(pis.getId()) >= minItemRating) {
                    filteredProducts.add(pis);
                }
            }
        }

        String results = "Results:\n\n";
        for (ProductInStore pis: filteredProducts) {
            String productInfo = pis.toString();
            for (String keyword: keywords) {
                if (productInfo.contains(keyword)) {
                    results += productInfo + "\n---------------------------------\n";
                    break;
                }
            }
        }


        return results;
    }

}
