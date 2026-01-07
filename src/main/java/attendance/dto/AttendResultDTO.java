package attendance.dto;

import attendance.domain.Status;
import java.time.LocalDateTime;

public record AttendResultDTO(LocalDateTime now, Status status) {
}
