package parcelpoint.domain;

public class Parcel {
    private final String code;
    private final String studentId;
    private final ParcelSize size;
    private ParcelStatus status;
    private Integer lockerNumber;

    public Parcel(String code, String studentId, ParcelSize size) {
        this(code, studentId, size, ParcelStatus.STORED, null);
    }

    public Parcel(String code, String studentId, ParcelSize size, ParcelStatus status,
                  Integer lockerNumber) {
        this.code = code;
        this.studentId = studentId;
        this.size = size;
        this.status = status;
        this.lockerNumber = lockerNumber;
    }

    public String getCode() {
        return code;
    }

    public String getStudentId() {
        return studentId;
    }

    public ParcelSize getSize() {
        return size;
    }

    public ParcelStatus getStatus() {
        return status;
    }

    public Integer getLockerNumber() {
        return lockerNumber;
    }

    public boolean isWaiting() {
        return status == ParcelStatus.STORED;
    }

    public void storeIn(int number) {
        lockerNumber = number;
        status = ParcelStatus.STORED;
    }

    public void markPickedUp() {
        lockerNumber = null;
        status = ParcelStatus.PICKED_UP;
    }

    public double fee(int days) {
        return days <= 3 ? 0 : size.feeFor(days - 3);
    }
}
