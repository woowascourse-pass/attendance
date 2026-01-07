package attendance.dto;

import java.util.List;

public record AttendRecordDTO(String name, List<AttendResultDTO> record) {
}
