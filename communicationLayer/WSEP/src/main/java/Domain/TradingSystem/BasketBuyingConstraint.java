package Domain.TradingSystem;

import java.util.Map;

public class BasketBuyingConstraint extends SimpleBuying {

    public boolean canBuy(ShoppingBasket basket) {
        return true;
    }

    public static class MaxAmountForProductConstraint extends BasketBuyingConstraint {

        private ProductInfo productInfo;
        private int maxAmount;

        public MaxAmountForProductConstraint(ProductInfo productInfo, int maxAmount) {
            this.productInfo = productInfo;
            this.maxAmount = maxAmount;
        }

        public boolean canBuy(ShoppingBasket basket) {
            return basket.getProducts().get(productInfo) <= maxAmount;
        }
    }

    public static class MinAmountForProductConstraint extends BasketBuyingConstraint {

        private ProductInfo productInfo;
        private int minAmount;

        public MinAmountForProductConstraint(ProductInfo productInfo, int minAmount) {
            this.productInfo = productInfo;
            this.minAmount = minAmount;
        }

        public boolean canBuy(ShoppingBasket basket) {
            return basket.getProducts().get(productInfo) >= minAmount;
        }
    }

    public static class MaxProductAmountConstraint extends BasketBuyingConstraint {

        private int maxAmount;

        public MaxProductAmountConstraint(int maxAmount) {
            this.maxAmount = maxAmount;
        }

        public boolean canBuy(ShoppingBasket basket) {
            int totalAmount = 0;
            Map<ProductInfo, Integer> products = basket.getProducts();
            for (Integer productAmount: products.values()) {
                totalAmount += productAmount;
                if (totalAmount > maxAmount) return false;
            }
            return true;
        }
    }

}
