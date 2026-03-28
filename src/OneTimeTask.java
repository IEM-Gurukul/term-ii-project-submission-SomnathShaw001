import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class OneTimeTask extends Task {

    public OneTimeTask(String taskId, String title, LocalDate deadline, double basePriority) {
        super(taskId, title, deadline, basePriority);
    }

    @Override
    public double calculateDynamicPriority() {
        long daysUntilDeadline = ChronoUnit.DAYS.between(LocalDate.now(), deadline);
        if (daysUntilDeadline < 0) {
            return basePriority + 100.0; // Overdue tasks
        } else if (daysUntilDeadline == 0) {
            return basePriority + 50.0; // Due today
        } else {
            return basePriority + (10.0 / daysUntilDeadline);
        }
    }
}
