package Domain.TradingSystem;

import Domain.Util.Pair;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class AdvancedDiscount implements DiscountType {

    public enum LogicalOperation {
        OR,
        AND,
        XOR
    }

    @Override
    public Pair<Map<Integer, Integer>, Integer> getDiscountedBasket(User user, ShoppingBasket basket) {
        return null;
    }

    public static class LogicalDiscount extends AdvancedDiscount {
        private List<DiscountType> discounts;
        private LogicalOperation type;

        public LogicalDiscount(List<DiscountType> discounts, LogicalOperation type) {
            this.discounts = discounts;
            this.type = type;
        }

        @Override
        public Pair<Map<Integer, Integer>, Integer> getDiscountedBasket(User user, ShoppingBasket basket) {
            if (type == LogicalOperation.OR) {
                if (!discounts.isEmpty()) return discounts.get(0).getDiscountedBasket(user, basket);
            }
            else if (type == LogicalOperation.AND) {
                //TODO: IMPLEMENT
                return null;
            }
            else if (type == LogicalOperation.XOR) {
                if (!discounts.isEmpty()) return discounts.get(0).getDiscountedBasket(user, basket);
            }
            return null;
        }
    }

}
