package DTOs;

public class DoubleActionResultDTO extends ActionResultDTO {
    double price;
    public DoubleActionResultDTO(ResultCode resultCode, String details, double price){
        super(resultCode,details);
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
