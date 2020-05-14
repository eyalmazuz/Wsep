package DTOs.SimpleDTOS;

import java.util.List;

public class PurchaseDetailsDTO {
    private int id;
    private List<ProductAmountDTO> mapProductsAmount;
    private double price;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public PurchaseDetailsDTO(int id, List<ProductAmountDTO> mapProductsAmount, double price) {
        this.id = id;
        this.mapProductsAmount = mapProductsAmount;
        this.price = price;
    }

    public List<ProductAmountDTO> getMapProductsAmount() {
        return mapProductsAmount;
    }

    public void setMapProductsAmount(List<ProductAmountDTO> mapProductsAmount) {
        this.mapProductsAmount = mapProductsAmount;
    }

    @Override
    public String toString() {
        String output = "";
        for (ProductAmountDTO pname : mapProductsAmount) {
            output += pname.getProductInfo().toString() + "\n" +
                    "Amount: " + String.valueOf(pname.getAmount()) + "\n";

        }
        output += "Price: " + String.valueOf(price) + "\n";
        return output;
    }
}
