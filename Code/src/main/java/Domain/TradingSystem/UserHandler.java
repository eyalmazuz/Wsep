package Domain.TradingSystem;

import Domain.Security.Security;

import java.util.LinkedList;
import java.util.List;


public class UserHandler {

    List<Subscriber> users;

    public UserHandler(){
        users = new LinkedList<Subscriber>();
    }

    public void setAdmin() {
        if(!hasAdmin()){
            Subscriber admin = new Subscriber();
            admin.setUserName("admin");
            String password = Security.getHash("admin");
            admin.setPassword(password);
            admin.setAdmin();
            users.add(admin);
        }
    }

    private boolean hasAdmin() {
        boolean found = false;
        for(Subscriber s : users){
            if(s.isAdmin()){
                found = true;
            }
        }
        return found;
    }

    /**
     * get users that are not in owners list
     * @param owners - the users to exclude
     * @return a list of the remaining user id's
     */
    public List <Integer> getAvailableUsersToOwn(List <Subscriber> owners) {
        List <Integer> availableSubs = new LinkedList<Integer>();
        for (Subscriber user: users){
            if (!owners.contains(user))
                availableSubs.add(user.getId());
        }
        return availableSubs;
    }

    public Subscriber getUser(int userId) {
        for(Subscriber s : users){
            if (s.getId() == userId){
                return s;
            }
        }
        return null;
    }

    public List<Integer> getManagersOfCurUser(int storeId, int grantorId) {
        List <Integer> ans = new LinkedList<Integer>();
        for (Subscriber user: users){
            if (user.isGrantedBy(storeId,grantorId)){
                ans.add(user.getId());
            }
        }
        return ans;
    }

    public String getManagerDetails(int managerId, int storeId) {
        for (Subscriber user: users){
            if (user.getId() == managerId)
                return user.getManagerDetails(storeId);
        }
        return null;
    }

}
