package attendance.domain;

import attendance.message.ErrorMessage;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    public int getAttend() {
        return attend;
    }

    public int getLate() {
        return late;
    }

    public int getAbsent() {
        return absent;
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
        /// 실제로는 time 값 사용해야 하지만 문제를 26년 1월에 풀다보니 생기는 에러로 24년 12월 14일로 하드코딩
        /// 14일은 오늘이기 때문에 오늘 출석은 아직 완료가 안되었을 수 있기 때문에 여기서는 13일까지만 아예 출석이 빠진 경우가 있나 체크
        for (int i = 2; i < now.getDayOfMonth(); i++) {
            LocalDate today = LocalDate.of(now.getYear(), now.getMonthValue(), i);

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
                .anyMatch(key -> key.toLocalDate().equals(now.toLocalDate()));

        if (match) {
            throw new IllegalArgumentException(ErrorMessage.ALREADY_ATTEND.getMessage());
        }
    }

    public Map.Entry<LocalDateTime, Status> findStatusByTime(LocalDate date) {
        Optional<LocalDateTime> found = attendance.keySet().stream()
                .filter(key -> key.toLocalDate().equals(date))
                .findFirst();

        // 비어있으면 결석 처리이기 때문에 값 넣어줘야함.
        if (found.isEmpty()) {
            LocalDateTime absentTime = LocalDateTime.of(date, LocalTime.of(0, 0));
            attendance.put(absentTime, Status.ABSENT);
            return Map.entry(absentTime, Status.ABSENT);
        }

        LocalDateTime foundTime = found.get();
        Status status = attendance.get(foundTime);
        return Map.entry(foundTime, status);

    }

    public void modifyAttend(LocalDateTime modifyTime) {
        // 없으면 만들고
        Optional<LocalDateTime> found = attendance.keySet().stream()
                .filter(key -> key.toLocalDate().equals(modifyTime.toLocalDate()))
                .findFirst();

        if (found.isEmpty()) {
            //새로 만들어서 넣기
            attend(modifyTime);
            return;
        }

        // 있는 경우는 해당 키 삭제하고 modifyTime으로 다시 넣고 출석 상태로 바꾸기
        LocalDateTime beforeTime = found.get();
        Status beforeStatus = attendance.get(beforeTime);

        attendance.remove(beforeTime);
        minusCount(beforeStatus);

        attend(modifyTime);
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

    public boolean isExpelledRisk() {
        int lateToAbsent = late / 3;

        int absentValue = absent + lateToAbsent;

        return 2<= absentValue;
    }

    public int calculateAbsentValue() {

        int lateToAbsent = late / 3;

        return absent + lateToAbsent;
    }

    public boolean isMatch(LocalDate today) {
        return attendance.keySet().stream()
                .anyMatch(key -> key.toLocalDate().equals(today));
    }

    public StudentStatus getStudentStatus() {
        return studentStatus;
    }
}
