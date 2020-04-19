package Service;

import Domain.TradingSystem.System;

public class Main {
    public static void main(String[] args) {
        System s = System.getInstance();
        s.setup("a","b");
    }
}
