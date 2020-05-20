package Domain.TradingSystem;


public class Permission {

    Subscriber user;
    Subscriber grantor;
    String type; //Owner\Manager
    Store store;
    String details ;

    public Permission(Subscriber user, Subscriber grantor, String type, Store store){
        this.grantor = grantor;
        this.user = user;
        this.type = type;
        this.store = store;
        this.details = "Simple";

    }


    public Store getStore() {
        return store;
    }

    public String getType() {
        return type;
    }

    public boolean isOwner(int storeId) {

        return((storeId == store.getId()) && type.equals("Owner"));
    }

    public boolean isManager(int storeId) {
        return ((storeId == store.getId()) && type.equals("Manager"));
    }

    public Subscriber getGrantor() {
        return grantor;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }


    public boolean hasPrivilage(String type) {
        return details.equals(type);
    }

    public Subscriber getUser() {
        return user;
    }
}
