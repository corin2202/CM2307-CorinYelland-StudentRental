package observer;

import data.BookingState;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

public class HomeOwnerInbox extends Inbox implements Serializable {

    @Override
    public void update(BookingState state, int bookingId, String roomName) {

        String message = switch (state) {
            case PENDING  -> String.format("Booking request received for room %s", roomName);
            case ACCEPTED -> String.format("You accepted a booking request for room %s", roomName);
            case REJECTED -> String.format("You rejected a booking request for room %s", roomName);
            case CANCELLED -> String.format("Booking cancelled for room %s as it was deleted", roomName);
        };

        messages.add(new InboxMessage(message, bookingId));
    }
}
