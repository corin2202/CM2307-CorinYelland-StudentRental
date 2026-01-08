package manager;

import data.Booking;
import data.Room;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

// singleton manager.BookingManager stores all bookings
public final class BookingManager {
    private static BookingManager instance;
    private int nextId = 1;

    private List<Booking> bookingsList = new ArrayList<>();

    private BookingManager(){
        loadBookings();
    };

    public static BookingManager getInstance(){
        if (instance == null){
            instance = new BookingManager();
        }

        return instance;
    }

    public List<Booking> getBookingsList(){
        return bookingsList;
    }

    public void addBooking(Booking booking){
        bookingsList.add(booking);
        saveBookings();
    }

    public List<Booking> getBookingsForRoom(Room r) {
        List<Booking> result = new ArrayList<>();

        for (Booking b : bookingsList) {
            if (b.getRoom() != null && b.getRoom().equals(r)) {
                result.add(b);
            }
        }

        return result;
    }

    public int generateBookingId(){
        nextId++;
        return nextId - 1;
    }

    public void loadBookings(){
        try{
            FileInputStream readData = new FileInputStream("bookingdata.ser");
            ObjectInputStream readStream = new ObjectInputStream(readData);

            List<Booking> bookings = (List<Booking>) readStream.readObject();
            readStream.close();
            bookingsList = bookings;
            System.out.println("Loaded bookings successfully");

        }catch (Exception e) {
            if (bookingsList.isEmpty()){
                System.out.println("Bookings list is empty");
            } else {
                System.out.println("Error loading booking data");
            }

        }
    }

    public void saveBookings(){

        // serialize and write list to bookingdata.ser file
        try{
            FileOutputStream writeData = new FileOutputStream("bookingdata.ser");
            ObjectOutputStream writeStream = new ObjectOutputStream(writeData);

            writeStream.writeObject(bookingsList);
            writeStream.flush();
            writeStream.close();

            System.out.println("Saved bookings successfully");

        } catch (IOException e){

            System.out.println("Error writing booking data to file");
        }

    };



}
