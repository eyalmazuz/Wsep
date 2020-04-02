package Service;

public class ProxyBridge implements Bridge {
    private RealBridge rb = null;


    public boolean login(String username, String password) {
        if (rb != null) {
            return rb.login(username, password);
        }
        else {
            return false;
        }
    }

    public void setRealBridge(RealBridge realBridge) {
        rb = realBridge;
    }
}
