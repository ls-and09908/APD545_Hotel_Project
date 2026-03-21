package ca.senecacollege.hotel.models;

import jakarta.persistence.*;

//@Entity
public class Charge {
    //@Id
    //@GeneratedValue
    private int chargeNumber;

    private ChargeSource source;
    private Double discount;
    private int quantity;
    private PricingModel pricing;
    private Double amount;

    //@ManyToOne
    //@JoinColumn(name = "bill_bill_number")
    private Billing bill;

    public Billing getBill() {
        return bill;
    }

    public void setBill(Billing bill) {
        this.bill = bill;
    }

    public Double getTotal(){
        return 0.0;
    }
}
