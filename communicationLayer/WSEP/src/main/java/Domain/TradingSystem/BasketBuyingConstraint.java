package Domain.TradingSystem;

import DTOs.ActionResultDTO;
import DTOs.ResultCode;

import java.util.Map;

public class BasketBuyingConstraint extends SimpleBuying {

    protected ProductInfo productInfo;
    protected int minAmount, maxAmount;

    public BasketBuyingConstraint(ProductInfo info, int minAmount, int maxAmount) {
        this.productInfo = info;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
    }

    public ProductInfo getProductInfo() {
        return productInfo;
    }

    public int getMinAmount() {
        return minAmount;
    }

    public int getMaxAmount() {
        return maxAmount;
    }

    public ActionResultDTO canBuy(ShoppingBasket basket) {
        return new ActionResultDTO(ResultCode.SUCCESS, null);
    }

    public static class MaxAmountForProductConstraint extends BasketBuyingConstraint {

        public MaxAmountForProductConstraint(ProductInfo productInfo, int maxAmount) {
            super(productInfo, -1 ,maxAmount);
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

        public MinAmountForProductConstraint(ProductInfo productInfo, int minAmount) {
            super(productInfo, minAmount, -1);
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

        public MaxProductAmountConstraint(int maxAmount) {
            super(null, -1, maxAmount);
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

        public MinProductAmountConstraint(int minAmount) {
            super(null, minAmount, -1);
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
