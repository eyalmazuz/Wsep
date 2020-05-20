package Domain.TradingSystem;

import DTOs.*;
import DTOs.SimpleDTOS.*;
import Domain.Logger.SystemLogger;
import Domain.Security.Security;
import Domain.Spelling.Spellchecker;
import NotificationPublisher.Publisher;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class System {

    private static System instance = null;
    private static int notificationId = 0;


    private SupplyHandler supplyHandler;
    private PaymentHandler paymentHandler;
    private UserHandler userHandler;
    private Map<Integer,Store> stores;
    private SystemLogger logger;
    private Map<Integer,ProductInfo> products;

    private Publisher publisher;

    // session id -> (store id -> (product id -> amount))
    private Map<Integer, Map<Integer, Map<Integer, Integer>>> ongoingPurchases = new HashMap<>();

    public System(){
        userHandler = new UserHandler();
        stores = new ConcurrentHashMap<>();
        logger = new SystemLogger();
        products = new ConcurrentHashMap<>();
    }

    public static System getInstance(){
        if (instance == null) instance = new System();
        return instance;
    }

    public ProductInfo getProductInfoById(int id) {
        return products.get(id);
    }

    public Map<Integer, ProductInfo> getProducts() {
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

    public void setStores(Map<Integer, Store> stores) {
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


    public ActionResultDTO setup(String supplyConfig, String paymentConfig){

        // TODO: INITIALIZE PRODUCT INFOS LIST
        logger.info("SETUP - supplyConfig = "+supplyConfig+", paymentConfig ="+paymentConfig+".");
        userHandler.setAdmin();
        try {
            setSupply(supplyConfig);
            setPayment(paymentConfig);
        }
        catch(Exception e){
            logger.error(e.getMessage());
            return new ActionResultDTO(ResultCode.ERROR_SETUP,"e.getMessage");
        }
//        instance = this;
        return new ActionResultDTO(ResultCode.SUCCESS,"Setup Succsess");
    }

    public IntActionResultDto startSession(){
        logger.info("startSession: no arguments");
        return new IntActionResultDto(ResultCode.SUCCESS,"start session",userHandler.createSession());
    }

    public int addStore (){
        logger.info("AddStore");
        Store store = new Store();
        stores.put(store.getId(),store);
        return store.getId();

    }

    //Usecase 3.1
    public boolean isSubscriber(int sessionId) {
        logger.info("IsSubscriber: sessionId "+sessionId);
        User u = userHandler.getUser(sessionId);
        return u!=null && !u.isGuest();
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
    public IntActionResultDto openStore(int sessionId){
        logger.info("openStore: sessionId "+sessionId);
        User u = userHandler.getUser(sessionId);
        if(u!=null) {
            Store newStore = u.openStore();
            if (newStore != null) {
                stores.put(newStore.getId(),newStore);

                return new IntActionResultDto(ResultCode.SUCCESS,"Open new store",newStore.getId());
            }

        }
        return new IntActionResultDto(ResultCode.ERROR_SESSIONID,"cannot find sessionId",-1);
    }


    //Usecase 3.7
    public UserPurchaseHistoryDTO getHistory(int sessionId){
        logger.info("getUserHistory: sessionId "+sessionId);
        User u = userHandler.getUser(sessionId);
        if(u!=null) {
            UserPurchaseHistory history = u.getHistory();
            if(history!=null) {
                return new UserPurchaseHistoryDTO(ResultCode.SUCCESS, "got User History", getHistoryMap(history));
            }
            return  new UserPurchaseHistoryDTO(ResultCode.ERROR_HISTORY,"Cannot get Guest history",null);
        }
        return new UserPurchaseHistoryDTO(ResultCode.ERROR_SESSIONID,"Illegal SessionId",null);
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
    public ActionResultDTO addProductToStore(int sessionId,int storeId, int productId,int ammount) {

        logger.info(String.format("SessionId %d Add %d of Product %d to Store %d", sessionId, ammount, productId, storeId));
        ProductInfo info = getProductInfoById(productId);
        if(info != null) {
            Store store = getStoreById(storeId);
            if (store != null) {
                ActionResultDTO result = store.addProduct(info, ammount);
                //Publisher Update
                if(result.getResultCode()==ResultCode.SUCCESS){
                    if(publisher != null) {
                        List<Integer> managers = store.getAllManagers().stream().map(Subscriber::getId).collect(Collectors.toList());
                        Notification notification = new Notification(notificationId++,"Store " + storeId + " has been updated");
                        updateAllUsers(store.getAllManagers(),notification);
                        publisher.notify(managers,notification);
                    }
                }
                return result;
            }
            return new ActionResultDTO(ResultCode.ERROR_STORE_PRODUCT_MODIFICATION, "No such store.");
        }
        return new ActionResultDTO(ResultCode.ERROR_STORE_PRODUCT_MODIFICATION, "No such product.");
    }

    //UseCase 4.1.2
    public ActionResultDTO editProductInStore(int sessionId, int storeId, int productId,String newInfo){
        logger.info("editProductInStore: sessionId: "+sessionId+", storeId: "+storeId + ", productId: " + productId + ", newInfo: " + newInfo);
        if(getProductInfoById(productId) != null) {
            Store store = getStoreById(storeId);
            if (store != null) {
                ActionResultDTO result =  store.editProduct(productId, newInfo);
                //Publisher Update
                if(result.getResultCode()==ResultCode.SUCCESS){
                    if(publisher != null) {
                        List<Integer> managers = store.getAllManagers().stream().map(Subscriber::getId).collect(Collectors.toList());
                        String message = "Store " + storeId + " has been updated: product has been edited";
                        Notification notification = new Notification(notificationId++,message);
                        updateAllUsers(store.getAllManagers(),notification);
                        publisher.notify(managers,notification);
                    }
                }
                return result;
            }
            return new ActionResultDTO(ResultCode.ERROR_STORE_PRODUCT_MODIFICATION, "No such store.");
        }
        return new ActionResultDTO(ResultCode.ERROR_STORE_PRODUCT_MODIFICATION, "No such product.");
    }

    //UseCase 4.1.3
    public ActionResultDTO deleteProductFromStore(int sessionId, int storeId, int productId){
        logger.info("deleteProductFromStore: sessionId: "+sessionId+", storeId: "+storeId + ", productId: " + productId );
        if(getProductInfoById(productId) != null) {
            Store store = getStoreById(storeId);
            if (store != null) {
               ActionResultDTO result =  store.deleteProduct(productId);
                //Publisher Update
                if(result.getResultCode()==ResultCode.SUCCESS){
                    if(publisher != null)
                    {
                        List<Integer> managers = store.getAllManagers().stream().map(Subscriber::getId).collect(Collectors.toList());
                        Notification notification = new Notification(notificationId++,"Store " + storeId + " has been updated: product delete");
                        updateAllUsers(store.getAllManagers(),notification);
                        publisher.notify(managers,notification);
                    }
                }
                return result;
            }
            return new ActionResultDTO(ResultCode.ERROR_STORE_PRODUCT_MODIFICATION, "No such store.");
        }
        return new ActionResultDTO(ResultCode.ERROR_STORE_PRODUCT_MODIFICATION, "No such product.");
    }

    // UseCase 4.3

    /**
     *
     * @param storeId
     * @return - list of available user id's
     *           if there is no premission returns null.
     */
    public SubscriberActionResultDTO getAvailableUsersToOwn(int storeId) {
        logger.info("getAvailableUsersToOwn:  storeId: "+storeId );

        List <Subscriber> owners = new LinkedList<Subscriber>(); //update store owners list

           Store store = getStoreById(storeId);
           if (store!=null) {
               owners = store.getAllManagers();
               List<Subscriber> filterd = userHandler.getAvailableUsersToOwn(owners); // return only available subs

               return new SubscriberActionResultDTO(ResultCode.SUCCESS, "List of optional managers", getSubsDtos(filterd));
           }
           return  new SubscriberActionResultDTO(ResultCode.ERROR_STOREID,"Store Id doesn't exist",null);


        }

        private List<SubscriberDTO> getSubsDtos(List<Subscriber> lst){
            List<SubscriberDTO> subscriberDTOS = new ArrayList<>();
            for (Subscriber subscriber : lst) {
                SubscriberDTO subscriberDTO = new SubscriberDTO(subscriber.getId(), subscriber.getUsername());
                subscriberDTOS.add(subscriberDTO);
            }
            return subscriberDTOS;
        }



    public ActionResultDTO addStoreOwner (int sessionId, int storeId, int subId) {
        logger.info("getAvailableUsersToOwn: sessionId: "+sessionId+", storeId: "+storeId+", subId: "+subId );
        User u = userHandler.getUser(sessionId);
        Subscriber newOwner = userHandler.getSubscriber(subId);
        if (newOwner == null)
            return new ActionResultDTO(ResultCode.ERROR_STORE_OWNER_MODIFICATION, "Specified new owner does not exist.");
        Store s = getStoreById(storeId);
        if(s!=null)
        {
            if(newOwner.addPermission(s, (Subscriber) u.getState(), "Owner")){
                s.addOwner(newOwner);

                return new ActionResultDTO(ResultCode.SUCCESS, null);
            }

        }


        return new ActionResultDTO(ResultCode.ERROR_STORE_OWNER_MODIFICATION, "Specified store does not exist.");
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


    public ActionResultDTO addStoreManager (int sessionId, int storeId, int userId) {
        User u = userHandler.getUser(sessionId);
        logger.info("addStoreManager: sessionId: "+sessionId+", storeId: "+storeId+", userId: "+userId );
        Subscriber newManager = userHandler.getSubscriber(userId);
        if (newManager == null)
            return new ActionResultDTO(ResultCode.ERROR_STORE_MANAGER_MODIFICATION, "The specified user is invalid.");
        Store store = getStoreById(storeId);
        if (store != null) {
            if(newManager.addPermission(store, (Subscriber)u.getState(), "Manager")){
                store.addOwner(newManager);

                return new ActionResultDTO(ResultCode.SUCCESS, null);
            }
        }

        return new ActionResultDTO(ResultCode.ERROR_STORE_MANAGER_MODIFICATION, "The specified store is invalid.");
    }

    /**
     * Delete manager that was granted by the current user.
     * @param storeId
     * @param userId
     * @return
     */
    public ActionResultDTO deleteManager (int sessionId, int storeId, int userId) {
        logger.info("deleteManager: sessionId: "+sessionId+", storeId: "+storeId+", userId: "+userId );
        User u = userHandler.getUser(sessionId);
        if(isSubscriber(sessionId)) {
            Subscriber subscriber = (Subscriber) u.getState();

            if (u != null) {
                Subscriber managerToDelete = userHandler.getSubscriber(userId);
                if (managerToDelete != null && !subscriber.equals(managerToDelete)) {
                    Store store = getStoreById(storeId);
                    if (store != null) {
                        managerToDelete.removePermission(store, "Manager");
                        store.removeManger(managerToDelete);

                        return new ActionResultDTO(ResultCode.SUCCESS, null);
                    }
                }
                return new ActionResultDTO(ResultCode.ERROR_STORE_MANAGER_MODIFICATION, "The specified manager must exist and not be yourself.");
            }
        }
        return new ActionResultDTO(ResultCode.ERROR_STORE_MANAGER_MODIFICATION, "Invalid user.");
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
    public ActionResultDTO setManagerDetalis(int sessionId, int managerId, int storeId, String details){
        logger.info("setManagerDetalis: sessionId " + sessionId + ", managerId " + managerId + ", storeId " + storeId + ", details " + details);
        User u = userHandler.getUser(sessionId);
        if(u!=null) {
            Subscriber manager = userHandler.getSubscriber(managerId);
            if (manager == null)
                return new ActionResultDTO(ResultCode.ERROR_STORE_MANAGER_MODIFICATION, "The specified manager does not exist.");
            Store store = getStoreById(storeId);
            if(store!=null) {

                String[] validDetailes = {"any", "add product", "edit product", "delete product"};

                if (Arrays.asList(validDetailes).contains(details)) {
                    manager.overridePermission("Manager", store, details);
                    return new ActionResultDTO(ResultCode.SUCCESS, null);
                }
            }

        }
        return new ActionResultDTO(ResultCode.ERROR_STORE_MANAGER_MODIFICATION, "Invalid user.");
    }

    //usecase 4.10,5.1,6.4.2
    public StorePurchaseHistoryDTO getStoreHistory(int storeId){
        logger.info("getStoreHistory: storeId "+storeId);
        Store s = getStoreById(storeId);
        if(s!=null){
            List<PurchaseDetails> history =  s.getStorePurchaseHistory();
            return new StorePurchaseHistoryDTO(ResultCode.SUCCESS,"Got store history",s.getId(),
                    getPurchasesDto(history));
        }
        return new StorePurchaseHistoryDTO(ResultCode.ERROR_STOREHISTORY,"Illeagal Store Id",-1,null);
    }

    // usecase 2.8
    /*public boolean requestConfirmedPurchase(int sessionId) {
        User u = userHandler.getUser(sessionId);

        makePayment(sessionId, paymentDetails, u.getShoppingCart().getStoreProductsIds());

        return u.requestConfirmedPurchase();
    }
*/
    public boolean setPaymentDetails(int sessionId, String details) {
        logger.info("setPaymentDetails: sessionId " + sessionId + ", details " + details);
        User u = userHandler.getUser(sessionId);
        return u.setPaymentDetails(details);
    }

    // usecase 2.8.3
    public ActionResultDTO makePayment(int sessionId, String paymentDetails) {
        // retrieve store product ids
        User u = userHandler.getUser(sessionId);
        Map<Integer, Map<Integer, Integer>> storeIdProductAmounts = u.getPrimitiveCartDetails();
        boolean success = paymentHandler.makePayment(sessionId, paymentDetails, storeIdProductAmounts);
        logger.info("makePayment: sessionId " + sessionId + ", status: " + (success ? "SUCCESS" : "FAIL"));
        return success? new ActionResultDTO(ResultCode.SUCCESS, null) : new ActionResultDTO(ResultCode.ERROR_PURCHASE, "Payment system denied the purchase.");

    }

    // usecase 2.8.4
    public boolean requestSupply(int sessionId) {
        // retrieve store product ids
        Map<Integer, Map<Integer, Integer>> storeProductsIds = ongoingPurchases.get(sessionId);
        if (storeProductsIds  == null) return false;

        boolean success = supplyHandler.requestSupply(sessionId, storeProductsIds);
        logger.info("requestSupply: sessionId " + sessionId + ", status: " + (success ? "SUCCESS" : "FAIL"));
        return success;
    }

    // Usecase 2.2
    public IntActionResultDto register(int sessionId, String username, String password) {
        if (username == null || password == null|| username.equals("") || password.equals("")) return new IntActionResultDto(ResultCode.ERROR_REGISTER,"invalid username/password",-1);;
        logger.info("register: sessionId " + sessionId + ", username " + username + ", password " + Security.getHash(password));
        User u = userHandler.getUser(sessionId);
        if (u!=null) {
            int subId = userHandler.register(username, Security.getHash(password));
            if (subId == -1) {
                return new IntActionResultDto(ResultCode.ERROR_REGISTER, "Username already Exists", subId);
            }
            return new IntActionResultDto(ResultCode.SUCCESS, "Register Success", subId);
        }
       return new IntActionResultDto(ResultCode.ERROR_REGISTER,"Session id not exist",-1);
    }

    // Usecase 2.3
    public boolean login(int sessionId, String username, String password) {
        User u = userHandler.getUser(sessionId);
        if (!u.isGuest()) return false;

        Subscriber subToLogin = userHandler.getSubscriberUser(username, Security.getHash(password));

        if (subToLogin != null) {

            u.setState(subToLogin);



            return true;
        }

        return false;
    }

    // Usecase 2.4
    public StoreActionResultDTO viewStoreProductInfo() {
        logger.info("searchProducts: no arguments");
        List<StoreDTO> result = new ArrayList<>();
        String info = "";
        for (Store store: stores.values()) {
            result.add(new StoreDTO(store.getId(),store.getBuyingPolicy().toString(),
                    store.getDiscountPolicy().toString(),getProductDTOlist(store.getProducts())));
        }
        Collections.sort(result, (i, j) -> i.getStoreId() < j.getStoreId() ? -1 : 1);

        return new StoreActionResultDTO(ResultCode.SUCCESS,"List of stores:",result);
    }

    private List<ProductInStoreDTO> getProductDTOlist(List<ProductInStore> products) {
        List<ProductInStoreDTO> result = new ArrayList<>();
        for (ProductInStore pis: products) {
            result.add(new ProductInStoreDTO(pis.getId(),
                    pis.getProductInfo().getName(),
                    pis.getProductInfo().getCategory(),
                    pis.getAmount(),
                    pis.getInfo(),
                    pis.getStore().getId()));
        }
        return result;

    }

    // Usecase 2.5
    public ProductsActionResultDTO searchProducts(int sessionId, String productName, String categoryName, String[] keywords, int minItemRating, int minStoreRating) {
        logger.info("searchProducts: sessionId " + sessionId + ", productName " + productName + ", categoryName: "
                + categoryName + ", keywords " + Arrays.toString(keywords) + ", minItemRating " + minItemRating + ", minStoreRating " + minStoreRating);
        List<ProductInStore> allProducts = new ArrayList<>();
        List<ProductInStore> filteredProducts = new ArrayList<>();

        List<String> productNames = new ArrayList<>();
        productNames.add(productName);
        if (productName != null) {
            List<String> productSugs = Spellchecker.getSuggestions(productName);
            if (productSugs != null) productNames.addAll(productSugs);
        }

        List<String> categoryNames = new ArrayList<>();
        categoryNames.add(categoryName);
        if (categoryName != null) {
            List<String> categorySugs = Spellchecker.getSuggestions(categoryName);
            if (categorySugs != null) categoryNames.addAll(categorySugs);
        }

        List<String> keywordsUpdated = new ArrayList<>();
        if (keywords != null) {
            keywordsUpdated = new ArrayList<>(Arrays.asList(keywords));
            for (String keyword: keywords) {
                List<String> keywordSugs = Spellchecker.getSuggestions(keyword);
                if (keywordSugs != null) keywordsUpdated.addAll(keywordSugs);
            }
        }


        for (Store store: stores.values())
            if (store.getRating() >= minStoreRating) allProducts.addAll(store.getProducts());

        for (ProductInStore pis: allProducts) {
            ProductInfo info = pis.getProductInfo();
            if (productName != null)
                if (productNames.contains(info.getName())) {
                    filteredProducts.add(pis);
                    continue;
                }
            if (categoryName != null)
                if (categoryNames.contains(info.getCategory())) {
                    filteredProducts.add(pis);
                    continue;
                }
            if (minItemRating != -1) {
                if (info.getRating()>= minItemRating) {
                    filteredProducts.add(pis);
                }
            }

            if (keywords != null) {
                for (String keyword: keywordsUpdated) {
                    if (pis.toString().contains(keyword))
                        filteredProducts.add(pis);
                }

            }
        }



        return new ProductsActionResultDTO(ResultCode.SUCCESS,"List of filterd products",getProductDTOlist(filteredProducts));
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
    public UserPurchaseHistoryDTO getUserHistory(int subId){
        logger.info("getUserHistory: SubscriberId "+subId);
            Subscriber subscriber = userHandler.getSubscriber(subId);
            if (subscriber != null) {
                UserPurchaseHistory history = subscriber.getHistory();
                return new UserPurchaseHistoryDTO(ResultCode.SUCCESS,"user history",getHistoryMap(history));

            }
            return new UserPurchaseHistoryDTO(ResultCode.ERROR_SUBID,"Invalid subscriber Id",null);

    }

    //Functions that turns Objects Into DTO's
    private Map<Integer,List<PurchaseDetailsDTO>> getHistoryMap(UserPurchaseHistory history){

        Map<Integer,List<PurchaseDetailsDTO>> historyMap = new HashMap<>();
        for(Store store : history.getStorePurchaseLists().keySet()) {
            List<PurchaseDetails> details = history.getStorePurchaseLists().get(store);

            historyMap.put(store.getId(),getPurchasesDto(details));
        }
        return historyMap;

    }
    private List<PurchaseDetailsDTO> getPurchasesDto(List<PurchaseDetails> details){
        List<PurchaseDetailsDTO> detailsDTOS = new ArrayList<>();
        for(PurchaseDetails purchaseDetails:details){
            List<ProductAmountDTO> productAmountDTOS = new ArrayList<>();
            for(ProductInfo pi : purchaseDetails.getProducts().keySet()){
                ProductInfoDTO pidto = new ProductInfoDTO(pi.getId(),pi.getName(),pi.getCategory(),pi.getRating());
                ProductAmountDTO paDTO = new ProductAmountDTO(pidto,purchaseDetails.getProducts().get(pi));
                productAmountDTOS.add(paDTO);
            }

            detailsDTOS.add(new PurchaseDetailsDTO(purchaseDetails.getId(),productAmountDTOS,purchaseDetails.getPrice()));
        }
        return detailsDTOS;

    }


    public Store getStoreById(int storeId){
       return stores.get(storeId);
    }

    private ActionResultDTO checkCartModificationDetails(int sessionId, int storeId, int productId, int amount) {
        if (userHandler.getUser(sessionId) == null) return new ActionResultDTO(ResultCode.ERROR_CART_MODIFICATION, "User does not exist.");
        Store store = getStoreById(storeId);
        if (store == null) return new ActionResultDTO(ResultCode.ERROR_CART_MODIFICATION, "Store " + storeId + " does not exist.");

        ProductInfo productInfo = getProductInfoById(productId);
        if (productInfo == null) return new ActionResultDTO(ResultCode.ERROR_CART_MODIFICATION, "Product " + productId + " does not exist.");

        if (amount < 1) return new ActionResultDTO(ResultCode.ERROR_CART_MODIFICATION, "Amount must be positive.");
        return new ActionResultDTO(ResultCode.SUCCESS, null);
    }

    public ActionResultDTO addToCart(int sessionId, int storeId, int productId, int amount){
        logger.info("addToCart: sessionId " + sessionId + ", storeId " + storeId + ", productId " + productId + ", amount " + amount);
        ActionResultDTO result = checkCartModificationDetails(sessionId, storeId, productId, amount);
        if (result.getResultCode() == ResultCode.ERROR_CART_MODIFICATION) return result;

        User u = userHandler.getUser(sessionId);
        Store store = getStoreById(storeId);
        result = u.addProductToCart(store, getProductInfoById(productId), amount);
        if (result.getResultCode() == ResultCode.SUCCESS) result.setDetails("Added " + amount + " instances of product " + getProductInfoById(productId).getName() + " (" + productId + ") for store " + storeId);
        return result;
    }

    public ActionResultDTO updateAmount(int sessionId, int storeId, int productId, int amount) {
        logger.info("updateAmount: sessionId " + sessionId + ", storeId " + storeId + ", productId " + productId + ", amount " + amount);
        ActionResultDTO result = checkCartModificationDetails(sessionId, storeId, productId, amount);
        if (result.getResultCode() == ResultCode.ERROR_CART_MODIFICATION) return result;

        User u = userHandler.getUser(sessionId);
        Store store = getStoreById(storeId);
        result = u.editCartProductAmount(store, getProductInfoById(productId), amount);
        if (result.getResultCode() == ResultCode.SUCCESS) result.setDetails("There are now " + amount + " instances of product " + getProductInfoById(productId).getName() + " (" + productId + ") for store " + storeId);
        return result;
    }

    public ActionResultDTO deleteItemInCart(int sessionId, int storeId, int productId) {
        logger.info("deleteItemInCart: sessionId " + sessionId + ", storeId " + storeId + ", productId " + productId);
        ActionResultDTO result = checkCartModificationDetails(sessionId, storeId, productId, 5);
        if (result.getResultCode() == ResultCode.ERROR_CART_MODIFICATION) return result;

        User u = userHandler.getUser(sessionId);
        Store store = getStoreById(storeId);
        result = u.removeProductFromCart(store, getProductInfoById(productId));
        if (result.getResultCode() == ResultCode.SUCCESS) result.setDetails("Deleted product " + getProductInfoById(productId) + " from basket of store " + storeId);
        return result;
    }

    public ActionResultDTO clearCart(int sessionId) {
        logger.info("clearCart: sessionId " + sessionId);
        User u = userHandler.getUser(sessionId);
        return u.removeAllProductsFromCart();
    }

    /*public double buyCart(int sessionId) {
        User u = userHandler.getUser(sessionId);
        return u.purchaseCart();
    }*/

    public ShoppingCartDTO getCart(int sessionId) {
        logger.info("getCart: sessionId " + sessionId);
        User u = userHandler.getUser(sessionId);
        if (u!=null) {
            List<ShoppingBasketDTO> cart = new LinkedList<>();
            ShoppingCart userCart = u.getShoppingCart();
            for(ShoppingBasket basket: userCart.getBaskets()){
                Map<ProductInfo,Integer> productMapping = basket.getProducts();
                List<SimpProductAmountDTO> dtoProductMapping = new ArrayList<>();
                for(ProductInfo product: productMapping.keySet()) {
                    SimpProductAmountDTO simpProductAmountDTO = new SimpProductAmountDTO(product.getId(),product.getName(), productMapping.get(product));
                    dtoProductMapping.add(simpProductAmountDTO);
                }
                cart.add(new ShoppingBasketDTO(basket.getStoreId(),dtoProductMapping));

            }

            return new ShoppingCartDTO(ResultCode.SUCCESS,"View cart sucsess",cart);
        }
        return new ShoppingCartDTO(ResultCode.ERROR_SESSIONID,"Cant find SessionId",null);
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


    public void deleteStores() {
        stores.clear();
    }

    public Map<Integer, Store> getStores(){
        return stores;
    }

    public User getUser(int sessionId) {
        return userHandler.getUser(sessionId);
    }

    public ActionResultDTO addProductInfo(int id, String name, String category) {
        logger.info("addProductInfo: id " + id + ", name " + name + ", category " + category);
        ProductInfo productInfo = new ProductInfo(id, name, category);
        if (products.get(id) != null) {
            return new ActionResultDTO(ResultCode.ERROR_ADMIN,"Product "+id+" already Exists");
        }
        products.put(id,productInfo);
        return new ActionResultDTO(ResultCode.SUCCESS,"Product "+id+" added to store");
    }

    public void removeStoreProductSupplies(Integer storeId, Map<Integer, Integer> productIdAmountMap) {
        Store store = getStoreById(storeId);
        for (Integer productId : productIdAmountMap.keySet()) {
            store.removeProductAmount(productId, productIdAmountMap.get(productId));
        }
    }

    public ActionResultDTO changeBuyingPolicy(int storeId, String newPolicy){
        logger.info("changeBuyingPolicy: storeId " + storeId + ", newPolicy " + newPolicy);
        Store s = getStoreById(storeId);
        if(s != null){
            s.setBuyingPolicy(new BuyingPolicy(newPolicy));
            return new ActionResultDTO(ResultCode.SUCCESS, null);
        }
        return new ActionResultDTO(ResultCode.ERROR_STORE_BUYING_POLICY_CHANGE, "The specified store does not exist.");
    }

    public ActionResultDTO checkBuyingPolicy(int sessionId) {
        logger.info("checkBuyingPolicy: sessionId " + sessionId);
        User u = userHandler.getUser(sessionId);
        return u.getShoppingCart().checkBuyingPolicy();
    }

    public boolean isCartEmpty(int sessionId) {
        logger.info("isCartEmpty: sessionId " + sessionId);
        User u = userHandler.getUser(sessionId);
        return u.isCartEmpty();
    }

    public double checkSuppliesAndGetPrice(int sessionId) {
        logger.info("checkSuppliesAndGetPrice: sessionId " + sessionId);
        User u = userHandler.getUser(sessionId);
        return u.checkStoreSupplies() ? u.getShoppingCartPrice().getPrice() : -1.0;
    }

    public void savePurchaseHistory(int sessionId) {
        logger.info("savePurchaseHistory: sessionId " + sessionId);
        User u = userHandler.getUser(sessionId);
        u.saveCurrentCartAsPurchase();
    }

    public boolean updateStoreSupplies(int sessionId) {
        logger.info("updateStoreSupplies: sessionId " + sessionId);
        User u = userHandler.getUser(sessionId);
        boolean result =  u.updateStoreSupplies();
        if(result){
            //publisher update
            if(publisher != null)
                notifyStoreOwners(u.getStoresInCart());
        }
        return result;

    }

    private void notifyStoreOwners(List<Integer> storesInCart) {
        for(int storeId:storesInCart){
            List<Integer> managers = getStoreById(storeId).getAllManagers().stream().map(Subscriber::getId).collect(Collectors.toList());
            Notification notification = new Notification(notificationId++, "Somone Buy from store "+storeId);
            updateAllUsers(getStoreById(storeId).getAllManagers(),notification);
            publisher.notify(managers,notification);
        }
    }

    private void updateAllUsers(List<Subscriber> users, Notification message) {
        for(Subscriber subscriber : users){
            subscriber.setNotification(message);
        }
    }

    public void emptyCart(int sessionId) {
        logger.info("emptyCart: sessionId " + sessionId);
        User u = userHandler.getUser(sessionId);
        u.emptyCart();
    }

    public void saveOngoingPurchaseForUser(int sessionId) {
        logger.info("saveOngoingPurchaseForUser: sessionId " + sessionId);
        User u = userHandler.getUser(sessionId);
        ongoingPurchases.put(sessionId, u.getPrimitiveCartDetails());
    }

    public void removeOngoingPurchase(int sessionId) {
        logger.info("removeOngoingPurchase: sessionId " + sessionId);
        ongoingPurchases.remove(sessionId);
    }

    public Map<Integer, Map<Integer, Map<Integer, Integer>>> getOngoingPurchases() {
        return ongoingPurchases;
    }

    public boolean requestRefund(int sessionId) {
        logger.info("requestRefund: sessionId " + sessionId);
        return paymentHandler.requestRefund(sessionId, ongoingPurchases.get(sessionId));
    }

    public void restoreSupplies(int sessionId) {
        logger.info("restoreSupplies: sessionId " + sessionId);
        Map<Integer, Map<Integer, Integer>> details = ongoingPurchases.get(sessionId);
        for (Integer storeId : details.keySet()) {
            Map<Integer, Integer> productAmounts = details.get(storeId);
            Store store = getStoreById(storeId);
            for (Integer productId : productAmounts.keySet()) {
                store.setProductAmount(productId, productAmounts.get(productId) + store.getProductAmount(productId));
            }
        }
    }

    public void restoreHistories(int sessionId) {
        logger.info("restoreHistories: sessionId " + sessionId);

        // remove the last history item for both the store and the user
        User u = userHandler.getUser(sessionId);

        // user restore
        List<Integer> storeIds = new ArrayList<>(ongoingPurchases.get(sessionId).keySet());
        List<Store> stores = new ArrayList<>();
        for (Integer storeId : storeIds) {
            stores.add(getStoreById(storeId));
        }
        u.removeLastHistoryItem(stores); // pass the method the list of stores!

        // store restore
        for (Store store : stores) {
            store.removeLastHistoryItem();
        }
    }

    public void restoreCart(int sessionId) {
        logger.info("restoreCart: sessionId " + sessionId);
        User u = userHandler.getUser(sessionId);
        Map<Integer, Map<Integer, Integer>> storeProductAmounts = ongoingPurchases.get(sessionId);
        for (Integer storeId : storeProductAmounts.keySet()) {
            Store store = getStoreById(storeId);
            Map<Integer, Integer> productAmounts = storeProductAmounts.get(storeId);
            for (Integer productId : productAmounts.keySet()) {
                int amount = productAmounts.get(productId);
                u.addProductToCart(store, getProductInfoById(productId), amount);
            }
        }
    }

    public StoreActionResultDTO getStoreInfo(int storeId) {
        Store store = getStoreById(storeId);
        if(store == null){
            return new StoreActionResultDTO(ResultCode.ERROR_STOREID,"Store "+storeId+" Does not exists.",null);
        }

        List<StoreDTO> result = new ArrayList<>();
        result.add(new StoreDTO(store.getId(),store.getBuyingPolicy().toString(),
                store.getDiscountPolicy().toString(),getProductDTOlist(store.getProducts())));

        return new StoreActionResultDTO(ResultCode.SUCCESS,"List of stores:",result);

    }

    public SubscriberActionResultDTO getAllManagers(int storeId) {
        Store store = getStoreById(storeId);
        if(store==null)
            return new SubscriberActionResultDTO(ResultCode.ERROR_STOREID,"StoreId not exist",null);
        else{
            List<Subscriber> managers = store.getAllManagers();
            List<SubscriberDTO> mangersDTO = new ArrayList<>();
            for(Subscriber manager : managers){
                ManagerDTO managerDto = new ManagerDTO(manager.getId(),manager.getUsername(),manager.getPermissionType(storeId));
                mangersDTO.add(managerDto);
            }
            return new SubscriberActionResultDTO(ResultCode.SUCCESS,"got All Managers",mangersDTO);
        }
    }

    public SubscriberActionResultDTO getAllSubscribers() {
        List<Subscriber> subscribers = userHandler.getSubscribers();
        return new SubscriberActionResultDTO(ResultCode.SUCCESS,"got subscribers List",getSubsDtos(subscribers));

    }

    public void setPublisher(Publisher publisher) {

        this.publisher = publisher;
    }

    public void removeProduct(int productId) {
        products.remove(productId);
    }

    public void setProducts(Map<Integer, ProductInfo> products) {
        this.products = products;
    }

    public void removeNotification(int subId, int notificationId) {
        Subscriber subscriber = userHandler.getSubscriber(subId);
        if(subscriber!=null){
            subscriber.removeNotification(notificationId);
        }
    }

    //publisher update
    public void pullNotifications(int subId) {
        Subscriber subToLogin = userHandler.getSubscriber(subId);
        if (subToLogin != null) {
            Queue<Notification> notifications = subToLogin.getAllNotification();
            List<Integer> user = new ArrayList<>();
            user.add(subToLogin.getId());
            synchronized (notifications) {
                for(Notification notification:notifications){
                    publisher.notify(user,notification);
                }
            }
        }
    }

    public int addSimpleBuyingTypeBasketConstraint(int storeId, int productId, String minmax, int amount) {
        ProductInfo info;
        if (productId < 0) info = null;
        else {
            info = getProductInfoById(productId);
            // valid product id is entered but product doesn't exist - abort action
            if (info == null) return -1;
        }
        return getStoreById(storeId).addSimpleBuyingTypeBasketConstraint(info, minmax, amount);
    }

    public int addSimpleBuyingTypeUserConstraint(int storeId, String country) {
        return getStoreById(storeId).addSimpleBuyingTypeUserConstraint(country);
    }

    public int addSimpleBuyingTypeSystemConstraint(int storeId, int dayOfWeek) {
        return getStoreById(storeId).addSimpleBuyingTypeSystemConstraint(dayOfWeek);
    }

    public void removeBuyingTypeFromStore(int storeId, int buyingTypeID) {
        getStoreById(storeId).removeBuyingType(buyingTypeID);
    }

    public void removeAllBuyingTypes(int storeId) {
        getStoreById(storeId).removeAllBuyingTypes();
    }

    public IntActionResultDto addAdvancedBuyingType(int storeId, List<Integer> buyingTypeIDs, String logicalOperation) {
        return getStoreById(storeId).addAdvancedBuyingType(buyingTypeIDs, logicalOperation);
    }

    public BuyingPolicyActionResultDTO getBuyingPolicyDetails(int storeId) {
        return getStoreById(storeId).getBuyingPolicyDetails();
    }
}
