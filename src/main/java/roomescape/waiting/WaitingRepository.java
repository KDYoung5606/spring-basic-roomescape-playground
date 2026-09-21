package roomescape.waiting;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface WaitingRepository extends JpaRepository<Waiting, Long> {

    boolean existsByMemberIdAndDateAndTimeIdAndThemeId(Long memberId, String date, Long timeId, Long themeId);

    Optional<Waiting> findByIdAndMemberId(Long id, Long memberId);

    @Query("SELECT new roomescape.waiting.WaitingWithRank(" +
           "    w, " +
           "    (SELECT COUNT(w2) " +
           "     FROM Waiting w2 " +
           "     WHERE w2.theme = w.theme " +
           "       AND w2.date = w.date " +
           "       AND w2.time = w.time " +
           "       AND w2.id < w.id)) " +
           "FROM Waiting w " +
           "WHERE w.member.id = ?1")
    List<WaitingWithRank> findWaitingsWithRankByMemberId(Long memberId);
}
