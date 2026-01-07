package attendance.domain;

import attendance.dto.AttendRecordDTO;
import attendance.dto.AttendResultDTO;
import attendance.dto.CrewDTO;
import attendance.dto.ModifyAttendResultDTO;
import attendance.message.ErrorMessage;
import attendance.util.InputFileReader;
import camp.nextstep.edu.missionutils.DateTimes;
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

        for (Crew crew : crews) {
            crew.checkAndAdd(DateTimes.now());
        }
    }

    private void createOrAddAttendance(CrewDTO crewDTO) {

        Optional<Crew> foundCrew = foundCrew(crewDTO.name());

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

    private Optional<Crew> foundCrew(String name) {
        return crews.stream()
                .filter(crew -> crew.getName().equals(name))
                .findFirst();
    }

    public AttendResultDTO attend(String name, LocalDateTime now) {
        // 닉네임 존재하는지, 출석했는지 여부 확인
        Crew crew = validateAttend(name, now);
        // 여기 왔다는 건 이미 이 이름이 존재하고, 이 now로 출석한 적 없다는 의미
        Status status = crew.attend(now);
        return new AttendResultDTO(now, status, true);
    }

    public String validateCrewName(String name) {
        Optional<Crew> foundCrew = foundCrew(name);

        // 없으면 에러
        if (foundCrew.isEmpty()) {
            throw new IllegalArgumentException(ErrorMessage.NICKNAME_NOT_FOUND.getMessage());
        }

        return name;
    }

    public Crew validateAttend(String name, LocalDateTime now) {
        Crew crew = foundCrew(name).get();
        // 이미 출석 했는지 여부
        crew.validateAttend(now);
        return crew;
    }

    public ModifyAttendResultDTO modifyAttend(String crewName, LocalDateTime parsedAttendTime) {
        // 여기까지 온건 이미 이 이름으로 존재한다는 의미;
        // 그래도 방어적
        Optional<Crew> foundCrew = foundCrew(crewName);
        if (foundCrew.isEmpty()) {
            throw new IllegalArgumentException(ErrorMessage.NICKNAME_NOT_FOUND.getMessage());
        }

        Crew crew = foundCrew.get();

        return crew.modifyAttend(parsedAttendTime);
    }

    public AttendRecordDTO getAttendanceRecord(String crewName, LocalDateTime now) {

        Optional<Crew> foundCrew = foundCrew(crewName);
        if (foundCrew.isEmpty()) {
            throw new IllegalArgumentException(ErrorMessage.NICKNAME_NOT_FOUND.getMessage());
        }

        Crew crew = foundCrew.get();

        return crew.getAttendanceRecord(now);
    }
}
