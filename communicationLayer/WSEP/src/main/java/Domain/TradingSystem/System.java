package Domain.TradingSystem;

import DTOs.*;
import DTOs.SimpleDTOS.*;
import Domain.Logger.SystemLogger;
import Domain.Security.Security;
import Domain.Spelling.Spellchecker;
import NotificationPublisher.Publisher;

import java.util.*;

public class System {

    private static System instance = null;


    private SupplyHandler supplyHandler;
    private PaymentHandler paymentHandler;
    private UserHandler userHandler;
    private List<Store> stores;
    private SystemLogger logger;
    private static List<ProductInfo> products;


    private Publisher publisher;

    // session id -> (store id -> (product id -> amount))
    private Map<Integer, Map<Integer, Map<Integer, Integer>>> ongoingPurchases = new HashMap<>();

    public System(){
        userHandler = new UserHandler();
        stores = new LinkedList<>();
        logger = new SystemLogger();
        products = new LinkedList<>();

        products.add(new ProductInfo(-1, "", ""));

        publisher = new Publisher();
    }

    public static System getInstance(){
        if (instance == null) instance = new System();
        return instance;
    }

    public static ProductInfo getProductInfoById(int id) {
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
                stores.add(newStore);
                //Publisher update
                publisher.addManager(newStore.getId(), ((Subscriber)u.getState()).getId());
                //
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
                    publisher.notifyStore(storeId);
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
                    publisher.notifyStore(storeId);
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
                    publisher.notifyStore(storeId);
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
                //Publisher addition
                publisher.addManager(s.getId(),((Subscriber)u.getState()).getId());
                //
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
                //Publisher Update
                publisher.addManager(store.getId(),newManager.getId());
                //
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
                        //Publisher update
                        publisher.deleteManager(store.getId(),managerToDelete.getId());
                        //
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
            StorePurchaseHistory history =  s.getStorePurchaseHistory();
            return new StorePurchaseHistoryDTO(ResultCode.SUCCESS,"Got store history",s.getId(),
                    getPurchasesDto(history.getPurchaseHistory()));
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
        for (Store store: stores) {
            result.add(new StoreDTO(store.getId(),store.getBuyingPolicy().toString(),
                    store.getDiscountPolicy().toString(),getProductDTOlist(store.getProducts())));
        }

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


        for (Store store: stores)
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
            Map<ProductInfoDTO,Integer> mapping = new HashMap<>();
            for(ProductInfo pi : purchaseDetails.getProducts().keySet()){
                ProductInfoDTO pidto = new ProductInfoDTO(pi.getId(),pi.getName(),pi.getCategory(),pi.getRating());
                mapping.put(pidto,purchaseDetails.getProducts().get(pi));
            }

            detailsDTOS.add(new PurchaseDetailsDTO(purchaseDetails.getId(),mapping,purchaseDetails.getPrice()));
        }
        return detailsDTOS;

    }


    public Store getStoreById(int storeId){
        for(Store s: stores){
            if (s.getId() == storeId){
                return s;
            }
        }
        return null;
    }

    private ActionResultDTO checkCartModificationDetails(int sessionId, int storeId, int productId, int amount) {
        if (userHandler.getUser(sessionId) == null) return new ActionResultDTO(ResultCode.ERROR_CART_MODIFICATION, "User does not exist.");
        boolean found = false;
        for (Store store : stores) {
            if (store.getId() == storeId) {
                found = true;
                break;
            }
        }
        if (!found) return new ActionResultDTO(ResultCode.ERROR_CART_MODIFICATION, "Store " + storeId + " does not exist.");

        found = false;
        for (ProductInfo product : products) {
            if (product.getId() == productId) {
                found = true;
                break;
            }
        }
        if (!found) return new ActionResultDTO(ResultCode.ERROR_CART_MODIFICATION, "Product " + productId + " does not exist.");

        if (amount < 1) return new ActionResultDTO(ResultCode.ERROR_CART_MODIFICATION, "Amount must be positive.");
        return new ActionResultDTO(ResultCode.SUCCESS, null);
    }

    public ActionResultDTO addToCart(int sessionId, int storeId, int productId, int amount){
        logger.info("addToCart: sessionId " + sessionId + ", storeId " + storeId + ", productId " + productId + ", amount " + amount);
        ActionResultDTO result = checkCartModificationDetails(sessionId, storeId, productId, amount);
        if (result.getResultCode() == ResultCode.ERROR_CART_MODIFICATION) return result;

        User u = userHandler.getUser(sessionId);
        Store store = getStoreById(storeId);
        result = u.addProductToCart(store, productId, amount);
        if (result.getResultCode() == ResultCode.SUCCESS) result.setDetails("Added " + amount + " instances of product " + getProductInfoById(productId).getName() + " (" + productId + ") for store " + storeId);
        return result;
    }

    public ActionResultDTO updateAmount(int sessionId, int storeId, int productId, int amount) {
        logger.info("updateAmount: sessionId " + sessionId + ", storeId " + storeId + ", productId " + productId + ", amount " + amount);
        ActionResultDTO result = checkCartModificationDetails(sessionId, storeId, productId, amount);
        if (result.getResultCode() == ResultCode.ERROR_CART_MODIFICATION) return result;

        User u = userHandler.getUser(sessionId);
        Store store = getStoreById(storeId);
        result = u.editCartProductAmount(store, productId, amount);
        if (result.getResultCode() == ResultCode.SUCCESS) result.setDetails("There are now " + amount + " instances of product " + getProductInfoById(productId).getName() + " (" + productId + ") for store " + storeId);
        return result;
    }

    public ActionResultDTO deleteItemInCart(int sessionId, int storeId, int productId) {
        logger.info("deleteItemInCart: sessionId " + sessionId + ", storeId " + storeId + ", productId " + productId);
        ActionResultDTO result = checkCartModificationDetails(sessionId, storeId, productId, 5);
        if (result.getResultCode() == ResultCode.ERROR_CART_MODIFICATION) return result;

        User u = userHandler.getUser(sessionId);
        Store store = getStoreById(storeId);
        result = u.removeProductFromCart(store, productId);
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
                Map<Integer,Integer> productMapping = basket.getProducts();
                Map<String,Integer> dtoProductMapping = new HashMap<>();
                for(Integer pid: productMapping.keySet()){
                    dtoProductMapping.put(getProductInfoById(pid).getName(),productMapping.get(pid));
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

    public List<Store> getStores(){
        return stores;
    }

    public User getUser(int sessionId) {
        return userHandler.getUser(sessionId);
    }

    public ProductInfo addProductInfo(int id, String name, String category) {
        logger.info("addProductInfo: id " + id + ", name " + name + ", category " + category);
        ProductInfo productInfo = new ProductInfo(id, name, category);
        products.add(productInfo);

        return productInfo;
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
        return u.checkStoreSupplies() ? u.getShoppingCartPrice() : -1.0;
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
            notifyStoreOwners(u.getStoresInCart());
        }
        return result;

    }

    private void notifyStoreOwners(List<Integer> storesInCart) {
        for(int storeId:storesInCart){
            publisher.notifyStore(storeId);
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
                u.addProductToCart(store, productId, amount);
            }
        }
    }
}
