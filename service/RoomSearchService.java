package service;

import data.Room;
import data.SearchCriteria;
import manager.RoomManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RoomSearchService {

    private final RoomManager roomManager;

    public RoomSearchService(RoomManager roomManager){
        this.roomManager = roomManager;
    }

    // filters rooms based on a criteria
    public List<Room> searchRooms(SearchCriteria criteria){

        return roomManager.getRoomList().stream()
                .filter(room -> room.getPricePerWeek() <= criteria.getMaxPrice())
                .filter(room -> room.isEnsuite() == criteria.isHasEnsuite())
                .filter(room -> room.getBedNum() >= criteria.getMinBeds())
                .toList();


    }

    public List<Room> searchRoomsImperative(SearchCriteria criteria){

        List<Room> filteredRooms = new ArrayList<>();

        for (Room room : roomManager.getRoomList()) {
            if (room.getPricePerWeek() <= criteria.getMaxPrice() &&
                    room.isEnsuite() == criteria.isHasEnsuite() &&
                    room.getBedNum() >= criteria.getMinBeds()) {

                filteredRooms.add(room);
            }
        }

        return filteredRooms;

    }



}
