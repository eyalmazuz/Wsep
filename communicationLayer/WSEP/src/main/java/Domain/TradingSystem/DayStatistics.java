package Domain.TradingSystem;

import DataAccess.DAOManager;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.time.LocalDate;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

@DatabaseTable(tableName = "daystatistics")
public class DayStatistics {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private Date date;

    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private AtomicInteger guests;

    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private AtomicInteger regularSubs;

    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private AtomicInteger managersNotOwners;

    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private AtomicInteger managersOwners;

    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private AtomicInteger admins;

    public DayStatistics() {}

    public DayStatistics(LocalDate date) {
        this.date = java.sql.Date.valueOf(date);
        this.guests = new AtomicInteger(0);
        this.regularSubs = new AtomicInteger(0);
        this.managersNotOwners = new AtomicInteger(0);
        this.managersOwners = new AtomicInteger(0);
        this.admins = new AtomicInteger(0);
    }

    public void increaseGuest() {
        guests.incrementAndGet();
        DAOManager.updateDayStatistics(this);
    }

    public void decreaseGuest() {
        guests.decrementAndGet();
        DAOManager.updateDayStatistics(this);
    }

    public void increaseAdmin() {
        admins.incrementAndGet();
        DAOManager.updateDayStatistics(this);
    }

    public void increaseOwner() {
        managersOwners.incrementAndGet();
        DAOManager.updateDayStatistics(this);
    }

    public void increaseManager() {
        managersNotOwners.incrementAndGet();
        DAOManager.updateDayStatistics(this);
    }

    public void increaseRegular() {
        regularSubs.incrementAndGet();
        DAOManager.updateDayStatistics(this);
    }

    public boolean isToday() {
        return date.equals(java.sql.Date.valueOf(LocalDate.now()));
    }

    public String getDate() {
        return date.toString();
    }

    public int getGuests() {
        return guests.get();
    }



    public int getRegularSubs() {
        return regularSubs.get();
    }


    public int getManagersNotOwners() {
        return managersNotOwners.get();
    }


    public int getManagersOwners() {
        return managersOwners.get();
    }


    public int getAdmins() {
        return admins.get();
    }

    public int getTotalCount(){
        return guests.get()+regularSubs.get()+managersNotOwners.get()+managersOwners.get()+admins.get();

    }


    public void reset() {
        this.guests.set(0);
        this.regularSubs.set(0);
        this.managersOwners.set(0);
        this.managersNotOwners.set(0);
        this.admins.set(0);

        DAOManager.updateDayStatistics(this);
    }
}
