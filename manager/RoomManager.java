package manager;

import data.Room;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


// singleton room manager
// stores all rooms in a global object for efficient lookup
public final class RoomManager {
    private static RoomManager instance;
    private List<Room> roomList = new ArrayList<>();

    private RoomManager(){

        loadRooms();
    };

    public static RoomManager getInstance(){
        if (instance == null){
            instance = new RoomManager();
        }

        return instance;
    }

    public void addRoom(Room roomToAdd){

        roomList.add(roomToAdd);
        saveRooms();
    }

    public void deleteRoom(Room room){
        roomList.remove(room);
        saveRooms();
    }

    public void saveRooms(){

        // serialize and write list to roomdata.ser file
        try{
            FileOutputStream writeData = new FileOutputStream("roomdata.ser");
            ObjectOutputStream writeStream = new ObjectOutputStream(writeData);

            writeStream.writeObject(roomList);
            writeStream.flush();
            writeStream.close();

            System.out.println("Saved rooms successfully");

        } catch (IOException e){

            System.out.println("Error writing room data to file");
        }

    };

    public void loadRooms(){
        // loads room data from the roomdata.ser file
        try{
            FileInputStream readData = new FileInputStream("roomdata.ser");
            ObjectInputStream readStream = new ObjectInputStream(readData);

            List<Room> rooms = (List<Room>) readStream.readObject();
            readStream.close();
            roomList = rooms;
            System.out.println("Loaded rooms successfully");

        }catch (Exception e) {
            if (roomList.isEmpty()){
                System.out.println("data.Room data is empty");
            } else {
                System.out.println("Error loading room data");
            }

        }

    };

    public List<Room> getRoomList(){
        return roomList;
    }


}