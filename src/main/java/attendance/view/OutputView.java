package attendance.view;

import attendance.domain.AttendanceTime;
import attendance.dto.AttendRecordDTO;
import attendance.dto.AttendResultDTO;
import attendance.dto.ExpelledRiskDTO;
import attendance.dto.ModifyAttendResultDTO;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

public class OutputView {

    private static final String DATE_FORMAT = "%d월 %s일 %s요일";
    private static final String TIME_FORMAT = "%s:%s";

    private static final String GREETING = DATE_FORMAT + "입니다. 기능을 선택해 주세요.";
    private static final String ATTEND_RESULT = DATE_FORMAT + " " + TIME_FORMAT + " (%s)";
    private static final String MODIFY_RESULT = DATE_FORMAT + " " + TIME_FORMAT + " (%s) -> " + TIME_FORMAT + " (%s) 수정 완료!";
    private static final String START_ATTEND_RECORD = "이번 달 %s의 출석 기록입니다.";
    private static final String ATTEND = "출석: %d회";
    private static final String LATE = "지각: %d회";
    private static final String ABSENT = "결석: %d회";
    private static final String TARGET = "%s 대상자입니다.";

    private static final String EXPELLED_RESULT = "제적 위험자 조회 결과";
    private static final String EXPELLED_RISK_RESULT = "- %s: 결석 %d회, 지각 %d회 (%s)";


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

    public void printAttendResult(AttendResultDTO result) {
        System.out.println();
        printTimeExists(result);
    }

    public void printModifyAttendResult(ModifyAttendResultDTO result) {
        System.out.println();
        System.out.printf(MODIFY_RESULT + "\n",
                result.beforeResult().time().getMonthValue(),
                String.format("%02d", result.beforeResult().time().getDayOfMonth()),
                parseDayOfWeek(result.beforeResult().time().getDayOfWeek()),
                printBeforeHour(result.beforeResult().time()),
                printBeforeMinute(result.beforeResult().time()),
                result.beforeResult().status().getStatus(),
                String.format("%02d", result.afterResult().time().getHour()),
                String.format("%02d", result.afterResult().time().getMinute()),
                result.afterResult().status().getStatus());
    }

    public void printAttendanceRecord(AttendRecordDTO result) {
        System.out.println();
        System.out.printf(START_ATTEND_RECORD + "\n", result.name());
        List<AttendResultDTO> records = result.record();

        for (AttendResultDTO record : records) {
            if (!record.timeExist()) {
                printTimeNotExists(record);
                continue;
            }
            printTimeExists(record);
        }

        printStatus(result);

        printStudentStatus(result);
    }

    private void printStudentStatus(AttendRecordDTO result) {
        if (!result.studentStatus().isBlank()) {
            System.out.println();
            System.out.printf(TARGET + "\n", result.studentStatus());
            System.out.println();
        }
    }

    private void printStatus(AttendRecordDTO result) {
        System.out.println();
        System.out.printf(ATTEND + "\n", result.attend());
        System.out.printf(LATE + "\n", result.late());
        System.out.printf(ABSENT + "\n", result.absent());
    }

    private void printTimeExists(AttendResultDTO record) {
        System.out.printf(ATTEND_RESULT + "\n",
                record.time().getMonthValue(),
                String.format("%02d",record.time().getDayOfMonth()),
                parseDayOfWeek(record.time().getDayOfWeek()),
                String.format("%02d",record.time().getHour()),
                String.format("%02d",record.time().getMinute()),
                record.status().getStatus());
    }

    private void printTimeNotExists(AttendResultDTO record) {
        System.out.printf(ATTEND_RESULT + "\n",
                record.time().getMonthValue(),
                String.format("%02d", record.time().getDayOfMonth()),
                parseDayOfWeek(record.time().getDayOfWeek()),
                "--",
                "--",
                record.status().getStatus()
        );
    }

    private Object printBeforeMinute(LocalDateTime beforeTime) {
        if (beforeTime == null) {
            return "--";
        }

        return String.format("%02d",beforeTime.getMinute());
    }

    private String printBeforeHour(LocalDateTime beforeTime) {
        if (beforeTime == null) {
            return "--";
        }

        return String.format("%02d",beforeTime.getHour());
    }

    public void printExpelledRiskStudent(List<ExpelledRiskDTO> result) {
        System.out.println(EXPELLED_RESULT);
        for (ExpelledRiskDTO expelledRisk : result) {
            System.out.printf(EXPELLED_RISK_RESULT + "\n", expelledRisk.name(), expelledRisk.absent(),
                    expelledRisk.late(), expelledRisk.studentStatus().getStatus());
        }
    }
}
