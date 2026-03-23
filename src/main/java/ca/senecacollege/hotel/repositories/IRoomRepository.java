package ca.senecacollege.hotel.repositories;

import ca.senecacollege.hotel.models.Room;

import java.util.List;

public interface IRoomRepository {
    // temporary function? commented out as it wasn't being used
    //public Room createRoom();

    List<Room> getAllRooms();
    void saveRoom(Room r);
    Room getRoom(int roomNum);
}
