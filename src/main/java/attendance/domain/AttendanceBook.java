package attendance.domain;

import attendance.dto.CrewDTO;
import attendance.util.InputFileReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AttendanceBook {
    private final List<Crew> crews = new ArrayList<>();

    public AttendanceBook(InputFileReader inputFileReader) {
        List<CrewDTO> crewDTOS = inputFileReader.readCsv("attendances.csv",
                columns -> new CrewDTO(
                        columns[0],
                        LocalDateTime.parse(columns[1] + ":00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                ));

        for (CrewDTO crewDTO : crewDTOS) {
            createOrAddAttendance(crewDTO);
        }
    }

    private void createOrAddAttendance(CrewDTO crewDTO) {

        Optional<Crew> foundCrew = foundCrew(crewDTO);

        // 해당 크루가 이미 존재하면
        if (foundCrew.isPresent()) {
            // 해당 crew의 출석부에 추가만
            Crew existCrew = foundCrew.get();
            existCrew.addAttendance(crewDTO.attendanceTime());
            return;
        }

        // 존재하지 않으면 출석부에 새로 생성
        Crew newCrew = new Crew(crewDTO.name());
        newCrew.addAttendance(crewDTO.attendanceTime());
        crews.add(newCrew);
    }

    private Optional<Crew> foundCrew(CrewDTO crewDTO) {
        return crews.stream()
                .filter(crew -> crew.getName().equals(crewDTO.name()))
                .findFirst();
    }
}
