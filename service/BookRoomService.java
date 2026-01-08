package service;

import data.*;
import manager.BookingManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookRoomService {

    private final BookingManager bookingManager;


    public BookRoomService(BookingManager bookingManager){
        this.bookingManager = bookingManager;
    }

    public void cancelBookingOnRoomDeletion(Room room){
        // get all bookings with this room
        List<Booking> bookingsForRoom = bookingManager.getBookingsForRoom(room);

        // change booking status to canceled
        for (Booking b: bookingsForRoom){
            b.setState(BookingState.CANCELLED);

        }

    }


    public void bookRoom(Student s, Room room, LocalDate startDate, LocalDate endDate){

        // create and assign a booking id to new booking
        int bookingId = bookingManager.generateBookingId();
        Booking newBooking = new Booking(room, s, startDate, endDate, bookingId);

        // add booking to central manager
        bookingManager.addBooking(newBooking);

        // subscribe student and homeowners inbox to this booking and set to pending
        newBooking.addObserver(s.getInbox());
        newBooking.addObserver(room.getOwner().getInbox());
        newBooking.setState(BookingState.PENDING);



    }

    public Booking getBookingById(int bookingId) {
        List<Booking> bookings = bookingManager.getBookingsList();
        for (Booking b: bookings){
            if (b.getBookingId() == bookingId){
                return b;
            }
        }
        return null;
    }

    public void rejectBookingsWithOverlappingDates(Booking booking){
        Room room = booking.getRoom();
        // Reject other bookings for same room that overlap dates
        for (Booking b : bookingManager.getBookingsList()) {
            if (b == booking) continue; // skip accepted booking

            boolean sameRoom = b.getRoom().equals(room);
            boolean pending = b.getState() == BookingState.PENDING;
            boolean overlap = room.isAvailable(b.getStartDate(), b.getEndDate());

            if (sameRoom && pending && overlap) {
                b.setState(BookingState.REJECTED);
            }
        }

    }

    public void acceptBooking(Booking booking) {

        Room room = booking.getRoom();

        // Reject any others with overlapping booking periods
        rejectBookingsWithOverlappingDates(booking);

        // Accept booking
        booking.setState(BookingState.ACCEPTED);
        room.addDates(booking.getStartDate(), booking.getEndDate());



        // Save all bookings
        bookingManager.saveBookings();

    }

    public void rejectBooking(Booking booking){
        booking.setState(BookingState.REJECTED);
        bookingManager.saveBookings();

    }

    // returns a list of bookings a student has
    public List<Booking> showStudentsBookings(Student s){
        List<Booking> studentsBookings = new ArrayList<>();
        for (Booking b : bookingManager.getBookingsList()) {
            if (b.getStudent().getUsername().equals(s.getUsername())){
                studentsBookings.add(b);
            }
        }
        return studentsBookings;

    }
}
