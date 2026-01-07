package attendance.util;

import attendance.message.ErrorMessage;

public class Parser {
    public static int parseSelect(String select) {
        try{
            int parseInput = Integer.parseInt(select);
            return InputValidator.validateParsedInput(parseInput);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_INPUT.getMessage());
        }
    }
}
