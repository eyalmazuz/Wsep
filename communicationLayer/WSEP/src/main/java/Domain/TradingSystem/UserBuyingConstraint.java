package Domain.TradingSystem;

import DTOs.ActionResultDTO;
import DTOs.ResultCode;

public class UserBuyingConstraint extends SimpleBuying {

    protected String validCountry;

    public UserBuyingConstraint(String validCountry) {
        this.validCountry = validCountry;
    }

    public String getValidCountry() {
        return validCountry;
    }

    public ActionResultDTO canBuy(String country) {
        return new ActionResultDTO(ResultCode.SUCCESS, null);
    }

    public static class NotOutsideCountryConstraint extends UserBuyingConstraint {

        public NotOutsideCountryConstraint(String validCountry) {
            super(validCountry);
        }

        public ActionResultDTO canBuy(String country) {
            return country.equals(validCountry) ? new ActionResultDTO(ResultCode.SUCCESS, null) :
                    new ActionResultDTO(ResultCode.ERROR_PURCHASE, "This purchase is only available in " + validCountry);
        }

        public String toString() {
            return "Cannot buy outside of " + validCountry;
        }
    }

}
