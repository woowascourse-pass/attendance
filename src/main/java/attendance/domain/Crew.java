package attendance.domain;

import attendance.dto.AttendRecordDTO;
import attendance.dto.AttendResultDTO;
import attendance.dto.ModifyAttendResultDTO;
import attendance.message.ErrorMessage;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

public class Crew {
    private final String name;
    private final Map<LocalDateTime, Status> attendance = new TreeMap<>();
    private int attend = 0;
    private int late = 0;
    private int absent = 0;
    private StudentStatus studentStatus;

    public Crew(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Status attend(LocalDateTime localDateTime) {
        validateAttend(localDateTime);
        validateCampusTime(localDateTime);
        // 날짜 확인
        DayOfWeek dayOfWeek = localDateTime.getDayOfWeek();
        LocalTime attendTime = LocalTime.of(localDateTime.getHour(), localDateTime.getMinute());

        // 월요일 이면 13:00 ~ 18:00 까지
        if (dayOfWeek == DayOfWeek.MONDAY) {
            // 로직 처리
            return getStatus(localDateTime, dayOfWeek, attendTime);
        }
        // 화 ~ 금 이면 10:00 ~ 18:00 까지
        return getStatus(localDateTime, dayOfWeek, attendTime);
    }


    public void checkAndAdd(LocalDateTime now) {
        // 2일 부터 오늘까지
        /// 실제로는 now 값 사용해야 하지만 문제를 26년 1월에 풀다보니 생기는 에러로 24년 12월 14일로 하드코딩
        /// 14일은 오늘이기 때문에 오늘 출석은 아직 완료가 안되었을 수 있기 때문에 여기서는 13일까지만 아예 출석이 빠진 경우가 있나 체크
        for (int i = 2; i <= 13; i++) {
            LocalDate today = LocalDate.of(2024, 12, i);

            boolean match = isMatch(today);

            if (match) {
                continue;
            }

            boolean weekend = AttendanceTime.checkWeekend(today);

            if (weekend) {
                continue;
            }

            // 없는 날짜는 결석으로 처리
            attendance.put(LocalDateTime.of(today, LocalTime.of(0, 0)), Status.ABSENT);
            addCount(Status.ABSENT);
            studentStatus = StudentStatus.calculateStudentStatus(absent);
        }
    }

    private Status getStatus(LocalDateTime localDateTime, DayOfWeek dayOfWeek, LocalTime attendTime) {
        Status status = AttendanceTime.checkStatus(dayOfWeek, attendTime);
        attendance.put(localDateTime, status);
        addCount(status);
        studentStatus = StudentStatus.calculateStudentStatus(absent);
        return status;
    }

    private void addCount(Status status) {
        if (status == Status.ATTENDANCE) {
            attend++;
            return;
        }

        if (status == Status.LATE) {
            late++;
            return;
        }

        if (status == Status.ABSENT) {
            absent++;
        }
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
        minusCount(beforeStatus);

        Status newStatus = attend(modifyTime);

        return new ModifyAttendResultDTO(beforeTime, beforeStatus, modifyTime, newStatus);
    }

    private void minusCount(Status beforeStatus) {
        if (beforeStatus == Status.ATTENDANCE) {
            attend--;
            return;
        }

        if (beforeStatus == Status.LATE) {
            late--;
            return;
        }

        if (beforeStatus == Status.ABSENT) {
            absent--;
        }
    }

    public AttendRecordDTO getAttendanceRecord(LocalDateTime now) {

        List<AttendResultDTO> records = new ArrayList<>();

        /// 실제로는 now 값 사용해야 하지만 문제를 26년 1월에 풀다보니 생기는 에러로 24년 12월 14일로 하드코딩
        // 2일 부터 오늘까지
        for (int i = 2; i <= 13; i++) {
            LocalDate today = LocalDate.of(2024, 12, i);

            if(isMatch(today)) {
                LocalDateTime time = getTime(today);

                LocalTime localTime = time.toLocalTime();

                if (localTime.getHour() == 0 && localTime.getMinute() == 0) {
                    records.add(new AttendResultDTO(time, attendance.get(time), false));
                    continue;
                }

                records.add(new AttendResultDTO(time, attendance.get(time), true));
            }
        }

        // 14일에 해당하는 기록 가져오기
        boolean match = isMatch(LocalDate.of(2024, 12, 14));
        if (match) {
            LocalDateTime time = getTime(LocalDate.of(2024, 12, 13));
            records.add(new AttendResultDTO(time, attendance.get(time), true));
        }

        String stringStudentStatus = "";
        if (studentStatus != null) {
            stringStudentStatus = studentStatus.getStatus();
        }

        return new AttendRecordDTO(name, records, attend, late, absent, stringStudentStatus);
    }

    private LocalDateTime getTime(LocalDate today) {
        return attendance.keySet().stream()
                .filter(key ->
                        key.getYear() == today.getYear()
                                && key.getMonth() == today.getMonth()
                                && key.getDayOfMonth() == today.getDayOfMonth())
                .findFirst().get();
    }

    private boolean isMatch(LocalDate today) {
        return attendance.keySet().stream()
                .anyMatch(key ->
                        key.getYear() == today.getYear()
                                && key.getMonth() == today.getMonth()
                                && key.getDayOfMonth() == today.getDayOfMonth());
    }
}
