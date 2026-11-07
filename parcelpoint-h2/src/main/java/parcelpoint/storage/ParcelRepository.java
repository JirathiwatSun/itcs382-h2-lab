package parcelpoint.storage;

import parcelpoint.domain.Parcel;

import java.util.List;
import java.util.Optional;

public interface ParcelRepository {
    void save(Parcel parcel);

    Optional<Parcel> findByCode(String code);

    List<Parcel> findAll();
}
