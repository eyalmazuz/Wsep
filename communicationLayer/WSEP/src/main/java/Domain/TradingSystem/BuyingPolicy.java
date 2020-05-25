package Domain.TradingSystem;

import DTOs.ActionResultDTO;
import DTOs.BuyingPolicyActionResultDTO;
import DTOs.IntActionResultDto;
import DTOs.ResultCode;
import DTOs.SimpleDTOS.BuyingTypeDTO;
import DataAccess.DAOManager;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.*;

@DatabaseTable(tableName = "buyingPolicies")
public class BuyingPolicy {

    // This class defines a purchase policy for a store

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String details;

    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private HashMap<Integer, String> buyingTypeIDs = new HashMap<>();

    private Map<Integer, BuyingType> buyingTypes = new HashMap<>();
    private static int nextId = 0;

    public BuyingPolicy () { }

    public BuyingPolicy(String details) {
        this.details = details;
    }

    public ActionResultDTO isAllowed(User user, ShoppingBasket basket) {
        if (details.equals("No one is allowed")) return new ActionResultDTO(ResultCode.ERROR_PURCHASE, "No one is allowed to buy at this store.");
        String buyingPolicyErrors = "";
        boolean error = false;
        for (BuyingType type : buyingTypes.values()) {
            ActionResultDTO buyingConstraintResult = type.canBuy(user, basket);
            if (buyingConstraintResult.getResultCode() != ResultCode.SUCCESS) {
                error = true;
                buyingPolicyErrors += buyingConstraintResult.getDetails() + "\n";
            }
        }
        if (error) return new ActionResultDTO(ResultCode.ERROR_PURCHASE, buyingPolicyErrors);
        return new ActionResultDTO(ResultCode.SUCCESS, null);
    }

    @Override
    public String toString() {
        String output = "";
        for (BuyingType type : buyingTypes.values()) {
            output += type.toString() + "\n";
        }
        return output;
    }

    public int addBuyingType(BuyingType type) {
        int id = nextId;
        nextId++;
        addBuyingType(id, type);
        DAOManager.addBuyingTypeToPolicy(this, id, type);
        return id;
}

    public void addBuyingType(int id, BuyingType type) {
        buyingTypes.put(id, type);
        String typeStr = "simple";
        if (type instanceof AdvancedBuying) typeStr = "advanced";
        buyingTypeIDs.put(id, typeStr);
    }

    public Map<Integer, BuyingType> getBuyingTypes() {
        return buyingTypes;
    }

    public void clearBuyingTypes() {
        buyingTypes.clear();
        buyingTypeIDs.clear();
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void removeBuyingType(int buyingTypeID) {
        buyingTypes.remove(buyingTypeID);
        buyingTypeIDs.remove(buyingTypeID);
    }

    public IntActionResultDto createAdvancedBuyingType(List<Integer> buyingTypeIDs, String logicalOperationStr) {
        synchronized (buyingTypes) {
            List<BuyingType> relevantBuyingTypes = new ArrayList<>();
            for (Integer typeID : buyingTypeIDs) {
                if (buyingTypes.get(typeID) == null) return new IntActionResultDto(ResultCode.ERROR_STORE_BUYING_POLICY_CHANGE, "There is no buying type ID" + typeID, -1);
                relevantBuyingTypes.add(buyingTypes.get(typeID));
            }
            // remove buying types to create one advanced out of all of them
            for (Integer typeID : buyingTypeIDs) {
                BuyingType type = buyingTypes.get(typeID);
                buyingTypes.remove(typeID);

                // PERSIST the type so that once reloaded, we can create advanced buying types
                DAOManager.addBuyingTypeToPolicy(this, typeID, type);

                buyingTypeIDs.remove(typeID);
            }

            AdvancedBuying.LogicalOperation logicalOperation = AdvancedBuying.LogicalOperation.AND;
            if (logicalOperationStr.toLowerCase().equals("or")) logicalOperation = AdvancedBuying.LogicalOperation.OR;
            else if (logicalOperationStr.toLowerCase().equals("xor")) logicalOperation = AdvancedBuying.LogicalOperation.XOR;
            else if (logicalOperationStr.toLowerCase().equals("implies")) logicalOperation = AdvancedBuying.LogicalOperation.IMPLIES;
            BuyingType advanced = new AdvancedBuying.LogicalBuying(relevantBuyingTypes, logicalOperation);
            return new IntActionResultDto(ResultCode.SUCCESS, null, addBuyingType(advanced));
        }
    }

    public BuyingPolicyActionResultDTO getDTO() {
        List<BuyingTypeDTO> dtos = new ArrayList<>();
        for (Integer id : buyingTypes.keySet()) {
            BuyingType type = buyingTypes.get(id);
            dtos.add(new BuyingTypeDTO(id, type.toString()));
        }
        return new BuyingPolicyActionResultDTO(ResultCode.SUCCESS, null, dtos);
    }

    public HashMap<Integer, String> getBuyingTypeIDs() {
        return buyingTypeIDs;
    }
}
