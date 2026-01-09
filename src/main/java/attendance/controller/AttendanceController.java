package attendance.controller;

import attendance.domain.AttendanceTime;
import attendance.dto.AttendRecordDTO;
import attendance.dto.AttendResultDTO;
import attendance.dto.ExpelledRiskDTO;
import attendance.dto.ModifyAttendResultDTO;
import attendance.service.AttendanceService;
import attendance.util.InputValidator;
import attendance.util.Parser;
import attendance.view.InputView;
import attendance.view.OutputView;
import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDateTime;
import java.util.List;

public class AttendanceController {

    private static final String END = "Q";
    private static final String ATTEND = "1";

    private final InputView inputView;
    private final OutputView outputView;
    private final AttendanceService attendanceService;

    public AttendanceController(InputView inputView, OutputView outputView, AttendanceService attendanceService) {
        this.inputView = inputView;
        this.outputView = outputView;
        this.attendanceService = attendanceService;
    }

    public void start() {
        String select;
        LocalDateTime now;
        while (!((select = readFunction(now = DateTimes.now())).equals(END))) {

            int number = Parser.parseSelect(select);

            if (number == 1) {
                attendanceCheck(now);
                continue;
            }

            if (number == 2) {
                modifyAttendance(now);
                continue;
            }

            if (number == 3) {
                showCrewAttendanceRecord(now);
                continue;
            }

            if (number == 4) {
                showExpelledRiskStudent();
            }
        }
    }

    private void showExpelledRiskStudent() {
        List<ExpelledRiskDTO> result = attendanceService.getExpelledRiskStudent();
        outputView.printExpelledRiskStudent(result);
    }

    private void showCrewAttendanceRecord(LocalDateTime now) {
        String name = inputView.readName();
        String crewName = attendanceService.validateCrewName(name);

        AttendRecordDTO result = attendanceService.getAttendanceRecord(crewName, now);

        outputView.printAttendanceRecord(result);
    }

    private void modifyAttendance(LocalDateTime now) {
        String name = inputView.readName();
        String crewName = attendanceService.validateCrewName(name);

        String parsedDate = inputView.readModifyDate();
        int date = Parser.parseDate(parsedDate);
        // 이 날이 휴일인지 여부
        InputValidator.validateTime(now, date);
        AttendanceTime.checkWeekend(now, date);

        String rawTime = inputView.readModifyTime();
        LocalDateTime parsedAttendTime = Parser.parseTime(now, date, rawTime);

        ModifyAttendResultDTO result = attendanceService.modifyAttend(crewName, parsedAttendTime);

        outputView.printModifyAttendResult(result);
    }

    private void attendanceCheck(LocalDateTime now) {
        // 주말 체크
        AttendanceTime.checkWeekend(now);
        String name = inputView.readName();
        // 이름 체크
        String crewName = attendanceService.validateCrewName(name);
        // 이미 출석 했는지 체크
        attendanceService.validateAttend(crewName, now);
        // 등교 시간 입력
        String attendTime = inputView.readAttendTime();
        LocalDateTime parsedAttendTime = Parser.parseTime(now, attendTime);
        // 출석 체크
        AttendResultDTO result = attendanceService.attend(crewName, parsedAttendTime);
        outputView.printAttendResult(result);
    }

    private String readFunction(LocalDateTime now) {
        outputView.printGreeting(now);
        String selectedFunction = inputView.readFunction();
        return InputValidator.validateSelectedFunction(selectedFunction);
    }
}
