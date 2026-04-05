package ca.senecacollege.hotel.models;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class WeekendPricingModel implements PricingModel {
    private String name = "Weekend Pricing";
    private final double multi;

    @Inject
    public WeekendPricingModel(@Named("weekendMultiplier") double multiplier){
        this.multi = multiplier;
    }

    @Override
    public double getPrice(double price, int qty) {
        return price * multi * qty;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
