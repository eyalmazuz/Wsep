package DTOs.SimpleDTOS;

public class SimpProductAmountDTO {
    private int pid;
    private String name;
    private int amount;
    private double price;

    public SimpProductAmountDTO(int pid, String name, int amount, double price) {
        this.pid = pid;
        this.name = name;
        this.amount = amount;
        this.price = price;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getPrice() {return price;}

    public void setPrice(double price){ this.price = price;}
}
