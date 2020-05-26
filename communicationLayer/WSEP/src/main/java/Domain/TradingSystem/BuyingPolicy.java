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

    public int addSimpleBuyingType(SimpleBuying type) {
        int id;
        if (type.getId() == -1) {
            id = nextId;
            nextId++;
            type.setId(id);
            buyingTypes.put(id, type);
            buyingTypeIDs.put(id, "simple");
            DAOManager.addBuyingTypeToPolicy(this, type);
        } else {
            if (buyingTypes.keySet().isEmpty()) nextId = type.getId() + 1;
            else nextId = Math.max(Collections.max(buyingTypes.keySet()) + 1, type.getId() + 1);
            id = type.getId();
            buyingTypes.put(id, type);
            buyingTypeIDs.put(id, "simple");
        }

        return id;
    }

    private void persistSubtypes(AdvancedBuying type) {
        List<BuyingType> types = type.getBuyingConstraints();
        for (BuyingType subType : types) {
            if (subType instanceof AdvancedBuying) persistSubtypes((AdvancedBuying) subType); // recursive
            subType.setId(nextId);
            nextId++;
            DAOManager.addBuyingTypeToPolicy(this, subType);
        }
    }

    public int addAdvancedBuyingType(AdvancedBuying type, boolean fromExisting) {
        int id;
        if (type.getId() == -1) { // type never seen before, not in database
            id = nextId;
            nextId++;
            type.setId(id);
            buyingTypes.put(id, type);
            buyingTypeIDs.put(id, "advanced");

            // if subtypes dont exist here, persist them
            if (!fromExisting) persistSubtypes(type);
            DAOManager.addBuyingTypeToPolicy(this, type);

        } else { // type is loaded from database
            if (buyingTypes.keySet().isEmpty()) nextId = type.getId() + 1;
            else nextId = Math.max(Collections.max(buyingTypes.keySet()) + 1, type.getId() + 1);
            id = type.getId();
            buyingTypes.put(id, type);
            buyingTypeIDs.put(id, "advanced");
        }
        return id;
    }

    public IntActionResultDto createAdvancedBuyingTypeFromExisting(List<Integer> subTypeIds, String logicalOperationStr) {
        synchronized (buyingTypes) {
            List<BuyingType> relevantBuyingTypes = new ArrayList<>();
            for (Integer typeID : subTypeIds) {
                if (buyingTypes.get(typeID) == null) return new IntActionResultDto(ResultCode.ERROR_STORE_BUYING_POLICY_CHANGE, "There is no buying type ID" + typeID, -1);
                relevantBuyingTypes.add(buyingTypes.get(typeID));
            }

            // remove buying types to create one advanced out of all of them
            for (Integer typeID : subTypeIds) {
                buyingTypes.remove(typeID);
                buyingTypeIDs.remove(typeID);
            }


            AdvancedBuying.LogicalOperation logicalOperation = AdvancedBuying.LogicalOperation.AND;
            if (logicalOperationStr.toLowerCase().equals("or")) logicalOperation = AdvancedBuying.LogicalOperation.OR;
            else if (logicalOperationStr.toLowerCase().equals("xor")) logicalOperation = AdvancedBuying.LogicalOperation.XOR;
            else if (logicalOperationStr.toLowerCase().equals("implies")) logicalOperation = AdvancedBuying.LogicalOperation.IMPLIES;
            BuyingType advanced = new AdvancedBuying.LogicalBuying(relevantBuyingTypes, logicalOperation);
            return new IntActionResultDto(ResultCode.SUCCESS, null, addAdvancedBuyingType((AdvancedBuying) advanced, true));
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

    public Map<Integer, BuyingType> getBuyingTypes() {
        return buyingTypes;
    }

    public void clearBuyingTypes() {
        buyingTypes.clear();
        buyingTypeIDs.clear();
    }

    public void removeBuyingType(int buyingTypeID) {
        buyingTypes.remove(buyingTypeID);
        buyingTypeIDs.remove(buyingTypeID);
    }

    public HashMap<Integer, String> getBuyingTypeIDs() {
        return buyingTypeIDs;
    }

    public void setDetails(String details) {
        this.details = details;
    }

}
