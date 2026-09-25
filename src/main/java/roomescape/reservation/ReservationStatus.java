package roomescape.reservation;

public enum ReservationStatus {
    RESERVED("예약");

    private final String label;

    ReservationStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
