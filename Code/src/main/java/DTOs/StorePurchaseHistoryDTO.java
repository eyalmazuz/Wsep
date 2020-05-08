package DTOs;

import DTOs.SimpleDTOS.PurchaseDetailsDTO;

import java.util.List;

public class StorePurchaseHistoryDTO extends ActionResultDTO {
    int storeId;
    List<PurchaseDetailsDTO> purchases;

    public StorePurchaseHistoryDTO(ResultCode resultCode, String details, int storeId, List<PurchaseDetailsDTO> purchases) {
        super(resultCode, details);
        this.storeId = storeId;
        this.purchases = purchases;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public List<PurchaseDetailsDTO> getPurchases() {
        return purchases;
    }

    public void setPurchases(List<PurchaseDetailsDTO> purchases) {
        this.purchases = purchases;
    }
}
