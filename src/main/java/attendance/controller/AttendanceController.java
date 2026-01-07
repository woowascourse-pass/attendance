package attendance.controller;

import attendance.domain.AttendanceBook;
import attendance.domain.AttendanceTime;
import attendance.dto.AttendResultDTO;
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

            }

            if (number == 3) {

            }

            if (number == 4) {

            }
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
