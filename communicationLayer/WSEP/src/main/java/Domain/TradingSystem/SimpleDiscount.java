package Domain.TradingSystem;

public class SimpleDiscount implements DiscountType {

    @Override
    public boolean isEligible() {
        return true;
    }
}
