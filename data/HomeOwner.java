package data;

import manager.RoomManager;
import observer.HomeOwnerInbox;
import observer.InboxMessage;

import java.io.Serializable;
import java.util.List;
import java.util.Scanner;

import java.util.ArrayList;

public class HomeOwner extends User implements Serializable {
    private final List<Room> myRooms = new ArrayList<>();

    public HomeOwner(String username, String password){
        super(username, password);
        this.inbox = new HomeOwnerInbox();
    }

    public void addRoom(Room createdRoom){
        myRooms.add(createdRoom);
    }

    public void deleteRoom(Room room){
        myRooms.remove(room);
    }

    public List<Room> getMyRooms(){
        return myRooms;
    }
}



