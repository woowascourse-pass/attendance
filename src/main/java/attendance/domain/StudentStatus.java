package attendance.domain;

import java.util.Arrays;

public enum StudentStatus {
    EXPELLED("제적",6),
    INTERVIEW("면담",3),
    WARNING("경고",2);

    private final String status;
    private final int count;

    StudentStatus(String status, int count) {
        this.status = status;
        this.count = count;
    }

    public String getStatus() {
        return status;
    }

    public static StudentStatus calculateStudentStatus(int absent) {
        return Arrays.stream(values())
                .filter(studentStatus -> studentStatus.count <= absent)
                .findFirst()
                .orElse(null);
    }
}
