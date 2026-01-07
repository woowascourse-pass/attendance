package attendance.domain;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;

public enum AttendanceTime {
    MONDAY(LocalTime.of(13, 0, 0), LocalTime.of(18, 0, 0)),
    OTHER(LocalTime.of(10, 0, 0), LocalTime.of(18, 0, 0)),
    ;

    private final LocalTime startTime;
    private final LocalTime endTime;

    AttendanceTime(LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public static Status checkStatus(DayOfWeek dayOfWeek, LocalTime attendTime) {
        if (dayOfWeek == DayOfWeek.MONDAY) {
            return getStatusByDayOfWeek(MONDAY.startTime, attendTime);
        }

        return getStatusByDayOfWeek(OTHER.startTime, attendTime);
    }

    private static Status getStatusByDayOfWeek(LocalTime startTime, LocalTime attendTime) {
        Duration between = Duration.between(startTime, attendTime);
        return Status.getStatus((int) between.toMinutes());
    }
}
