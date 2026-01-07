package attendance.util;

import attendance.message.ErrorMessage;

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
}
