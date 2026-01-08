package data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class Room implements Serializable {
    private HomeOwner owner;
    private int bedNum;
    private double pricePerWeek;
    private boolean ensuite;
    private String location;

    private List<LocalDate[]> bookedDates = new ArrayList<>();
    private String roomName;




    public void setBedNum(int bedNum) {
        this.bedNum = bedNum;
    }

    public void setPricePerWeek(double pricePerWeek) {
        this.pricePerWeek = pricePerWeek;
    }

    public boolean isEnsuite() {
        return ensuite;
    }



    public String getLocation() {
        return location;
    }



    public List<LocalDate[]> getBookedDates() {
        return bookedDates;
    }

    public boolean isAvailable(LocalDate start, LocalDate end) {
        for (LocalDate[] period : bookedDates) {
            LocalDate bookedStart = period[0];
            LocalDate bookedEnd = period[1];

            // Check for overlap
            if (!end.isBefore(bookedStart) && !start.isAfter(bookedEnd)) {
                return false; // overlapping booking exists
            }
        }
        return true; // no conflicts
    }

    public void addDates(LocalDate start, LocalDate end){
        bookedDates.add(new LocalDate[]{start, end});
    }



    public Room(HomeOwner owner, int bedNum, double price, boolean ensuite, String location,  String roomName){
        this.owner = owner;
        this.bedNum = bedNum;
        this.pricePerWeek = price;
        this.ensuite = ensuite;
        this.location = location;
        this.roomName = roomName;
    }

    public String getRoomName() {
        return roomName;
    }


    public HomeOwner getOwner() {
        return owner;
    }

    public int getBedNum() {
        return bedNum;
    }

    public double getPricePerWeek() {
        return pricePerWeek;
    }


    public void setEnsuite(boolean ensuite) {
        this.ensuite = ensuite;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setRoomName(String name) {
        this.roomName = name;
    }
}
