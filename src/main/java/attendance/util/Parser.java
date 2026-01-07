package attendance.util;

import attendance.message.ErrorMessage;
import java.time.LocalDateTime;

public class Parser {
    public static int parseSelect(String select) {
        try{
            int parseInput = Integer.parseInt(select);
            return InputValidator.validateParsedInput(parseInput);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_INPUT.getMessage());
        }
    }

    public static LocalDateTime parseTime(LocalDateTime now, String attendTime) {
        try {
            String[] inputTime = attendTime.split(":");

            return LocalDateTime.of(
                    now.getYear(),
                    now.getMonthValue(),
                    now.getDayOfMonth(),
                    Integer.parseInt(inputTime[0]),
                    Integer.parseInt(inputTime[1]),
                    0);
        } catch (Exception e) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_INPUT.getMessage());
        }
    }
}
