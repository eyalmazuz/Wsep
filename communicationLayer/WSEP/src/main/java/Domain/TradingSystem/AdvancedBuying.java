package Domain.TradingSystem;

import sun.rmi.runtime.Log;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class AdvancedBuying implements BuyingType {

    public enum LogicalOperation {
        OR,
        AND,
        XOR
    }

    public boolean canBuy(User user, ShoppingBasket basket) {
        return true;
    }

    public static class LogicalBuying extends AdvancedBuying {
        private List<BuyingType> buyingConstraints;
        private LogicalOperation type;

        public LogicalBuying(List<BuyingType> buyingConstraints, LogicalOperation type) {
            this.buyingConstraints = buyingConstraints;
            this.type = type;
        }

        @Override
        public boolean canBuy(User user, ShoppingBasket basket) {
            boolean[] canBuys = new boolean[buyingConstraints.size()];
            int i=0;
            for (BuyingType constraint: buyingConstraints) {
                canBuys[i] = constraint.canBuy(user, basket);
                i++;
            }
            Stream<Boolean> stream = IntStream.range(0, canBuys.length).mapToObj(idx -> canBuys[idx]);
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
