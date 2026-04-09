package ca.senecacollege.hotel.models;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class PeakPricingModel implements PricingModel {
    private String name = "Seasonal Pricing";
    private final double multi;
    private final double baseIncrease;

    @Inject
    public PeakPricingModel(@Named("peakMultiplier") double multi, @Named("peakBaseIncrease") double baseIncrease) {
        this.multi = multi;
        this.baseIncrease = baseIncrease;
    }

    @Override
    public double getPrice(double price, int qty) {
        return baseIncrease + price * multi * qty;
    }

    @Override
    public String getName() {
        return name;
    }
}
