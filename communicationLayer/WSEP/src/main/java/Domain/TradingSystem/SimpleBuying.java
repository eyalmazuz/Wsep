package Domain.TradingSystem;

import java.util.ArrayList;
import java.util.List;

public class SimpleBuying implements BuyingType {


    public SimpleBuying() {

    }

    public boolean canBuy(User user, ShoppingBasket basket) {
        if (this instanceof BasketBuyingConstraint) {
            BasketBuyingConstraint c = (BasketBuyingConstraint) this;
            return c.canBuy(basket);
        }
        return true;
    }
}

