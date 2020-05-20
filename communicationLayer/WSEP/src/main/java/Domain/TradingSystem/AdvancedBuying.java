package Domain.TradingSystem;

import DTOs.ActionResultDTO;
import DTOs.ResultCode;


import javax.xml.transform.Result;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class AdvancedBuying implements BuyingType {

    public enum LogicalOperation {
        OR,
        AND,
        XOR,
        IMPLIES
    }

    public ActionResultDTO canBuy(User user, ShoppingBasket basket) {
        return new ActionResultDTO(ResultCode.SUCCESS, null);
    }

    public static class LogicalBuying extends AdvancedBuying {
        private List<BuyingType> buyingConstraints;
        private LogicalOperation type;

        public LogicalBuying(List<BuyingType> buyingConstraints, LogicalOperation type) {
            this.buyingConstraints = buyingConstraints;
            this.type = type;
        }

        @Override
        public ActionResultDTO canBuy(User user, ShoppingBasket basket) {
            boolean[] canBuys = new boolean[buyingConstraints.size()];
            int i=0;
            for (BuyingType constraint: buyingConstraints) {
                canBuys[i] = constraint.canBuy(user, basket).getResultCode() == ResultCode.SUCCESS;
                i++;
            }
            Stream<Boolean> stream = IntStream.range(0, canBuys.length).mapToObj(idx -> canBuys[idx]);
            if (type == LogicalOperation.OR) {
                boolean success = stream.reduce(false, (a, b) -> a || b);
                if (success) return new ActionResultDTO(ResultCode.SUCCESS, null);
                String errorStrings = "OR condition not satisfied. Policy fails:\n";
                for (BuyingType constraint : buyingConstraints) {
                    ActionResultDTO result = constraint.canBuy(user, basket);
                    if (result.getResultCode() != ResultCode.SUCCESS) {
                        errorStrings += result.getDetails() + "\n";
                    }
                }
                return new ActionResultDTO(ResultCode.ERROR_PURCHASE, errorStrings);
            }
            else if (type == LogicalOperation.AND) {
                boolean success = stream.reduce(true, (a,b) -> a && b);
                if (success) return new ActionResultDTO(ResultCode.SUCCESS, null);
                String errorStrings = "AND condition not satisfied. Policy fails:\n";
                for (BuyingType constraint : buyingConstraints) {
                    ActionResultDTO result = constraint.canBuy(user, basket);
                    if (result.getResultCode() != ResultCode.SUCCESS) {
                        errorStrings += result.getDetails() + "\n";
                    }
                }
                return new ActionResultDTO(ResultCode.ERROR_PURCHASE, errorStrings);
            }
            else if (type == LogicalOperation.XOR) {
                boolean success = stream.reduce(false, (a,b) -> a^b);
                if (success) return new ActionResultDTO(ResultCode.SUCCESS, null);
                String errorStrings = "XOR condition not satisfied. Policy fails:\n";
                for (BuyingType constraint : buyingConstraints) {
                    ActionResultDTO result = constraint.canBuy(user, basket);
                    if (result.getResultCode() != ResultCode.SUCCESS) {
                        errorStrings += result.getDetails() + "\n";
                    }
                }
                return new ActionResultDTO(ResultCode.ERROR_PURCHASE, errorStrings);
            } else if (type == LogicalOperation.IMPLIES) { // a -> b = (not a) or b
                boolean success = !canBuys[0] || canBuys[1];
                if (success) return new ActionResultDTO(ResultCode.SUCCESS, null);
                String errorStrings = "IMPLIES condition not satisfied. Policy fails:\n";
                for (BuyingType constraint : buyingConstraints) {
                    ActionResultDTO result = constraint.canBuy(user, basket);
                    if (result.getResultCode() != ResultCode.SUCCESS) {
                        errorStrings += result.getDetails() + "\n";
                    }
                }
                return new ActionResultDTO(ResultCode.ERROR_PURCHASE, errorStrings);
            }
            return new ActionResultDTO(ResultCode.ERROR_PURCHASE, null);
        }

        public String toString() {
            String output = "";
            for (int i = 0; i < buyingConstraints.size(); i++) {
                output += buyingConstraints.get(i).toString();
                if (i < buyingConstraints.size() - 1) output += (" " + type.toString() + " ");
            }
            return output;
        }
    }


}
