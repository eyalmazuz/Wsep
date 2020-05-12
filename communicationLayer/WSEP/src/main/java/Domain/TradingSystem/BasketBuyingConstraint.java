package Domain.TradingSystem;

import java.util.Map;

public class BasketBuyingConstraint extends SimpleBuying {

    public class MaxAmountForProductConstraint extends BasketBuyingConstraint {

        private int productId;
        private int maxAmount;

        public MaxAmountForProductConstraint(int productId, int maxAmount) {
            this.productId = productId;
            this.maxAmount = maxAmount;
        }

        public boolean canBuy(ShoppingBasket basket) {
            return basket.getProducts().get(productId) <= maxAmount;
        }
    }

    public class MinAmountForProductConstraint extends BasketBuyingConstraint {

        private int productId;
        private int minAmount;

        public MinAmountForProductConstraint(int productId, int minAmount) {
            this.productId = productId;
            this.minAmount = minAmount;
        }

        public boolean canBuy(ShoppingBasket basket) {
            return basket.getProducts().get(productId) >= minAmount;
        }
    }

    public class MaxProductAmountConstraint extends BasketBuyingConstraint {

        private int maxAmount;

        public MaxProductAmountConstraint(int maxAmount) {
            this.maxAmount = maxAmount;
        }

        public boolean canBuy(ShoppingBasket basket) {
            int totalAmount = 0;
            Map<Integer, Integer> products = basket.getProducts();
            for (Integer productAmount: products.values()) {
                totalAmount += productAmount;
                if (totalAmount >= maxAmount) return false;
            }
            return true;
        }
    }

}
