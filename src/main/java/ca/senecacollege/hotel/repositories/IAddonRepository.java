package ca.senecacollege.hotel.repositories;

import ca.senecacollege.hotel.models.AddOn;

import java.util.List;

public interface IAddonRepository {
    List<AddOn> getAllAddOns();
    void saveAddOn(AddOn r);
    AddOn getAddOn(int addOnID);
}
