package Domain.TradingSystem;

import DTOs.ActionResultDTO;
import DTOs.ResultCode;

public class UserBuyingConstraint extends SimpleBuying {

    public ActionResultDTO canBuy(String country) {
        return new ActionResultDTO(ResultCode.SUCCESS, null);
    }

    public static class NotOutsideCountryConstraint extends UserBuyingConstraint {

        private String validCountry;

        public NotOutsideCountryConstraint(String validCountry) {
            this.validCountry = validCountry;
        }

        public ActionResultDTO canBuy(String country) {
            return country.equals(validCountry) ? new ActionResultDTO(ResultCode.SUCCESS, null) :
                    new ActionResultDTO(ResultCode.ERROR_PURCHASE, "This purchase is only available in " + validCountry);
        }
    }

}
