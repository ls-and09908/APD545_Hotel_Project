package ca.senecacollege.hotel.models;

import java.util.ArrayList;
import java.util.List;

public enum PaymentMethod {
    CREDIT("Credit"),
    CASH("Cash"),
    LOYALTY("Loyalty Pts"),
    REFUND("Refund"),
    DEPOSIT("Deposit");

    private final String label;

    PaymentMethod(String label){
        this.label = label;
    }

    public String label(){ return this.label; }

    public static List<String> labels(){
        List<String> labels = new ArrayList<>();
        for (PaymentMethod p : PaymentMethod.values()){
            labels.add(p.label());
        }
        return labels;
    }
}
