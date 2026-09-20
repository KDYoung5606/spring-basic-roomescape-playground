package roomescape;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class JpaTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TimeRepository timeRepository;

    @Test
    @DisplayName("저장한 시간을 id로 조회하면 저장한 시간 값이 조회된다")
    void test_저장한_시간을_id로_조회하면_저장한_시간_값이_조회된다() {
        Time time = new Time("10:00");
        entityManager.persist(time);
        entityManager.flush();

        Time persistTime = timeRepository.findById(time.getId()).orElse(null);

        assertThat(persistTime.getValue()).isEqualTo(time.getValue());
    }
}
