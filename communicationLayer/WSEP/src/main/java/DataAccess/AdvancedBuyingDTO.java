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
    private HashMap<Integer, String> buyingTypeIDs; // id -> type (simple or advanced)

    public AdvancedBuyingDTO() {}

    public AdvancedBuyingDTO(int id, AdvancedBuying.LogicalOperation logicalOperation, HashMap<Integer, String> buyingTypeIDs) {
        this.id = id;
        this.logicalOperation = logicalOperation;
        this.buyingTypeIDs = buyingTypeIDs;
    }
}
