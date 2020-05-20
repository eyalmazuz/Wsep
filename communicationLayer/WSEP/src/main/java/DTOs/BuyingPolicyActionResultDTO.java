package DTOs;

import DTOs.SimpleDTOS.BuyingTypeDTO;
import DTOs.SimpleDTOS.ShoppingBasketDTO;

import java.util.List;

public class BuyingPolicyActionResultDTO extends ActionResultDTO {
    List<BuyingTypeDTO> dtos;

    public BuyingPolicyActionResultDTO(ResultCode code, String details, List<BuyingTypeDTO> dtos) {
        super(code, details);
        this.dtos = dtos;
    }

    public List<BuyingTypeDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<BuyingTypeDTO> dtos) {
        this.dtos = dtos;
    }

    public String toString() {
        String output = "";
        for (BuyingTypeDTO dto : dtos) output += dto.toString() + "\n";
        return output;
    }
}
