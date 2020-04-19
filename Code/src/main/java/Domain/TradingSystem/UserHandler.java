package Domain.TradingSystem;

import Domain.Security.Security;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class UserHandler {

    List<Subscriber> subscribers;

    public UserHandler(){
        subscribers = new LinkedList<Subscriber>();
    }

    public void setAdmin() {
        if(!hasAdmin()){
            Subscriber admin = new Subscriber("admin", Security.getHash("admin"), true);
            subscribers.add(admin);
        }
    }

    private boolean hasAdmin() {
        boolean found = false;
        for(Subscriber sub : subscribers){
            if(sub.isAdmin()){
                found = true;
            }
        }
        return found;
    }

    // Usecase 2.2 - Register
    public int register(String username, String password) {
        for (Subscriber sub: subscribers)
            if (sub.getUsername().equals(username))
                return -1;

        Subscriber newSubscriber = new Subscriber(username, password, false);
        subscribers.add(newSubscriber);
        return newSubscriber.getId();
    }

    // Usecase 2.3 - Login
    public Subscriber getSubscriber(String username, String password) {
        for (Subscriber sub: subscribers) {
            if (sub.getUsername().equals(username) && sub.getPassword().equals(password))
                return sub;
        }
        return null;
    }


    /**
     * get users that are not in owners list
     * @param owners - the users to exclude
     * @return a list of the remaining user id's
     */
    public List <Integer> getAvailableUsersToOwn(List <Subscriber> owners) {
        List <Integer> availableSubs = new LinkedList<Integer>();
        for (Subscriber user: subscribers){
            if (!owners.contains(user))
                availableSubs.add(user.getId());
        }
        return availableSubs;
    }

    public Subscriber getUser(int userId) {
        for(Subscriber s : subscribers){
            if (s.getId() == userId){
                return s;
            }
        }
        return null;
    }

    public List<Integer> getManagersOfCurUser(int storeId, int grantorId) {
        List <Integer> ans = new LinkedList<Integer>();
        for (Subscriber user: subscribers){
            if (user.isGrantedBy(storeId,grantorId)){
                ans.add(user.getId());
            }
        }
        return ans;
    }

    public String getManagerDetails(int managerId, int storeId) {
        for (Subscriber user: subscribers){
            if (user.getId() == managerId)
                return user.getManagerDetails(storeId);
        }
        return null;
    }

}
