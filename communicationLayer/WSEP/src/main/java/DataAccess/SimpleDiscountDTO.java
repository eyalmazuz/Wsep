package DataAccess;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "simpleDiscountTypes")
public class SimpleDiscountDTO {
    @DatabaseField(id = true)
    private int id;

    @DatabaseField
    private String typeStr;

    @DatabaseField
    private int productId;

    @DatabaseField
    private String categoryName;

    @DatabaseField
    private double salePercentage;

    public SimpleDiscountDTO() {}

    public SimpleDiscountDTO(int id, String typeStr, int productId, String categoryName, double salePercentage) {
        this.id = id;
        this.typeStr = typeStr;
        this.productId = productId;
        this.categoryName = categoryName;
        this.salePercentage = salePercentage;
    }

    public int getId() {
        return id;
    }

    public String getTypeStr() {
        return typeStr;
    }

    public int getProductId() {
        return productId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public double getSalePercentage() {
        return salePercentage;
    }
}
