package roomescape.reservation;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import roomescape.exception.BusinessException;
import roomescape.exception.ErrorCode;
import roomescape.member.Member;
import roomescape.theme.Theme;
import roomescape.time.Time;

@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String date;
    @ManyToOne
    private Member member;
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Time time;
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Theme theme;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(255) default 'RESERVED'")
    private ReservationStatus status = ReservationStatus.RESERVED;

    public Reservation(Long id, String name, String date, Time time, Theme theme) {
        validateRequired(time, theme);
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public Reservation(String name, String date, Time time, Theme theme) {
        validateRequired(time, theme);
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public Reservation(Member member, String date, Time time, Theme theme) {
        validateRequired(time, theme);
        this.member = member;
        this.name = "";
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public Reservation() {

    }

    private static void validateRequired(Time time, Theme theme) {
        if (time == null) {
            throw new BusinessException(ErrorCode.BLANK_TIME);
        }
        if (theme == null) {
            throw new BusinessException(ErrorCode.BLANK_THEME);
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Member getMember() {
        return member;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public String getDate() {
        return date;
    }

    public Time getTime() {
        return time;
    }

    public Theme getTheme() {
        return theme;
    }
}
