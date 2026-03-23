package ca.senecacollege.hotel.models;

import com.google.inject.Inject;

public class StandardPricingModel implements PricingModel{
    private String name = "Standard Pricing";

    @Inject
    StandardPricingModel(){}

    @Override
    public Double getPrice(Double price, int qty) {
        return price * qty;
    }

    @Override
    public String getName() {
        return this.name;
    }


}
