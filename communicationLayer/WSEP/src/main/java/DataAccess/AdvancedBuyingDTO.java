package DataAccess;

import Domain.TradingSystem.AdvancedBuying;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.HashMap;

@DatabaseTable(tableName = "advancedBuyingTypes")
public class AdvancedBuyingDTO {
    @DatabaseField(id = true)
    private int id;

    @DatabaseField
    private AdvancedBuying.LogicalOperation logicalOperation;

    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private HashMap<Integer, String> buyingTypeIdTypeMap; // id -> type (simple or advanced)

    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private ArrayList<Integer> orderedBuyingTypeIds;

    public AdvancedBuyingDTO() {}

    public AdvancedBuyingDTO(int id, AdvancedBuying.LogicalOperation logicalOperation, HashMap<Integer, String> buyingTypeIdTypeMap, ArrayList<Integer> orderedBuyingTypeIds) {
        this.id = id;
        this.logicalOperation = logicalOperation;
        this.buyingTypeIdTypeMap = buyingTypeIdTypeMap;
        this.orderedBuyingTypeIds = orderedBuyingTypeIds;
    }

    public int getId() {
        return id;
    }

    public AdvancedBuying.LogicalOperation getLogicalOperation() {
        return logicalOperation;
    }

    public HashMap<Integer, String> getBuyingTypeIdTypeMap() {
        return buyingTypeIdTypeMap;
    }

    public ArrayList<Integer> getOrderedBuyingTypeIds() {
        return orderedBuyingTypeIds;
    }
}
