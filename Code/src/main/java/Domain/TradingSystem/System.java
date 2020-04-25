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

    //For Testing Purpose only:
    public void setSupplyHandler(SupplyHandler supplyHandler) {
        this.supplyHandler = supplyHandler;
    }

    public void setPaymentHandler(PaymentHandler paymentHandler) {
        this.paymentHandler = paymentHandler;
    }

    public void setUserHandler(UserHandler userHandler) {
        this.userHandler = userHandler;
    }

    public void setStores(List<Store> stores) {
        this.stores = stores;
    }

    public void setProductNames(Map<Integer, String> productNames) {
        this.productNames = productNames;
    }

    public void setProductCategories(Map<Integer, String> productCategories) {
        this.productCategories = productCategories;
    }

    public void setProductRatings(Map<Integer, Integer> productRatings) {
        this.productRatings = productRatings;
    }

    public void setLogger(SystemLogger log){
        this.logger = log;
    }
    //Usecase 1.1
    private void setSupply(String config) throws Exception {
        supplyHandler = new SupplyHandler(config);
    }

    private void setPayment(String config) throws Exception {
       paymentHandler = new PaymentHandler(config);
    }


    public boolean setup(String supplyConfig,String paymentConfig){
        logger.info("SETUP - supplyConfig = "+supplyConfig+", paymentConfig ="+paymentConfig+".");
        userHandler.setAdmin();
        try {
            setSupply(supplyConfig);
            setPayment(paymentConfig);
        }
        catch(Exception e){
            logger.error(e.getMessage());
            return false;
        }
        instance = this;
        return true;
    }

    public int startSession(){
        return userHandler.createSession();
    }

    public void addStore (){
        Store store = new Store();
        stores.add(store);

    }

    //Usecase 3.1

    public boolean isSubscriber(int sessionId) {
        User u = userHandler.getUser(sessionId);
        return u!=null && !u.isGuest();
    }

    public void saveLatestCart(int sessionId) {
        User u = userHandler.getUser(sessionId);
        if(u!= null)
            u.saveLatestCart();
    }
    public boolean logout(int sessionId){
        User u = userHandler.getUser(sessionId);
        if(u!=null)
            return u.logout();
        return false;
    }

    //Usecase 3.2
    /**
     * open new store
     * @return the new store id
     */
    public int openStore(int sessionId){
        User u = userHandler.getUser(sessionId);
        if(u!=null) {
            Store newStore = u.openStore();
            if (newStore != null) {
                stores.add(newStore);
                return newStore.getId();
            }

        }
        return -1;
    }


    //Usecase 3.7
    public String getHistory(int sessionId){
        User u = userHandler.getUser(sessionId);
        if(u!=null)
            return u.getHistory();
        return null;
    }

    //Usecase 5.1

    public boolean isManagerWith(int sessionId, int storeId,String details) {
        User u = userHandler.getUser(sessionId);
        if(u!=null){
            Subscriber s = (Subscriber) u.getState();
            return s.hasManagerPermission(storeId) && s.checkPrivilage(storeId,details);
        }
        return false;
    }

    // UseCase 4.1.1

    public boolean isOwner(int sessionId, int storeId) {
        User u = userHandler.getUser(sessionId);
        Subscriber s = (Subscriber) u.getState();
        return s.hasOwnerPermission(storeId);

    }
    public boolean addProductToStore(int sessionId,int storeId, int productId,int ammount){

        User u = userHandler.getUser(sessionId);
        logger.info(String.format("UserId %d Add %d of Product %d to Store %d",u.getId(),ammount,productId,storeId));
        if(u!=null) {
            Store s = getStoreById(storeId);
            if(s!=null) {
                return u.addProductToStore(s, productId, ammount);
            }
        }
        return false;
    }

    //UseCase 4.1.2
    public boolean editProductInStore(int sessionId, int storeId, int productId,String newInfo){
        //TODO:Add logger call
        User u = userHandler.getUser(sessionId);
        if(u!=null) {
            Store s = getStoreById(storeId);
            if(s!=null)
                return u.editProductInStore(s, productId, newInfo);
        }
        return false;
    }

    //UseCase 4.1.3
    public boolean deleteProductFromStore(int sessionId, int storeId, int productId){
        //TODO:Add logger call
        User u = userHandler.getUser(sessionId);
        if(u!=null) {
            Store s = getStoreById(storeId);
            if(s!=null)
                return u.deleteProductFromStore(s, productId);
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
    public List <Integer> getAvailableUsersToOwn(int sessionId,int storeId) {
        //TODO:Add logger call
        User u = userHandler.getUser(sessionId);
        List <Subscriber> owners = new LinkedList<Subscriber>(); //update store owners list
        if (u.getState().hasOwnerPermission(storeId)){
            for (Store store: stores) {
                if (store.getId()==storeId)
                    owners = store.getOwners();
            }

            return userHandler.getAvailableUsersToOwn(owners); // return only available subs
        }
        else
            return null;
    }

    public boolean addStoreOwner (int sessionId, int storeId, int subId) {
        //TODO:Add logger call
        User u = userHandler.getUser(sessionId);
        Subscriber newOwner = userHandler.getSubscriber(subId);
        if (newOwner == null)
            return false;
        Store s = getStoreById(storeId);
        if(s!=null)
            return u.addOwner(s,newOwner);

        return false;
    }

    public boolean subIsOwner(int subId,int storeId) {
        Subscriber s = userHandler.getSubscriber(subId);
        return s!=null && s.hasOwnerPermission(storeId);
    }


    /**
     *  Use Case 4.5
     * @param storeId
     * @return - list of available user id's
     *           if there is no premission returns null.
     */
    public List <Integer> getAvailableUsersToManage(int sessionId, int storeId) {
        //TODO:Add logger call
        User u = userHandler.getUser(sessionId);
        List <Subscriber> managers = new LinkedList<Subscriber>(); //update store owners list
        if (u.getState().hasOwnerPermission(storeId)){
            for (Store store: stores) {
                if (store.getId()==storeId)
                    managers = store.getManagers();
            }

            return userHandler.getAvailableUsersToOwn(managers); // return only available subs
        }
        else
            return null;
    }

    public boolean addStoreManager (int sessionId, int storeId, int userId) {
        User u = userHandler.getUser(sessionId);
        //TODO:Add logger call
        Subscriber newManager = userHandler.getSubscriber(userId);
        if (newManager == null)
            return false;
        for (Store store : stores) {
            if (store.getId() == storeId) {
                return u.addManager(store,newManager);

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
    public boolean deleteManager (int sessionId, int storeId, int userId) {
        //TODO:Add logger call
        User u = userHandler.getUser(sessionId);
        if(u!= null) {
            Subscriber managerToDelete = userHandler.getSubscriber(userId);
            if (managerToDelete != null) {
                Store s = getStoreById(storeId);
                if (s != null)
                    return u.deleteManager(s, managerToDelete);
            }
            return false;
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

    public boolean subIsManager(int subId, int storeId) {
        Subscriber s = userHandler.getSubscriber(subId);
        return s!=null && s.hasManagerPermission(storeId);
    }

    public List <Integer> getManagersOfCurUser(int sessionId, int storeId){
        //TODO:Add logger call
        User u = userHandler.getUser(sessionId);
        if (u.getState().hasOwnerPermission(storeId)){
            return userHandler.getManagersOfCurUser(storeId,
                    ((Subscriber)u.getState()).getId());
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
    public String getManagerDetails(int sessionId, int managerId, int storeId){
        User u = userHandler.getUser(sessionId);
        if (u.getState().hasOwnerPermission(storeId)) {
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
    public boolean setManagerDetalis(int sessionId, int managerId, int storeId, String details){
        User u = userHandler.getUser(sessionId);
        if(u!=null) {
            Subscriber manager = userHandler.getSubscriber(managerId);
            if (manager == null)
                return false;
            Store store = getStoreById(storeId);
            return u.getState().editPermission(manager, store, details);

        }
        return false;
    }

    //usecase 4.10,5.1,6.4.2
    public String getStoreHistory( int storeId){
        Store s = getStoreById(storeId);
        if(s!=null){
            return s.getHistory().toString();
        }
        return null;
    }

    // usecase 2.8
    public boolean confirmPurchase(int sessionId, double totalPrice) {
        User u = userHandler.getUser(sessionId);
        return u.confirmPrice(totalPrice);
    }

    public boolean requestConfirmedPurchase(int sessionId) {
        User u = userHandler.getUser(sessionId);
        return u.requestConfirmedPurchase();
    }


    // usecase 2.8.3
    public boolean makePayment(int sessionId, String paymentDetails, Map<Integer, Map<Integer, Integer>> storeProductsIds) {
        return paymentHandler.makePayment(paymentDetails, storeProductsIds);
    }

    public boolean cancelPayment(int sessionId, Map<Integer, Map<Integer, Integer>> storeProductsIds) {
        return paymentHandler.cancelPayment(sessionId, storeProductsIds);
    }

    // usecase 2.8.4
    public boolean requestSupply(int sessionId, Map<Integer, Map<Integer, Integer>> storeProductsIds) {
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

        return supplyHandler.requestSupply(sessionId, storeProductsIds);
    }

    // Usecase 2.2
    public int register(int sessionId,String username, String password) {
        User u = userHandler.getUser(sessionId);
        if (!u.isGuest()) return -1;
        if (username == null || password == null) return -1;
        return userHandler.register(username, Security.getHash(password));
    }

    // Usecase 2.3
    public boolean login(int sessionId, String username, String password) {
        User u = userHandler.getUser(sessionId);
        if (!u.isGuest()) return false;

        Subscriber subToLogin = userHandler.getSubscriberUser(username, Security.getHash(password));

        if (subToLogin != null) {
            ShoppingCart subscriberCart = subToLogin.getPurchaseHistory().getLatestCart();
            if(subscriberCart != null) {
                subscriberCart.merge(u.getShoppingCart());
            }
            u.setState(subToLogin);
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
    public String searchProducts(int sessionId, String productName, String categoryName, String[] keywords, Pair<Integer, Integer> priceRange, int minItemRating, int minStoreRating) {
        User u = userHandler.getUser(sessionId);
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
                double price = pis.getPrice(u);
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



    public void deleteUsers() {
        userHandler.deleteUsers();
    }

    //Usecases 6.*

    //usecase 6.4.1

    public boolean isAdmin(int sessionId) {
        User u = userHandler.getUser(sessionId);
        return u!=null && u.isAdmin();
    }
    public String getUserHistory(int subId){

            Subscriber subscriber = userHandler.getSubscriber(subId);
            if (subscriber != null)
                return subscriber.getHistory();
            return null;

    }



    public Store getStoreById(int storeId){
        for(Store s: stores){
            if (s.getId() == storeId){
                return s;
            }
        }
        return null;
    }

    public boolean addToCart(int sessionId, int storeId, int productId, int amount){
        User u = userHandler.getUser(sessionId);
        Store store = getStoreById(storeId);
        return u.addProductToCart(store, productId, amount);


    }

    public boolean updateAmount(int sessionId, int storeId, int productId, int amount) {
        User u = userHandler.getUser(sessionId);
        Store store = getStoreById(storeId);
        return u.editCartProductAmount(store, productId, amount);
    }

    public boolean deleteItemInCart(int sessionId, int storeId, int productId) {
        User u = userHandler.getUser(sessionId);
        Store store = getStoreById(storeId);
        return u.removeProductFromCart(store, productId);
    }

    public boolean clearCart(int sessionId) {
        User u = userHandler.getUser(sessionId);
        return u.removeAllProductsFromCart();
    }

    public double buyCart(int sessionId) {
        User u = userHandler.getUser(sessionId);
        return u.purchaseCart();
    }

    public String getCart(int sessionId) {
        User u = userHandler.getUser(sessionId);
        return u.getShoppingCart().toString();
    }

    //New uscase 2.3
    public boolean isGuest(int sessionId) {
        User u = userHandler.getUser(sessionId);
        return u!=null && u.isGuest();
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


    public void deleteStores() {
        stores.clear();
    }

    public List<Store> getStores(){
        return stores;
    }
}
