package DTOs.SimpleDTOS;

public class DiscountTypeDTO {

    private int id;
    private String toString;

    public DiscountTypeDTO(int id, String toString) {
        this.id = id;
        this.toString = toString;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToString() {
        return toString;
    }

    public void setToString(String toString) {
        this.toString = toString;
    }

    public String toString() {
        return "Type ID: " + id + "\n" +
                "Details: " + toString;
    }
}
