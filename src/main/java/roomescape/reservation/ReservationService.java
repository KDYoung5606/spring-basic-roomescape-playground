package roomescape.reservation;

import org.springframework.stereotype.Service;
import roomescape.member.LoginMember;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

import java.util.List;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              TimeRepository timeRepository,
                              ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
    }

    public ReservationResponse save(ReservationRequest request, LoginMember loginMember) {
        String name = resolveName(request, loginMember);
        Time time = timeRepository.findById(request.getTime()).orElseThrow();
        Theme theme = themeRepository.findById(request.getTheme()).orElseThrow();
        Reservation reservation = reservationRepository.save(
                new Reservation(name, request.getDate(), time, theme));
        return new ReservationResponse(reservation.getId(), name,
                reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getValue());
    }

    private String resolveName(ReservationRequest request, LoginMember loginMember) {
        if (request.getName() != null && !request.getName().isBlank()) {
            return request.getName();
        }
        return loginMember.getName();
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getValue()))
                .toList();
    }
}
