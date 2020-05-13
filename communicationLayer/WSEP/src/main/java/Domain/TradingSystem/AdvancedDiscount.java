package Domain.TradingSystem;

public class AdvancedDiscount implements DiscountType {

    @Override
    public boolean isEligible() {
        return true;
    }
}
