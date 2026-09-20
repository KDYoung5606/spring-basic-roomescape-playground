package roomescape.time;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TimeRepository extends JpaRepository<Time, Long> {

    List<Time> findByDeletedFalse();

    @Modifying
    @Transactional
    @Query("update Time t set t.deleted = true where t.id = ?1")
    void updateDeletedTrueById(Long id);
}
