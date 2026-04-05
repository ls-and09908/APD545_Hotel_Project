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

    /**
     * percent value to discount on the charge (0.0-30.0)
     */
    private Double discount = 0.0; //
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

    /**
     * @return total charge applied, (amount)*(remaining percentage after discount)
     */
    public Double getTotal(){
        return amount * ((this.discount > 0.0) ? (1.0 - this.discount/100) : 1.0);
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
}
