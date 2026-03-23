package ca.senecacollege.hotel.models;

import com.google.inject.Inject;

public class WeekendPricingModel implements PricingModel {
    private String name = "Weekend Pricing";
    private Double multi = 2.6;

    @Inject
    public WeekendPricingModel(){}

    public WeekendPricingModel(Double multiplier){
        this.multi = multiplier;
    }

    @Override
    public Double getPrice(Double price, int qty) {
        return price * multi * qty;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
