package DTOs.SimpleDTOS;

import java.util.Map;

public class PurchaseDetailsDTO {
    private int id;
    private Map<ProductInfoDTO,Integer> mapProductsAmount;
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

    public Map<ProductInfoDTO, Integer> getMapProductsAmount() {
        return mapProductsAmount;
    }

    public void setMapProductsAmount(Map<ProductInfoDTO, Integer> mapProductsAmount) {
        this.mapProductsAmount = mapProductsAmount;
    }

    public PurchaseDetailsDTO(int id, Map<ProductInfoDTO, Integer> mapProductsAmount, double price) {
        this.id = id;
        this.mapProductsAmount = mapProductsAmount;
        this.price = price;
    }

    @Override
    public String toString() {
        String output = "";
        for (ProductInfoDTO pname : mapProductsAmount.keySet()) {
            output += pname.toString() + "\n" +
                    "Amount: " + String.valueOf(mapProductsAmount.get(pname)) + "\n";

        }
        output += "Price: " + String.valueOf(price) + "\n";
        return output;
    }
}
