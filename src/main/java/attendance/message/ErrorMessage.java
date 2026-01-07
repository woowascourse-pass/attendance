package attendance.message;

public enum ErrorMessage {
    FILE_READ_ERROR("[ERROR] 파일 읽기 중 오류가 발생했습니다."),
    FILE_NOT_FOUND("[ERROR] 파일을 찾을 수 없습니다."),
    CAMPUS_NOT_OPEN("[ERROR] 캠퍼스 운영 시간에만 출석이 가능합니다."),
    INVALID_DAY_OF_WEEK("[ERROR] 존재하지 않는 요일입니다."),
    INVALID_INPUT("[ERROR] 잘못된 형식을 입력하였습니다."),
    NICKNAME_NOT_FOUND("[ERROR] 등록되지 않은 닉네임입니다."),
    NOT_ATTEND_DAY("[ERROR] %d월 %d일 %s요일은 등교일이 아닙니다."),
    ALREADY_ATTEND("[ERROR] 이미 출석을 확인하였습니다. 필요한 경우 수정 기능을 이용해주세요."),
    FUTURE_ATTEND_NOT_POSSIBLE("[ERROR] 아직 수정할 수 없습니다."),
    ;

    private final String description;

    ErrorMessage(String description) {
        this.description = description;
    }

    public String getMessage() {
        return description;
    }
}
