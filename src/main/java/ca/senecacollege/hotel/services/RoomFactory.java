package ca.senecacollege.hotel.services;

import ca.senecacollege.hotel.models.Room;
import ca.senecacollege.hotel.models.RoomType;

public class RoomFactory {
    public static Room createRoom(int nextAvailableRoom, RoomType type){
        switch (type){
            case PENTHOUSE:
                return new Room(nextAvailableRoom, type);
            case SINGLE:
                return new Room(nextAvailableRoom, type);
            case DOUBLE:
                return new Room(nextAvailableRoom, type);
            case DELUXE:
                return new Room(nextAvailableRoom, type);
        }
        return null;
    }
}
