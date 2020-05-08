package DTOs;

import DTOs.SimpleDTOS.PurchaseDetailsDTO;
import Domain.TradingSystem.PurchaseDetails;
import Domain.TradingSystem.Store;

import java.util.List;
import java.util.Map;

public class UserPurchaseHistoryDTO extends ActionResultDTO{
    private Map<Integer, List<PurchaseDetailsDTO>> store2details;

    public UserPurchaseHistoryDTO(ResultCode resultCode, String details, Map<Integer, List<PurchaseDetailsDTO>> store2details) {
        super(resultCode, details);
        this.store2details = store2details;
    }

    public Map<Integer, List<PurchaseDetailsDTO>> getStore2details() {
        return store2details;
    }

    public void setStore2details(Map<Integer, List<PurchaseDetailsDTO>> store2details) {
        this.store2details = store2details;
    }

    @Override
    public String toString() {
        String output = "";
        for (Map.Entry<Integer, List<PurchaseDetailsDTO>> purchase: store2details.entrySet()) {
            output += "Basket Purchase for store ID: " + purchase.getKey() + "\n";
            for(PurchaseDetailsDTO p: purchase.getValue()){
                output += p.toString() + "\n";
            }

        }
        return output;
    }
}
