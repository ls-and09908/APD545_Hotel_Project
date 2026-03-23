package ca.senecacollege.hotel.models;

public class StandardPricingModel implements PricingModel{
    private String name = "Standard Pricing";
    StandardPricingModel(){}

    @Override
    public Double getPrice(Double price, int qty) {
        return price * qty + 10.0;
    }
}
