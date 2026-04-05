package ca.senecacollege.hotel.models;

import jakarta.persistence.*;

@Entity
public class Charge {
    @Id
    @GeneratedValue
    @Column(name = "CHARGE_NUM")
    private int chargeNumber;

    private transient ChargeSource source;
    private transient PricingModel pricing;
    private Double discount = 0.0; // percent value to discount on the charge (0.0-30.0)
    private int quantity;
    private Double amount;

    @ManyToOne
    @JoinColumn(name = "BILL_NUM")
    private Billing bill;

    @ManyToOne
    @JoinColumn(name="SRC_ADD_ON")
    private AddOn srcAddOn = null;

    @ManyToOne
    @JoinColumn(name = "SRC_ROOM")
    private Room srcRoom = null;

    @Column(name = "PRICING_MODEL")
    private String pricingModelName;

    public Billing getBill() {
        return bill;
    }

    public void setBill(Billing bill) {
        this.bill = bill;
    }

    // Returns total charge applied as amount * (remaining percent after discount)
    public Double getTotal(){
        return this.calcAmount() * ((this.discount > 0.0) ? (1.0 - this.discount/100) : 1.0);
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Charge(Billing billing){
        this.bill = billing;
    }

    public Double calcAmount(){
        if(source != null) {
            return pricing.getPrice(source.getBasePrice(), quantity);
        }
        return null;
    }

    public Charge(){}

    public Charge(ChargeSource src, Billing bill, int qty, PricingModel model){
        this.source = src;
        this.bill = bill;
        this.quantity = qty;
        this.pricing = model;
        this.pricingModelName = model.getName();

        if(source.getClass() == AddOn.class){
            this.srcAddOn = (AddOn) source;
        } else if (source.getClass() == Room.class) {
            this.srcRoom = (Room) source;
        }

        this.amount = calcAmount();
    }

    public ChargeSource getSource() {
        return source;
    }
}
