package observer;

import java.io.Serializable;

public class InboxMessage implements Serializable {
    private final String text;
    private final int bookingId;


    public InboxMessage(String text, int bookingId) {
        this.text = text;
        this.bookingId = bookingId;

    }

    public String getText() {
        return text;
    }

    public int getBookingId() {
        return bookingId;
    }


}