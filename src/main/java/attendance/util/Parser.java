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

    public static LocalDateTime parseTime(LocalDateTime now, int date, String attendTime) {
        try {
            String[] inputTime = attendTime.split(":");

            /// 실제로는 time 값 사용해야 하지만 문제를 26년 1월에 풀다보니 생기는 에러로 24년 12월로 하드코딩
            return LocalDateTime.of(
                    now.getYear(),
                    now.getMonthValue(),
                    date,
                    Integer.parseInt(inputTime[0]),
                    Integer.parseInt(inputTime[1])
            );
        } catch (Exception e) {
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

    public static int parseDate(String date) {
        try{
            int parsedDate = Integer.parseInt(date);
            InputValidator.validateDate(parsedDate);
            return parsedDate;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_INPUT.getMessage());
        }
    }
}
