package attendance.view;

import camp.nextstep.edu.missionutils.Console;

public class InputView {

    private static final String FUNCTION =
            "1. 출석 확인\n"
            + "2. 출석 수정\n"
            + "3. 크루별 출석 기록 확인\n"
            + "4. 제적 위험자 확인\n"
            + "Q. 종료";

    public String readFunction() {
        System.out.println(FUNCTION);
        return Console.readLine();
    }
}
