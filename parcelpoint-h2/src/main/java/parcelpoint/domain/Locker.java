package parcelpoint.domain;

public class Locker {
    private final int number;
    private final ParcelSize size;
    private Parcel parcel;

    public Locker(int number, ParcelSize size) {
        this.number = number;
        this.size = size;
    }

    public int getNumber() {
        return number;
    }

    public Parcel getParcel() {
        return parcel;
    }

    public void setParcel(Parcel parcel) {
        this.parcel = parcel;
    }

    public boolean isFree() {
        return parcel == null;
    }

    public boolean fits(Parcel candidate) {
        return isFree() && candidate.getSize().fitsIn(size);
    }
}
