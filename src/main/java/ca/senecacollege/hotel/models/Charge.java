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

    public Double getTotal(){
        return amount;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    private Double calcAmount(){
        return pricing.getPrice(source.getBasePrice(), quantity);
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

    public void updateQuantity(int quantity){
        setQuantity(quantity);
        calcAmount();
    }

    public PricingModel getPricing() {
        return pricing;
    }
}
