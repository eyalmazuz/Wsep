package DTOs.SimpleDTOS;

public class DailyStatsDTO {
    String date;
    private int guests;
    private int regularSubs;
    private int managersNotOwners;
    private int managersOwners;
    private int admins;

    public DailyStatsDTO(String date, int guests, int regularSubs, int managersNotOwners, int managersOwners, int admins) {
        this.date = date;
        this.guests = guests;
        this.regularSubs = regularSubs;
        this.managersNotOwners = managersNotOwners;
        this.managersOwners = managersOwners;
        this.admins = admins;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getGuests() {
        return guests;
    }

    public void setGuests(int guests) {
        this.guests = guests;
    }

    public int getRegularSubs() {
        return regularSubs;
    }

    public void setRegularSubs(int regularSubs) {
        this.regularSubs = regularSubs;
    }

    public int getManagersNotOwners() {
        return managersNotOwners;
    }

    public void setManagersNotOwners(int managersNotOwners) {
        this.managersNotOwners = managersNotOwners;
    }

    public int getManagersOwners() {
        return managersOwners;
    }

    public void setManagersOwners(int managersOwners) {
        this.managersOwners = managersOwners;
    }

    public int getAdmins() {
        return admins;
    }

    public void setAdmins(int admins) {
        this.admins = admins;
    }
}
