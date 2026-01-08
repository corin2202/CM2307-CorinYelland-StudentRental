package observer;

import data.BookingState;

public interface Observer {
    void update(BookingState state, int bookingId, String roomName);
}
