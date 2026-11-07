package parcelpoint.service;

import parcelpoint.domain.Locker;
import parcelpoint.domain.Parcel;
import parcelpoint.domain.ParcelSize;
import parcelpoint.storage.ParcelRepository;

import java.util.List;
import java.util.Optional;

public class ParcelService {
    private final List<Locker> lockers = List.of(
            new Locker(1, ParcelSize.SMALL),
            new Locker(2, ParcelSize.MEDIUM),
            new Locker(3, ParcelSize.LARGE));
    private final ParcelRepository repository;

    public ParcelService(ParcelRepository repository) {
        this.repository = repository;
        for (Parcel parcel : repository.findAll()) {
            if (parcel.isWaiting()) {
                lockerNumbered(parcel.getLockerNumber()).setParcel(parcel);
            }
        }
    }

    public Optional<Locker> store(Parcel parcel) {
        for (Locker locker : lockers) {
            if (locker.fits(parcel)) {
                locker.setParcel(parcel);
                parcel.storeIn(locker.getNumber());
                repository.save(parcel);
                return Optional.of(locker);
            }
        }
        return Optional.empty();
    }

    public Optional<Double> pickUp(String code, int days) {
        for (Locker locker : lockers) {
            Parcel parcel = locker.getParcel();
            if (parcel != null && parcel.getCode().equals(code)) {
                locker.setParcel(null);
                parcel.markPickedUp();
                repository.save(parcel);
                return Optional.of(parcel.fee(days));
            }
        }
        return Optional.empty();
    }

    public Optional<Parcel> find(String code) {
        return repository.findByCode(code);
    }

    public List<Parcel> allParcels() {
        return repository.findAll();
    }

    public long countFreeLockers() {
        return lockers.stream().filter(Locker::isFree).count();
    }

    private Locker lockerNumbered(int number) {
        for (Locker locker : lockers) {
            if (locker.getNumber() == number) {
                return locker;
            }
        }
        throw new IllegalStateException("No locker " + number);
    }
}
