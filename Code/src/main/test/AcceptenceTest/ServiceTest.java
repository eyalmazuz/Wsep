package AcceptenceTest;
import Service.Bridge;
import junit.framework.TestCase;

public abstract class ServiceTest extends TestCase {

    Bridge bridge;

    public void setUp(){
        this.bridge = Driver.getBridge();
    }

    public boolean login (String username , String password){
        return bridge.login(username, password);
    }

}
