package observer;

import java.util.ArrayList;
import java.util.List;

public abstract class Inbox implements Observer {
    protected List<InboxMessage> messages = new ArrayList<>();

    public List<InboxMessage> getInboxMessages() {
        return messages;
    }
}