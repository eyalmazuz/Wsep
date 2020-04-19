package AcceptanceTest;

import Service.Bridge;
import Service.ProxyBridge;
import Service.RealBridge;

public abstract class Driver {

    public static Bridge getBridge() {
        ProxyBridge proxy = new ProxyBridge();
        proxy.setRealBridge(new RealBridge()); // add real bridge here
        return proxy;
    }
}
