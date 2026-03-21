package ca.senecacollege.hotel.models;

public interface ChargeSource {
    Double getBasePrice();
    Boolean isPaidNightly();
}
