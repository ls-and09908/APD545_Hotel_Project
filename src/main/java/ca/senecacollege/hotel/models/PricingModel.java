package ca.senecacollege.hotel.models;

public interface PricingModel {
    double getPrice(double price, int qty);
    String getName();
}
