package Domain.TradingSystem;

public class SimpleBuying implements BuyingType {


    public SimpleBuying() {

    }

    public boolean canBuy(User user, ShoppingBasket basket) {
        if (this instanceof BasketBuyingConstraint) {
            BasketBuyingConstraint c = (BasketBuyingConstraint) this;
            return c.canBuy(basket);
        }
        if (this instanceof SystemBuyingConstraint) {
            SystemBuyingConstraint c = (SystemBuyingConstraint) this;
            return c.canBuy();
        }
        if (this instanceof UserBuyingConstraint) {
            UserBuyingConstraint c = (UserBuyingConstraint) this;
            return c.canBuy(user.getCountry());
        }
        return true;
    }
}

