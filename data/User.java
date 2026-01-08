package data;

import observer.HomeOwnerInbox;
import observer.Inbox;
import observer.InboxMessage;

import java.io.Serializable;
import java.util.List;
import java.util.Scanner;


// abstract superclass factory uses to create student/homeowner
public abstract class User implements Serializable {

    protected String username;

    protected String password;

    protected Inbox inbox;

    public String getUsername() {
        return username;
    }


    public String getPassword() {
        return password;
    }


    public User(String username, String password){
        this.username = username;
        this.password = password;

    }

    public List<InboxMessage> getInboxMessages(){
        return inbox.getInboxMessages();
    }

    public Inbox getInbox(){
        return inbox;
    }




}
