package DTOs.SimpleDTOS;

public class PermissionDTO {
    int storeId;
    SubscriberDTO user;
    SubscriberDTO grantor;
    String type;
    String details;


    public PermissionDTO(int storeId, SubscriberDTO user, SubscriberDTO grantor, String type, String details) {
        this.storeId = storeId;
        this.user = user;
        this.grantor = grantor;
        this.type = type;
        this.details = details;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public SubscriberDTO getUser() {
        return user;
    }

    public void setUser(SubscriberDTO user) {
        this.user = user;
    }

    public SubscriberDTO getGrantor() {
        return grantor;
    }

    public void setGrantor(SubscriberDTO grantor) {
        this.grantor = grantor;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

}
