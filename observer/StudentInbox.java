package observer;

import data.BookingState;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

import static java.lang.String.format;

public class StudentInbox extends Inbox implements Serializable {

    @Override
    public void update(BookingState state, int bookingId, String roomName) {

        String message = switch (state) {
            case PENDING -> String.format("Your booking request for room %s has been sent", roomName);
            case ACCEPTED -> String.format("Your booking request for room %s was ACCEPTED", roomName);
            case REJECTED -> String.format("Your booking request for room %s was REJECTED", roomName);
            case CANCELLED -> String.format("Booking cancelled as room %s no longer exists", roomName);
        };

        messages.add(new InboxMessage(message, bookingId));
    }
}