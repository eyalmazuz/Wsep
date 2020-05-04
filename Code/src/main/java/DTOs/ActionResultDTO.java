package DTOs;

import javax.swing.*;

public class ActionResultDTO {
    private ResultCode resultCode;
    private String details;

    public ActionResultDTO() {}

    public ActionResultDTO(ResultCode resultCode, String details) {
        this.resultCode = resultCode;
        this.details = details;
    }

    public ResultCode getResultCode() {
        return resultCode;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
