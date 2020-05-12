package Domain.TradingSystem;

public class UserBuyingConstraint extends SimpleBuying {

    public class NotOutsideCountryConstraint extends UserBuyingConstraint {

        private String validCountry;

        public NotOutsideCountryConstraint(String validCountry) {
            this.validCountry = validCountry;
        }

        public boolean canBuy(String country) {
            return country.equals(validCountry);
        }
    }

}
