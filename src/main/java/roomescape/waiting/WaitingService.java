package roomescape.waiting;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.exception.BusinessException;
import roomescape.exception.ErrorCode;
import roomescape.member.LoginMember;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.reservation.Reservation;
import roomescape.reservation.ReservationRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

@Service
public class WaitingService {
    private final WaitingRepository waitingRepository;
    private final ReservationRepository reservationRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;
    private final MemberRepository memberRepository;

    public WaitingService(WaitingRepository waitingRepository,
                          ReservationRepository reservationRepository,
                          TimeRepository timeRepository,
                          ThemeRepository themeRepository,
                          MemberRepository memberRepository) {
        this.waitingRepository = waitingRepository;
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public WaitingResponse save(WaitingRequest request, LoginMember loginMember) {
        Time time = timeRepository.findById(request.getTime())
                .orElseThrow(() -> new BusinessException(ErrorCode.TIME_NOT_FOUND));
        Theme theme = themeRepository.findById(request.getTheme())
                .orElseThrow(() -> new BusinessException(ErrorCode.THEME_NOT_FOUND));
        Member member = memberRepository.findById(loginMember.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        validateNotDuplicated(member, request);

        if (!isAlreadyReserved(request)) {
            return reserve(member, request, time, theme);
        }
        return createWaiting(member, request, time, theme);
    }

    public void cancel(Long id, LoginMember loginMember) {
        Waiting waiting = waitingRepository.findByIdAndMemberId(id, loginMember.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.WAITING_NOT_FOUND));
        waitingRepository.delete(waiting);
    }

    private void validateNotDuplicated(Member member, WaitingRequest request) {
        if (reservationRepository.existsByMemberIdAndDateAndTimeIdAndThemeId(
                member.getId(), request.getDate(), request.getTime(), request.getTheme())
                || waitingRepository.existsByMemberIdAndDateAndTimeIdAndThemeId(
                member.getId(), request.getDate(), request.getTime(), request.getTheme())) {
            throw new BusinessException(ErrorCode.DUPLICATE_WAITING);
        }
    }

    private boolean isAlreadyReserved(WaitingRequest request) {
        return reservationRepository.existsByDateAndTimeIdAndThemeId(
                request.getDate(), request.getTime(), request.getTheme());
    }

    private WaitingResponse reserve(Member member, WaitingRequest request, Time time, Theme theme) {
        Reservation reservation = reservationRepository.save(
                new Reservation(member, request.getDate(), time, theme));
        return new WaitingResponse(reservation.getId(), 0L);
    }

    private WaitingResponse createWaiting(Member member, WaitingRequest request, Time time, Theme theme) {
        Waiting waiting = waitingRepository.save(new Waiting(member, request.getDate(), time, theme));
        long rank = waitingRepository.findWaitingsWithRankByMemberId(member.getId()).stream()
                .filter(it -> it.getWaiting().getId().equals(waiting.getId()))
                .findFirst()
                .orElseThrow()
                .getRank();
        return new WaitingResponse(waiting.getId(), rank + 1);
    }
}
