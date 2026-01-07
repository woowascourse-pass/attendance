package attendance.domain;

import attendance.dto.ModifyAttendResultDTO;
import attendance.message.ErrorMessage;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;
import java.util.Optional;
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

    public Status attend(LocalDateTime localDateTime) {
        validateCampusTime(localDateTime);
        // 날짜 확인
        DayOfWeek dayOfWeek = localDateTime.getDayOfWeek();
        LocalTime attendTime = LocalTime.of(localDateTime.getHour(), localDateTime.getMinute());

        // TODO : 시간 남으면 이 똑같은 로직 메소드 추출
        // 월요일 이면 13:00 ~ 18:00 까지
        if (dayOfWeek == DayOfWeek.MONDAY) {
            // 로직 처리
            Status status = AttendanceTime.checkStatus(dayOfWeek, attendTime);
            attendance.put(localDateTime, status);
            return status;
        }
        // 화 ~ 금 이면 10:00 ~ 18:00 까지
        Status status = AttendanceTime.checkStatus(dayOfWeek, attendTime);
        attendance.put(localDateTime, status);
        return status;
    }

    private void validateCampusTime(LocalDateTime localDateTime) {
        LocalTime now = localDateTime.toLocalTime();

        if (now.isBefore(LocalTime.of(8, 0)) || now.isAfter(LocalTime.of(23, 0))) {
            throw new IllegalArgumentException(ErrorMessage.CAMPUS_NOT_OPEN.getMessage());
        }
    }

    public void validateAttend(LocalDateTime now) {
        // 년도, 월, 일 비교
        boolean match = attendance.keySet().stream()
                .anyMatch(key ->
                        key.getYear() == now.getYear()
                                && key.getMonth() == now.getMonth()
                                && key.getDayOfMonth() == now.getDayOfMonth());

        if (match) {
            throw new IllegalArgumentException(ErrorMessage.ALREADY_ATTEND.getMessage());
        }
    }

    public ModifyAttendResultDTO modifyAttend(LocalDateTime modifyTime) {
        // 없으면 만들고
        Optional<LocalDateTime> found = attendance.keySet().stream()
                .filter(key ->
                        key.getYear() == modifyTime.getYear()
                                && key.getMonth() == modifyTime.getMonth()
                                && key.getDayOfMonth() == modifyTime.getDayOfMonth())
                .findFirst();

        if (found.isEmpty()) {
            //새로 만들어서 넣기
            Status newStatus = attend(modifyTime);
            return new ModifyAttendResultDTO(null, Status.ABSENT, modifyTime, newStatus);
        }

        // 있는 경우는 해당 키 삭제하고 modifyTime으로 다시 넣고 출석 상태로 바꾸기
        LocalDateTime beforeTime = found.get();
        Status beforeStatus = attendance.get(beforeTime);

        attendance.remove(beforeTime);

        Status newStatus = attend(modifyTime);

        return new ModifyAttendResultDTO(beforeTime, beforeStatus, modifyTime, newStatus);
    }
}
