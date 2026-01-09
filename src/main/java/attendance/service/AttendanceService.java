package attendance.service;

import attendance.domain.AttendanceBook;
import attendance.domain.Crew;
import attendance.domain.Status;
import attendance.domain.StudentStatus;
import attendance.dto.AttendRecordDTO;
import attendance.dto.AttendResultDTO;
import attendance.dto.ExpelledRiskDTO;
import attendance.dto.ModifyAttendResultDTO;
import attendance.message.ErrorMessage;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;

public class AttendanceService {
    private final AttendanceBook attendanceBook;

    public AttendanceService(AttendanceBook attendanceBook) {
        this.attendanceBook = attendanceBook;
    }

    public String validateCrewName(String name) {
        Optional<Crew> foundCrew = attendanceBook.foundCrew(name);

        // 없으면 에러
        if (foundCrew.isEmpty()) {
            throw new IllegalArgumentException(ErrorMessage.NICKNAME_NOT_FOUND.getMessage());
        }

        return name;
    }

    public AttendResultDTO attend(String name, LocalDateTime now) {
        // 닉네임 존재하는지, 출석했는지 여부 확인
        validateAttend(name, now);
        Crew crew = getExistCrew(name);

        // 여기 왔다는 건 이미 이 이름이 존재하고, 이 now로 출석한 적 없다는 의미
        Status status = crew.attend(now);
        return new AttendResultDTO(now, status, true);
    }

    public void validateAttend(String name, LocalDateTime now) {
        Crew crew = getExistCrew(name);
        // 이미 출석 했는지 여부
        crew.validateAttend(now);
    }

    public ModifyAttendResultDTO modifyAttend(String crewName, LocalDateTime parsedAttendTime) {
        // 여기까지 온건 이미 이 이름으로 존재한다는 의미;
        // 그래도 방어적
        Crew crew = getExistCrew(crewName);

        Entry<LocalDateTime, Status> beforeTime = crew.findStatusByTime(parsedAttendTime.toLocalDate());
        crew.modifyAttend(parsedAttendTime);
        Entry<LocalDateTime, Status> afterTime = crew.findStatusByTime(parsedAttendTime.toLocalDate());

        AttendResultDTO beforeDTO = AttendResultDTO.from(beforeTime);
        AttendResultDTO afterDTO = AttendResultDTO.from(afterTime);

        return new ModifyAttendResultDTO(beforeDTO, afterDTO);
    }

    public AttendRecordDTO getAttendanceRecord(String crewName, LocalDateTime now) {

        List<AttendResultDTO> records = new ArrayList<>();
        Crew crew = getExistCrew(crewName);

        addRecordsBeforeToday(now, crew, records);
        addTodayRecord(now, crew, records);

        String stringStudentStatus = getStudentStatus(crew);

        return new AttendRecordDTO(crew.getName(), records, crew.getAttend(), crew.getLate(), crew.getAbsent(), stringStudentStatus);
    }

    private String getStudentStatus(Crew crew) {
        StudentStatus studentStatus = crew.getStudentStatus();

        String stringStudentStatus = "";
        if (studentStatus != null) {
            stringStudentStatus = studentStatus.getStatus();
        }
        return stringStudentStatus;
    }

    private void addRecordsBeforeToday(LocalDateTime now, Crew crew, List<AttendResultDTO> records) {
        for (int i = 2; i < now.getDayOfMonth(); i++) {
            LocalDate today = LocalDate.of(now.getYear(), now.getMonthValue(), i);

            Entry<LocalDateTime, Status> foundAttend = crew.findStatusByTime(today);

            records.add(AttendResultDTO.from(foundAttend));
        }
    }

    private void addTodayRecord(LocalDateTime now, Crew crew, List<AttendResultDTO> records) {
        boolean match = crew.isMatch(now.toLocalDate());
        if (match) {
            Entry<LocalDateTime, Status> foundAttend = crew.findStatusByTime(now.toLocalDate());
            records.add(AttendResultDTO.from(foundAttend));
        }
    }

    private Crew getExistCrew(String crewName) {
        Optional<Crew> foundCrew = attendanceBook.foundCrew(crewName);
        if (foundCrew.isEmpty()) {
            throw new IllegalArgumentException(ErrorMessage.NICKNAME_NOT_FOUND.getMessage());
        }

        return foundCrew.get();
    }

    public List<ExpelledRiskDTO> getExpelledRiskStudent() {

        List<Crew> expelledRiskStudent = attendanceBook.getExpelledRiskStudent();

        return expelledRiskStudent.stream()
                .map((crew) -> {
                    int absentValue = crew.calculateAbsentValue();
                    StudentStatus status = StudentStatus.calculateStudentStatus(absentValue);
                    return new ExpelledRiskDTO(crew.getName(), crew.getAbsent(), crew.getLate(), status);
                })
                .sorted()
                .toList();
    }
}
