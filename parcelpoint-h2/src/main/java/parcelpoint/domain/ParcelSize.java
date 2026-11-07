package parcelpoint.domain;

public enum ParcelSize {
    SMALL(10.0),
    MEDIUM(15.0),
    LARGE(20.0);

    private final double feePerDay;

    ParcelSize(double feePerDay) {
        this.feePerDay = feePerDay;
    }

    public boolean fitsIn(ParcelSize lockerSize) {
        return compareTo(lockerSize) <= 0;
    }

    public double feeFor(int extraDays) {
        return extraDays * feePerDay;
    }
}
