package Domain.TradingSystem;

import Domain.Security.Security;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class UserHandler {

    Map<Integer, Subscriber> subscribers;
    Map<Integer, User> users;

    public UserHandler(){
        subscribers = new HashMap<>();
        users = new HashMap<>();
    }

    public void setAdmin() {
        if(!hasAdmin()){
            Subscriber admin = new Subscriber("admin", Security.getHash("admin"), true);
            subscribers.put(admin.getId(),admin);
        }
    }

    private boolean hasAdmin() {
        boolean found = false;
        for(Subscriber sub : subscribers.values()){
            if(sub.isAdmin()){
                found = true;
            }
        }
        return found;
    }

    public int register(String username, String password) {
        if(username==null || password == null){
            return -1;
        }

        for (Subscriber sub: subscribers.values())
            if (sub.getUsername().equals(username))
                return -1;

        Subscriber subscriberState = new Subscriber(username, password, false);
        subscribers.put(subscriberState.getId(),subscriberState);
        return subscriberState.getId();
    }

    public User getUser(int sessionId){
        return users.get(sessionId);
    }

    public Subscriber getSubscriberUser(String username, String hashedPassword) {
        for (Subscriber sub: subscribers.values()) {
            if (sub.getUsername().equals(username) && sub.getHashedPassword().equals(hashedPassword))
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
        if(owners!=null) {
            for (Subscriber user : subscribers.values()) {
                if (!owners.contains(user))
                    availableSubs.add(user.getId());
            }
        }
        return availableSubs;
    }

    public Subscriber getSubscriber(int subId) {
        return subscribers.get(subId);
    }

    public List<Integer> getManagersOfCurUser(int storeId, int grantorId) {
        List <Integer> ans = new LinkedList<Integer>();
        for (Subscriber user: subscribers.values()){
            if (user.isGrantedBy(storeId,grantorId)){
                ans.add(user.getId());
            }
        }
        return ans;
    }

    public String getManagerDetails(int managerId, int storeId) {
        for (Subscriber user: subscribers.values()){
            if (user.getId() == managerId)
                return user.getManagerDetails(storeId);
        }
        return null;
    }

    public void deleteUsers() {
        subscribers.clear();
    }

    public void setState(int sessionId, int subId) {
        User u = users.get(sessionId);
        if(u!= null){
            Subscriber s = subscribers.get(subId);
            if(s!=null){
                u.setState(s);
            }
        }

    }

    public int createSession() {
        User newUser = new User();
        users.put(newUser.getId(),newUser);
        return newUser.getId();
    }


    public void mergeCartWithSubscriber(int sessionId) {
        User u = users.get(sessionId);
        if (u!= null){
            Subscriber s = (Subscriber) u.getState();
            s.getPurchaseHistory().getLatestCart().merge(u.getShoppingCart());
        }
    }

    public void setSubscribers(Map<Integer, Subscriber> subscribers) {
        this.subscribers = subscribers;
    }
}
