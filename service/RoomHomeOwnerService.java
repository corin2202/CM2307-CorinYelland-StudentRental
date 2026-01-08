package service;

import data.HomeOwner;
import data.Room;
import manager.RoomManager;

import java.util.List;

public class RoomHomeOwnerService {

    private RoomManager roomManager;
    public RoomHomeOwnerService(RoomManager roomManager){
        this.roomManager = roomManager;
    }


    public void addRoom(HomeOwner owner, int bedNum, double pricePerWeek, boolean ensuite, String location, String roomName){
        Room newRoom = new Room(owner, bedNum, pricePerWeek, ensuite, location,  roomName);

        // add room to homeowner and manager
        owner.addRoom(newRoom);
        roomManager.addRoom(newRoom);


    }

    public void deleteRoom(HomeOwner owner, Room room){
        owner.deleteRoom(room);
        roomManager.deleteRoom(room);
    }




}
