package attendance.util;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ParserTest {

    @DisplayName("잘못된 시간 입력 테스트")
    @ParameterizedTest
    @ValueSource(strings = {"123:123", "-1:9", "123"})
    public void 잘못된_시간_테스트(String inputTime) {
        //given
        //when
        //then
        assertThatThrownBy(() -> Parser.parseTime(DateTimes.now(), inputTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 잘못된 형식을 입력하였습니다.");
    }

    @DisplayName("제대로된 시간 입력 테스트")
    @Test
    public void 제대로_된_시간_테스트() {
        //given
        LocalDateTime now = DateTimes.now();
        LocalDateTime selectTime = Parser.parseTime(now, "12:12");
        //when
        LocalTime localTime = LocalTime.of(12, 12, 0);
        int hour = localTime.getHour();
        int minute = localTime.getMinute();
        //then
        Assertions.assertThat(selectTime.getHour()).isEqualTo(hour);
        Assertions.assertThat(selectTime.getMinute()).isEqualTo(minute);
    }
}