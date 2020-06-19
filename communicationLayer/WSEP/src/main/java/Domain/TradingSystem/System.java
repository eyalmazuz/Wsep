package Domain.TradingSystem;

import DTOs.*;
import DTOs.SimpleDTOS.*;
import DataAccess.DAOManager;
import Domain.BGUExternalSystems.PaymentSystem;
import Domain.BGUExternalSystems.SupplySystem;
import DataAccess.DatabaseFetchException;
import Domain.Logger.SystemLogger;
import Domain.Security.Security;
import Domain.Spelling.Spellchecker;
import NotificationPublisher.Publisher;
import com.j256.ormlite.dao.DaoManager;
// import jdk.nashorn.internal.runtime.regexp.joni.exception.SyntaxException;
import com.j256.ormlite.jdbc.JdbcConnectionSource;

import java.io.File;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class System {

    private static System instance = null;
    private static int notificationId = 0;

    private DayStatistics dailyStats;
    private SupplyHandler supplyHandler;
    private PaymentHandler paymentHandler;
    private UserHandler userHandler;
    private Map<Integer,Store> stores;
    private SystemLogger logger;
    private Map<Integer,ProductInfo> products;

    private Publisher publisher;

    // session id -> (store id -> (product id -> amount))
    private Map<Integer, Map<Integer, Map<Integer, Integer>>> ongoingPurchases = new HashMap<>();

    public static boolean testing = false;
    private boolean init = false;

    public System() {
        String databaseName = testing? "trading_system_test" : "trading_system";
        DAOManager.init(databaseName, "root", "weloveshahaf");

        userHandler = new UserHandler();
        stores = new ConcurrentHashMap<>();
        logger = new SystemLogger();
        products = new ConcurrentHashMap<>();

        User.idCounter = DAOManager.getMaxSubscriberId() + 1;
        PurchaseDetails.nextPurchaseId = DAOManager.getMaxPurchaseDetailsId() + 1;
        BuyingPolicy.nextId = DAOManager.getMaxBuyingPolicyId() + 1;
        DiscountPolicy.nextId = DAOManager.getMaxDiscountPolicyId() + 1;

        dailyStats = DAOManager.getDayStatisticsByDay(LocalDate.now());

        if (dailyStats == null) {
            dailyStats = new DayStatistics(LocalDate.now());
            DAOManager.addDayStatistics(dailyStats);
        }
    }

    public static System getInstance(){
        if (instance == null) instance = new System();
        return instance;
    }

    public ProductInfo getProductInfoById(int id) throws DatabaseFetchException {
        ProductInfo info = products.get(id);
        if (info == null) {
            info = DAOManager.loadProductInfoById(id);
            if (info != null) products.put(info.getId(),info);
        }
        return info;
    }

    public Map<Integer, ProductInfo> getProducts() throws DatabaseFetchException {
        List<ProductInfo> productInfos = DAOManager.loadAllProductInfos();
        Map<Integer, ProductInfo> allProducts = new HashMap<>();
        for (ProductInfo info : productInfos) {
            allProducts.put(info.getId(), info);
        }
        return allProducts;
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
        supplyHandler = new SupplyHandler(config, new SupplySystem());
    }

    private void setPayment(String config) throws Exception {
        paymentHandler = new PaymentHandler(config, new PaymentSystem());
    }


    public ActionResultDTO setup(String supplyConfig, String paymentConfig , String filePath){

        logger.info("SETUP - supplyConfig = "+supplyConfig+", paymentConfig ="+paymentConfig+".");
        try {
            userHandler.setAdmin();
        } catch (DatabaseFetchException e) {
            return new ActionResultDTO(ResultCode.ERROR_DATABASE, "Could not contact database. Please try agin later.");
        }
        try {
            setSupply(supplyConfig);
            setPayment(paymentConfig);
            if(!filePath.equals(""))
                parseFile(filePath);
        }
        catch(Exception e){
            logger.error(e.getMessage());
            return new ActionResultDTO(ResultCode.ERROR_SETUP,"e.getMessage");
        }
//        instance = this;
        init =true;
        return new ActionResultDTO(ResultCode.SUCCESS,"Setup Succsess");
    }

    private void parseFile(String filePath) throws Exception {

        if (filePath!= null){
            int sessionId = startSession().getId();
            int storeId;
            File file = new File(filePath);
            Scanner fileScanner = new Scanner(file);
            while(fileScanner.hasNextLine()){
                String command = fileScanner.nextLine();
                String action = command.substring(0,command.indexOf("("));
                String[] args = command.substring(command.indexOf("(")+1,command.indexOf(")")).split(",");
                switch (action){
                    case "register":
                        register(sessionId,args[0],"123");
                        break;
                    case "login":
                        login(sessionId,args[0],"123");
                        break;
                    case "logout" :
                        logout(sessionId);
                        break;
                    case "set-admin":
                        setAdmin(args[0]);
                        break;
                    case "open-store":
                        if(getStoreByName(args[1]) == -1) {
                            login(sessionId, args[0], "123");
                            storeId = openStore(sessionId).getId();
                            setStoreName(storeId, args[1]);
                            logout(sessionId);
                        }
                        break;
                    case "appoint-manager":
                        //appoint-manager(<Manager-name>,<Store-name>,<New Manager name>,<Details>);
                        login(sessionId,args[0],"123");
                        storeId = getStoreByName(args[1]);
                        int managerId = userHandler.getSubscriberUser(args[2]).getId();
                        addStoreManager(sessionId,storeId,managerId);
                        setManagerDetalis(sessionId,managerId,storeId,args[3]);
                        logout(sessionId);
                        break;
                    case "add-product":
                        //add-product(<manager-name>,<store-name>,<product-name>,<amount>,<price>);
                        login(sessionId,args[0],"123");
                        storeId = getStoreByName(args[1]);
                        int productInfo;
                        ProductInfo pInfo  =DAOManager.loadProductInfoByName(args[2]);
                        if( pInfo == null)
                            productInfo = addProductInfo(-1,args[2],"",Integer.valueOf(args[4])).getId();
                        else
                            productInfo = pInfo.getId();
                        addProductToStore(sessionId,storeId,productInfo,Integer.valueOf(args[3]));
                        logout(sessionId);
                        break;

                    default:
                        throw new Exception("Wrong syntax");


                }
            }

        }
    }

    public int getStoreByName(String name) throws DatabaseFetchException {
        for(Integer storeId : stores.keySet()){
            if (stores.get(storeId).getName().equals(name)){
                return storeId;
            }
        }
        // not found in memory, look in database
        Store store = DAOManager.loadStoreByName(name);

        return store == null ? -1 : store.getId();
    }

    private void setStoreName(int storeId, String name) throws DatabaseFetchException {
        getStoreById(storeId).setName(name);
    }

    private void setAdmin(String username) throws DatabaseFetchException {
        Subscriber subscriber = userHandler.getSubscriberUser(username);
        if(subscriber!=null){
            subscriber.setAdmin();
        }
    }

    public IntActionResultDto startSession(){
        logger.info("startSession: no arguments");
        int sessionId =userHandler.createSession();
        updateStats(sessionId);

        return new IntActionResultDto(ResultCode.SUCCESS,"start session",sessionId);
    }

    private void updateStats(int sessionId) {
        if(!dailyStats.isToday()) {
            dailyStats = new DayStatistics(LocalDate.now());
            DAOManager.addDayStatistics(dailyStats);
        }
        User user = userHandler.getUser(sessionId);
        if(user.isGuest()){
            dailyStats.increaseGuest();
        }
        else{
            Subscriber subscriber = (Subscriber) user.getState();
            dailyStats.decreaseGuest();
            if(subscriber.isAdmin())
                dailyStats.increaseAdmin();
            else if(subscriber.isOwner())
                dailyStats.increaseOwner();
            else if(subscriber.isManager())
                dailyStats.increaseManager();
            else
                dailyStats.increaseRegular();
        }

        java.lang.System.out.println("notify admins on daily stats");

        notifyAdmins();
    }

    private void notifyAdmins() {
        DailyStatsDTO dto = new DailyStatsDTO(dailyStats.getDate(),dailyStats.getGuests(),
                dailyStats.getRegularSubs(),dailyStats.getManagersNotOwners(),
                dailyStats.getManagersOwners(),dailyStats.getAdmins());
        if(publisher!=null){
            java.lang.System.out.println("sending the notify message");
            publisher.notify("/statsUpdate/0",new ArrayList<>(),dto);
        }
    }

    public int addStore (){
        logger.info("AddStore");
        Store store = new Store();
        stores.put(store.getId(),store);
        DAOManager.addStore(store);
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
        if(u!=null){
            boolean result =u.logout();
            if(result){
                updateStats(sessionId);
                return true;
            }
            return false;

        }
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
                DAOManager.addStore(newStore);

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
            Map<Store, List<PurchaseDetails>> history = u.getState().getStorePurchaseLists();
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
    public ActionResultDTO addProductToStore(int sessionId,int storeId, int productId,int amount) {

        logger.info(String.format("SessionId %d Add %d of Product %d to Store %d", sessionId, amount, productId, storeId));
        ProductInfo info = null;
        try {
            info = getProductInfoById(productId);
        } catch (DatabaseFetchException e) {
            return new ActionResultDTO(ResultCode.ERROR_DATABASE, "Could not contact database. Please try again later.");
        }
        if(info != null) {

            Store store = null;
            try {
                store = getStoreById(storeId);
            } catch (DatabaseFetchException e) {
                return new ActionResultDTO(ResultCode.ERROR_DATABASE, "Could not contact database. Please try again later.");
            }


            if (store != null) {
                ActionResultDTO result = store.addProduct(info, amount);
                //Publisher Update
                if(result.getResultCode()==ResultCode.SUCCESS){
                    if(publisher != null) {
                        List<Integer> managers = store.getAllManagers().stream().map(Subscriber::getId).collect(Collectors.toList());
                        Notification notification = new Notification(notificationId++,"Store " + storeId + " has been updated");
                        updateAllUsers(store.getAllManagers(),notification);
                        publisher.notify("/storeUpdate/",managers,notification);
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
        try {
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
                            publisher.notify("/storeUpdate/", managers,notification);
                        }
                    }
                    return result;
                }
                return new ActionResultDTO(ResultCode.ERROR_STORE_PRODUCT_MODIFICATION, "No such store.");
            }
        } catch (DatabaseFetchException e) {
            return new ActionResultDTO(ResultCode.ERROR_DATABASE, "Could not contact database. Please try again later.");
        }
        return new ActionResultDTO(ResultCode.ERROR_STORE_PRODUCT_MODIFICATION, "No such product.");
    }

    //UseCase 4.1.3
    public ActionResultDTO deleteProductFromStore(int sessionId, int storeId, int productId){
        logger.info("deleteProductFromStore: sessionId: "+sessionId+", storeId: "+storeId + ", productId: " + productId );
        try {
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
                            publisher.notify("/storeUpdate/", managers,notification);
                        }
                    }
                    return result;
                }
                return new ActionResultDTO(ResultCode.ERROR_STORE_PRODUCT_MODIFICATION, "No such store.");
            }
        } catch (DatabaseFetchException e) {
            return new ActionResultDTO(ResultCode.ERROR_DATABASE, "Could not contact database. Please try again later.");
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

        Store store = null;
        try {
            store = getStoreById(storeId);
        } catch (DatabaseFetchException e) {
            return new SubscriberActionResultDTO(ResultCode.ERROR_DATABASE, "Could not contact database. Please try again later.", null);
        }
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
        logger.info("addStoreOwner: sessionId: "+sessionId+", storeId: "+storeId+", subId: "+subId );
        User u = userHandler.getUser(sessionId);
        Subscriber owner = (Subscriber)u.getState();
        Subscriber newOwner = userHandler.getSubscriber(subId);
        if (newOwner == null)
            return new ActionResultDTO(ResultCode.ERROR_STORE_OWNER_MODIFICATION, "Specified new owner does not exist.");
        Store store = null;
        try {
            store = getStoreById(storeId);
        } catch (DatabaseFetchException e) {
            return new ActionResultDTO(ResultCode.ERROR_DATABASE, "Could not contact database. Please try again later.");
        }
        if(store!=null)
        {
            List<Integer> owners = store.getOwners().stream().map(Subscriber::getId).collect(Collectors.toList());
            GrantingAgreement agreement = new GrantingAgreement(storeId,owner.getId(),newOwner.getId(),owners);
            if(agreement.allAproved()){
                if (setStoreOwner(owner, newOwner, store))
                    return new ActionResultDTO(ResultCode.SUCCESS, "Owner was added");
            }
            else{
                if(store.addAgreement(agreement)) {
                    notifyAndUpdate(owners, "New Malshab Waits to your approval in store " + storeId);
                    return new ActionResultDTO(ResultCode.SUCCESS, "Granting agreement has been created");
                }
                else {
                    return new ActionResultDTO(ResultCode.ERROR_STORE_OWNER_MODIFICATION, "user have already pending agreemant");
                }
            }

        }


        return new ActionResultDTO(ResultCode.ERROR_STORE_OWNER_MODIFICATION, "Specified store does not exist.");
    }

    private boolean setStoreOwner( Subscriber owner, Subscriber newOwner, Store store) {
        if(newOwner.addPermission(store, owner, "Owner")){
            store.addOwner(newOwner);

            List<Integer> id = new ArrayList<>();
            id.add(newOwner.getId());
            if(publisher!=null) {
                notifyAndUpdate(id,"You are now store owner of store " + store.getId());
            }
            return true;
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


    public ActionResultDTO addStoreManager (int sessionId, int storeId, int userId) {
        User u = userHandler.getUser(sessionId);
        Subscriber subscriber = (Subscriber)u.getState();
        if(!(subscriber.hasOwnerPermission(storeId) || subscriber.hasManagerPermission(storeId)))
            return new ActionResultDTO(ResultCode.ERROR_STORE_MANAGER_MODIFICATION, "grantor is not manager on store! OMG!.");

        logger.info("addStoreManager: sessionId: "+sessionId+", storeId: "+storeId+", userId: "+userId );
        Subscriber newManager = userHandler.getSubscriber(userId);
        if (newManager == null)
            return new ActionResultDTO(ResultCode.ERROR_STORE_MANAGER_MODIFICATION, "The specified user is invalid.");
        Store store = null;
        try {
            store = getStoreById(storeId);
        } catch (DatabaseFetchException e) {
            return new ActionResultDTO(ResultCode.ERROR_DATABASE, "Could not contact database. Please try again later.");
        }
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

                if(!managerToDelete.isGrantedBy(storeId, subscriber.getId())){
                    return new ActionResultDTO(ResultCode.ERROR_STORE_MANAGER_MODIFICATION, "cannot remove manager who wasn't granted by you");
                }

                if (managerToDelete != null && !subscriber.equals(managerToDelete)) {

                    managerToDelete.removeManagment(storeId);
                    List<Integer> id = new ArrayList<>();

                    id.add(managerToDelete.getId());
                    if(publisher!= null)
                        notifyAndUpdate(id,"You got deleted from store "+storeId);

                    return new ActionResultDTO(ResultCode.SUCCESS, null);

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
    public ArrayList<String> getManagerDetails(int sessionId, int managerId, int storeId){
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
            Store store = null;
            try {
                store = getStoreById(storeId);
            } catch (DatabaseFetchException e) {
                return new ActionResultDTO(ResultCode.ERROR_DATABASE, "Could not contact database. Please try again later.");
            }
            if(store!=null) {

                String[] validDetailes = {"any", "add product", "edit product", "delete product","manage-inventory"};

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
        Store s = null;
        try {
            s = getStoreById(storeId);
        } catch (DatabaseFetchException e) {
            return new StorePurchaseHistoryDTO(ResultCode.ERROR_DATABASE, "Could not contact database. Please try again later.", -1, null);
        }
        if(s!=null){
            List<PurchaseDetails> history =  s.getStorePurchaseHistory();
            return new StorePurchaseHistoryDTO(ResultCode.SUCCESS,"Got store history",s.getId(),
                    getPurchasesDto(history));
        }
        return new StorePurchaseHistoryDTO(ResultCode.ERROR_STOREHISTORY,"Illeagal Store Id",-1,null);
    }

    // usecase 2.8.3
    public IntActionResultDto makePayment(int sessionId, String cardNumber, String expirationMonth, String expirationYear, String holder, String ccv, String cardId) {
        // retrieve store product ids
        User u = userHandler.getUser(sessionId);
        IntActionResultDto result = paymentHandler.makePayment(cardNumber, expirationMonth, expirationYear, holder, ccv, cardId);
        logger.info("makePayment: sessionId " + sessionId + ", status: " + (result.getResultCode() == ResultCode.SUCCESS ? "SUCCESS" : "FAIL"));
        return result;
    }

    // usecase 2.8.4
    public IntActionResultDto requestSupply(int sessionId, String buyerName, String address, String city, String country, String zip) {
        IntActionResultDto result = supplyHandler.requestSupply(buyerName, address, city, country, zip);
        boolean success = result.getResultCode() == ResultCode.SUCCESS;
        logger.info("requestSupply: sessionId " + sessionId + ", status: " + (success ? "SUCCESS" : "FAIL"));
        return result;
    }

    public boolean cancelSupply(int sessionId, int transactionId) {
        boolean success = supplyHandler.cancelSupply(transactionId).getResultCode() == ResultCode.SUCCESS;
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
            DAOManager.createOrUpdateShoppingCart(u.getShoppingCart());
            return new IntActionResultDto(ResultCode.SUCCESS, "Register Success", subId);
        }
        return new IntActionResultDto(ResultCode.ERROR_REGISTER,"Session id not exist",-1);
    }

    // Usecase 2.3
    public boolean login(int sessionId, String username, String password) throws DatabaseFetchException {
        logger.info(String.format("Login : SeesionId %d , username %s",sessionId,username));
        User u = userHandler.getUser(sessionId);
        if (!u.isGuest()) return false;

        Subscriber subToLogin = userHandler.getSubscriberUser(username, Security.getHash(password));

        if (subToLogin != null) {

            userHandler.subscribers.put(subToLogin.getId(), subToLogin);
            u.setState(subToLogin);
            setState(sessionId, subToLogin.getId());

            // fix user shopping cart
            ShoppingCart cart = subToLogin.getShoppingCart();
            cart.setUser(u);

            updateStats(sessionId);

            return true;
        }

        return false;
    }

    // Usecase 2.4
    public StoreActionResultDTO viewStoreProductInfo(){
        logger.info("searchProducts: no arguments");
        List<StoreDTO> result = new ArrayList<>();
        String info = "";
        try {
            for (Store store : DAOManager.loadAllStores()) {
                result.add(new StoreDTO(store.getId(), store.getBuyingPolicy().toString(),
                        store.getDiscountPolicy().toString(), getProductDTOlist(store.getProducts())));
            }
            Collections.sort(result, (i, j) -> i.getStoreId() < j.getStoreId() ? -1 : 1);
        }catch(DatabaseFetchException e){
            return new StoreActionResultDTO(ResultCode.ERROR_DATABASE, "coult not connet to database", null);
        }
        return new StoreActionResultDTO(ResultCode.SUCCESS,"List of stores:",result);
    }

    private List<ProductInStoreDTO> getProductDTOlist(List<ProductInStore> products) {
        List<ProductInStoreDTO> result = new ArrayList<>();
        for (ProductInStore pis: products) {
            result.add(new ProductInStoreDTO(pis.getProductInfoId(),
                    pis.getProductInfo().getName(),
                    pis.getProductInfo().getCategory(),
                    pis.getAmount(),
                    pis.getInfo(),
                    pis.getStore().getId(),
                    pis.getPrice()));
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
        if (!productName.equals("")) {
            List<String> productSugs = Spellchecker.getSuggestions(productName);
            if (productSugs != null) productNames.addAll(productSugs);
        }

        List<String> categoryNames = new ArrayList<>();
        categoryNames.add(categoryName);
        if (!categoryName.equals("")) {
            List<String> categorySugs = Spellchecker.getSuggestions(categoryName);
            if (categorySugs != null) categoryNames.addAll(categorySugs);
        }

        List<String> keywordsUpdated = new ArrayList<>();
        if (!keywords.equals("")) {
            keywordsUpdated = new ArrayList<>(Arrays.asList(keywords));
            for (String keyword: keywords) {
                List<String> keywordSugs = Spellchecker.getSuggestions(keyword);
                if (keywordSugs != null) keywordsUpdated.addAll(keywordSugs);
            }
        }

        try {
            for (Store store : DAOManager.loadAllStores())
                if (store.getRating() >= minStoreRating) allProducts.addAll(store.getProducts());
        }catch (DatabaseFetchException e){

        }
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
            Map<Store, List<PurchaseDetails>> history = subscriber.getStorePurchaseLists();
            return new UserPurchaseHistoryDTO(ResultCode.SUCCESS,"user history", getHistoryMap(history));

        }
        return new UserPurchaseHistoryDTO(ResultCode.ERROR_SUBID,"Invalid subscriber Id",null);

    }

    //Functions that turns Objects Into DTO's
    private Map<Integer,List<PurchaseDetailsDTO>> getHistoryMap(Map<Store, List<PurchaseDetails>> history){

        Map<Integer,List<PurchaseDetailsDTO>> historyMap = new HashMap<>();
        for(Store store : history.keySet()) {
            List<PurchaseDetails> details = history.get(store);

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
                ProductAmountDTO paDTO = new ProductAmountDTO(pidto,purchaseDetails.getProducts().get(pi), pi.getDefaultPrice());
                productAmountDTOS.add(paDTO);
            }

            detailsDTOS.add(new PurchaseDetailsDTO(purchaseDetails.getId(),productAmountDTOS,purchaseDetails.getPrice()));
        }
        return detailsDTOS;

    }


    public Store getStoreById(int storeId) throws DatabaseFetchException {
        Store store = stores.get(storeId);
        if (store == null) store = DAOManager.loadStoreById(storeId);
        return store;
    }

    private ActionResultDTO checkCartModificationDetails(int sessionId, int storeId, int productId, int amount) {
        if (userHandler.getUser(sessionId) == null) return new ActionResultDTO(ResultCode.ERROR_CART_MODIFICATION, "User does not exist.");
        Store store = null;
        try {
            store = getStoreById(storeId);
        } catch (DatabaseFetchException e) {
            return new ActionResultDTO(ResultCode.ERROR_DATABASE, "Could not contact database. Please try again later.");
        }
        if (store == null) return new ActionResultDTO(ResultCode.ERROR_CART_MODIFICATION, "Store " + storeId + " does not exist.");

        ProductInfo productInfo = null;
        try {
            productInfo = getProductInfoById(productId);
        } catch (DatabaseFetchException e) {
            return new ActionResultDTO(ResultCode.ERROR_DATABASE, "Could not contact database. Please try again later.");
        }


        if (productInfo == null) return new ActionResultDTO(ResultCode.ERROR_CART_MODIFICATION, "Product " + productId + " does not exist.");

        if (amount < 1) return new ActionResultDTO(ResultCode.ERROR_CART_MODIFICATION, "Amount must be positive.");
        return new ActionResultDTO(ResultCode.SUCCESS, null);
    }

    public ActionResultDTO addToCart(int sessionId, int storeId, int productId, int amount){
        logger.info("addToCart: sessionId " + sessionId + ", storeId " + storeId + ", productId " + productId + ", amount " + amount);
        ActionResultDTO result = checkCartModificationDetails(sessionId, storeId, productId, amount);
        if (result.getResultCode() == ResultCode.ERROR_CART_MODIFICATION) return result;

        User u = userHandler.getUser(sessionId);
        Store store = null;
        try {
            store = getStoreById(storeId);
            result = u.addProductToCart(store, getProductInfoById(productId), amount);
            if (result.getResultCode() == ResultCode.SUCCESS) result.setDetails("Added " + amount + " instances of product " + getProductInfoById(productId).getName() + " (" + productId + ") for store " + storeId);
        } catch (DatabaseFetchException e) {
            result = new ActionResultDTO(ResultCode.ERROR_DATABASE, "Could not contact database. Please try again later.");
        }
        return result;
    }

    public ActionResultDTO updateAmount(int sessionId, int storeId, int productId, int amount) {
        logger.info("updateAmount: sessionId " + sessionId + ", storeId " + storeId + ", productId " + productId + ", amount " + amount);
        ActionResultDTO result = checkCartModificationDetails(sessionId, storeId, productId, amount);
        if (result.getResultCode() == ResultCode.ERROR_CART_MODIFICATION) return result;

        User u = userHandler.getUser(sessionId);
        Store store = null;
        try {
            store = getStoreById(storeId);
            result = u.editCartProductAmount(store, getProductInfoById(productId), amount);
            if (result.getResultCode() == ResultCode.SUCCESS) result.setDetails("There are now " + amount + " instances of product " + getProductInfoById(productId).getName() + " (" + productId + ") for store " + storeId);
        } catch (DatabaseFetchException e) {
            result = new ActionResultDTO(ResultCode.ERROR_DATABASE, "Could not contact database. Please try again later.");
        }
        return result;
    }

    public ActionResultDTO deleteItemInCart(int sessionId, int storeId, int productId) {
        logger.info("deleteItemInCart: sessionId " + sessionId + ", storeId " + storeId + ", productId " + productId);
        ActionResultDTO result = checkCartModificationDetails(sessionId, storeId, productId, 5);
        if (result.getResultCode() == ResultCode.ERROR_CART_MODIFICATION) return result;

        User u = userHandler.getUser(sessionId);
        Store store = null;
        try {
            store = getStoreById(storeId);
            result = u.removeProductFromCart(store, getProductInfoById(productId));
            if (result.getResultCode() == ResultCode.SUCCESS) result.setDetails("Deleted product " + getProductInfoById(productId) + " from basket of store " + storeId);
        } catch (DatabaseFetchException e) {
            result = new ActionResultDTO(ResultCode.ERROR_DATABASE, "Could not contact database. Please try again later.");
        }
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
                    SimpProductAmountDTO simpProductAmountDTO = new SimpProductAmountDTO(product.getId(),product.getName(), productMapping.get(product), basket.getProductPrice(product.getId()));
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

    public int getSubscriber(String username, String password) throws DatabaseFetchException {
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

    public Map<Integer, Store> getStores() throws DatabaseFetchException {
        List<Store> stores = DAOManager.loadAllStores();
        Map<Integer, Store> map = new HashMap<>();
        for (Store store : stores) map.put(store.getId(), store);
        return map;
    }

    public User getUser(int sessionId) {
        return userHandler.getUser(sessionId);
    }

    public IntActionResultDto addProductInfo(int id, String name, String category, double basePrice) {
        if(id<0){
            id = DAOManager.getMaxProductInfoId()+1;
        }
        logger.info("addProductInfo: id " + id + ", name " + name + ", category " + category);
        ProductInfo productInfo = new ProductInfo(id, name, category, basePrice);
        try {
            if (getProductInfoById(id) != null) {
                return new IntActionResultDto(ResultCode.ERROR_ADMIN,"Product "+id+" already Exists",-1);
            }
        } catch (DatabaseFetchException e) {
            return new IntActionResultDto(ResultCode.ERROR_DATABASE, "Could not contact database. Please try again later.", -1);
        }

        products.put(id,productInfo);
        DAOManager.createProductInfo(productInfo);
        return new IntActionResultDto(ResultCode.SUCCESS,"Product "+id+" added to system",id);
    }



    public void removeStoreProductSupplies(Integer storeId, Map<Integer, Integer> productIdAmountMap) throws DatabaseFetchException {
        Store store = getStoreById(storeId);
        for (Integer productId : productIdAmountMap.keySet()) {
            store.removeProductAmount(productId, productIdAmountMap.get(productId));
        }
    }

    public ActionResultDTO changeBuyingPolicy(int storeId, String newPolicy){
        logger.info("changeBuyingPolicy: storeId " + storeId + ", newPolicy " + newPolicy);
        Store s = null;
        try {
            s = getStoreById(storeId);
        } catch (DatabaseFetchException e) {
            return new ActionResultDTO(ResultCode.ERROR_DATABASE, "Could not contact database. Please try again later.");
        }
        if(s != null){
            s.setBuyingPolicy(new BuyingPolicy(newPolicy));
            return new ActionResultDTO(ResultCode.SUCCESS, null);
        }
        return new ActionResultDTO(ResultCode.ERROR_STORE_BUYING_POLICY_CHANGE, "The specified store does not exist.");
    }

    public ActionResultDTO changeDiscountPolicy(int storeId, String newPolicy){
        logger.info("changeDiscountPolicy: storeId " + storeId + ", newPolicy " + newPolicy);
        Store s = null;
        try {
            s = getStoreById(storeId);
        } catch (DatabaseFetchException e) {
            return new ActionResultDTO(ResultCode.ERROR_DATABASE, "Could not contact database. Please try again later.");
        }
        if(s != null){
            s.setDiscountPolicy(new DiscountPolicy(newPolicy));
            return new ActionResultDTO(ResultCode.SUCCESS, null);
        }
        return new ActionResultDTO(ResultCode.ERROR_STORE_DISCOUNT_POLICY_CHANGE, "The specified store does not exist.");
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

    public boolean updateStoreSupplies(int sessionId) throws DatabaseFetchException {
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

    private void notifyStoreOwners(List<Integer> storesInCart) throws DatabaseFetchException {
        for(int storeId:storesInCart){
            List<Integer> managers = getStoreById(storeId).getAllManagers().stream().map(Subscriber::getId).collect(Collectors.toList());
            Notification notification = new Notification(notificationId++, "Someone Buy from store "+storeId);
            updateAllUsers(getStoreById(storeId).getAllManagers(),notification);
            if(publisher!=null)
                publisher.notify("/storeUpdate/",managers,notification);
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

    public boolean requestRefund(int sessionId, int transactionId) {
        logger.info("requestRefund: sessionId " + sessionId + ", transactionId " + transactionId);
        return paymentHandler.requestRefund(transactionId).getResultCode() == ResultCode.SUCCESS;
    }

    public void restoreSupplies(int sessionId) throws DatabaseFetchException {
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

    public void restoreHistories(int sessionId) throws DatabaseFetchException {
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

    public void restoreCart(int sessionId) throws DatabaseFetchException {
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
        Store store = null;
        try {
            store = getStoreById(storeId);
        } catch (DatabaseFetchException e) {
            return new StoreActionResultDTO(ResultCode.ERROR_DATABASE, "Could not contact database. Please try again later.", null);
        }
        if(store == null){
            return new StoreActionResultDTO(ResultCode.ERROR_STOREID,"Store "+storeId+" Does not exists.",null);
        }

        List<StoreDTO> result = new ArrayList<>();
        result.add(new StoreDTO(store.getId(),store.getBuyingPolicy().toString(),
                store.getDiscountPolicy().toString(),getProductDTOlist(store.getProducts())));

        return new StoreActionResultDTO(ResultCode.SUCCESS,"List of stores:",result);

    }

    public SubscriberActionResultDTO getAllManagers(int storeId) {
        Store store = null;
        try {
            store = getStoreById(storeId);
        } catch (DatabaseFetchException e) {
            return new SubscriberActionResultDTO(ResultCode.ERROR_DATABASE, "Could not contact database. Please try again later.", null);
        }
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
        if (products.containsKey(productId)) products.remove(productId);
        DAOManager.removeProductInfo(productId);
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
            ArrayList<Notification> notifications = subToLogin.getAllNotification();
            List<Integer> user = new ArrayList<>();
            user.add(subToLogin.getId());
            synchronized (notifications) {
                for(Notification notification:notifications){
                    publisher.notify("/storeUpdate/",user,notification);
                }
            }
        }
    }

    public int addSimpleBuyingTypeBasketConstraint(int storeId, int productId, String minmax, int amount) throws DatabaseFetchException {
        ProductInfo info;
        if (productId < 0) info = null;
        else {
            info = getProductInfoById(productId);
            // valid product id is entered but product doesn't exist - abort action
            if (info == null) return -1;
        }
        return getStoreById(storeId).addSimpleBuyingTypeBasketConstraint(info, minmax, amount);
    }

    public int addSimpleBuyingTypeUserConstraint(int storeId, String country) throws DatabaseFetchException {
        return getStoreById(storeId).addSimpleBuyingTypeUserConstraint(country);
    }

    public int addSimpleBuyingTypeSystemConstraint(int storeId, int dayOfWeek) throws DatabaseFetchException {
        return getStoreById(storeId).addSimpleBuyingTypeSystemConstraint(dayOfWeek);
    }

    public void removeBuyingTypeFromStore(int storeId, int buyingTypeID) throws DatabaseFetchException {
        getStoreById(storeId).removeBuyingType(buyingTypeID);
    }

    public void removeAllBuyingTypes(int storeId) throws DatabaseFetchException {
        getStoreById(storeId).removeAllBuyingTypes();
    }

    public IntActionResultDto addAdvancedBuyingType(int storeId, List<Integer> buyingTypeIDs, String logicalOperation) {
        try {
            return getStoreById(storeId).addAdvancedBuyingType(buyingTypeIDs, logicalOperation);
        } catch (DatabaseFetchException e) {
            return new IntActionResultDto(ResultCode.ERROR_DATABASE, "Could not contact database. Please try again later.", -1);
        }
    }

    public BuyingPolicyActionResultDTO getBuyingPolicyDetails(int storeId) {
        try {
            return getStoreById(storeId).getBuyingPolicyDetails();
        } catch (DatabaseFetchException e) {
            return new BuyingPolicyActionResultDTO(ResultCode.ERROR_DATABASE, "Could not contact database. Please try again later.", null);
        }
    }

    public DiscountPolicyActionResultDTO getDiscountPolicyDetails(int storeId) {
        try {
            return getStoreById(storeId).getDiscountPolicyDetails();
        } catch (DatabaseFetchException e) {
            return new DiscountPolicyActionResultDTO(ResultCode.ERROR_DATABASE, "Could not contact database. Please try again later.", null);
        }
    }

    public ActionResultDTO changeProductPrice(int storeId, int productId, double price) {
        Store store = null;
        ProductInfo info = null;
        try {
            store = getStoreById(storeId);
            if (store == null) return new ActionResultDTO(ResultCode.ERROR_CHANGE_PRODUCT_PRICE, "No such store");
            info = getProductInfoById(productId);
        } catch (DatabaseFetchException e) {
            return new ActionResultDTO(ResultCode.ERROR_DATABASE, "Could not contact database. Please try again later.");
        }
        if (!store.hasProduct(info)) return new ActionResultDTO(ResultCode.ERROR_CHANGE_PRODUCT_PRICE, "No such product in the store");
        if (price < 0) return new ActionResultDTO(ResultCode.ERROR_CHANGE_PRODUCT_PRICE, "Invalid price. Must be non-negative.");
        store.setProductPrice(info.getId(), price);

        return new ActionResultDTO(ResultCode.SUCCESS, "Changed price of " + info.getName() + " (" + info.getId() + ") to " + price);
    }

    public PermissionActionResultDTO getPermission(int subId, int storeId) {
        Subscriber s = userHandler.getSubscriber(subId);
        if(s == null){
            return new PermissionActionResultDTO(ResultCode.ERROR_SUBID,"subid "+ subId+"Not exist!",null);
        }
        Permission permission = s.getPermission(storeId);
        if ( permission == null){
            return new PermissionActionResultDTO(ResultCode.ERROR_STOREID,"permission in store "+ storeId+"Not exist!",null);
        }
        PermissionDTO permissionDTO = getPermissionDTO(permission);
        return new PermissionActionResultDTO(ResultCode.SUCCESS,"got user permissions",permissionDTO);
    }

    private PermissionDTO getPermissionDTO(Permission permission) {
        SubscriberDTO user  = null;
        SubscriberDTO grantor = null;
        if(permission.getGrantor() != null){
            grantor = new SubscriberDTO(permission.getGrantor().getId(),permission.getGrantor().getUsername());
        }
        user = new SubscriberDTO(permission.getUser().getId(),permission.getUser().getUsername());

        return new PermissionDTO(permission.getStore().getId(),user,grantor,permission.getType(),permission.getDetails());

    }

    public void removeDiscountTypeFromStore(int storeId, int discountTypeID) throws DatabaseFetchException {
        getStoreById(storeId).removeDiscountType(discountTypeID);
    }

    public void removeAllDiscountTypes(int storeId) throws DatabaseFetchException {
        getStoreById(storeId).removeAllDiscountTypes();
    }

    public int addSimpleProductDiscount(int storeId, int productId, double salePercentage) throws DatabaseFetchException {
        return getStoreById(storeId).addSimpleProductDiscount(productId, salePercentage);
    }

    public int addSimpleCategoryDiscount(int storeId, String categoryName, double salePercentage) throws DatabaseFetchException {
        return getStoreById(storeId).addSimpleCategoryDiscount(categoryName, salePercentage);
    }

    public IntActionResultDto addAdvancedDiscountType(int storeId, List<Integer> discountTypeIDs, String logicalOperation) {
        try {
            return getStoreById(storeId).addAdvancedDiscountType(discountTypeIDs, logicalOperation);
        } catch (DatabaseFetchException e) {
            return new IntActionResultDto(ResultCode.ERROR_DATABASE, "Could not contact database. Please try again later.", -1);
        }
    }

    public void clearDatabase() {
        DAOManager.clearDatabase();
    }

    public ActionResultDTO deleteOwner(int sessionId, int storeId, int userId) {
        logger.info("deleteOwner: sessionId: "+sessionId+", storeId: "+storeId+", userId: "+userId );
        User u = userHandler.getUser(sessionId);

        if(isSubscriber(sessionId)) {
            Subscriber subscriber = (Subscriber) u.getState();

            if (u != null) {
                Subscriber ownerToDelete = userHandler.getSubscriber(userId);
                if (ownerToDelete != null && !subscriber.equals(ownerToDelete)) {
                    if (ownerToDelete.isGrantedBy(storeId, subscriber.getId())) {

                        List<Integer> allRemoved = ownerToDelete.removeOwnership(storeId);
                        if(publisher!=null)
                            notifyAndUpdate(allRemoved,"Your management In store "+storeId+" has been ended.");
                        try {
                            handleGrantingAgreements(storeId);
                        } catch (DatabaseFetchException e) {
                            return new ActionResultDTO(ResultCode.ERROR_DATABASE, "Could not contact database. Please try again later.");
                        }
                        return  new ActionResultDTO(ResultCode.SUCCESS,"Managers were removed");
                    } else {
                        return new ActionResultDTO(ResultCode.ERROR_DELETE, "Cannot delete owner that is not granted by you");
                    }
                }
                return new ActionResultDTO(ResultCode.ERROR_STORE_MANAGER_MODIFICATION, "The specified manager must exist and not be yourself.");
            }
        }
        return new ActionResultDTO(ResultCode.ERROR_STORE_MANAGER_MODIFICATION, "Invalid user.");
    }

    /**
     * check all granting agreements and if any of them is aprroved finish him.
     * @param storeId
     */
    private void handleGrantingAgreements(int storeId) throws DatabaseFetchException {
        Store store = getStoreById(storeId);
        if(store != null){
            Collection<GrantingAgreement> agreements = store.getAllGrantingAgreements();
            for (GrantingAgreement agreement : agreements){
                if (agreement.allAproved()){
                    Subscriber grantor = userHandler.getSubscriber(agreement.getGrantorId());
                    Subscriber newOwner = userHandler.getSubscriber(agreement.getMalshabId());
                    if (setStoreOwner(grantor, newOwner, store)) {
                        store.removeAgreement(agreement.getMalshabId());
                    }

                }
            }
        }
    }

    private void notifyAndUpdate(List<Integer> users, String message){
        Notification notification = new Notification(notificationId++,message);
        for(Integer subId : users){
            Subscriber s = userHandler.getSubscriber(subId);
            if(s!=null){
                s.setNotification(notification);
            }
        }
        if(publisher!=null)
            publisher.notify("/storeUpdate/",users,message);
    }

    public ActionResultDTO approveStoreOwner(int sessionId, int storeId, int subId) {
        Subscriber owner = (Subscriber) userHandler.getUser(sessionId).getState();
        Subscriber newOwner = userHandler.getSubscriber(subId);
        Store store = null;
        try {
            store = getStoreById(storeId);
        } catch (DatabaseFetchException e) {
            return new ActionResultDTO(ResultCode.ERROR_DATABASE, "Could not contact database. Please try again later.");
        }
        if(store != null){
            if(store.approveMalshab(owner.getId(),subId)){
                if(store.allAproved(subId)){
                    int grantorId = store.getAgreementGrantor(subId);
                    if (setStoreOwner(userHandler.getSubscriber(grantorId), newOwner, store)) {
                        store.removeAgreement(subId);
                        return new ActionResultDTO(ResultCode.SUCCESS, "Owner was added");
                    }
                }
                else{
                    return new ActionResultDTO(ResultCode.SUCCESS,"approve succeed, wait for other owners decision");
                }
            }
            else{
                return new ActionResultDTO(ResultCode.ERROR_STORE_OWNER_MODIFICATION,"agreement not exist");
            }
        }


        return new ActionResultDTO(ResultCode.ERROR_STOREID,"Store not exist");
    }

    public GrantingResultDTO getAllGrantings(int sessionId, int subId) {
        User user = userHandler.getUser(sessionId);
        if(user==null)
            return new GrantingResultDTO(ResultCode.ERROR_SESSIONID,"Invalid sessionId",null);
        Subscriber subscriber = (Subscriber) user.getState();
        if(subscriber.getId()!=subId){
            return new GrantingResultDTO(ResultCode.ERROR_SUBID,"this is not your Id!",null);
        }

        List<Store> ownerIn = subscriber.getAllOwnedStores();
        List<GrantingAgreement> agreements = new ArrayList<>();

        for(Store store:ownerIn){
            agreements.addAll(store.getAgreementsOf(subId));
        }
        List<GrantingAgreementDTO> output = new ArrayList<>();
        for(GrantingAgreement agreement: agreements){
            output.add(agreement2DTO(agreement));
        }
        return new GrantingResultDTO(ResultCode.SUCCESS,"List of granting agreements of "+subId,output);
    }

    private GrantingAgreementDTO agreement2DTO(GrantingAgreement agreement) {
        int grantorId = agreement.getGrantorId();
        String grantorName = userHandler.getSubscriber(grantorId).getUsername();
        int candidateId = agreement.getMalshabId();
        String candidateName = userHandler.getSubscriber(candidateId).getUsername();
        return new GrantingAgreementDTO(
                agreement.getStoreId(),
                new SubscriberDTO(grantorId,grantorName),
                new SubscriberDTO(candidateId,candidateName),
                agreement.getOwner2approve()
        );
    }

    public ActionResultDTO declineStoreOwner(int sessionId, int storeId, int subId) {
        Subscriber owner = (Subscriber) userHandler.getUser(sessionId).getState();
        Subscriber newOwner = userHandler.getSubscriber(subId);
        Store store = null;
        try {
            store = getStoreById(storeId);
        } catch (DatabaseFetchException e) {
            return new ActionResultDTO(ResultCode.ERROR_DATABASE, "Could not contact database. Please try again later.");
        }

        if(store != null){
            if(store.agreementExist(owner.getId(),subId)) {
                store.removeAgreement(subId);
                return new ActionResultDTO(ResultCode.SUCCESS, "agreement is declined.");
            }
            else{
                return new ActionResultDTO(ResultCode.ERROR_STORE_OWNER_MODIFICATION,"agreement not exist");
            }
        }


        return new ActionResultDTO(ResultCode.ERROR_STOREID,"Store not exist");
    }

    // for use only in test (SystemTest)
    public Map<Integer, Store> getStoresMemory() {
        return stores;
    }

    public StatisticsResultsDTO getStatistics(String from, String to) {

        logger.info("requests statistics from: " + from + " to: " + to);
        // DD.MM.YYYY
        int fromDay = Integer.valueOf(from.substring(8, 10));
        int fromMonth = Integer.valueOf(from.substring(5, 7));
        int fromYear = Integer.valueOf(from.substring(0, 4));
        int toDay = Integer.valueOf(to.substring(8, 10));
        int toMonth = Integer.valueOf(to.substring(5, 7));
        int toYear = Integer.valueOf(to.substring(0, 4));

        java.lang.System.out.println(String.format("from: %s-%s-%s, to: %s-%s-%S", fromYear, fromMonth, fromDay, toYear, toMonth, toDay));

        List<DayStatistics> results = DAOManager.getStatisticsBetween(LocalDate.of(fromYear, fromMonth, fromDay), LocalDate.of(toYear, toMonth, toDay));

        if (results == null)
            return new StatisticsResultsDTO(ResultCode.ERROR_STATISTICS_BETWEEN, "Could not get day statistics between dates", null);
        else return new StatisticsResultsDTO(ResultCode.SUCCESS, "statistics between " + from + " to " + to, convertStatsToDTO(results));


//        //Tmp function for tests
//        List<DayStatistics> lst = new ArrayList<>();
//        lst.add(dailyStats);
//        return new StatisticsResultsDTO(ResultCode.SUCCESS,"List of stats",convertStatsToDTO(lst));
//        //
    }

    private List<DailyStatsDTO> convertStatsToDTO(List<DayStatistics> list){
      return  list.stream().map((stat)->new DailyStatsDTO(stat.getDate(),stat.getGuests(),stat.getRegularSubs()
        ,stat.getManagersNotOwners(),stat.getManagersOwners(),stat.getAdmins())).collect(Collectors.toList());
    }

    public DayStatistics getDailyStats() {
        return dailyStats;
    }

    public void clearStats() {
        dailyStats.reset();
    }

    public void addDayStatistics(int year, int month, int day) {
        DayStatistics stats = new DayStatistics(LocalDate.of(year, month, day));
        DAOManager.addDayStatistics(stats);
    }
}
