package ca.senecacollege.hotel.models;

import jakarta.persistence.*;

@Entity
public class Charge {
    @Id
    @GeneratedValue
    @Column(name = "CHARGE_NUM")
    private int chargeNumber;

    private transient ChargeSource source;
    private Double discount;
    private int quantity;
    //private PricingModel pricing;
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

    public Billing getBill() {
        return bill;
    }

    public void setBill(Billing bill) {
        this.bill = bill;
    }

    public Double getTotal(){
        return 0.0;
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

    public Charge(Double discount, int quantity, Double amount, Billing bill, ChargeSource source) {
        this.discount = discount;
        this.quantity = quantity;
        this.amount = amount;
        this.bill = bill;
        this.source = source;

        if(source.getClass() == AddOn.class){
            this.srcAddOn = (AddOn) source;
        } else if (source.getClass() == Room.class) {
            this.srcRoom = (Room) source;
        }
    }
}
