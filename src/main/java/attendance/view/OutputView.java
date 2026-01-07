package attendance.view;

import attendance.domain.AttendanceTime;
import attendance.dto.AttendResultDTO;
import java.time.DayOfWeek;
import java.time.LocalDateTime;

public class OutputView {

    private static final String GREETING = "오늘은 %d월 %d일 %s요일입니다. 기능을 선택해 주세요.";
    private static final String ATTEND_RESULT = "%d월 %d일 %s요일 %d:%d (%s)";

    public void printGreeting(LocalDateTime now) {
        System.out.printf(GREETING + "\n",
                now.getMonthValue(),
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

    public void printAttendResult(AttendResultDTO result) {
        System.out.println();
        System.out.printf(ATTEND_RESULT + "\n",
                result.now().getMonthValue(),
                result.now().getDayOfMonth(),
                parseDayOfWeek(result.now().getDayOfWeek()),
                result.now().getHour(),
                result.now().getMinute(),
                result.status().getStatus());
    }
}
