package Domain.TradingSystem;

import DataAccess.DAOManager;
import Domain.Security.Security;

import java.util.*;
import java.util.stream.Collectors;


public class UserHandler {

    Map<Integer, Subscriber> subscribers;
    Map<Integer, User> users;

    public UserHandler(){
        subscribers = new HashMap<>();
        //List<Subscriber> subscriberList = DAOManager.loadAllSubscribers();
        //for (Subscriber subscriber : subscriberList) subscribers.put(subscriber.getId(), subscriber);
        users = new HashMap<>();
    }

    public void setAdmin() {
        if(!hasAdmin()){
            Subscriber admin = new Subscriber("admin", Security.getHash("admin"), true);
            subscribers.put(admin.getId(),admin);
            DAOManager.createOrUpdateSubscriber(admin);
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

        for (Subscriber sub: subscribers.values()) {
            if (sub.getUsername().equals(username))
                return -1;
        }

        if (DAOManager.subscriberExists(username)) return -1;

        Subscriber subscriberState = new Subscriber(username, password, false);
        subscriberState.setId(DAOManager.getMaxSubscriberId() + 1);
        subscribers.put(subscriberState.getId(),subscriberState);
        DAOManager.addSubscriber(subscriberState);
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
        Subscriber subscriber = DAOManager.getSubscriberByUsername(username);
        if (subscriber == null) return null;
        if (subscriber.getHashedPassword().equals(hashedPassword)) return subscriber;
        return null;
    }

    public Subscriber getSubscriberUser(String username) {
        for (Subscriber sub: subscribers.values()) {
            if (sub.getUsername().equals(username))
                return sub;
        }
        return null;
    }


    /**
     * get users that are not in owners list
     * @param owners - the users to exclude
     * @return a list of the remaining user id's
     */
    public List<Subscriber> getAvailableUsersToOwn(List <Subscriber> owners) {

        List <Subscriber> availableSubs = new LinkedList<>();
        if(owners!=null) {
            List<Integer> ids = owners.stream().map(Subscriber::getId).collect(Collectors.toList());
            for (Subscriber user : subscribers.values()) {
                if (!ids.contains(user.getId()))
                    availableSubs.add(user);
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


    public void setSubscribers(Map<Integer, Subscriber> subscribers) {
        this.subscribers = subscribers;
    }

    public List<Subscriber> getSubscribers() {
        return new ArrayList<>(subscribers.values());
    }
}
