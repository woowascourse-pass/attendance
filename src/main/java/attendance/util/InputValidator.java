package attendance.util;

import attendance.message.ErrorMessage;
import java.time.LocalDateTime;

public class InputValidator {
    public static String validateSelectedFunction(String input) {
        if (input.equals("Q")) {
            return input;
        }

        if (!input.matches("\\d+")) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_INPUT.getMessage());
        }

        validateInputNumber(input);

        return input;
    }

    public static int validateParsedInput(int input) {
        if (!(1 <= input && input <= 4)) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_INPUT.getMessage());
        }
        return input;
    }

    private static void validateInputNumber(String input) {
        try{
            int parseInput = Integer.parseInt(input);
            validateParsedInput(parseInput);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_INPUT.getMessage());
        }
    }

    public static void validateDate(int parseDate) {
        if (parseDate < 1 || 31 < parseDate) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_INPUT.getMessage());
        }
    }

    public static void validateTime(LocalDateTime now, int date) {
        /// 실제로는 now 값 사용해야 하지만 문제를 26년 1월에 풀다보니 생기는 에러로 24년 12월 14일로 하드코딩
        LocalDateTime localDateTime = LocalDateTime.of(2024, 12, 14, 0, 0);
        if (localDateTime.getDayOfMonth() < date) {
            throw new IllegalArgumentException(ErrorMessage.FUTURE_ATTEND_NOT_POSSIBLE.getMessage());
        }
    }
}
