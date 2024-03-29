package DTOs;

public class ActionResultDTO {
    private ResultCode resultCode;
    private String details;

    public ActionResultDTO() {}

    public ActionResultDTO(ResultCode resultCode, String details) {
        this.resultCode = resultCode;
        this.details = details;
    }

    public void setResultCode(ResultCode resultCode) {
        this.resultCode = resultCode;
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
