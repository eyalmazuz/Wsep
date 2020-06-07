package DataAccess;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "simpleBuyingTypes")
public class SimpleBuyingDTO {
    @DatabaseField(id = true)
    private int id;

    @DatabaseField
    private String typeStr;
    @DatabaseField
    private int productInfoId;
    @DatabaseField
    private int minAmount;
    @DatabaseField
    private int maxAmount;
    @DatabaseField
    private String validCountry;
    @DatabaseField
    private int dayOfWeek;

    public SimpleBuyingDTO() {}

    public SimpleBuyingDTO(int id, String typeStr, int productInfoId, int minAmount, int maxAmount, String validCountry, int dayOfWeek) {
        this.id = id;
        this.typeStr = typeStr;
        this.productInfoId = productInfoId;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.validCountry = validCountry;
        this.dayOfWeek = dayOfWeek;
    }

    public int getId() {
        return id;
    }

    public String getTypeStr() {
        return typeStr;
    }

    public int getProductInfoId() {
        return productInfoId;
    }

    public int getMinAmount() {
        return minAmount;
    }

    public int getMaxAmount() {
        return maxAmount;
    }

    public String getValidCountry() {
        return validCountry;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

}
