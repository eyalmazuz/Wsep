package Domain.TradingSystem;

import java.util.List;

public class AdvancedDiscount implements DiscountType {

    protected int id = -1;

    public enum LogicalOperation {
        OR,
        AND,
        XOR
    }

    protected List<DiscountType> discounts;
    protected AdvancedDiscount.LogicalOperation type;

    public AdvancedDiscount(List<DiscountType> discounts, AdvancedDiscount.LogicalOperation type) {
        this.discounts = discounts;
        this.type = type;
    }

    public List<DiscountType> getDiscounts() {
        return discounts;
    }

    public AdvancedDiscount.LogicalOperation getLogicalOperation() {
        return type;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }


    @Override
    public DiscountBasket getDiscountedBasket(User user, DiscountBasket discountBasket) {
        return discountBasket;
    }

    public static class LogicalDiscount extends AdvancedDiscount {

        public LogicalDiscount(List<DiscountType> discounts, LogicalOperation type) {
            super(discounts, type);
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
                // return the best one for the store
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

            return discountBasket;
        }

        public String toString() {
            String output = "";
            for (int i = 0; i < discounts.size(); i++) {
                output += discounts.get(i).toString();
                if (i < discounts.size() - 1) output += (" " + type.toString() + " ");
            }
            return output;
        }
    }


}
