package Domain.TradingSystem;

public class SimpleDiscount implements DiscountType {

    protected int id = -1;

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

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }

}
