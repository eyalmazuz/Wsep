package AcceptanceTest.Data;

import java.util.ArrayList;
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
    };

    public static Map<String, Integer> userToId = new HashMap<String, Integer>();

    public static Map<String, Integer> userToStore = new HashMap<String, Integer>();

    public static String Stores;

    public static String[][] Products = {
            //ID, STORE ID, AMOUNT
            {"1", "1", "5"},
            {"2", "1", "5"},
            {"2", "2", "10"},

    };

    public static String[][] ProductsFiltered = {
            {"1", "1", "5"},
    };



    public static String[][] Cart0 = {
            //ID, STORE ID, AMOUNT
    };


    public static String[][] Cart1 = {
            //ID, STORE ID, AMOUNT
            {"1", "1", "5"},
    };

    public static String[][] Cart2 = {
            //ID, STORE ID, AMOUNT
            {"1", "1", "5"},
            {"2", "1", "5"}
    };


    public static String[][] History = {
            //PRSTORE ID, ODUCT ID, AMOUNT BOUGHT
            {"1", "1", "5"},
            {"1", "2", "5"}
    };

    public static String[][] Managers = {
            //STORE ID, MANAGER, GRANTOR
            {"1", "dia", "chika", "manager"},
            {"1", "ruby", "dia", "manager"},
            {"1", "kanan", "chika", "owner"}
    };

}
