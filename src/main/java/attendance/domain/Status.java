package attendance.domain;

public enum Status {
    ATTENDANCE("출석"),
    LATE("지각"),
    ABSENT("결석");

    private final String status;

    Status(String status) {
        this.status = status;
    }

    public static Status getStatus(int timeDiff) {

        if (30 < timeDiff) {
            return ABSENT;
        }

        if (5 < timeDiff) {
            return LATE;
        }

        return ATTENDANCE;
    }

    public String getStatus() {
        return status;
    }
}
