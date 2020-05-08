package DTOs;

import DTOs.SimpleDTOS.ShoppingBasketDTO;

import java.util.List;

public class ShoppingCartDTO {
    ResultCode resultCode;
    String details;
    List<ShoppingBasketDTO> baskets;

    public ShoppingCartDTO(ResultCode resultCode,String details, List<ShoppingBasketDTO> baskets) {
        this.resultCode = resultCode;
        this.details = details;
        this.baskets = baskets;
    }

    public ResultCode getResultCode() {
        return resultCode;
    }

    public void setResultCode(ResultCode resultCode) {
        this.resultCode = resultCode;
    }

    public List<ShoppingBasketDTO> getBaskets() {
        return baskets;
    }

    public void setBaskets(List<ShoppingBasketDTO> baskets) {
        this.baskets = baskets;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
