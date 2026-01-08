package data;

import observer.InboxMessage;
import observer.StudentInbox;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Student extends User implements Serializable {

    public Student(String username, String password){
        super(username, password);
        this.inbox = new StudentInbox();
    }
}