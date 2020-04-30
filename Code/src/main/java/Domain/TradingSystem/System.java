package Domain.TradingSystem;

import Domain.Logger.SystemLogger;
import Domain.Security.Security;

import java.util.*;

public class System {

    private static System instance = null;


    private SupplyHandler supplyHandler;
    private PaymentHandler paymentHandler;
    private UserHandler userHandler;
    private List<Store> stores;
    private SystemLogger logger;
    private List<ProductInfo> products;

    public System(){
        userHandler = new UserHandler();
        stores = new LinkedList<>();
        logger = new SystemLogger();
        products = new LinkedList<>();
    }

    public static System getInstance(){
        if (instance == null) instance = new System();
        return instance;
    }

    public ProductInfo getProductInfoById(int id) {
        for (ProductInfo productInfo: products) {
            if (productInfo.getId() == id)
                return productInfo;
        }
        return null;
    }

    public List<ProductInfo> getProducts() {
        return products;
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

    public SupplyHandler getSupplyHandler() {
        return supplyHandler;
    }

    public PaymentHandler getPaymentHandler() {
        return paymentHandler;
    }

    public UserHandler getUserHandler() {
        return userHandler;
    }

    public void setStores(List<Store> stores) {
        this.stores = stores;
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

        // TODO: INITIALIZE PRODUCT INFOS LIST
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
        logger.info("startSession: no arguments");
        return userHandler.createSession();
    }

    public void addStore (){
        logger.info("AddStore");
        Store store = new Store();
        stores.add(store);

    }

    //Usecase 3.1
    public boolean isSubscriber(int sessionId) {
        logger.info("IsSubscriber: sessionId "+sessionId);
        User u = userHandler.getUser(sessionId);
        return u!=null && !u.isGuest();
    }

    public void saveLatestCart(int sessionId) {
        logger.info("saveLatestCart: sessionId "+sessionId);
        User u = userHandler.getUser(sessionId);
        if(u!= null)
            u.saveLatestCart();
    }
    public boolean logout(int sessionId){
        logger.info("Logout: sessionId "+sessionId);
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
        logger.info("openStore: sessionId "+sessionId);
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
        logger.info("getUserHistory: sessionId "+sessionId);
        User u = userHandler.getUser(sessionId);
        if(u!=null)
            return u.getHistory();
        return null;
    }

    //Usecase 5.1

    public boolean isManagerWith(int sessionId, int storeId,String details) {
        logger.info("isManagerWith: sessionId "+sessionId+", storeId: "+storeId+", details: "+details);
        User u = userHandler.getUser(sessionId);
        if(u!=null){
            Subscriber s = (Subscriber) u.getState();
            return s.hasManagerPermission(storeId) && s.checkPrivilage(storeId,details);
        }
        return false;
    }

    // UseCase 4.1.1

    public boolean isOwner(int sessionId, int storeId) {
        logger.info("isOwner: sessionId "+sessionId+", storeId: "+storeId);
        User u = userHandler.getUser(sessionId);
        Subscriber s = (Subscriber) u.getState();
        return s.hasOwnerPermission(storeId);

    }
    public boolean addProductToStore(int sessionId,int storeId, int productId,int ammount) {

        logger.info(String.format("SessionId %d Add %d of Product %d to Store %d", sessionId, ammount, productId, storeId));
        ProductInfo info = getProdctInfo(productId);
        if(info != null) {
            Store store = getStoreById(storeId);
            if (store != null) {
                return store.addProduct(info, ammount);
            }
            return false;
        }
        return false;
    }

    //UseCase 4.1.2
    public boolean editProductInStore(int sessionId, int storeId, int productId,String newInfo){
        logger.info("editProductInStore: sessionId: "+sessionId+", storeId: "+storeId + ", productId: " + productId + ", newInfo: " + newInfo);
        if(getProdctInfo(productId) != null) {
            Store store = getStoreById(storeId);
            if (store != null) {
                return store.editProduct(productId, newInfo);
            }
            return false;
        }
        return false;
    }

    //UseCase 4.1.3
    public boolean deleteProductFromStore(int sessionId, int storeId, int productId){
        logger.info("deleteProductFromStore: sessionId: "+sessionId+", storeId: "+storeId + ", productId: " + productId );
        if(getProdctInfo(productId) != null) {
            Store store = getStoreById(storeId);
            if (store != null) {
                return store.deleteProduct(productId);
            }
            return false;
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
        logger.info("getAvailableUsersToOwn: sessionId: "+sessionId+", storeId: "+storeId );
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
        logger.info("getAvailableUsersToOwn: sessionId: "+sessionId+", storeId: "+storeId+", subId: "+subId );
        User u = userHandler.getUser(sessionId);
        Subscriber newOwner = userHandler.getSubscriber(subId);
        if (newOwner == null)
            return false;
        Store s = getStoreById(storeId);
        if(s!=null)
        {
            if(newOwner.addPermission(s, (Subscriber) u.getState(), "Owner")){
                s.addOwner(newOwner);
                return true;
            }

        }


        return false;
    }

    public boolean subIsOwner(int subId,int storeId) {
        logger.info("subIsOwner: subId " + subId + ", storeId " + storeId);
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
        logger.info("getAvailableUsersToManage: sessionId: "+sessionId+", storeId: "+storeId );
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
        logger.info("addStoreManager: sessionId: "+sessionId+", storeId: "+storeId+", userId: "+userId );
        Subscriber newManager = userHandler.getSubscriber(userId);
        if (newManager == null)
            return false;
            Store store = getStoreById(storeId);
            if (store != null) {
                if(newManager.addPermission(store, (Subscriber)u.getState(), "Manager")){
                    store.addOwner(newManager);
                    return true;
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
        logger.info("deleteManager: sessionId: "+sessionId+", storeId: "+storeId+", userId: "+userId );
        User u = userHandler.getUser(sessionId);
        if(u!= null) {
            Subscriber managerToDelete = userHandler.getSubscriber(userId);
            if (managerToDelete != null) {
                Store store = getStoreById(storeId);
                if (store != null) {
                    managerToDelete.removePermission(store, "Manager");
                    store.removeManger(managerToDelete);
                    return true;
                }
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
        logger.info("subIsManager: subId " + subId + ", storeId " + storeId);
        Subscriber s = userHandler.getSubscriber(subId);
        return s!=null && s.hasManagerPermission(storeId);
    }

    public List <Integer> getManagersOfCurUser(int sessionId, int storeId){
        logger.info("getManagersOfCurUser: sessionId: "+sessionId+", storeId: "+storeId);
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
        logger.info("setManagerDetalis: sessionId " + sessionId + ", managerId " + managerId + ", storeId " + storeId + ", details " + details);
        User u = userHandler.getUser(sessionId);
        if(u!=null) {
            Subscriber manager = userHandler.getSubscriber(managerId);
            if (manager == null)
                return false;
            Store store = getStoreById(storeId);
            if(store!=null) {

                String[] validDetailes = {"any", "add product", "edit product", "delete product"};

                if (Arrays.asList(validDetailes).contains(details)) {
                    manager.overridePermission("Manager", store, details);
                    return true;
                }
            }

        }
        return false;
    }

    //usecase 4.10,5.1,6.4.2
    public String getStoreHistory( int storeId){
        logger.info("getStoreHistory: storeId "+storeId);
        Store s = getStoreById(storeId);
        if(s!=null){
            return s.getHistory().toString();
        }
        return null;
    }

    // usecase 2.8
    public boolean requestConfirmedPurchase(int sessionId, String paymentDetails) {
        logger.info("requestConfirmedPurchase: sessionId " + sessionId + ", paymentDetails " + paymentDetails);
        User u = userHandler.getUser(sessionId);

        makePayment(sessionId, paymentDetails, u.getShoppingCart().getStoreProductsIds());

        return u.requestConfirmedPurchase();
    }

    public boolean setPaymentDetails(int sessionId, String details) {
        logger.info("setPaymentDetails: sessionId " + sessionId + ", details " + details);
        User u = userHandler.getUser(sessionId);
        return u.setPaymentDetails(details);
    }

    // usecase 2.8.3
    public boolean makePayment(int sessionId, String paymentDetails, Map<Integer, Map<Integer, Integer>> storeProductsIds) {
        return paymentHandler.makePayment(sessionId, paymentDetails, storeProductsIds);
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
    public int register(int sessionId, String username, String password) {
        logger.info("register: sessionId " + sessionId + ", username " + username + ", password " + Security.getHash(password));
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
        logger.info("searchProducts: no arguments");
        String info = "";
        for (Store store: stores) {
            info += store.toString() + "\n--------------------------\n";
        }

        return info;
    }

    // Usecase 2.5
    public String searchProducts(int sessionId, String productName, String categoryName, String[] keywords, int minItemRating, int minStoreRating) {
        logger.info("searchProducts: sessionId " + sessionId + ", productName " + productName + ", categoryName: "
                + categoryName + ", keywords " + Arrays.toString(keywords) + ", minItemRating " + minItemRating + ", minStoreRating " + minStoreRating);
        List<ProductInStore> allProducts = new ArrayList<>();
        List<ProductInStore> filteredProducts = new ArrayList<>();

       for (Store store: stores)
            if (store.getRating() >= minStoreRating) allProducts.addAll(store.getProducts());

        for (ProductInStore pis: allProducts) {
            ProductInfo info = pis.getProductInfo();
            if (productName != null)
                if (info.getName().equals(productName)) {
                    filteredProducts.add(pis);
                    continue;
                }
            if (categoryName != null)
                if (info.getCategory().equals(categoryName)) {
                    filteredProducts.add(pis);
                    continue;
                }
            if (minItemRating != -1) {
                if (info.getRating()>= minItemRating) {
                    filteredProducts.add(pis);
                }
            }

            if (keywords != null) {
                for (String keyword: keywords) {
                    if (pis.toString().contains(keyword))
                        filteredProducts.add(pis);
                }

            }
        }

        String results = "Results:\n\n";
        for (ProductInStore pis: filteredProducts) {
            results += pis.toString() + "\n---------------------------------\n";
        }

        return results;
    }



    public void deleteUsers() {
        userHandler.deleteUsers();
    }

    //Usecases 6.*

    //usecase 6.4.1

    public boolean isAdmin(int sessionId) {
        logger.info("IsAdmin: sessionId "+sessionId);
        User u = userHandler.getUser(sessionId);
        return u!=null && u.isAdmin();
    }
    public String getUserHistory(int subId){
        logger.info("getUserHistory: SubscriberId "+subId);
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

    private boolean checkCartModificationDetails(int sessionId, int storeId, int productId, int amount) {
        if (userHandler.getUser(sessionId) == null) return false;
        boolean found = false;
        for (Store store : stores) {
            if (store.getId() == storeId) {
                found = true;
                break;
            }
        }
        if (!found) return false;

        found = false;
        for (ProductInfo product : products) {
            if (product.getId() == productId) {
                found = true;
                break;
            }
        }
        if (!found) return false;

        return amount >= 1;
    }

    public boolean addToCart(int sessionId, int storeId, int productId, int amount){
        logger.info("addToCart: sessionId " + sessionId + ", storeId " + storeId + ", productId " + productId + ", amount " + amount);
        if (!checkCartModificationDetails(sessionId, storeId, productId, amount)) return false;
        User u = userHandler.getUser(sessionId);
        Store store = getStoreById(storeId);
        return u.addProductToCart(store, productId, amount);
    }

    public boolean updateAmount(int sessionId, int storeId, int productId, int amount) {
        logger.info("updateAmount: sessionId " + sessionId + ", storeId " + storeId + ", productId " + productId + ", amount " + amount);
        if (!checkCartModificationDetails(sessionId, storeId, productId, amount)) return false;
        User u = userHandler.getUser(sessionId);
        Store store = getStoreById(storeId);
        return u.editCartProductAmount(store, productId, amount);
    }

    public boolean deleteItemInCart(int sessionId, int storeId, int productId) {
        logger.info("deleteItemInCart: sessionId " + sessionId + ", storeId " + storeId + ", productId " + productId);
        if (!checkCartModificationDetails(sessionId, storeId, productId, 1)) return false;
        User u = userHandler.getUser(sessionId);
        Store store = getStoreById(storeId);
        return u.removeProductFromCart(store, productId);
    }

    public boolean clearCart(int sessionId) {
        logger.info("clearCart: sessionId " + sessionId);
        User u = userHandler.getUser(sessionId);
        return u.removeAllProductsFromCart();
    }

    public double buyCart(int sessionId) {
        logger.info("buyCart: sessionId " + sessionId);
        User u = userHandler.getUser(sessionId);
        return u.purchaseCart();
    }

    public String getCart(int sessionId) {
        logger.info("getCart: sessionId " + sessionId);
        User u = userHandler.getUser(sessionId);
        return u.getShoppingCart().toString();
    }

    // Usecase 2.3
    public boolean isGuest(int sessionId) {
        logger.info("isGuest: sessionId "+ sessionId);
        User u = userHandler.getUser(sessionId);
        return u!=null && u.isGuest();
    }

    public int getSubscriber(String username, String password) {
        logger.info("getSubscriber: username " + username + ", password " + password);
        if(username == null || password == null || username.equals("") || password.equals("")) return -1;
        Subscriber s = userHandler.getSubscriberUser(username,Security.getHash(password));
        if (s == null)
            return -1;
        else
            return s.getId();
    }

    public void setState(int sessionId, int subId) {
        logger.info("setState: sessionId " + sessionId + ", subId " + subId);
        userHandler.setState(sessionId, subId);
    }

    // Usecase 2.3
    public void mergeCartWithSubscriber(int sessionId) {
        logger.info("mergeCartWithSubscriber: sessionId " + sessionId);
        userHandler.mergeCartWithSubscriber(sessionId);
    }


    public void deleteStores() {
        stores.clear();
    }

    public List<Store> getStores(){
        return stores;
    }

    public User getUser(int sessionId) {
        return userHandler.getUser(sessionId);
    }

    public void addProductInfo(int id, String name, String category) {
        logger.info("addProductInfo: id " + id + ", name " + name + ", category " + category);
        ProductInfo productInfo = new ProductInfo(id, name, category);
        products.add(productInfo);
    }

    private ProductInfo getProdctInfo(int productId){
        for(ProductInfo productInfo: products){
            if(productInfo.getId() == productId){
                return productInfo;
            }
        }
        return null;
    }

    public void removeStoreProductSupplies(Integer storeId, Map<Integer, Integer> productIdAmountMap) {
        Store store = getStoreById(storeId);
        for (Integer productId : productIdAmountMap.keySet()) {
            store.removeProductAmount(productId, productIdAmountMap.get(productId));
        }
    }

    public boolean changeBuyingPolicy(int storeId, String newPolicy){
        logger.info("changeBuyingPolicy: storeId " + storeId + ", newPolicy " + newPolicy);
        Store s = getStoreById(storeId);
        if(s != null){
            s.setBuyingPolicy(new BuyingPolicy(newPolicy));
            return true;
        }
        return false;
    }
}
