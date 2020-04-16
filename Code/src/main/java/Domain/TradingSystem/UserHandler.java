package Domain.TradingSystem;

import Domain.Security.Security;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class UserHandler {

    Map<Subscriber, User> subscribers;

    public UserHandler(){
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

    public int register(String username, String password) {
        for (Subscriber sub: subscribers.keySet())
            if (sub.getUsername().equals(username))
                return -1;

        User newUser = new User();
        Subscriber subscriberState = new Subscriber(username, password, false);
        newUser.setState(subscriberState);
        subscribers.put(subscriberState, newUser);
        return subscriberState.getId();
    }

    public User getSubscriberUser(String username, String password) {
        for (Map.Entry<Subscriber, User> subUser: subscribers.entrySet()) {
            Subscriber sub = subUser.getKey();
            if (sub.getUsername().equals(username) && sub.getPassword().equals(password))
                return subUser.getValue();
        }
        return null;
    }

}
