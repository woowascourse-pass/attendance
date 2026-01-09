package attendance.domain;

import java.util.List;
import java.util.Optional;

public class AttendanceBook {
    private final List<Crew> crews;

    public AttendanceBook(List<Crew> crews) {
        this.crews = crews;
    }

    public Optional<Crew> foundCrew(String name) {
        return crews.stream()
                .filter(crew -> crew.getName().equals(name))
                .findFirst();
    }

    public List<Crew> getExpelledRiskStudent() {
        return crews.stream()
                .filter(Crew::isExpelledRisk)
                .toList();
    }
}
