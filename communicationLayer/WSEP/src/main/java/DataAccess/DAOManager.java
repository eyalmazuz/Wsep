package DataAccess;

import Domain.TradingSystem.ProductInStore;
import Domain.TradingSystem.ProductInfo;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class DAOManager {

    private ConnectionSource csrc;
    private Dao<ProductInfo, String> productInfoDao;

    public DAOManager(ConnectionSource csrc) {
        this.csrc = csrc;
        try {
            productInfoDao = DaoManager.createDao(csrc, ProductInfo.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createTable(Class<?> tableClass) {
        try {
            TableUtils.createTableIfNotExists(csrc, tableClass);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createProductInfo(ProductInfo productInfo) {
        try {
            productInfoDao.create(productInfo);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
