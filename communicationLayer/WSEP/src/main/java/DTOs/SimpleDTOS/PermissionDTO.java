package DTOs.SimpleDTOS;

import java.util.ArrayList;

public class PermissionDTO {
    int storeId;
    SubscriberDTO user;
    SubscriberDTO grantor;
    String type;
    ArrayList<String> details;


    public PermissionDTO(int storeId, SubscriberDTO user, SubscriberDTO grantor, String type, ArrayList<String> details) {
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

    public ArrayList<String> getDetails() {
        return details;
    }

    public void setDetails(ArrayList<String> details) {
        this.details = details;
    }

}
