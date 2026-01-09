package attendance.dto;

import attendance.domain.StudentStatus;

public record ExpelledRiskDTO(String name, int absent, int late, StudentStatus studentStatus)
        implements Comparable<ExpelledRiskDTO> {

    @Override
    public int compareTo(ExpelledRiskDTO o) {
        // 1순위: 제적(0) > 면담(1) > 경고(2)
        int statusCompare = this.studentStatus.compareTo(o.studentStatus);
        if (statusCompare != 0) {
            return statusCompare;
        }

        // 2순위: 총 결석 횟수 (absent + late/3) 내림차순
        int thisTotalAbsent = this.absent + (this.late / 3);
        int otherTotalAbsent = o.absent + (o.late / 3);
        int absentCompare = Integer.compare(otherTotalAbsent, thisTotalAbsent);  // 내림차순
        if (absentCompare != 0) {
            return absentCompare;
        }

        // 3순위: 남은 지각 횟수 (late % 3) 내림차순
        int thisRemainingLate = this.late % 3;
        int otherRemainingLate = o.late % 3;
        int lateCompare = Integer.compare(otherRemainingLate, thisRemainingLate);  // 내림차순
        if (lateCompare != 0) {
            return lateCompare;
        }

        // 4순위: 이름 오름차순
        return this.name.compareTo(o.name);

    }
}
