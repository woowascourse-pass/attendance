package attendance.dto;

import java.time.LocalDateTime;

public record CrewDTO(String name, LocalDateTime attendanceTime) {
}
