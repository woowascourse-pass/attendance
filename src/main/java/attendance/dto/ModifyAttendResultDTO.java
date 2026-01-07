package attendance.dto;

import attendance.domain.Status;
import java.time.LocalDateTime;

public record ModifyAttendResultDTO(
        LocalDateTime beforeTime, Status beforeStatus,
        LocalDateTime afterTime, Status afterStatus) {
}
