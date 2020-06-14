package Domain.TradingSystem;

import org.apache.tomcat.jni.Local;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class DayStatistics {

    private LocalDate date;

    private AtomicInteger guests;
    private AtomicInteger regularSubs;
    private AtomicInteger managersNotOwners;
    private AtomicInteger managersOwners;
    private AtomicInteger admins;

    public DayStatistics(LocalDate date) {
        this.date = date;
        this.guests = new AtomicInteger(0);
        this.regularSubs = new AtomicInteger(0);
        this.managersNotOwners = new AtomicInteger(0);
        this.managersOwners = new AtomicInteger(0);
        this.admins = new AtomicInteger(0);
    }

    public void increaseGuest() {
        guests.incrementAndGet();
    }

    public void decreaseGuest() {
        guests.decrementAndGet();
    }

    public void increaseAdmin() {
        admins.incrementAndGet();
    }

    public void increaseOwner() {
        managersOwners.incrementAndGet();
    }

    public void increaseManager() {
        managersNotOwners.incrementAndGet();
    }

    public void increaseRegular() {
        regularSubs.incrementAndGet();
    }

    public boolean isToday() {
        return date.isEqual(LocalDate.now());
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

    }
}
