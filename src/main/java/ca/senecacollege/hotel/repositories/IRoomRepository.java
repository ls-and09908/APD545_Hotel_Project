package ca.senecacollege.hotel.repositories;

import ca.senecacollege.hotel.models.Room;
import ca.senecacollege.hotel.models.RoomType;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IRoomRepository {
    List<Room> getAllRooms();
    void saveRoom(Room r);
    Optional<Room> getRoom(int roomNum);
    List<Room> getRoomsByType(RoomType type);

    // gets rooms that are Not reserved during the specified dates
    List<Room> getAvailableRooms(LocalDate checkIn, LocalDate checkOut);
}
