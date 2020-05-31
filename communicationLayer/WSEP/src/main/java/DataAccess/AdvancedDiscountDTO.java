package DataAccess;

import Domain.TradingSystem.AdvancedDiscount;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.HashMap;

@DatabaseTable(tableName = "advancedDiscountTypes")
public class AdvancedDiscountDTO {
    @DatabaseField(id = true)
    private int id;

    @DatabaseField
    private AdvancedDiscount.LogicalOperation logicalOperation;

    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private HashMap<Integer, String> discountTypeIdTypeMap; // id -> type (simple or advanced)

    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private ArrayList<Integer> orderedDiscountTypeIds;

    public AdvancedDiscountDTO() {}

    public AdvancedDiscountDTO(int id, AdvancedDiscount.LogicalOperation logicalOperation, HashMap<Integer, String> discountTypeIdTypeMap, ArrayList<Integer> orderedDiscountTypeIds) {
        this.id = id;
        this.logicalOperation = logicalOperation;
        this.discountTypeIdTypeMap = discountTypeIdTypeMap;
        this.orderedDiscountTypeIds = orderedDiscountTypeIds;
    }

    public int getId() {
        return id;
    }

    public AdvancedDiscount.LogicalOperation getLogicalOperation() {
        return logicalOperation;
    }

    public HashMap<Integer, String> getDiscountTypeIdTypeMap() {
        return discountTypeIdTypeMap;
    }

    public ArrayList<Integer> getOrderedDiscountTypeIds() {
        return orderedDiscountTypeIds;
    }
}
