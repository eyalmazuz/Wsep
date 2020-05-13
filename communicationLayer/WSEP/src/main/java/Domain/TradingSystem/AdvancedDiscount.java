package Domain.TradingSystem;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class AdvancedDiscount implements DiscountType {

    public enum LogicalOperation {
        OR,
        AND,
        XOR
    }

    @Override
    public boolean isEligible() {
        return true;
    }

    public static class LogicalDiscount extends AdvancedDiscount {
        private List<DiscountType> discounts;
        private LogicalOperation type;

        public LogicalDiscount(List<DiscountType> discounts, LogicalOperation type) {
            this.discounts = discounts;
            this.type = type;
        }

        @Override
        public boolean isEligible() {
            boolean[] isEligibles = new boolean[discounts.size()];
            int i=0;
            for (DiscountType discount: discounts) {
                isEligibles[i] = discount.isEligible();
                i++;
            }
            Stream<Boolean> stream = IntStream.range(0, isEligibles.length).mapToObj(idx -> isEligibles[idx]);
            if (type == LogicalOperation.OR) {
                return stream.reduce(false, (a,b) -> a||b);
            }
            else if (type == LogicalOperation.AND) {
                return stream.reduce(true, (a,b) -> a&&b);
            }
            else if (type == LogicalOperation.XOR) {
                return stream.reduce(true, (a,b) -> a^b);
            }
            return false;
        }
    }

}
