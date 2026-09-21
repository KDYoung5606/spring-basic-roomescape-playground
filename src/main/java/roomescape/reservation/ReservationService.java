package roomescape.reservation;

import org.springframework.stereotype.Service;
import roomescape.exception.BusinessException;
import roomescape.exception.ErrorCode;
import roomescape.member.LoginMember;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;
import roomescape.waiting.WaitingRepository;

import java.util.List;
import java.util.stream.Stream;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;
    private final MemberRepository memberRepository;
    private final WaitingRepository waitingRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              TimeRepository timeRepository,
                              ThemeRepository themeRepository,
                              MemberRepository memberRepository,
                              WaitingRepository waitingRepository) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
        this.memberRepository = memberRepository;
        this.waitingRepository = waitingRepository;
    }

    public ReservationResponse save(ReservationRequest request, LoginMember loginMember) {
        Time time = timeRepository.findById(request.getTime())
                .orElseThrow(() -> new BusinessException(ErrorCode.TIME_NOT_FOUND));
        Theme theme = themeRepository.findById(request.getTheme())
                .orElseThrow(() -> new BusinessException(ErrorCode.THEME_NOT_FOUND));
        if (reservationRepository.existsByDateAndTimeIdAndThemeId(
                request.getDate(), request.getTime(), request.getTheme())) {
            throw new BusinessException(ErrorCode.DUPLICATE_RESERVATION);
        }
        Reservation reservation = reservationRepository.save(
                createReservation(request, loginMember, time, theme));
        return new ReservationResponse(reservation.getId(), reserverName(reservation),
                reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getValue());
    }

    private Reservation createReservation(ReservationRequest request, LoginMember loginMember,
                                          Time time, Theme theme) {
        if (request.getName() != null && !request.getName().isBlank()) {
            return new Reservation(request.getName(), request.getDate(), time, theme);
        }
        Member member = memberRepository.findById(loginMember.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        return new Reservation(member, request.getDate(), time, theme);
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), reserverName(it), it.getTheme().getName(), it.getDate(), it.getTime().getValue()))
                .toList();
    }

    public List<MyReservationResponse> findMyReservations(LoginMember loginMember) {
        List<MyReservationResponse> reservations = reservationRepository.findByMemberId(loginMember.getId()).stream()
                .map(it -> new MyReservationResponse(
                        it.getId(),
                        it.getTheme().getName(),
                        it.getDate(),
                        it.getTime().getValue(),
                        it.getStatus().getLabel()))
                .toList();
        List<MyReservationResponse> waitings = waitingRepository.findWaitingsWithRankByMemberId(loginMember.getId()).stream()
                .map(it -> new MyReservationResponse(
                        it.getWaiting().getId(),
                        it.getWaiting().getTheme().getName(),
                        it.getWaiting().getDate(),
                        it.getWaiting().getTime().getValue(),
                        (it.getRank() + 1) + "번째 " + ReservationStatus.WAITING.getLabel()))
                .toList();
        return Stream.concat(reservations.stream(), waitings.stream()).toList();
    }

    private String reserverName(Reservation reservation) {
        if (reservation.getMember() != null) {
            return reservation.getMember().getName();
        }
        return reservation.getName();
    }
}
