package attendance.config;

import attendance.controller.AttendanceController;
import attendance.domain.AttendanceBook;
import attendance.domain.Crew;
import attendance.dto.CrewDTO;
import attendance.service.AttendanceService;
import attendance.util.InputFileReader;
import attendance.view.InputView;
import attendance.view.OutputView;
import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AppConfig {
    public AttendanceController attendanceController() {
        return new AttendanceController(inputView(), outputView(), attendanceService());
    }

    public AttendanceService attendanceService() {
        return new AttendanceService(createAttendanceBook());
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

    private AttendanceBook createAttendanceBook() {

        List<Crew> crews = new ArrayList<>();

        InputFileReader inputFileReader = inputFileReader();

        List<CrewDTO> crewDTOS = inputFileReader.readCsv("attendances.csv",
                columns -> new CrewDTO(
                        columns[0],
                        LocalDateTime.parse(columns[1], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                ));

        for (CrewDTO crewDTO : crewDTOS) {
            createOrAddAttendance(crews, crewDTO);
        }

        for (Crew crew : crews) {
            crew.checkAndAdd(DateTimes.now());
        }

        return new AttendanceBook(crews);
    }

    private void createOrAddAttendance(List<Crew> crews, CrewDTO crewDTO) {

        Optional<Crew> foundCrew = foundCrew(crews, crewDTO.name());

        // 해당 크루가 이미 존재하면
        if (foundCrew.isPresent()) {
            // 해당 crew의 출석부에 추가만
            Crew existCrew = foundCrew.get();
            existCrew.attend(crewDTO.attendanceTime());
            return;
        }

        // 존재하지 않으면 출석부에 새로 생성
        Crew newCrew = new Crew(crewDTO.name());
        newCrew.attend(crewDTO.attendanceTime());
        crews.add(newCrew);
    }

    private Optional<Crew> foundCrew(List<Crew> crews, String name) {
        return crews.stream()
                .filter(crew -> crew.getName().equals(name))
                .findFirst();
    }
}
