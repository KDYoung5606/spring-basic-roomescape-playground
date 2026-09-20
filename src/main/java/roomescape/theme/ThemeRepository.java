package roomescape.theme;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ThemeRepository extends JpaRepository<Theme, Long> {

    List<Theme> findByDeletedFalse();

    @Modifying
    @Transactional
    @Query("update Theme t set t.deleted = true where t.id = ?1")
    void updateDeletedTrueById(Long id);
}
