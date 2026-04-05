package ca.senecacollege.hotel.models;

public enum AuditAction {
    LOGIN("Login Attempt"),
    SEARCH("Search"),
    UPDATE_RES("Reservation Update"),
    CHECKOUT("Check-out"),
    CANCELLATION("Cancellation"),
    DISCOUNT("Discount Application"),
    PAYMENT("Processed Payment"),
    REFUND("Processed Refund"),
    FEEDBACK("Received Feedback");

    private final String label;

    AuditAction(String label){
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public String label() {
        return label;
    }
}
