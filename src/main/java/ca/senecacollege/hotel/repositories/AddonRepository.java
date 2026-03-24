package ca.senecacollege.hotel.repositories;

import ca.senecacollege.hotel.models.AddOn;

import java.util.List;

public class AddonRepository implements IAddonRepository {
    @Override
    public List<AddOn> getAllAddOns() {
        return List.of();
    }

    @Override
    public void saveAddOn(AddOn r) {

    }

    @Override
    public AddOn getAddOn(int addOnID) {
        return null;
    }

    @Override
    public AddOn getAddOn(String name) {
        return null;
    }
}
