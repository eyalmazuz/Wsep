package Domain.TradingSystem;

public interface DiscountType {

    DiscountBasket getDiscountedBasket(User user, DiscountBasket discountBasket);

    void setId(int id);

    int getId();

}
