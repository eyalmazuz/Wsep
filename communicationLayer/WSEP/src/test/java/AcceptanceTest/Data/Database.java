package AcceptanceTest.Data;

import java.util.HashMap;
import java.util.Map;

public class Database {


    public static String[][] Users = {
            //USERNAME, PASSWORD
            {"hanamaru", "12345"},
            {"chika", "12345"},
            {"kanan", "12345"},
            {"ruby", "12345"},
            {"dia", "12345"},
            {"yoshiko", "12345"},
            {"riko", "12345"},
            {"you", "12345"},
            {"mari", "12345"},
            {"iggy", "12345"},
    };

    public static Map<String, Integer> userToId = new HashMap<String, Integer>();

    public static Map<String, Integer> userToStore = new HashMap<String, Integer>();

    public static String Stores;


    public static String Cart;
}
