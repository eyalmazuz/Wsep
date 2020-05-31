package Domain.TradingSystem;

import DataAccess.DAOManager;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "permissions")
public class Permission {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(foreign = true)
    private Subscriber user;

    @DatabaseField(foreign = true)
    private Subscriber grantor;

    @DatabaseField
    private String type; //Owner\Manager

    @DatabaseField(foreign = true)
    private Store store;

    @DatabaseField
    private String details;

    public Permission () {}

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
        DAOManager.updatePermission(this);
    }


    public boolean hasPrivilage(String type) {
        return details.equals(type);
    }

    public Subscriber getUser() {
        return user;
    }
}
