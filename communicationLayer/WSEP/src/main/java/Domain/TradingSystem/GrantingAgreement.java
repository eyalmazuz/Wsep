package Domain.TradingSystem;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GrantingAgreement {
    private int storeId;
    private int grantorId;
    private int malshabId;
    private Map<Integer, Boolean> owner2approve;


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
    }

    public int getGrantorId() {
        return grantorId;
    }

    public void setGrantorId(int grantorId) {
        this.grantorId = grantorId;
    }

    public int getMalshabId() {
        return malshabId;
    }

    public void setMalshabId(int malshabId) {
        this.malshabId = malshabId;
    }

    public Map<Integer, Boolean> getOwner2approve() {
        return owner2approve;
    }

    public void setOwner2approve(Map<Integer, Boolean> owner2approve) {
        this.owner2approve = owner2approve;
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
            return true;
        }
        return false;
    }

    public void removeApprove(int id) {
        owner2approve.remove(id);
    }

    public boolean hasApprove(int grantorId) {
        return owner2approve.get(grantorId)!=null;
    }
}
