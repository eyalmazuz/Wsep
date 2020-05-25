package DataAccess;

import Domain.TradingSystem.ProductInStore;
import Domain.TradingSystem.ProductInfo;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class DAOManager {

    private static ConnectionSource connectionSource;
    private static Dao<ProductInfo, String> productInfoDao;
    private static Dao<ProductInStore, String> productInStoreDao;


    public static void init(ConnectionSource csrc) {
        connectionSource = csrc;
        try {
            productInfoDao = DaoManager.createDao(csrc, ProductInfo.class);
            //productInStoreDao = DaoManager.createDao(csrc, ProductInStore.class);
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
}
