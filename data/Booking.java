package data;

import observer.Observer;
import observer.Subject;

import java.awt.print.Book;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// booking record, no methods needed as only stores data
public class Booking implements Subject, Serializable {

    private int bookingId;
    private Room room;
    private Student student;
    private LocalDate startDate;
    private LocalDate endDate;
    private BookingState state = BookingState.PENDING;
    List<Observer> observerList = new ArrayList<>();

    public Booking(Room room, Student student, LocalDate startDate, LocalDate endDate, int bookingId){
        this.room = room;
        this.student = student;
        this.startDate = startDate;
        this.endDate = endDate;
        this.bookingId = bookingId;


    }

    public Room getRoom(){
        return room;
    }

    public Student getStudent(){
        return student;
    }

    public BookingState getState(){
        return state;
    }

    public int getBookingId(){
        return bookingId;
    }

    @Override
    public void addObserver(Observer observer){
        observerList.add(observer);

    }

    @Override
    public void removeObserver(Observer observer) {
        observerList.remove(observer);

    }

    @Override
    public void notifyObservers() {
        for (Observer o: observerList){
            o.update(state, bookingId, room.getRoomName());
        }
    }

    public void setState(BookingState state){
        this.state = state;
        notifyObservers();
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
}
