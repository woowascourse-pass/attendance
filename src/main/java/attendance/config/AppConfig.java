package attendance.config;

import attendance.controller.AttendanceController;
import attendance.util.InputFileReader;
import attendance.view.InputView;
import attendance.view.OutputView;

public class AppConfig {
    public AttendanceController attendanceController() {
        return new AttendanceController(inputView(), outputView(), inputFileReader());
    }

    public InputFileReader inputFileReader() {
        return new InputFileReader();
    }

    public InputView inputView() {
        return new InputView();
    }

    public OutputView outputView() {
        return new OutputView();
    }
}
