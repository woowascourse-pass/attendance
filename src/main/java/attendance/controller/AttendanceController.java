package attendance.controller;

import attendance.domain.AttendanceBook;
import attendance.util.InputFileReader;
import attendance.util.InputValidator;
import attendance.util.Parser;
import attendance.view.InputView;
import attendance.view.OutputView;
import camp.nextstep.edu.missionutils.DateTimes;

public class AttendanceController {

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
        while (!((select = readFunction()).equals("Q"))) {

            int number = Parser.parseSelect(select);

            if (number == 1) {

            }

            if (number == 2) {

            }

            if (number == 3) {

            }

            if (number == 4) {

            }
        }
    }

    private String readFunction() {
        while (true) {
            try {
                outputView.printGreeting(DateTimes.now());
                String selectedFunction = inputView.readFunction();
                return InputValidator.validateSelectedFunction(selectedFunction);
            } catch (IllegalArgumentException e) {
                outputView.printError(e.getMessage());
            }
        }
    }
}
