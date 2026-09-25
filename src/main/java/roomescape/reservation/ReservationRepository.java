package roomescape.reservation;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Override
    @EntityGraph(attributePaths = {"member", "time", "theme"})
    List<Reservation> findAll();

    List<Reservation> findByDateAndThemeId(String date, Long themeId);

    List<Reservation> findByMemberId(Long memberId);

    boolean existsByDateAndTimeIdAndThemeId(String date, Long timeId, Long themeId);

    boolean existsByMemberIdAndDateAndTimeIdAndThemeId(Long memberId, String date, Long timeId, Long themeId);
}
