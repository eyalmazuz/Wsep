package DTOs;


import DTOs.SimpleDTOS.DiscountTypeDTO;

import java.util.List;

public class DiscountPolicyActionResultDTO extends ActionResultDTO {
    List<DiscountTypeDTO> dtos;

    public DiscountPolicyActionResultDTO(ResultCode code, String details, List<DiscountTypeDTO> dtos) {
        super(code, details);
        this.dtos = dtos;
    }

    public List<DiscountTypeDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<DiscountTypeDTO> dtos) {
        this.dtos = dtos;
    }

    public String toString() {
        String output = "";
        for (DiscountTypeDTO dto : dtos) output += dto.toString() + "\n";
        return output;
    }
}
