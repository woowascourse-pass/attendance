package attendance.controller;

import attendance.domain.AttendanceBook;
import attendance.domain.AttendanceTime;
import attendance.dto.AttendRecordDTO;
import attendance.dto.AttendResultDTO;
import attendance.dto.ModifyAttendResultDTO;
import attendance.util.InputFileReader;
import attendance.util.InputValidator;
import attendance.util.Parser;
import attendance.view.InputView;
import attendance.view.OutputView;
import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDateTime;

public class AttendanceController {

    private static final String END = "Q";

    private final InputView inputView;
    private final OutputView outputView;
    private final AttendanceBook attendanceBook;

    public AttendanceController(InputView inputView, OutputView outputView, InputFileReader inputFileReader) {
        this.inputView = inputView;
        this.outputView = outputView;
        this.attendanceBook = new AttendanceBook(inputFileReader);
    }

    public void start() {
        String select;
        LocalDateTime now;
        boolean shouldContinue = true;
        while (shouldContinue && !((select = readFunction(now = DateTimes.now())).equals(END))) {

            int number = Parser.parseSelect(select);

            if (number == 1) {
                shouldContinue = attendanceCheck(now);
            }

            if (number == 2) {
                shouldContinue = modifyAttendance(now);
            }

            if (number == 3) {
                shouldContinue = showCrewAttendanceRecord(now);
            }

            if (number == 4) {

            }
        }
    }

    private boolean showCrewAttendanceRecord(LocalDateTime now) {
        try {
            String name = inputView.readName();
            String crewName = attendanceBook.validateCrewName(name);

            AttendRecordDTO result = attendanceBook.getAttendanceRecord(crewName, now);

            outputView.printAttendanceRecord(result);

            return true;
        } catch (IllegalArgumentException e) {
            outputView.printError(e.getMessage());
            return false;
        }
    }

    private boolean modifyAttendance(LocalDateTime now) {
        try {
            String name = inputView.readName();
            String crewName = attendanceBook.validateCrewName(name);

            String parsedDate = inputView.readModifyDate();
            int date = Parser.parseDate(parsedDate);
            // 이 날이 휴일인지 여부
            InputValidator.validateTime(now, date);
            AttendanceTime.checkWeekend(now, date);

            String rawTime = inputView.readModifyTime();
            LocalDateTime parsedAttendTime = Parser.parseTime(now, rawTime);

            ModifyAttendResultDTO result = attendanceBook.modifyAttend(crewName, parsedAttendTime);

            outputView.printModifyAttendResult(result);

            return true;
        } catch (IllegalArgumentException e) {
            outputView.printError(e.getMessage());
            return false;
        }
    }

    private boolean attendanceCheck(LocalDateTime now) {
        try {
            // 주말 체크
            AttendanceTime.checkWeekend(now);
            String name = inputView.readName();
            // 이름 체크
            String crewName = attendanceBook.validateCrewName(name);
            // 이미 출석 했는지 체크
            attendanceBook.validateAttend(crewName, now);
            // 등교 시간 입력
            String attendTime = inputView.readAttendTime();
            LocalDateTime parsedAttendTime = Parser.parseTime(now, attendTime);
            // 출석 체크
            AttendResultDTO result = attendanceBook.attend(crewName, parsedAttendTime);
            outputView.printAttendResult(result);
            return true;
        } catch (IllegalArgumentException e) {
            outputView.printError(e.getMessage());
            return false;
        }
    }

    private String readFunction(LocalDateTime now) {
        try {
            outputView.printGreeting(now);
            String selectedFunction = inputView.readFunction();
            return InputValidator.validateSelectedFunction(selectedFunction);
        } catch (IllegalArgumentException e) {
            outputView.printError(e.getMessage());
            return END;
        }
    }
}
