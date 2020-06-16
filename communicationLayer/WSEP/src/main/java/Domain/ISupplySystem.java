package Domain;

import DTOs.ActionResultDTO;
import DTOs.IntActionResultDto;

public interface ISupplySystem {
    boolean handshake();
    IntActionResultDto requestSupply(String name, String address, String city, String country, String zip);
    ActionResultDTO cancelSupply(int transactionId);
}
