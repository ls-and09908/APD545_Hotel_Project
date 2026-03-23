package ca.senecacollege.hotel.repositories;

import ca.senecacollege.hotel.models.Room;

import java.util.List;

public interface IRoomRepository {
    List<Room> getAllRooms();
    void saveRoom(Room r);
    Room getRoom(int roomNum);
}
