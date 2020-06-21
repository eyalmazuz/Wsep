package Domain.TradingSystem;

import DataAccess.DAOManager;
import DataAccess.DatabaseFetchException;
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

    public void setAdmin() throws DatabaseFetchException {
        if(!hasAdmin()){
            Subscriber admin = new Subscriber("admin", Security.getHash("admin"), true);
            subscribers.put(admin.getId(),admin);
            DAOManager.createOrUpdateSubscriber(admin);
        }
    }

    private boolean hasAdmin() throws DatabaseFetchException {
        boolean found = false;
        if(DAOManager.getSubscriberByUsername("admin") != null){
            return true;
        }
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

        if (DAOManager.subscriberExists(username)) {
            return -1;
        }

        Subscriber subscriberState = new Subscriber(username, password, false);
        subscriberState.setId(DAOManager.getMaxSubscriberId() + 1);
        subscribers.put(subscriberState.getId(),subscriberState);
        DAOManager.addSubscriber(subscriberState);
        return subscriberState.getId();
    }

    public User getUser(int sessionId){
        return users.get(sessionId);
    }

    public Subscriber getSubscriberUser(String username, String hashedPassword) throws DatabaseFetchException {
        Subscriber cachedSub = null;
        for (Subscriber sub: subscribers.values()) {
            if (sub.getUsername().equals(username) && sub.getHashedPassword().equals(hashedPassword))
                cachedSub = sub;
        }
        Subscriber subscriber = DAOManager.getSubscriberByUsername(username);
        if (subscriber == null)  return null;
        if (subscriber.getHashedPassword().equals(hashedPassword)) {
            syncSubscribers(cachedSub,subscriber);
            return subscribers.get(subscriber.getId());
        }

        return null;
    }

    /**
     * Check if the DB version of subscriber is more updates and if so update subscribers list.
     * @param cachedSub
     * @param DBsub
     */
    private void syncSubscribers(Subscriber cachedSub, Subscriber DBsub) {
        if(cachedSub == null)
            subscribers.put(DBsub.getId(),DBsub);
        else
        if(DBsub.getpVersion() > cachedSub.getpVersion()){
            subscribers.put(DBsub.getId(),DBsub);
        }
    }

    public Subscriber getSubscriberUser(String username) throws DatabaseFetchException {
        Subscriber cachedSub = null;
        for (Subscriber sub: subscribers.values()) {
            if (sub.getUsername().equals(username))
                cachedSub = sub;
        }

        Subscriber subscriber = DAOManager.getSubscriberByUsername(username);

        if(subscriber != null) {
            syncSubscribers(cachedSub, subscriber);
            return subscribers.get(subscriber.getId());
        }
        return null;
    }


    /**
     * get users that are not in owners list
     * @param owners - the users to exclude
     * @return a list of the remaining user id's
     */
    public List<Subscriber> getAvailableUsersToOwn(List <Subscriber> owners)  {
        List <Subscriber> availableSubs = new LinkedList<>();
        if(owners!=null) {
            List<Integer> ids = owners.stream().map(Subscriber::getId).collect(Collectors.toList());
            try {
                for (Subscriber user : DAOManager.loadAllSubscribers() ){
                    if (!ids.contains(user.getId()))
                        availableSubs.add(user);
                }
            } catch (DatabaseFetchException e) {
                e.printStackTrace();
            }
        }
        return availableSubs;
    }

    public Subscriber  getSubscriber(int subId) {
        Subscriber inDB = null;
        try {
            inDB = DAOManager.getSubscriberByID(subId);
        } catch (DatabaseFetchException e) {
            e.printStackTrace();
        }
        syncSubscribers(subscribers.get(subId),inDB);
        return subscribers.get(subId);
    }

    public List<Integer> getManagersOfCurUser(int storeId, int grantorId) {
        List <Integer> ans = new LinkedList<Integer>();
        try {
            for (Subscriber user: DAOManager.loadAllSubscribers()){
                if (user.isGrantedBy(storeId,grantorId)){
                    ans.add(user.getId());
                }
            }
        } catch (DatabaseFetchException e) {
            e.printStackTrace();
        }
        return ans;
    }

    public ArrayList<String> getManagerDetails(int managerId, int storeId) {
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
