package AcceptenceTest;

import Service.Bridge;
import Service.ProxyBridge;
import Service.RealBridge;

public abstract class Driver {

    public static Bridge getBridge() {
        ProxyBridge proxy = new ProxyBridge();
        proxy.setRealBridge(null); // add real bridge here
        return proxy;
    }
}
