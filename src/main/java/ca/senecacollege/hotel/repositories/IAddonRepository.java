package ca.senecacollege.hotel.repositories;

import ca.senecacollege.hotel.models.AddOn;

import java.util.List;
import java.util.Optional;

public interface IAddonRepository {
    List<AddOn> getAllAddOns();
    Optional<AddOn> getAddOn(int addOnID);
    Optional<AddOn> getAddOn(String name);
}
