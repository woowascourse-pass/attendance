package attendance.view;

import camp.nextstep.edu.missionutils.Console;

public class InputView {

    private static final String FUNCTION =
            "1. 출석 확인\n"
            + "2. 출석 수정\n"
            + "3. 크루별 출석 기록 확인\n"
            + "4. 제적 위험자 확인\n"
            + "Q. 종료";
    private static final String GET_NAME = "닉네임을 입력해 주세요.";
    private static final String GET_ATTEND_TIME = "등교 시간을 입력해 주세요.";
    private static final String GET_MODIFY_DATE = "수정하려는 날짜(일)를 입력해 주세요.";
    private static final String GET_MODIFY_TIME = "언제로 변경하겠습니까?";

    public String readFunction() {
        System.out.println(FUNCTION);
        return Console.readLine();
    }

    public String readName() {
        System.out.println();
        System.out.println(GET_NAME);
        return Console.readLine();
    }

    public String readAttendTime() {
        System.out.println();
        System.out.println(GET_ATTEND_TIME);
        return Console.readLine();
    }

    public String readModifyDate() {
        System.out.println(GET_MODIFY_DATE);
        return Console.readLine();
    }

    public String readModifyTime() {
        System.out.println(GET_MODIFY_TIME);
        return Console.readLine();
    }
}
