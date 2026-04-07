package ca.senecacollege.hotel.models;

public enum AuditAction {
    LOGIN("Login Attempt"),
    SEARCH("Searched For"),
    CREATE_RES("Added Reservation"),
    EDIT_RES("Edited Reservation"),
    CHECKOUT("Checked Out Reservation"),
    CHECKIN("Checked In Reservation"),
    CANCELLATION("Cancelled Reservation"),
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
