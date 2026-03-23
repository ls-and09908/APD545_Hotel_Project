package ca.senecacollege.hotel.models;

public class WeekendPricingModel implements PricingModel {
    private String name = "Weekend Pricing";
    private Double multi;

    public WeekendPricingModel(Double multiplier){
        this.multi = multiplier;
    }

    @Override
    public Double getPrice(Double price, int qty) {
        return price * multi * qty;
    }
}
