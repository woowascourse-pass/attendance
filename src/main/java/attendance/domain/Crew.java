package attendance.domain;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;
import java.util.TreeMap;

public class Crew {
    private final String name;
    private final Map<LocalDateTime, Status> attendance = new TreeMap<>();

    public Crew(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addAttendance(LocalDateTime localDateTime) {
        // 날짜 확인
        DayOfWeek dayOfWeek = localDateTime.getDayOfWeek();
        LocalTime attendTime = LocalTime.of(localDateTime.getHour(), localDateTime.getMinute());

        // TODO : 시간 남으면 이 똑같은 로직 메소드 추출
        // 월요일 이면 13:00 ~ 18:00 까지
        if (dayOfWeek == DayOfWeek.MONDAY) {
            // 로직 처리
            Status status = AttendanceTime.checkStatus(dayOfWeek, attendTime);
            attendance.put(localDateTime, status);
        }
        // 화 ~ 금 이면 10:00 ~ 18:00 까지
        Status status = AttendanceTime.checkStatus(dayOfWeek, attendTime);
        attendance.put(localDateTime, status);
    }
}
