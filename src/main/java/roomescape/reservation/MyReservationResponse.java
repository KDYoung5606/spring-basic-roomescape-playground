package roomescape.reservation;

public class MyReservationResponse {
    private Long id;
    private String theme;
    private String date;
    private String time;
    private String status;

    public MyReservationResponse(Long id, String theme, String date, String time, String status) {
        this.id = id;
        this.theme = theme;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getTheme() {
        return theme;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getStatus() {
        return status;
    }
}
