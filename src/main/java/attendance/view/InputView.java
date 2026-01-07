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
}
