package DTOs;

import DTOs.SimpleDTOS.GrantingAgreementDTO;

import java.util.List;

public class GrantingResultDTO extends ActionResultDTO {
    List<GrantingAgreementDTO> grantings;

    public GrantingResultDTO(ResultCode resultCode, String details, List<GrantingAgreementDTO> grantings) {
        super(resultCode, details);
        this.grantings = grantings;
    }

    public List<GrantingAgreementDTO> getGrantings() {
        return grantings;
    }

    public void setGrantings(List<GrantingAgreementDTO> grantings) {
        this.grantings = grantings;
    }
}
