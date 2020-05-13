package Domain.TradingSystem;

import java.util.List;

public class AdvancedDiscount implements DiscountType {

    public enum LogicalOperation {
        OR,
        AND,
        XOR
    }

    @Override
    public DiscountBasket getDiscountedBasket(User user, DiscountBasket discountBasket) {
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
        public DiscountBasket getDiscountedBasket(User user, DiscountBasket discountBasket) {
            if (type == LogicalOperation.OR) {
                // return the best one for the customer
                double minPrice = Double.MAX_VALUE;
                DiscountBasket minimumBasket = null;

                for (DiscountType discount: discounts) {
                    DiscountBasket updatedDiscountBasket = discount.getDiscountedBasket(user, discountBasket);

                    if (updatedDiscountBasket != null) {
                        if (updatedDiscountBasket.getTotalPrice() < minPrice) {
                            minPrice = updatedDiscountBasket.getTotalPrice();
                            minimumBasket = updatedDiscountBasket;
                        }
                    }
                }
                return minimumBasket;
            }

            else if (type == LogicalOperation.AND) {
                DiscountBasket curDiscountBasket = discountBasket;

                for (DiscountType discount: discounts) {
                    curDiscountBasket = discount.getDiscountedBasket(user, curDiscountBasket);
                }
                return curDiscountBasket;
            }

            else if (type == LogicalOperation.XOR) {
                // return the best one for the customer
                double maxPrice = Double.MIN_VALUE;
                DiscountBasket maximumBasket = null;

                for (DiscountType discount: discounts) {
                    DiscountBasket updatedDiscountBasket = discount.getDiscountedBasket(user, discountBasket);

                    if (updatedDiscountBasket != null) {
                        if (updatedDiscountBasket.getTotalPrice() > maxPrice) {
                            maxPrice = updatedDiscountBasket.getTotalPrice();
                            maximumBasket = updatedDiscountBasket;
                        }
                    }
                }
                return maximumBasket;
            }

            return null;
        }
    }


}
