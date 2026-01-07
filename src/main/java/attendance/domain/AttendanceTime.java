package attendance.domain;

import attendance.message.ErrorMessage;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;

public enum AttendanceTime {
    MONDAY(LocalTime.of(13, 0, 0), LocalTime.of(18, 0, 0), DayOfWeek.MONDAY, "월"),
    TUESDAY(LocalTime.of(10, 0, 0), LocalTime.of(18, 0, 0), DayOfWeek.TUESDAY, "화"),
    WEDNESDAY(LocalTime.of(10, 0, 0), LocalTime.of(18, 0, 0), DayOfWeek.WEDNESDAY, "수"),
    THURSDAY(LocalTime.of(10, 0, 0), LocalTime.of(18, 0, 0), DayOfWeek.THURSDAY, "목"),
    FRIDAY(LocalTime.of(10, 0, 0), LocalTime.of(18, 0, 0), DayOfWeek.FRIDAY, "금"),
    SATURDAY(null, null, DayOfWeek.SATURDAY, "토"),
    SUNDAY(null, null, DayOfWeek.SUNDAY, "일"),
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

    public static void checkWeekend(LocalDateTime now) {
        LocalDate date = now.toLocalDate();

        //25일은 휴일 처리해야함....
        if (now.getDayOfMonth() == 25) {
            throw new IllegalArgumentException(format(date));
        }

        if (now.getDayOfWeek() == DayOfWeek.SATURDAY || now.getDayOfWeek() == DayOfWeek.SUNDAY) {
            throw new IllegalArgumentException(format(date));
        }
    }

    public static void checkWeekend(LocalDateTime now, int date) {
        LocalDate newDate = LocalDate.of(now.getYear(), now.getMonthValue(), date);

        //25일은 휴일 처리해야함....
        if (date == 25) {
            throw new IllegalArgumentException(format(newDate));
        }

        if (now.getDayOfWeek() == DayOfWeek.SATURDAY || now.getDayOfWeek() == DayOfWeek.SUNDAY) {
            throw new IllegalArgumentException(format(newDate));
        }
    }

    public static boolean checkWeekend(LocalDate today) {

        //25일은 휴일 처리해야함....
        if (today.getDayOfMonth() == 25) {
            return true;
        }

        if (today.getDayOfWeek() == DayOfWeek.SATURDAY || today.getDayOfWeek() == DayOfWeek.SUNDAY) {
            return true;
        }

        return false;
    }

    private static Status getStatusByDayOfWeek(LocalTime startTime, LocalTime attendTime) {
        Duration between = Duration.between(startTime, attendTime);
        return Status.getStatus((int) between.toMinutes());
    }

    private static String format(LocalDate now) {
        return String.format(ErrorMessage.NOT_ATTEND_DAY.getMessage(),
                now.getMonthValue(),
                now.getDayOfMonth(),
                AttendanceTime.getKoreanDayOfWeek(now.getDayOfWeek())
        );
    }
}
