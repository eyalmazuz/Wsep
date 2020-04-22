package Domain.TradingSystem;

import java.util.LinkedList;
import java.util.List;

import Domain.Logger.SystemLogger;
import Domain.Security.Security;

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
    private SystemLogger logger;

    private Map<Integer, String> productNames = new HashMap<>();
    private Map<Integer, String> productCategories = new HashMap<>();
    private Map<Integer, Integer> productRatings = new HashMap<>();

    public System(){
        userHandler = new UserHandler();
        stores = new LinkedList<>();
        productCategories = new HashMap<>();
        productNames = new HashMap<>();
        productRatings = new HashMap<>();
        logger = new SystemLogger();

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
        instance = this;
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


    // usecase 2.8.3
    public boolean makePayment(String paymentDetails, Map<Integer, Map<Integer, Integer>> storeProductsIds) {
        return paymentHandler.makePayment(paymentDetails, storeProductsIds);
    }

    public boolean cancelPayment(User user, Map<Integer, Map<Integer, Integer>> storeProductsIds) {
        return paymentHandler.cancelPayment(user, storeProductsIds);
    }

    // usecase 2.8.4
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

    // Usecase 2.2
    public int register(String username, String password) {
        if (!currentUser.isGuest()) return -1;
        if (username == null || password == null) return -1;
        return userHandler.register(username, password);
    }

    // Usecase 2.3
    public boolean login(String username, String password) {
        if (!currentUser.isGuest()) return false;
        if(username == null || password == null || username.equals("") || password.equals("")) return false;

        Subscriber subToLogin = userHandler.getSubscriberUser(username, Security.getHash(password));

        if (subToLogin != null) {
            ShoppingCart subscriberCart = subToLogin.getPurchaseHistory().getLatestCart();
            if(subscriberCart != null) {
                subscriberCart.merge(currentUser.getShoppingCart());
            }
            currentUser.setState(subToLogin);
            return true;
        }

        return false;
    }

    // Usecase 2.4
    public String viewStoreProductInfo() {
        String info = "";
        for (Store store: stores) {
            info += store.toString() + "\n--------------------------\n";
        }

        return info;
    }

    // Usecase 2.5
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
                if (price >= priceRange.getFirst() && price <= priceRange.getSecond()) {
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
            if (keywords != null) {
                for (String keyword : keywords) {
                    if (productInfo.contains(keyword)) {
                        results += productInfo + "\n---------------------------------\n";
                        break;
                    }
                }
            }
            else results += productInfo + "\n---------------------------------\n";
        }


        return results;
    }

    public void setCurrentUser(User user){
        currentUser = user;
    }

    public void deleteUsers() {
        userHandler.deleteUsers();
    }

    //Usecases 6.*

    //usecase 6.4.1
    public String getUserHistory(int userId){
        if(currentUser.isAdmin()){
            Subscriber subscriber = userHandler.getUser(userId);
            if (subscriber != null)
                return subscriber.getHistory();
        }
        return null;
    }

    //usecase 6.4.2
    public String getStoreHistoryAsAdmin(int storeId){
        if(currentUser.isAdmin()){
            for(Store s : stores){
                if(s.getId() == storeId){
                    return s.getHistory().toString();
                }
            }
        }
        return null;
    }

    private Store getStoreById(int storeId){
        for(Store s: stores){
            if (s.getId() == storeId){
                return s;
            }
        }
        return null;
    }

    public boolean addToCart(int storeId, int productId, int amount){
        Store store = getStoreById(storeId);
        return currentUser.addProductToCart(store, productId, amount);


    }

    public boolean updateAmount(int storeId, int productId, int amount) {
        Store store = getStoreById(storeId);
        return currentUser.editCartProductAmount(store, productId, amount);
    }

    public boolean deleteItemInCart(int storeId, int productId) {
        Store store = getStoreById(storeId);
        currentUser.removeProductFromCart(store, productId);
        return true;
    }

    public boolean clearCart() {
        currentUser.removeAllProductsFromCart();
        return true;
    }

    //TODO FIX THIS
    public boolean buyCart() {
        currentUser.purchaseCart();
        return true;
    }

    public String getCart() {
        return currentUser.getShoppingCart().toString();
    }

    //New uscase 2.3
    public boolean isGuest(int sessionId) {
        //User u = userHandler.getUser(sessionId);
        //return u!=null && u.isGuest();
        return true;
    }

    public int getSubscriber(String username, String password) {
        Subscriber s = userHandler.getSubscriberUser(username,Security.getHash(password));
        if (s == null)
            return -1;
        else
            return s.getId();
    }

    public void setState(int sessionId, int subId) {
        userHandler.setState(sessionId,subId);
    }

    public void mergeCartWithSubscriber(int sessionId) {

    }
}
