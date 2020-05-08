package DTOs;

import DTOs.SimpleDTOS.StoreDTO;
import Domain.TradingSystem.Store;

import java.util.List;

public class StoreActionResultDTO extends ActionResultDTO {
    List<StoreDTO> stores;

    public StoreActionResultDTO(ResultCode resultCode, String details, List<StoreDTO> stores) {
        super(resultCode, details);
        this.stores = stores;
    }

    public List<StoreDTO> getStores() {
        return stores;
    }

    public void setStores(List<StoreDTO> stores) {
        this.stores = stores;
    }

    @Override
    public String toString() {
        String info = "";
        for (StoreDTO store: stores) {
            info += store.toString() + "\n--------------------------\n";
        }

        return info;
    }
}
