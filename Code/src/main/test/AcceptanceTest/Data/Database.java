package AcceptanceTest.Data;

import java.util.ArrayList;

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


    public static String[][] Stores = {
            //STORE ID, OWNER
            {"1", "chika"},
            {"2", "hanamaru"},
    };

    public static String[][] Products = {
            //ID, STORE ID, AMOUNT
            {"1", "1", "5"},
            {"2", "1", "5"},
            {"2", "2", "10"},

    };

    public static String[][] ProductsFiltered = {
            {}
    };

    public static String[][] Cart = {
            //ID, STORE ID, AMOUNT
            {"1", "1", "5"},
            {"2", "1", "5"}
    };

    public static String[][] History = {
            {}
    };

    public static String[][] Managers = {
            //STORE ID, MANAGER, GRANTOR
            {"1", "dia", "chika", "manager"},
            {"1", "ruby", "dia", "manager"},
            {"1", "kanan", "chika", "owner"}
    };

}
