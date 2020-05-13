package Domain.TradingSystem;

import Domain.Util.Pair;

import java.util.Map;

public interface  DiscountType {

    Pair<Map<Integer, Integer>, Integer> getDiscountedBasket(User user, ShoppingBasket basket);
}
