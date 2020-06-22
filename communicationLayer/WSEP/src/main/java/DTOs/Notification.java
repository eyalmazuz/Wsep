package DTOs;

import java.io.Serializable;

public class Notification implements Serializable {
    int id;
    String massage;

    public static final long serialVersionUID = 123456L;

    public Notification(int id,String message){
        this.id = id;
        this.massage = message;
    }

    public Notification(Notification notification) {
        this.id = notification.getId();
        this.massage = notification.massage;
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
