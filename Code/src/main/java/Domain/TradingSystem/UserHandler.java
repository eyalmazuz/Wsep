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

    public List <Subscriber> getAvailableUsersToOwn(List <Subscriber> owners) {
        List <Subscriber> availableSubs = new LinkedList<Subscriber>();
        for (Subscriber user: users){
            if (!owners.contains(user))
                availableSubs.add(user);
        }
        return availableSubs;
    }
}
