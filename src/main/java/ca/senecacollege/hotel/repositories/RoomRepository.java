package ca.senecacollege.hotel.repositories;

import ca.senecacollege.hotel.models.Room;
import com.google.inject.Inject;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class RoomRepository implements IRoomRepository {
    private EntityManagerFactory emf;

    @Inject
    public RoomRepository(EntityManagerFactory emf){    }

    @Override
    public List<Room> getAllRooms() {
        return List.of();
    }

    @Override
    public void saveRoom(Room r) {

    }

    @Override
    public Room getRoom(int roomNum) {
        return null;
    }
}
