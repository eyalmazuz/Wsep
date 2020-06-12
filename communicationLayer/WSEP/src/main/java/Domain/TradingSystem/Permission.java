package Domain.TradingSystem;

import DataAccess.DAOManager;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;

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

    @DatabaseField (dataType = DataType.SERIALIZABLE)
    private ArrayList<String> details;

    public Permission () {}

    public Permission(Subscriber user, Subscriber grantor, String type, Store store){
        this.grantor = grantor;
        this.user = user;
        this.type = type;
        this.store = store;
        this.details = new ArrayList<>();
        details.add("Simple");
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

    public ArrayList<String> getDetails() {
        return details;
    }

    public void setDetails(String details) {
        if(details.equals("manage-inventory")){
            this.details.add("add product");
            this.details.add("edit product");
            this.details.add("remove product");
        }
        else
            this.details.add(details);
        DAOManager.updatePermission(this);
    }


    public boolean hasPrivilage(String type) {
        return details.contains(type);
    }

    public Subscriber getUser() {
        return user;
    }
}
