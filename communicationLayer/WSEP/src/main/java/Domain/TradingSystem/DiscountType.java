package Domain.TradingSystem;

import Domain.Util.Pair;

import java.util.List;
import java.util.Map;

public interface DiscountType {

    DiscountBasket getDiscountedBasket(User user, DiscountBasket discountBasket);
}
