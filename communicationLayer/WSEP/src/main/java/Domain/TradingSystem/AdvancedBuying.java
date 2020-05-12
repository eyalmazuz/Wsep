package Domain.TradingSystem;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

enum LogicalOperation {
    OR,
    AND,
    XOR
}

public class AdvancedBuying extends SimpleBuying {

    private List<BuyingType> buyingConstraints;

    public class LogicalBuying extends AdvancedBuying {
        private List<BuyingType> buyingConstraints;
        private LogicalOperation type;

        public LogicalBuying(List<BuyingType> buyingConstraints, LogicalOperation type) {
            this.buyingConstraints = buyingConstraints;
            this.type = type;
        }

        @Override
        public boolean canBuy(User user, int storeId) {
            boolean[] canBuys = new boolean[buyingConstraints.size()];
            int i=0;
            for (BuyingType constraint: buyingConstraints) {
                canBuys[i] = constraint.canBuy(user, storeId);
                i++;
            }
            Stream<Boolean> stream = IntStream.range(9, canBuys.length).mapToObj(idx -> canBuys[idx]);
            if (type == LogicalOperation.OR) {
                return stream.reduce(true, (a,b) -> a||b);
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
