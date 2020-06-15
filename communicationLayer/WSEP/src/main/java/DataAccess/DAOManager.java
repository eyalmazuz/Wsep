package DataAccess;

import Domain.TradingSystem.*;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.table.TableUtils;

import java.lang.System;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class DAOManager {

    private static JdbcConnectionSource connectionSource;
    private static Dao<ProductInfo, String> productInfoDao;
    private static Dao<BuyingPolicy, String> buyingPolicyDao;
    private static Dao<SimpleBuyingDTO, String> simpleBuyingDao;
    private static Dao<AdvancedBuyingDTO, String> advancedBuyingDao;
    private static Dao<ProductInStore, String> productInStoreDao;
    private static Dao<Store, String> storeDao;
    private static Dao<ShoppingCart, String> shoppingCartDao;
    private static Dao<ShoppingBasket, String> shoppingBasketDao;
    private static Dao<Subscriber, String> subscriberDao;
    private static Dao<PurchaseDetails, String> purchaseDetailsDao;
    private static Dao<Permission, String> permissionDao;
    private static Dao<AdvancedDiscountDTO, String> advancedDiscountDao;
    private static Dao<DiscountPolicy, String> discountPolicyDao;
    private static Dao<SimpleDiscountDTO, String> simpleDiscountDao;
    private static Dao<GrantingAgreement, String> grantingAgreementDao;
    private static Dao<DayStatistics, String> dayStatisticsDao;

    private static boolean isOn;
    private static Queue<Runnable> toDo = new LinkedBlockingQueue<>();

    private static Class[] persistentClasses = {ProductInfo.class, BuyingPolicy.class, SimpleBuyingDTO.class, AdvancedBuyingDTO.class, ProductInStore.class,
            Store.class, ShoppingCart.class, ShoppingBasket.class, Subscriber.class, PurchaseDetails.class, Permission.class,
            AdvancedDiscountDTO.class, DiscountPolicy.class, SimpleDiscountDTO.class, GrantingAgreement.class, DayStatistics.class};

    public static void close() {
        isOn = false;
    }

    public static void open() {
        isOn = true;
        executeTodos();
    }

    public static void init(String databaseName, String username, String password) {
        try {
            connectionSource = new JdbcConnectionSource("jdbc:mysql://localhost/" + databaseName + "?user=" + username + "&password=" + password + "&serverTimezone=UTC");

            isOn = true;

            productInfoDao = DaoManager.createDao(connectionSource, ProductInfo.class);
            buyingPolicyDao = DaoManager.createDao(connectionSource, BuyingPolicy.class);
            simpleBuyingDao = DaoManager.createDao(connectionSource, SimpleBuyingDTO.class);
            advancedBuyingDao = DaoManager.createDao(connectionSource, AdvancedBuyingDTO.class);
            productInStoreDao = DaoManager.createDao(connectionSource, ProductInStore.class);
            storeDao = DaoManager.createDao(connectionSource, Store.class);
            shoppingCartDao = DaoManager.createDao(connectionSource, ShoppingCart.class);
            shoppingBasketDao = DaoManager.createDao(connectionSource, ShoppingBasket.class);
            subscriberDao = DaoManager.createDao(connectionSource, Subscriber.class);
            purchaseDetailsDao = DaoManager.createDao(connectionSource, PurchaseDetails.class);
            permissionDao = DaoManager.createDao(connectionSource, Permission.class);
            advancedDiscountDao = DaoManager.createDao(connectionSource, AdvancedDiscountDTO.class);
            discountPolicyDao = DaoManager.createDao(connectionSource, DiscountPolicy.class);
            simpleDiscountDao = DaoManager.createDao(connectionSource, SimpleDiscountDTO.class);
            grantingAgreementDao = DaoManager.createDao(connectionSource, GrantingAgreement.class);
            dayStatisticsDao = DaoManager.createDao(connectionSource, DayStatistics.class);

            for (Class c : persistentClasses) TableUtils.createTableIfNotExists(connectionSource, c);


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void createProductInfo(ProductInfo productInfo) {
        try {
            if (!isOn) throw new com.mysql.cj.exceptions.CJCommunicationsException();
            productInfoDao.create(productInfo);
            executeTodos();
        } catch (com.mysql.cj.exceptions.CJCommunicationsException e) {
            Runnable action = () -> createProductInfo(productInfo);
            toDo.add(action);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean executeTodos() {
        // isOn = true;
        boolean ret = false;
        while (!toDo.isEmpty()) {
            toDo.poll().run();
            ret = true;
        }
        return ret;
    }

    public static void advancedBuyingDaoCreate(AdvancedBuyingDTO buyingDTO) {
        try {
            if (!isOn) throw new com.mysql.cj.exceptions.CJCommunicationsException();
            advancedBuyingDao.create(buyingDTO);
            executeTodos();
        } catch (com.mysql.cj.exceptions.CJCommunicationsException e) {
            Runnable action = () -> advancedBuyingDaoCreate(buyingDTO);
            toDo.add(action);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void buyingPolicyDaoCreateOrUpdate(BuyingPolicy policy) {
        try {
            if (!isOn) throw new com.mysql.cj.exceptions.CJCommunicationsException();
            buyingPolicyDao.createOrUpdate(policy);
            executeTodos();
        } catch (com.mysql.cj.exceptions.CJCommunicationsException e) {
            Runnable action = () -> buyingPolicyDaoCreateOrUpdate(policy);
            toDo.add(action);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void simpleBuyingDaoCreate(SimpleBuyingDTO buyingDTO) {
        try {
            if (!isOn) throw new com.mysql.cj.exceptions.CJCommunicationsException();
            simpleBuyingDao.create(buyingDTO);
            executeTodos();
        } catch (com.mysql.cj.exceptions.CJCommunicationsException e) {
            Runnable action = () -> simpleBuyingDaoCreate(buyingDTO);
            toDo.add(action);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addBuyingTypeToPolicy(BuyingPolicy policy, BuyingType type) {
        if (type instanceof AdvancedBuying) {
            HashMap<Integer, String> buyingTypeIdTypeMap = new HashMap<>();
            ArrayList<Integer> orderedBuyingIds = new ArrayList<>();
            for (BuyingType subType : ((AdvancedBuying) type).getBuyingConstraints()) {
                buyingTypeIdTypeMap.put(subType.getId(), subType instanceof AdvancedBuying ? "advanced" : "simple");
                orderedBuyingIds.add(subType.getId());
            }
            advancedBuyingDaoCreate(new AdvancedBuyingDTO(type.getId(), ((AdvancedBuying) type).getLogicalOperation(), buyingTypeIdTypeMap, orderedBuyingIds));
            buyingPolicyDaoCreateOrUpdate(policy);

        } else {
            // save via Type Per Hierarchy
            String constraintTypeStr = "";
            int productId = -1, minAmount = -1, maxAmount = -1, dayOfWeek = -1;
            String validCountry = null;

            if (type instanceof BasketBuyingConstraint) {
                constraintTypeStr = "basket";
                ProductInfo info = ((BasketBuyingConstraint) type).getProductInfo();
                if (info != null) productId = info.getId();
                minAmount = ((BasketBuyingConstraint) type).getMinAmount();
                maxAmount = ((BasketBuyingConstraint) type).getMaxAmount();
            } else if (type instanceof SystemBuyingConstraint) {
                constraintTypeStr = "system";
                dayOfWeek = ((SystemBuyingConstraint) type).getForbiddenDay();
            } else if (type instanceof UserBuyingConstraint) {
                constraintTypeStr = "user";
                validCountry = ((UserBuyingConstraint) type).getValidCountry();
            }

            simpleBuyingDaoCreate(new SimpleBuyingDTO(type.getId(), constraintTypeStr, productId, minAmount, maxAmount, validCountry, dayOfWeek));
            buyingPolicyDaoCreateOrUpdate(policy);
        }
    }

    private static void fixBuyingPolicy(BuyingPolicy policy) {
        HashMap<Integer, String> typeIds = policy.getBuyingTypeIDs();
        for (Integer typeId : typeIds.keySet()) {
            String type = typeIds.get(typeId);
            if (type.equals("simple")) {
                BuyingType simpleType = loadSimpleBuyingType(typeId);
                policy.addSimpleBuyingType((SimpleBuying) simpleType);
            } else policy.addAdvancedBuyingType((AdvancedBuying) loadAdvancedBuyingType(typeId), true);
        }
    }

    public static List<BuyingPolicy> loadAllBuyingPolicies() {
        List<BuyingPolicy> buyingPolicies = null;
        try {
            buyingPolicies = buyingPolicyDao.queryForAll();
            for (BuyingPolicy policy : buyingPolicies) {
                fixBuyingPolicy(policy);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return buyingPolicies;
    }


    private static BuyingPolicy loadBuyingPolicy(int id) {
        BuyingPolicy policy = null;
        try {
            policy = buyingPolicyDao.queryForId(Integer.toString(id));
            if (policy != null) fixBuyingPolicy(policy);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return policy;
    }

    private static BuyingType loadSimpleBuyingType(Integer typeId) {
        SimpleBuyingDTO dto = null;
        try {
            dto = simpleBuyingDao.queryForId(Integer.toString(typeId));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (dto == null) System.err.println("Could not find simple buying type " + typeId + " in database");
        SimpleBuying result = null;
        if (dto.getTypeStr().equals("basket")) {
            if (dto.getProductInfoId() == -1) {
                if (dto.getMinAmount() != -1)
                    result = new BasketBuyingConstraint.MinProductAmountConstraint(dto.getMinAmount());
                else result = new BasketBuyingConstraint.MaxProductAmountConstraint(dto.getMaxAmount());
            } else {
                if (dto.getMinAmount() != -1)
                    result = new BasketBuyingConstraint.MinAmountForProductConstraint(loadProductInfo(dto.getProductInfoId()), dto.getMinAmount());
                else
                    result = new BasketBuyingConstraint.MaxAmountForProductConstraint(loadProductInfo(dto.getProductInfoId()), dto.getMaxAmount());
            }
        } else if (dto.getTypeStr().equals("user"))
            result = new UserBuyingConstraint.NotOutsideCountryConstraint(dto.getValidCountry());
        else if (dto.getTypeStr().equals("system"))
            result = new SystemBuyingConstraint.NotOnDayConstraint(dto.getDayOfWeek());

        result.setId(dto.getId());

        return result;
    }


    private static BuyingType loadAdvancedBuyingType(Integer typeId) {
        AdvancedBuyingDTO dto = null;
        try {
            dto = advancedBuyingDao.queryForId(Integer.toString(typeId));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        List<BuyingType> constraints = new ArrayList<>();
        for (Integer subTypeId : dto.getOrderedBuyingTypeIds()) {
            String typeStr = dto.getBuyingTypeIdTypeMap().get(subTypeId);
            BuyingType resultingBuyingType = typeStr.equals("simple") ? loadSimpleBuyingType(subTypeId) : loadAdvancedBuyingType(subTypeId);
            resultingBuyingType.setId(subTypeId);
            constraints.add(resultingBuyingType);
        }

        AdvancedBuying advancedBuying = new AdvancedBuying.LogicalBuying(constraints, dto.getLogicalOperation());
        advancedBuying.setId(dto.getId());
        return advancedBuying;
    }

    private static DiscountPolicy loadDiscountPolicy(int id) {
        DiscountPolicy policy = null;
        try {
            policy = discountPolicyDao.queryForId(Integer.toString(id));
            if (policy != null) fixDiscountPolicy(policy);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return policy;
    }

    private static void fixDiscountPolicy(DiscountPolicy policy) {
        HashMap<Integer, String> typeIds = policy.getDiscountTypeIDs();
        for (Integer typeId : typeIds.keySet()) {
            String type = typeIds.get(typeId);
            if (type.equals("simple")) {
                DiscountType simpleType = loadSimpleDiscountType(typeId);
                policy.addSimpleDiscountType((SimpleDiscount) simpleType);
            } else policy.addAdvancedDiscountType((AdvancedDiscount) loadAdvancedDiscountType(typeId), true);
        }
    }

    private static DiscountType loadSimpleDiscountType(Integer typeId) {
        SimpleDiscountDTO dto = null;
        try {
            dto = simpleDiscountDao.queryForId(Integer.toString(typeId));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (dto == null) System.err.println("Could not find simple discount type " + typeId + " in database");
        SimpleDiscount result = null;
        if (dto.getTypeStr().equals("product")) {
            if (dto.getCategoryName() == null)
                result = new ProductDiscount.ProductSaleDiscount(dto.getProductId(), dto.getSalePercentage());
            else result = new ProductDiscount.CategorySaleDiscount(dto.getCategoryName(), dto.getSalePercentage());
        }
        result.setId(dto.getId());

        return result;
    }

    private static DiscountType loadAdvancedDiscountType(Integer typeId) {
        AdvancedDiscountDTO dto = null;
        try {
            dto = advancedDiscountDao.queryForId(Integer.toString(typeId));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        List<DiscountType> discounts = new ArrayList<>();
        for (Integer subTypeId : dto.getOrderedDiscountTypeIds()) {
            String typeStr = dto.getDiscountTypeIdTypeMap().get(subTypeId);
            DiscountType resultingDiscountType = typeStr.equals("simple") ? loadSimpleDiscountType(subTypeId) : loadAdvancedDiscountType(subTypeId);
            resultingDiscountType.setId(subTypeId);
            discounts.add(resultingDiscountType);
        }

        AdvancedDiscount advancedDiscount = new AdvancedDiscount.LogicalDiscount(discounts, dto.getLogicalOperation());
        advancedDiscount.setId(dto.getId());
        return advancedDiscount;
    }

    private static ProductInfo loadProductInfo(int productInfoId) {
        try {
            return productInfoDao.queryForId(Integer.toString(productInfoId));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static List<ProductInfo> loadAllProductInfos() {
        try {
            return productInfoDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void updateProductInfo(ProductInfo productInfo) {
        try {
            if (!isOn) throw new com.mysql.cj.exceptions.CJCommunicationsException();
            productInfoDao.update(productInfo);
            executeTodos();
        } catch (com.mysql.cj.exceptions.CJCommunicationsException e) {
            Runnable action = () -> updateProductInfo(productInfo);
            toDo.add(action);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void createProductInStoreListForStore(Store store) {
        try {
            if (!isOn) throw new com.mysql.cj.exceptions.CJCommunicationsException();
            storeDao.assignEmptyForeignCollection(store, "products");
            executeTodos();
        } catch (com.mysql.cj.exceptions.CJCommunicationsException e) {
            Runnable action = () -> createProductInStoreListForStore(store);
            toDo.add(action);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addStore(Store store) {
        try {
            if (!isOn) throw new com.mysql.cj.exceptions.CJCommunicationsException();
            storeDao.create(store);
            executeTodos();
        } catch (com.mysql.cj.exceptions.CJCommunicationsException e) {
            Runnable action = () -> addStore(store);
            toDo.add(action);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void clearDatabase() {
        try {
            if (!isOn) throw new com.mysql.cj.exceptions.CJCommunicationsException();
            for (Class c : persistentClasses) TableUtils.clearTable(connectionSource, c);
            executeTodos();
        } catch (com.mysql.cj.exceptions.CJCommunicationsException e) {
            Runnable action = () -> clearDatabase();
            toDo.add(action);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static void fixStore(Store store) {
       for (ProductInStore pis : store.getProducts()) {
           pis.setProductInfo(loadProductInfoById(pis.getProductInfoId()));
       }

        BuyingPolicy fixedBuyingPolicy = loadBuyingPolicy(store.getBuyingPolicy().getId());
        if (fixedBuyingPolicy != null) store.setBuyingPolicy(fixedBuyingPolicy);

        DiscountPolicy fixedDiscountPolicy = loadDiscountPolicy(store.getDiscountPolicy().getId());
        if (fixedDiscountPolicy != null) store.setDiscountPolicy(fixedDiscountPolicy);

        List<Integer> managerIds = store.getManagerIds();
        List<Subscriber> managers = new ArrayList<>();
        for (Integer managerId : managerIds) {
            Subscriber manager = loadSubscriber(managerId);
            managers.add(manager);
        }
        store.setManagers(managers);
    }

    public static List<Store> loadAllStores() {
        try {
            List<Store> stores = storeDao.queryForAll();
            for (Store store : stores) fixStore(store);
            return stores;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void fixSubscriber(Subscriber subscriber) {
        Map<Integer, List<Integer>> storePurchaseListsPrimitive = subscriber.getStorePurchaseListsPrimitive();
        HashMap<Store, List<PurchaseDetails>> storePurchaseLists = new HashMap<>();
        for (Integer storeId : storePurchaseListsPrimitive.keySet()) {
            List<Integer> purchaseDetailsIds = storePurchaseListsPrimitive.get(storeId);
            List<PurchaseDetails> purchaseDetailsList = new ArrayList<>();
            for (Integer purchaseDetailsId : purchaseDetailsIds)
                purchaseDetailsList.add(loadPurchaseDetails(purchaseDetailsId));
            storePurchaseLists.put(loadStore(storeId), purchaseDetailsList);
        }
        subscriber.setStorePurchaseLists(storePurchaseLists);

        Map<Integer, Permission> permissionMap = new HashMap<>();
        for (Permission permission : loadSubscriberPermissions(subscriber.getId())) {
            permissionMap.put(permission.getStore().getId(), permission);
        }
        subscriber.setPermissions(permissionMap);
    }

    private static List<Permission> loadSubscriberPermissions(int id) {
        QueryBuilder<Permission, String> queryBuilder = permissionDao.queryBuilder();
        SelectArg selectArg = new SelectArg();
        selectArg.setValue(id);
        Where<Permission, String> where = queryBuilder.where();
        try {
            where.eq("user_id", selectArg);
            PreparedQuery<Permission> query = queryBuilder.prepare();
            return permissionDao.query(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static Subscriber loadSubscriber(Integer managerId) {
        try {
            Subscriber subscriber = subscriberDao.queryForId(Integer.toString(managerId));
            fixSubscriber(subscriber);
            return subscriber;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void updateStore(Store store) {
        try {
            if (!isOn) throw new com.mysql.cj.exceptions.CJCommunicationsException();
            storeDao.update(store);
            executeTodos();
        } catch (com.mysql.cj.exceptions.CJCommunicationsException e) {
            Runnable action = () -> updateStore(store);
            toDo.add(action);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateProductInStore(ProductInStore product) {
        try {
            if (!isOn) throw new com.mysql.cj.exceptions.CJCommunicationsException();
            productInStoreDao.update(product);
            executeTodos();
        } catch (com.mysql.cj.exceptions.CJCommunicationsException e) {
            Runnable action = () -> updateProductInStore(product);
            toDo.add(action);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createBasketListForCart(ShoppingCart shoppingCart) {
        try {
            if (!isOn) throw new com.mysql.cj.exceptions.CJCommunicationsException();
            shoppingCartDao.assignEmptyForeignCollection(shoppingCart, "persistentShoppingBaskets");
            executeTodos();
        } catch (com.mysql.cj.exceptions.CJCommunicationsException e) {
            Runnable action = () -> createBasketListForCart(shoppingCart);
            toDo.add(action);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<ShoppingBasket> loadAllShoppingBaskets() {
        try {
            return shoppingBasketDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void createOrUpdateShoppingCart(ShoppingCart shoppingCart) {
        try {
            if (!isOn) throw new com.mysql.cj.exceptions.CJCommunicationsException();
            shoppingCartDao.createOrUpdate(shoppingCart);
            executeTodos();
        } catch (com.mysql.cj.exceptions.CJCommunicationsException e) {
            Runnable action = () -> createOrUpdateShoppingCart(shoppingCart);
            toDo.add(action);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateShoppingBasket(ShoppingBasket shoppingBasket) {
        try {
            if (!isOn) throw new com.mysql.cj.exceptions.CJCommunicationsException();
            shoppingBasketDao.update(shoppingBasket);
            executeTodos();
        } catch (com.mysql.cj.exceptions.CJCommunicationsException e) {
            Runnable action = () -> updateShoppingBasket(shoppingBasket);
            toDo.add(action);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Subscriber> loadAllSubscribers() {
        List<Subscriber> subscribers = null;
        try {
            subscribers = subscriberDao.queryForAll();
            for (Subscriber s : subscribers) fixSubscriber(s);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subscribers;
    }

    private static Store loadStore(Integer storeId) {
        try {
            return storeDao.queryForId(Integer.toString(storeId));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static PurchaseDetails loadPurchaseDetails(Integer purchaseDetailsId) {
        try {
            return purchaseDetailsDao.queryForId(Integer.toString(purchaseDetailsId));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void addSubscriber(Subscriber subscriberState) {
        try {
            if (!isOn) throw new com.mysql.cj.exceptions.CJCommunicationsException();
            subscriberDao.create(subscriberState);
            executeTodos();
        } catch (com.mysql.cj.exceptions.CJCommunicationsException e) {
            Runnable action = () -> addSubscriber(subscriberState);
            toDo.add(action);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void updateSubscriber(Subscriber subscriber) {
        try {
            if (!isOn) throw new com.mysql.cj.exceptions.CJCommunicationsException();
            subscriberDao.update(subscriber);
            executeTodos();
        } catch (com.mysql.cj.exceptions.CJCommunicationsException e) {
            Runnable action = () -> updateSubscriber(subscriber);
            toDo.add(action);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createOrUpdateSubscriber(Subscriber state) {
        try {
            if (!isOn) throw new com.mysql.cj.exceptions.CJCommunicationsException();
            subscriberDao.createOrUpdate(state);
            executeTodos();
        } catch (com.mysql.cj.exceptions.CJCommunicationsException e) {
            Runnable action = () -> createOrUpdateSubscriber(state);
            toDo.add(action);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createPurchaseDetails(PurchaseDetails details) {
        try {
            if (!isOn) throw new com.mysql.cj.exceptions.CJCommunicationsException();
            purchaseDetailsDao.createOrUpdate(details);
            executeTodos();
        } catch (com.mysql.cj.exceptions.CJCommunicationsException e) {
            Runnable action = () -> createPurchaseDetails(details);
            toDo.add(action);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createManagerListForStore(Store store) {
        try {
            if (!isOn) throw new com.mysql.cj.exceptions.CJCommunicationsException();
            storeDao.assignEmptyForeignCollection(store, "managers");
            executeTodos();
        } catch (com.mysql.cj.exceptions.CJCommunicationsException e) {
            Runnable action = () -> createManagerListForStore(store);
            toDo.add(action);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createPurchaseHistoryForStore(Store store) {
        try {
            if (!isOn) throw new com.mysql.cj.exceptions.CJCommunicationsException();
            storeDao.assignEmptyForeignCollection(store, "purchaseHistory");
            executeTodos();
        } catch (com.mysql.cj.exceptions.CJCommunicationsException e) {
            Runnable action = () -> createPurchaseHistoryForStore(store);
            toDo.add(action);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updatePermission(Permission permission) {
        try {
            if (!isOn) throw new com.mysql.cj.exceptions.CJCommunicationsException();
            permissionDao.update(permission);
            executeTodos();
        } catch (com.mysql.cj.exceptions.CJCommunicationsException e) {
            Runnable action = () -> updatePermission(permission);
            toDo.add(action);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addPermission(Permission permission) {
        try {
            if (!isOn) throw new com.mysql.cj.exceptions.CJCommunicationsException();
            permissionDao.create(permission);
            executeTodos();
        } catch (com.mysql.cj.exceptions.CJCommunicationsException e) {
            Runnable action = () -> addPermission(permission);
            toDo.add(action);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void advancedDiscountDaoCreate(AdvancedDiscountDTO discountDTO) {
        try {
            if (!isOn) throw new com.mysql.cj.exceptions.CJCommunicationsException();
            advancedDiscountDao.create(discountDTO);
            executeTodos();
        } catch (com.mysql.cj.exceptions.CJCommunicationsException e) {
            Runnable action = () -> advancedDiscountDaoCreate(discountDTO);
            toDo.add(action);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void discountPolicyDaoCreateOrUpdate(DiscountPolicy policy) {
        try {
            if (!isOn) throw new com.mysql.cj.exceptions.CJCommunicationsException();
            discountPolicyDao.create(policy);
            executeTodos();
        } catch (com.mysql.cj.exceptions.CJCommunicationsException e) {
            Runnable action = () -> discountPolicyDaoCreateOrUpdate(policy);
            toDo.add(action);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void simpleDiscountDaoCreate(SimpleDiscountDTO discountDTO) {
        try {
            if (!isOn) throw new com.mysql.cj.exceptions.CJCommunicationsException();
            simpleDiscountDao.create(discountDTO);
            executeTodos();
        } catch (com.mysql.cj.exceptions.CJCommunicationsException e) {
            Runnable action = () -> simpleDiscountDaoCreate(discountDTO);
            toDo.add(action);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addDiscountTypeToPolicy(DiscountPolicy policy, DiscountType type) {
        if (type instanceof AdvancedDiscount) {
            HashMap<Integer, String> discountTypeIdTypeMap = new HashMap<>();
            ArrayList<Integer> orderedDiscountIds = new ArrayList<>();
            for (DiscountType subType : ((AdvancedDiscount) type).getDiscounts()) {
                discountTypeIdTypeMap.put(subType.getId(), subType instanceof AdvancedDiscount ? "advanced" : "simple");
                orderedDiscountIds.add(subType.getId());
            }

            advancedDiscountDaoCreate(new AdvancedDiscountDTO(type.getId(), ((AdvancedDiscount) type).getLogicalOperation(), discountTypeIdTypeMap, orderedDiscountIds));
            discountPolicyDaoCreateOrUpdate(policy);

        } else {
            // save via Type Per Hierarchy
            String discountTypeStr = "";
            int productId = -1;
            double salePercentage = -1.0;
            String categoryName = null;

            if (type instanceof ProductDiscount) {
                discountTypeStr = "product";
                productId = ((ProductDiscount) type).getProductId();
                salePercentage = ((ProductDiscount) type).getSalePercentage();
                categoryName = ((ProductDiscount) type).getCategoryName();
            }

            simpleDiscountDaoCreate(new SimpleDiscountDTO(type.getId(), discountTypeStr, productId, categoryName, salePercentage));
            discountPolicyDaoCreateOrUpdate(policy);
        }
    }

    public static List<Permission> loadAllPermissions() {
        try {
            return permissionDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void removePermission(Permission permission) {
        try {
            if (!isOn) throw new com.mysql.cj.exceptions.CJCommunicationsException();
            permissionDao.delete(permission);
            executeTodos();
        } catch (com.mysql.cj.exceptions.CJCommunicationsException e) {
            Runnable action = () -> removePermission(permission);
            toDo.add(action);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ShoppingCart loadShoppingCartBySubscriberId(int subscriberId) {
        try {
            return shoppingCartDao.queryForId(Integer.toString(subscriberId));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void updateGrantingAgreement(GrantingAgreement grantingAgreement) {
        try {
            if (!isOn) throw new com.mysql.cj.exceptions.CJCommunicationsException();
            grantingAgreementDao.update(grantingAgreement);
            executeTodos();
        } catch (com.mysql.cj.exceptions.CJCommunicationsException e) {
            Runnable action = () -> updateGrantingAgreement(grantingAgreement);
            toDo.add(action);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int getMaxSubscriberId() {
        try {
            return subscriberDao.countOf() == 0 ? -1 : (int) subscriberDao.queryRawValue("SELECT MAX(id) FROM subscribers");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static int getMaxProductInfoId() {
        try {
            return productInfoDao.countOf() == 0 ? -1 : (int) productInfoDao.queryRawValue("SELECT MAX(id) FROM productinfos");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }


    public static int getMaxPurchaseDetailsId() {
        try {
            return purchaseDetailsDao.countOf() == 0 ? -1 : (int) purchaseDetailsDao.queryRawValue("SELECT MAX(id) FROM purchasedetails");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static int getMaxBuyingPolicyId() {
        try {
            return buyingPolicyDao.countOf() == 0 ? -1 : (int) buyingPolicyDao.queryRawValue("SELECT MAX(id) FROM buyingpolicies");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static int getMaxDiscountPolicyId() {
        try {
            return discountPolicyDao.countOf() == 0 ? -1 : (int) discountPolicyDao.queryRawValue("SELECT MAX(id) FROM discountpolicies");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }


    public static boolean subscriberExists(String username) {
        QueryBuilder<Subscriber, String> queryBuilder = subscriberDao.queryBuilder();
        SelectArg selectArg = new SelectArg();
        selectArg.setValue(username);
        Where<Subscriber, String> where = queryBuilder.where();
        try {
            where.eq("username", selectArg);
            PreparedQuery<Subscriber> query = queryBuilder.prepare();
            return !subscriberDao.query(query).isEmpty();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static Subscriber getSubscriberByUsername(String username) {
        QueryBuilder<Subscriber, String> queryBuilder = subscriberDao.queryBuilder();
        SelectArg selectArg = new SelectArg();
        selectArg.setValue(username);
        Where<Subscriber, String> where = queryBuilder.where();
        try {
            where.eq("username", selectArg);
            PreparedQuery<Subscriber> query = queryBuilder.prepare();
            List<Subscriber> subscribers = subscriberDao.query(query);
            if (subscribers.isEmpty()) return null;
            Subscriber subscriber = subscribers.get(0);
            fixSubscriber(subscriber);
            return subscriber;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean hasAdmin() {
        QueryBuilder<Subscriber, String> queryBuilder = subscriberDao.queryBuilder();
        SelectArg selectArg = new SelectArg();
        selectArg.setValue(true);
        Where<Subscriber, String> where = queryBuilder.where();
        try {
            where.eq("isAdmin", selectArg);
            PreparedQuery<Subscriber> query = queryBuilder.prepare();
            List<Subscriber> subscribers = subscriberDao.query(query);
            return !subscribers.isEmpty();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static int getNumShoppingCarts() {
        try {
            return shoppingCartDao.queryForAll().size();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static int getNumShoppingBaskets() {
        try {
            return shoppingBasketDao.queryForAll().size();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static ProductInfo loadProductInfoById(int id) {
        try {
            return productInfoDao.queryForId(Integer.toString(id));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Store loadStoreById(int storeId) {
        try {
            Store store = storeDao.queryForId(Integer.toString(storeId));
            if (store != null) fixStore(store);

            return store;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void removeProductInfo(int productId) {
        try {
            productInfoDao.deleteById(Integer.toString(productId));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Store loadStoreByName(String name) {
        QueryBuilder<Store, String> queryBuilder = storeDao.queryBuilder();
        SelectArg selectArg = new SelectArg();
        selectArg.setValue(name);
        Where<Store, String> where = queryBuilder.where();
        try {
            where.eq("name", selectArg);
            PreparedQuery<Store> query = queryBuilder.prepare();
            return storeDao.query(query).get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        catch (IndexOutOfBoundsException e){
            return null;
        }
        return null;
    }

    public static ProductInfo loadProductInfoByName(String name) {
        QueryBuilder<ProductInfo, String> queryBuilder = productInfoDao.queryBuilder();
        SelectArg selectArg = new SelectArg();
        selectArg.setValue(name);
        Where<ProductInfo, String> where = queryBuilder.where();
        try {
            where.eq("name", selectArg);
            PreparedQuery<ProductInfo> query = queryBuilder.prepare();
            return productInfoDao.query(query).get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        catch (IndexOutOfBoundsException e){
            return null;
        }
        return null;
    }

    public static void updateDayStatistics(DayStatistics dayStatistics) {
        try {
            if (!isOn) throw new com.mysql.cj.exceptions.CJCommunicationsException();
            dayStatisticsDao.update(dayStatistics);
            executeTodos();
        } catch (com.mysql.cj.exceptions.CJCommunicationsException e) {
            Runnable action = () -> updateDayStatistics(dayStatistics);
            toDo.add(action);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static DayStatistics getDayStatisticsByDay(LocalDate date) {
        QueryBuilder<DayStatistics, String> queryBuilder = dayStatisticsDao.queryBuilder();
        SelectArg selectArg = new SelectArg();
        selectArg.setValue(date);
        Where<DayStatistics, String> where = queryBuilder.where();
        try {
            where.eq("date", selectArg);
            PreparedQuery<DayStatistics> query = queryBuilder.prepare();
            List<DayStatistics> dayStatistics = dayStatisticsDao.query(query);
            if (dayStatistics.isEmpty()) return null;
            return dayStatistics.get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
