package Domain.TradingSystem;

import Domain.Security.Security;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class UserHandler {

    //TODO:WORK WITH ONLY ONE OF THOSE
    List<Subscriber> users;
    Map<Subscriber, User> subscribers;

    public UserHandler(){
        users = new LinkedList<Subscriber>();
        subscribers = new HashMap<>();
    }

    public void setAdmin() {
        if(!hasAdmin()){
            User adminUser = new User();
            Subscriber adminState = new Subscriber("admin", Security.getHash("admin"), true);
            adminUser.setState(adminState);
            subscribers.put(adminState, adminUser);
        }
    }

    private boolean hasAdmin() {
        boolean found = false;
        for(Subscriber sub : subscribers.keySet()){
            if(sub.isAdmin()){
                found = true;
            }
        }
        return found;
    }

    public int register(String username, String hashedPassword) {
        for (Subscriber sub: subscribers.keySet())
            if (sub.getUsername().equals(username))
                return -1;

        User newUser = new User();
        Subscriber subscriberState = new Subscriber(username, hashedPassword, false);
        newUser.setState(subscriberState);
        subscribers.put(subscriberState, newUser);
        return subscriberState.getId();
    }

    public User getSubscriberUser(String username, String hashedPassword) {
        for (Map.Entry<Subscriber, User> subUser: subscribers.entrySet()) {
            Subscriber sub = subUser.getKey();
            if (sub.getUsername().equals(username) && sub.getHashedPassword().equals(hashedPassword))
                return subUser.getValue();
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
