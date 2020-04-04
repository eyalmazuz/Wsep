package Service;

public class ProxyBridge implements Bridge {
    private RealBridge rb = null;


    public boolean login(String username, String password) {
        if (rb != null) {
            return rb.login(username, password);
        }
        else {
            return username.equals("bob") && password.equals("1234");
        }
    }

    public boolean register(String username, String password) {
        if (rb != null) {
            return rb.register(username, password);
        }
        else {
            return !username.equals("bob");
        }
    }

    public void setRealBridge(RealBridge realBridge) {
        rb = realBridge;
    }
}
