package ca.senecacollege.hotel.models;


public enum Role {
    ADMINISTRATOR("ADMN"),
    MANAGER("MNGR");

    private final String label;

    Role(String label){
        this.label = label;
    }

    public String label(){
        return this.label;
    }
}
