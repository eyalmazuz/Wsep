package DataAccess;

import Domain.TradingSystem.*;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.lang.System;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DAOManager {

    private static ConnectionSource connectionSource;
    private static Dao<ProductInfo, String> productInfoDao;
    private static Dao<BuyingPolicy, String> buyingPolicyDao;
    private static Dao<SimpleBuyingDTO, String> simpleBuyingDao;
    private static Dao<AdvancedBuyingDTO, String> advancedBuyingDao;
    private static Dao<ProductInStore, String> productInStoreDao;
    private static Dao<Store, String> storeDao;

    private static Class[] persistentClasses = {ProductInfo.class, BuyingPolicy.class, SimpleBuyingDTO.class, AdvancedBuyingDTO.class, ProductInStore.class,
            Store.class};

    public static void init(ConnectionSource csrc) {
        connectionSource = csrc;
        try {
            productInfoDao = DaoManager.createDao(csrc, ProductInfo.class);
            buyingPolicyDao = DaoManager.createDao(csrc, BuyingPolicy.class);
            simpleBuyingDao = DaoManager.createDao(csrc, SimpleBuyingDTO.class);
            advancedBuyingDao = DaoManager.createDao(csrc, AdvancedBuyingDTO.class);
            productInStoreDao = DaoManager.createDao(csrc, ProductInStore.class);
            storeDao = DaoManager.createDao(csrc, Store.class);

            for (Class c : persistentClasses) TableUtils.createTableIfNotExists(csrc, c);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void createProductInfo(ProductInfo productInfo) {
        try {
            productInfoDao.create(productInfo);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createProductInStore(ProductInStore pis) {
        try {
            productInStoreDao.create(pis);
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
            try {
                advancedBuyingDao.create(new AdvancedBuyingDTO(type.getId(), ((AdvancedBuying) type).getLogicalOperation(), buyingTypeIdTypeMap, orderedBuyingIds));
                buyingPolicyDao.createOrUpdate(policy);
            } catch (SQLException e) {
                e.printStackTrace();
            }
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
            }
            else if (type instanceof SystemBuyingConstraint) {
                constraintTypeStr = "system";
                dayOfWeek = ((SystemBuyingConstraint) type).getForbiddenDay();
            }
            else if (type instanceof UserBuyingConstraint) {
                constraintTypeStr = "user";
                validCountry = ((UserBuyingConstraint) type).getValidCountry();
            }

            try {
                simpleBuyingDao.create(new SimpleBuyingDTO(type.getId(), constraintTypeStr, productId, minAmount, maxAmount, validCountry, dayOfWeek));
                buyingPolicyDao.createOrUpdate(policy);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static List<BuyingPolicy> loadAllBuyingPolicies() {
        List<BuyingPolicy> buyingPolicies = null;
        try {
            buyingPolicies = buyingPolicyDao.queryForAll();
            for (BuyingPolicy policy : buyingPolicies) {
                HashMap<Integer, String> typeIds = policy.getBuyingTypeIDs();
                for (Integer typeId : typeIds.keySet()) {
                    String type = typeIds.get(typeId);
                    if (type.equals("simple")) {
                        BuyingType simpleType = loadSimpleBuyingType(typeId);
                        policy.addSimpleBuyingType((SimpleBuying) simpleType);
                    }
                    else policy.addAdvancedBuyingType((AdvancedBuying) loadAdvancedBuyingType(typeId), true);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return buyingPolicies;
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
                if (dto.getMinAmount() != -1) result = new BasketBuyingConstraint.MinProductAmountConstraint(dto.getMinAmount());
                else result = new BasketBuyingConstraint.MaxProductAmountConstraint(dto.getMaxAmount());
            } else {
                if (dto.getMinAmount() != -1) result = new BasketBuyingConstraint.MinAmountForProductConstraint(loadProductInfo(dto.getProductInfoId()), dto.getMinAmount());
                else result = new BasketBuyingConstraint.MaxAmountForProductConstraint(loadProductInfo(dto.getProductInfoId()), dto.getMaxAmount());
            }
        } else if (dto.getTypeStr().equals("user")) result = new UserBuyingConstraint.NotOutsideCountryConstraint(dto.getValidCountry());
        else if (dto.getTypeStr().equals("system")) result = new SystemBuyingConstraint.NotOnDayConstraint(dto.getDayOfWeek());

        result.setId(dto.getId());

        return result;
    }

    private static ProductInfo loadProductInfo(int productInfoId) {
        try {
            return productInfoDao.queryForId(Integer.toString(productInfoId));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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
            productInfoDao.update(productInfo);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void createProductInStoreListForStore(Store store, ForeignCollection<ProductInStore> products) {
        try {
            storeDao.assignEmptyForeignCollection(store, "products");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addStore(Store store) {
        try {
            storeDao.create(store);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void clearDatabase() {
        try {
            for (Class c : persistentClasses) TableUtils.clearTable(connectionSource, c);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Store> loadAllStores() {
        try {
            return storeDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void updateStore(Store store) {
        try {
            storeDao.update(store);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateProductInStore(ProductInStore product) {
        try {
            productInStoreDao.update(product);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
