package DTOs.SimpleDTOS;

public class ManagerDTO extends SubscriberDTO {
    String type;
    public ManagerDTO(int id, String username,String type) {
        super(id, username);
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
