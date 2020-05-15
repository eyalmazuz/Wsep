package Domain.TradingSystem;

public class SimpleDiscount implements DiscountType {

    public SimpleDiscount() {

    }

    @Override
    public DiscountBasket getDiscountedBasket(User user, DiscountBasket discountBasket) {
        if (this instanceof ProductDiscount) {
            ProductDiscount pd = (ProductDiscount) this;
            return pd.getDiscountedBasket(user, discountBasket);
        }

        return discountBasket;
    }
}
