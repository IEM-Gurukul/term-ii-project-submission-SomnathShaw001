import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Inheritance: Extends the generic Task class to define a specific type of task.
 */
public class OneTimeTask extends Task {

    /** Constructor reusing the parent class's constructor via super(). */
    public OneTimeTask(String taskId, String title, LocalDate deadline, double basePriority) {
        super(taskId, title, deadline, basePriority);
    }

    /**
     * Polymorphism: Overrides the calculateDynamicPriority method to provide
     * customized calculation for one-time tasks.
     */
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
