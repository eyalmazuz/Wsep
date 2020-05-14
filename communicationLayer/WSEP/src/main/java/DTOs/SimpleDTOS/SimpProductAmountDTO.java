package DTOs.SimpleDTOS;

public class SimpProductAmountDTO {
    private int pid;
    private String name;
    private int amount;

    public SimpProductAmountDTO(int pid, String name, int amount) {
        this.pid = pid;
        this.name = name;
        this.amount = amount;
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
}
