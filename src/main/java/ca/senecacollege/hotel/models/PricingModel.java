package ca.senecacollege.hotel.models;

public interface PricingModel {
    Double getPrice(Double price, int qty);
    String getName();
}
