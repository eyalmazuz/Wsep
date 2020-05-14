package DTOs.SimpleDTOS;

public class PermissionDTO {
    int storeId;
    SubscriberDTO user;
    SubscriberDTO grantor;
    String type;

    public PermissionDTO(int storeId, SubscriberDTO user, SubscriberDTO grantor, String type) {
        this.storeId = storeId;
        this.user = user;
        this.grantor = grantor;
        this.type = type;
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
}
