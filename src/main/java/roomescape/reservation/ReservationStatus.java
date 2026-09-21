package roomescape.reservation;

public enum ReservationStatus {
    RESERVED("예약"),
    COMPLETED("완료"),
    CANCELED("취소");

    private final String label;

    ReservationStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
