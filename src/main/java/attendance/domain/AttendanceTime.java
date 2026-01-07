package attendance.domain;

import attendance.message.ErrorMessage;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Arrays;

public enum AttendanceTime {
    MONDAY(LocalTime.of(13, 0, 0), LocalTime.of(18, 0, 0), DayOfWeek.MONDAY, "월"),
    TUESDAY(LocalTime.of(10, 0, 0), LocalTime.of(18, 0, 0), DayOfWeek.TUESDAY, "화"),
    WEDNESDAY(LocalTime.of(10, 0, 0), LocalTime.of(18, 0, 0), DayOfWeek.WEDNESDAY, "수"),
    THURSDAY(LocalTime.of(10, 0, 0), LocalTime.of(18, 0, 0), DayOfWeek.THURSDAY, "목"),
    FRIDAY(LocalTime.of(10, 0, 0), LocalTime.of(18, 0, 0), DayOfWeek.FRIDAY, "금"),
    SATURDAY(LocalTime.of(10, 0, 0), LocalTime.of(18, 0, 0), DayOfWeek.SATURDAY, "토"),
    SUNDAY(LocalTime.of(10, 0, 0), LocalTime.of(18, 0, 0), DayOfWeek.SUNDAY, "일"),
    ;

    private final LocalTime startTime;
    private final LocalTime endTime;
    private final DayOfWeek dayOfWeek;
    private final String koreanDayOfWeek;

    AttendanceTime(LocalTime startTime, LocalTime endTime, DayOfWeek dayOfWeek, String koreanDayOfWeek) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.dayOfWeek = dayOfWeek;
        this.koreanDayOfWeek = koreanDayOfWeek;
    }

    public static Status checkStatus(DayOfWeek dayOfWeek, LocalTime attendTime) {
        if (dayOfWeek == DayOfWeek.MONDAY) {
            return getStatusByDayOfWeek(MONDAY.startTime, attendTime);
        }

        return getStatusByDayOfWeek(TUESDAY.startTime, attendTime);
    }

    public static String getKoreanDayOfWeek(DayOfWeek dayOfWeek) {
        AttendanceTime found = Arrays.stream(values())
                .filter(attendanceTime -> attendanceTime.dayOfWeek == dayOfWeek)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.INVALID_DAY_OF_WEEK.getMessage()));

        return found.koreanDayOfWeek;
    }

    private static Status getStatusByDayOfWeek(LocalTime startTime, LocalTime attendTime) {
        Duration between = Duration.between(startTime, attendTime);
        return Status.getStatus((int) between.toMinutes());
    }
}
