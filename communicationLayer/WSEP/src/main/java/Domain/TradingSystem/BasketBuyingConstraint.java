package Domain.TradingSystem;

import DTOs.ActionResultDTO;
import DTOs.ResultCode;

import java.util.Map;

public class BasketBuyingConstraint extends SimpleBuying {

    public ActionResultDTO canBuy(ShoppingBasket basket) {
        return new ActionResultDTO(ResultCode.SUCCESS, null);
    }

    public static class MaxAmountForProductConstraint extends BasketBuyingConstraint {

        private ProductInfo productInfo;
        private int maxAmount;

        public MaxAmountForProductConstraint(ProductInfo productInfo, int maxAmount) {
            this.productInfo = productInfo;
            this.maxAmount = maxAmount;
        }

        public ActionResultDTO canBuy(ShoppingBasket basket) {
            if (basket.getProducts().get(productInfo) > maxAmount) return new ActionResultDTO(ResultCode.ERROR_PURCHASE, "Cannot purchase " + basket.getProducts().get(productInfo) + " of " + productInfo + ". Max amount is " + maxAmount);
            return new ActionResultDTO(ResultCode.SUCCESS, null);
        }

        public String toString() {
            return "Cannot purchase more than " + maxAmount + " of " + productInfo.getName() + " (" + productInfo.getId() + ")";
        }
    }

    public static class MinAmountForProductConstraint extends BasketBuyingConstraint {

        private ProductInfo productInfo;
        private int minAmount;

        public MinAmountForProductConstraint(ProductInfo productInfo, int minAmount) {
            this.productInfo = productInfo;
            this.minAmount = minAmount;
        }

        public ActionResultDTO canBuy(ShoppingBasket basket) {
            if (basket.getProducts().get(productInfo) < minAmount) return new ActionResultDTO(ResultCode.ERROR_PURCHASE, "Cannot purchase " + basket.getProducts().get(productInfo) + " of " + productInfo + ". Min amount is " + minAmount);
            return new ActionResultDTO(ResultCode.SUCCESS, null);
        }

        public String toString() {
            return "Cannot purchase less than " + minAmount + " of " + productInfo.getName() + " (" + productInfo.getId() + ")";
        }
    }

    public static class MaxProductAmountConstraint extends BasketBuyingConstraint {

        private int maxAmount;

        public MaxProductAmountConstraint(int maxAmount) {
            this.maxAmount = maxAmount;
        }

        public ActionResultDTO canBuy(ShoppingBasket basket) {
            int totalAmount = 0;
            Map<ProductInfo, Integer> products = basket.getProducts();
            for (Integer productAmount: products.values()) {
                totalAmount += productAmount;
                if (totalAmount > maxAmount) return new ActionResultDTO(ResultCode.ERROR_PURCHASE, "Cannot purchase more than " + maxAmount + " products.");
            }
            return new ActionResultDTO(ResultCode.SUCCESS, null);
        }

        public String toString() {
            return "Cannot purchase more than " + maxAmount + " products";
        }
    }

    public static class MinProductAmountConstraint extends BasketBuyingConstraint {

        private int minAmount;

        public MinProductAmountConstraint(int minAmount) {
            this.minAmount = minAmount;
        }

        public ActionResultDTO canBuy(ShoppingBasket basket) {
            int totalAmount = 0;
            Map<ProductInfo, Integer> products = basket.getProducts();
            for (Integer productAmount: products.values()) {
                totalAmount += productAmount;
            }
            if (totalAmount < minAmount) return new ActionResultDTO(ResultCode.ERROR_PURCHASE, "Cannot purchase less than " + minAmount + " products.");
            return new ActionResultDTO(ResultCode.SUCCESS, null);
        }

        public String toString() {
            return "Cannot purchase less than " + minAmount + " products";
        }
    }
}
