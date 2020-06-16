package Domain.TradingSystem;

import DTOs.ActionResultDTO;
import DTOs.IntActionResultDto;
import DTOs.ResultCode;
import Domain.ISupplySystem;

public class SupplyHandler {
    private final String config;
    private SupplySystemProxy supplySystemProxy;

    public SupplyHandler(String config) throws Exception {
        if (config.equals("Error")){
            throw new Exception("Failed To connect Supply Handler");
        }
        supplySystemProxy = new SupplySystemProxy();
        this.config = config;
    }

    public SupplyHandler(String config, ISupplySystem supplySystem) throws Exception {
        this(config);
        this.supplySystemProxy.setSupplySystem(supplySystem);
    }

    public IntActionResultDto requestSupply(String name, String address, String city, String country, String zip) {
        if (config.equals("No supplies")) return new IntActionResultDto(ResultCode.ERROR_SUPPLY_DENIED, "Supply config does not allow supplies.", -1);
        return supplySystemProxy.requestSupply(name, address, city, country, zip);
    }

    public ActionResultDTO cancelSupply(int transactionId) {
        return supplySystemProxy.cancelSupply(transactionId);
    }
}
