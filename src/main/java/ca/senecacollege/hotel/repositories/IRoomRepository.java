package ca.senecacollege.hotel.repositories;

import ca.senecacollege.hotel.models.Room;
import ca.senecacollege.hotel.models.RoomType;

import java.time.LocalDate;
import java.util.List;

public interface IRoomRepository {
    List<Room> getAllRooms();
    void saveRoom(Room r);
    Room getRoom(int roomNum);
    List<Room> getRoomsByType(RoomType type);

    // gets rooms that are fully available in the future (no concern for reservation overlap)
    List<Room> getAvailableRooms();

    // gets rooms that are Not reserved during the specified dates
    List<Room> getAvailableRooms(LocalDate checkIn, LocalDate checkOut);
}
