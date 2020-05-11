package DTOs;

public class IntActionResultDto extends ActionResultDTO {
    int id;
    public IntActionResultDto(ResultCode resultCode, String details, int id){
        super(resultCode,details);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
