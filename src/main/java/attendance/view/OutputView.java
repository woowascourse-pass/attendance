package attendance.view;

import attendance.domain.AttendanceTime;
import java.time.DayOfWeek;
import java.time.LocalDateTime;

public class OutputView {

    private static final String GREETING = "오늘은 %d월 %d일 %s요일입니다. 기능을 선택해 주세요.";

    public void printGreeting(LocalDateTime now) {
        System.out.printf(GREETING + "\n",
                now.getMonth().getValue(),
                now.getDayOfMonth(),
                parseDayOfWeek(now.getDayOfWeek())
        );
    }

    private String parseDayOfWeek(DayOfWeek dayOfWeek) {
        return AttendanceTime.getKoreanDayOfWeek(dayOfWeek);
    }

    public void printError(String errorMessage) {
        System.out.println(errorMessage);
        System.out.println();
    }
}
