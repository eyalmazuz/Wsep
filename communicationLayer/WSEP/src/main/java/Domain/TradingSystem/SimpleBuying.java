package Domain.TradingSystem;

import java.util.ArrayList;
import java.util.List;

public class SimpleBuying implements BuyingType {

    List<BasketBuyingConstraint> basketConstraints = new ArrayList<>();

    public SimpleBuying() {

    }

    public boolean canBuy(User user, int storeId) {
        if (!basketConstraints.isEmpty()) {
            ShoppingBasket basket = null;
            for (ShoppingBasket b : user.getShoppingCart().getBaskets()) {
                if (b.getStoreId() == storeId) {
                    basket = b;
                    break;
                }
            }
            if (basket == null) return false;

            for (BasketBuyingConstraint c : basketConstraints) {
                if (!c.canBuy(basket)) return false;
            }
        }
        return true;
    }


}


