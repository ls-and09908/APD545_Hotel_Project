package ca.senecacollege.hotel.models;

public class Room implements ChargeSource {
    private int roomNumber;
    private RoomStatus roomStatus;
    private RoomType roomType;

    @Override
    public Double getBasePrice() {
        return this.roomType.getPrice();
    }

    @Override
    public Boolean isPaidNightly() {
        return true;
    }
}
