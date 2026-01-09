package attendance.dto;

import attendance.domain.Status;
import java.time.LocalDateTime;
import java.util.Map;

public record AttendResultDTO(LocalDateTime time, Status status, boolean timeExist) {

    public static AttendResultDTO from(LocalDateTime time, Status status) {
        boolean timeExist = !(time.getHour() == 0 && time.getMinute() == 0);
        return new AttendResultDTO(time, status, timeExist);
    }

    public static AttendResultDTO from(Map.Entry<LocalDateTime, Status> entry) {
        return from(entry.getKey(), entry.getValue());
    }

}
