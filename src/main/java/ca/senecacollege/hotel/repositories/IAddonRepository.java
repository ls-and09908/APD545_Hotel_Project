package ca.senecacollege.hotel.repositories;

import ca.senecacollege.hotel.models.AddOn;

import java.util.List;

public interface IAddonRepository {
    List<AddOn> getAllAddOns();
    void saveAddOn(AddOn a);
    AddOn getAddOn(int addOnID);
    AddOn getAddOn(String name);
}
