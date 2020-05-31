package Domain.TradingSystem;

import DTOs.DiscountPolicyActionResultDTO;
import DTOs.IntActionResultDto;
import DTOs.ResultCode;
import DTOs.SimpleDTOS.DiscountTypeDTO;
import DataAccess.DAOManager;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.*;

@DatabaseTable(tableName = "discountPolicies")
public class DiscountPolicy {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String details;

    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private HashMap<Integer, String> discountTypeIDs = new HashMap<>();
    private Map<Integer, DiscountType> discountTypes = new HashMap<>();
    private static int nextId = 0;

    public DiscountPolicy () { }

    public DiscountPolicy(String details) {
        this.details = details;
    }

    public double getBasketDiscountedPrice(User user, ShoppingBasket basket) {
        List<DiscountBasket.DiscountProduct> discountProducts = new ArrayList<>();

        Map<ProductInfo, Integer> products = basket.getProducts();

        for (Map.Entry<ProductInfo, Integer> entry : products.entrySet()) {
            double productPrice = basket.getProductPrice(entry.getKey().getId());

            discountProducts.add(new DiscountBasket.DiscountProduct(entry.getKey().getId(), entry.getKey().getCategory(), entry.getValue(), entry.getValue() * productPrice));
        }


        DiscountBasket discountBasket = new DiscountBasket(discountProducts);


        for (DiscountType discount: discountTypes.values()) {
            discountBasket = discount.getDiscountedBasket(user, discountBasket);
        }

        return discountBasket.getTotalPrice();
    }

    public String getDetails() {
        return details;
    }

    public Map<Integer, DiscountType> getDiscountTypes() {
        return discountTypes;
    }

    @Override
    public String toString() {
        String output = "";
        for (DiscountType type : discountTypes.values()) {
            output += type.toString() + "\n";
        }
        return output;
    }

    public int addSimpleDiscountType(SimpleDiscount type) {
        int id;
        if (type.getId() == -1) {
            id = nextId;
            nextId++;
            type.setId(id);
            discountTypes.put(id, type);
            discountTypeIDs.put(id, "simple");
            DAOManager.addDiscountTypeToPolicy(this, type);
        } else {
            if (discountTypes.keySet().isEmpty()) nextId = type.getId() + 1;
            else nextId = Math.max(Collections.max(discountTypes.keySet()) + 1, type.getId() + 1);
            id = type.getId();
            discountTypes.put(id, type);
            discountTypeIDs.put(id, "simple");
        }

        return id;
    }

    private void persistSubtypes(AdvancedDiscount type) {
        List<DiscountType> types = type.getDiscounts();
        for (DiscountType subType : types) {
            if (subType instanceof AdvancedDiscount) persistSubtypes((AdvancedDiscount) subType); // recursive
            subType.setId(nextId);
            nextId++;
            DAOManager.addDiscountTypeToPolicy(this, subType);
        }
    }

    public int addAdvancedDiscountType(AdvancedDiscount type, boolean fromExisting) {
        int id;
        if (type.getId() == -1) { // type never seen before, not in database
            id = nextId;
            nextId++;
            type.setId(id);
            discountTypes.put(id, type);
            discountTypeIDs.put(id, "advanced");

            // if subtypes dont exist here, persist them
            if (!fromExisting) persistSubtypes(type);
            DAOManager.addDiscountTypeToPolicy(this, type);

        } else { // type is loaded from database
            if (discountTypes.keySet().isEmpty()) nextId = type.getId() + 1;
            else nextId = Math.max(Collections.max(discountTypes.keySet()) + 1, type.getId() + 1);
            id = type.getId();
            discountTypes.put(id, type);
            discountTypeIDs.put(id, "advanced");
        }
        return id;
    }

    public IntActionResultDto createAdvancedDiscountTypeFromExisting(List<Integer> subTypeIds, String logicalOperationStr) {
        synchronized (discountTypes) {
            List<DiscountType> relevantDiscountTypes = new ArrayList<>();
            for (Integer typeID : subTypeIds) {
                if (discountTypes.get(typeID) == null) return new IntActionResultDto(ResultCode.ERROR_STORE_DISCOUNT_POLICY_CHANGE, "There is no discount type ID" + typeID, -1);
                relevantDiscountTypes.add(discountTypes.get(typeID));
            }

            // remove buying types to create one advanced out of all of them
            for (Integer typeID : subTypeIds) {
                discountTypes.remove(typeID);
                discountTypeIDs.remove(typeID);
            }


            AdvancedDiscount.LogicalOperation logicalOperation = AdvancedDiscount.LogicalOperation.AND;
            if (logicalOperationStr.toLowerCase().equals("or")) logicalOperation = AdvancedDiscount.LogicalOperation.OR;
            else if (logicalOperationStr.toLowerCase().equals("xor")) logicalOperation = AdvancedDiscount.LogicalOperation.XOR;
            DiscountType advanced = new AdvancedDiscount.LogicalDiscount(relevantDiscountTypes, logicalOperation);
            return new IntActionResultDto(ResultCode.SUCCESS, null, addAdvancedDiscountType((AdvancedDiscount) advanced, true));
        }
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public DiscountPolicyActionResultDTO getDTO() {
        List<DiscountTypeDTO> dtos = new ArrayList<>();
        for (Integer id : discountTypes.keySet()) {
            DiscountType type = discountTypes.get(id);
            dtos.add(new DiscountTypeDTO(id, type.toString()));
        }
        return new DiscountPolicyActionResultDTO(ResultCode.SUCCESS, null, dtos);
    }

    public void clearDiscountTypes() {
        discountTypes.clear();
        discountTypeIDs.clear();
    }

    public void removeDiscountType(int discountTypeID) {
        discountTypes.remove(discountTypeID);
        discountTypeIDs.remove(discountTypeID);
    }


    public int getId() {
        return id;
    }

    public HashMap<Integer, String> getDiscountTypeIDs() {
        return discountTypeIDs;
    }
}
