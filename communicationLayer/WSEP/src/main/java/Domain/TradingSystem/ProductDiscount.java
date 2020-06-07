package Domain.TradingSystem;

import java.util.ArrayList;
import java.util.List;

public class ProductDiscount extends SimpleDiscount {

    protected int productId;
    protected double salePercentage;
    protected String categoryName;

    public ProductDiscount(int productId, double salePercentage, String categoryName) {
        this.productId = productId;
        this.salePercentage = salePercentage;
        this.categoryName = categoryName;
    }

    public int getProductId() {
        return productId;
    }

    public double getSalePercentage() {
        return salePercentage;
    }

    public String getCategoryName() {
        return categoryName;
    }

    @Override
    public DiscountBasket getDiscountedBasket(User user, DiscountBasket discountBasket) {
        return discountBasket;
    }

    public static class ProductSaleDiscount extends ProductDiscount {

        public ProductSaleDiscount(int productId, double salePercentage) {
            super(productId, salePercentage, null);
        }

        public DiscountBasket getDiscountedBasket(User user, DiscountBasket discountBasket) {

            List<DiscountBasket.DiscountProduct> discountProducts = discountBasket.getDiscountProducts();
            List<DiscountBasket.DiscountProduct> resDiscountProducts = new ArrayList<>();

            for (DiscountBasket.DiscountProduct discountProduct: discountProducts) {
                resDiscountProducts.add(new DiscountBasket.DiscountProduct(discountProduct.getProductId(), discountProduct.getCategory(), discountProduct.getAmount(), discountProduct.getPrice()));
            }

            DiscountBasket resBasket = new DiscountBasket(resDiscountProducts);

            for (DiscountBasket.DiscountProduct discountProduct: resDiscountProducts) {
                if (discountProduct.getProductId() == productId) {
                    double curPrice = discountProduct.getPrice();
                    discountProduct.setPrice((1-salePercentage) * curPrice);
                }
            }
            return resBasket;
        }

        public String toString() {
            return salePercentage*100 + "% sale on product ID " + productId;
        }

    }

    public static class CategorySaleDiscount extends ProductDiscount {

        public CategorySaleDiscount(String categoryName, double salePercentage) {
            super(-1, salePercentage, categoryName);
        }

        public DiscountBasket getDiscountedBasket(User user, DiscountBasket discountBasket) {

            List<DiscountBasket.DiscountProduct> discountProducts = discountBasket.getDiscountProducts();
            List<DiscountBasket.DiscountProduct> resDiscountProducts = new ArrayList<>();

            for (DiscountBasket.DiscountProduct discountProduct: discountProducts) {
                resDiscountProducts.add(new DiscountBasket.DiscountProduct(discountProduct.getProductId(), discountProduct.getCategory(), discountProduct.getAmount(), discountProduct.getPrice()));
            }

            DiscountBasket resBasket = new DiscountBasket(resDiscountProducts);

            for (DiscountBasket.DiscountProduct discountProduct: resDiscountProducts) {
                if (discountProduct.getCategory().equals(categoryName)) {
                    double curPrice = discountProduct.getPrice();
                    discountProduct.setPrice((1-salePercentage) * curPrice);
                }
            }
            return resBasket;
        }

        public String toString() {
            return salePercentage*100 + "% sale on the " + categoryName + " category";
        }
    }

}

