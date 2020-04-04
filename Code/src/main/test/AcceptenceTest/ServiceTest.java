package AcceptenceTest;
import Service.Bridge;
import junit.framework.TestCase;

public abstract class ServiceTest extends TestCase {

    Bridge bridge;

    public void setUp(){
        this.bridge = Driver.getBridge();
        this.setUpUsers();

    }

    private void setUpUsers() {
        this.bridge.register("bob", "1234");
    }

    public boolean login (String username , String password){
        return this.bridge.login(username, password);
    }

    public boolean register(String username, String password){
        return this.bridge.register(username, password);
    }

}
