package DTOs;

import java.io.Serializable;

public class Notification implements Serializable {
    int id;
    String massage;

    public Notification(int id,String message){
        this.id = id;
        this.massage = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMassage() {
        return massage;
    }

    public void setMassage(String massage) {
        this.massage = massage;
    }
}
