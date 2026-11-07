package parcelpoint.storage;

import parcelpoint.domain.Parcel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryParcelRepository implements ParcelRepository {
    private final Map<String, Parcel> parcels = new LinkedHashMap<>();

    @Override
    public void save(Parcel parcel) {
        parcels.put(parcel.getCode(), parcel);
    }

    @Override
    public Optional<Parcel> findByCode(String code) {
        return Optional.ofNullable(parcels.get(code));
    }

    @Override
    public List<Parcel> findAll() {
        return new ArrayList<>(parcels.values());
    }
}
