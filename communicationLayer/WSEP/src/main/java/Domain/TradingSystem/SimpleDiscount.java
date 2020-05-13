package Domain.TradingSystem;

import Domain.Util.Pair;

import java.util.Map;

public class SimpleDiscount implements DiscountType {

    @Override
    public Pair<Map<Integer, Integer>, Integer> getDiscountedBasket(User user, ShoppingBasket basket) {
        return null;
    }
}
