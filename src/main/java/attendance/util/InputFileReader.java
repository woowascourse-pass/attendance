package attendance.util;

import attendance.message.ErrorMessage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.function.Function;

public class InputFileReader {

    public <T> List<T> readCsv(String fileName, Function<String[], T> mapper) {
        try (InputStream inputStream = getClass().getClassLoader()
                .getResourceAsStream(fileName)) {

            validateInputStream(inputStream);

            return new BufferedReader(new InputStreamReader(inputStream))
                    .lines()
                    .skip(1)
                    .map(line -> line.split(","))
                    .map(mapper)
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(ErrorMessage.FILE_READ_ERROR.getMessage());
        }
    }

    private void validateInputStream(InputStream inputStream) {
        if (inputStream == null) {
            throw new IllegalArgumentException(ErrorMessage.FILE_NOT_FOUND.getMessage());
        }
    }
}
