package DataAccess;

import Domain.TradingSystem.*;
import Domain.Util.Pair;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.lang.System;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DAOManager {

    private static ConnectionSource connectionSource;
    private static Dao<ProductInfo, String> productInfoDao;
    private static Dao<ProductInStore, String> productInStoreDao;
    private static Dao<BuyingPolicy, String> buyingPolicyDao;
    private static Dao<SimpleBuyingDTO, String> simpleBuyingDao;
    private static Dao<AdvancedBuyingDTO, String> advancedBuyingDao;


    public static void init(ConnectionSource csrc) {
        connectionSource = csrc;
        try {
            productInfoDao = DaoManager.createDao(csrc, ProductInfo.class);
            //productInStoreDao = DaoManager.createDao(csrc, ProductInStore.class);
            buyingPolicyDao = DaoManager.createDao(csrc, BuyingPolicy.class);
            simpleBuyingDao = DaoManager.createDao(csrc, SimpleBuyingDTO.class);
            advancedBuyingDao = DaoManager.createDao(csrc, AdvancedBuyingDTO.class);

            TableUtils.createTableIfNotExists(csrc, ProductInfo.class);
            //TableUtils.createTableIfNotExists(csrc, ProductInStore.class);
            TableUtils.createTableIfNotExists(csrc, BuyingPolicy.class);
            TableUtils.createTableIfNotExists(csrc, SimpleBuyingDTO.class);
            TableUtils.createTableIfNotExists(csrc, AdvancedBuyingDTO.class);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void createTable(Class<?> tableClass) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, tableClass);
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

    public static void addBuyingTypeToPolicy(BuyingPolicy policy, int typeId, BuyingType type) {
        if (type instanceof AdvancedBuying) {
            HashMap<Integer, String> buyingConstraintIDs = new HashMap<>();
            for (BuyingType buyingType : ((AdvancedBuying) type).getBuyingConstraints()) {
                String typeStr = "";
                if (buyingType instanceof AdvancedBuying) typeStr = "advanced";
                else typeStr = "simple";
                buyingConstraintIDs.put(typeId, typeStr);
            }
            AdvancedBuying.LogicalOperation logicalOperation = ((AdvancedBuying) type).getType();
            try {
                advancedBuyingDao.create(new AdvancedBuyingDTO(typeId, logicalOperation, buyingConstraintIDs));
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
                simpleBuyingDao.create(new SimpleBuyingDTO(typeId, constraintTypeStr, productId, minAmount, maxAmount, validCountry, dayOfWeek));
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
                        Pair<Integer, BuyingType> simpleType = loadSimpleBuyingType(typeId);
                        policy.addBuyingType(simpleType.getFirst(), simpleType.getSecond());
                    }
                    else policy.addBuyingType(loadAdvancedBuyingType(typeId));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return buyingPolicies;
    }

    private static Pair<Integer, BuyingType> loadSimpleBuyingType(Integer typeId) {
        SimpleBuyingDTO dto = null;
        try {
            dto = simpleBuyingDao.queryForId(Integer.toString(typeId));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (dto.getTypeStr() != null && dto.getTypeStr().equals("basket")) {
            if (dto.getProductInfoId() == -1) {
                if (dto.getMinAmount() != -1) return new Pair(dto.getId(), new BasketBuyingConstraint.MinProductAmountConstraint(dto.getMinAmount()));
                else return new Pair(dto.getId(), new BasketBuyingConstraint.MaxProductAmountConstraint(dto.getMaxAmount()));
            } else {
                if (dto.getMinAmount() != -1) return new Pair(dto.getId(), new BasketBuyingConstraint.MinAmountForProductConstraint(loadProductInfo(dto.getProductInfoId()), dto.getMinAmount()));
                else return new Pair(dto.getId(), new BasketBuyingConstraint.MaxAmountForProductConstraint(loadProductInfo(dto.getProductInfoId()), dto.getMaxAmount()));
            }
        } else if (dto.getTypeStr().equals("user")) return new Pair(dto.getId(), new UserBuyingConstraint.NotOutsideCountryConstraint(dto.getValidCountry()));
        else if (dto.getTypeStr().equals("system")) return new Pair(dto.getId(), new SystemBuyingConstraint.NotOnDayConstraint(dto.getDayOfWeek()));

        return null;
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
        return null;
    }


    public static void clearDatabase() {
        try {
            TableUtils.clearTable(connectionSource, ProductInfo.class);
            //TableUtils.createTableIfNotExists(connectionSource, ProductInStore.class);
            TableUtils.clearTable(connectionSource, BuyingPolicy.class);
            TableUtils.clearTable(connectionSource, SimpleBuyingDTO.class);
            TableUtils.clearTable(connectionSource, AdvancedBuyingDTO.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
