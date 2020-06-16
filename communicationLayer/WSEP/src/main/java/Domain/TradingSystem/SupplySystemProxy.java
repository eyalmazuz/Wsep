package Domain.TradingSystem;

import DTOs.ActionResultDTO;
import DTOs.IntActionResultDto;
import DTOs.ResultCode;
import Domain.ISupplySystem;

public class SupplySystemProxy implements ISupplySystem {

    private ISupplySystem supplySystem = null;

    public static boolean testing = false;
    public static boolean succeedSupply = true;

    private static int fakeTransactionId = 10000;


    @Override
    public boolean handshake() {
        if (supplySystem == null) return false;
        return supplySystem.handshake();
    }

    @Override
    public IntActionResultDto requestSupply(String name, String address, String city, String country, String zip) {
        if (testing) {
            int transactionId = -1;
            if (succeedSupply) {
                transactionId = fakeTransactionId;
                fakeTransactionId++;
            }
            if (transactionId == -1) return new IntActionResultDto(ResultCode.ERROR_SUPPLY_DENIED, null, transactionId);
            else return new IntActionResultDto(ResultCode.SUCCESS, null, transactionId);
        }

        if (supplySystem != null && supplySystem.handshake())
            return supplySystem.requestSupply(name, address, city, country, zip);
        return new IntActionResultDto(ResultCode.ERROR_SUPPLY_SYSTEM_UNAVAILABLE, "Could not contact the supply system. Please try again later.", -1);
    }

    @Override
    public ActionResultDTO cancelSupply(int transactionId) {
        if (testing) {
            return new ActionResultDTO(ResultCode.SUCCESS, null);
        }

        if(supplySystem !=null && supplySystem.handshake()) return supplySystem.cancelSupply(transactionId);

        return new ActionResultDTO(ResultCode.ERROR_SUPPLY_SYSTEM_UNAVAILABLE, "Could not contact supply system. Please try again later.");
    }

    public void setSupplySystem(ISupplySystem supplySystem) {
        this.supplySystem = supplySystem;
    }

}
