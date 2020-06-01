package Domain.TradingSystem;

import DataAccess.DAOManager;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@DatabaseTable(tableName="pendingGrantingAgreement")
public class GrantingAgreement implements Serializable {

    @DatabaseField (generatedId = true)
    private int id;

    @DatabaseField(uniqueCombo = true)
    private int storeId;

    @DatabaseField(uniqueCombo = true)
    private int grantorId;

    @DatabaseField(uniqueCombo = true)
    private int malshabId;

    @DatabaseField (dataType =  DataType.SERIALIZABLE)
    private ConcurrentHashMap<Integer, Boolean> owner2approve;

    public GrantingAgreement() {}

    public GrantingAgreement(int storeId, int grantorId, int malshabId, List<Integer> ownersId) {
        this.storeId = storeId;
        this.grantorId = grantorId;
        this.malshabId = malshabId;
        owner2approve = new ConcurrentHashMap<>();
        for (Integer ownerID : ownersId) {
            if (ownerID != grantorId)
                owner2approve.put(ownerID, false);
        }

    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
        DAOManager.updateGrantingAgreement(this);
    }

    public int getGrantorId() {
        return grantorId;
    }

    public void setGrantorId(int grantorId) {
        this.grantorId = grantorId;
        DAOManager.updateGrantingAgreement(this);
    }

    public int getMalshabId() {
        return malshabId;
    }

    public void setMalshabId(int malshabId) {
        this.malshabId = malshabId;
        DAOManager.updateGrantingAgreement(this);
    }

    public Map<Integer, Boolean> getOwner2approve() {
        return owner2approve;
    }

    public void setOwner2approve(ConcurrentHashMap<Integer, Boolean> owner2approve) {
        this.owner2approve = owner2approve;
        DAOManager.updateGrantingAgreement(this);
    }

    public boolean allAproved() {
        if (owner2approve.size() == 0){
            return true;
        }
        for(Integer id : owner2approve.keySet()){
            if(!owner2approve.get(id)){
                return false;
            }
        }
        return true;
    }

    public boolean approve(int grantorid) {
        if(owner2approve.get(grantorid)!= null){
            owner2approve.put(grantorid,true);
            DAOManager.updateGrantingAgreement(this);
            return true;
        }
        return false;
    }

    public void removeApprove(int id) {
        owner2approve.remove(id);
        DAOManager.updateGrantingAgreement(this);
    }

    public String toString() {
        String output = "";
        output += "Store ID: " + storeId + ", Grantor ID: " + grantorId + ", Candidate ID: " + malshabId + "\n";
        for (Integer approvalSubId : owner2approve.keySet()) {
            boolean hasApproved = owner2approve.get(approvalSubId);
            output += (approvalSubId + ": " + (hasApproved ? "approved" : "not yet approved") + "\n");
        }
        return output;
    }
}
