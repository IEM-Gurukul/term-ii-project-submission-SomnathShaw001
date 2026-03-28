import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Inheritance: Extends the Task base class, adding recurring-specific logic.
 */
public class RecurringTask extends Task {
    // Encapsulation: Private field to explicitly prevent external access.
    private int recurringIntervalDays;

    /** Constructor initializes base properties and the specific interval field. */
    public RecurringTask(String taskId, String title, LocalDate deadline, double basePriority, int recurringIntervalDays) {
        super(taskId, title, deadline, basePriority);
        this.recurringIntervalDays = recurringIntervalDays;
    }

    /** Encapsulation: Getter for specific attribute. */
    public int getRecurringIntervalDays() { return recurringIntervalDays; }
    /** Encapsulation: Setter for specific attribute. */
    public void setRecurringIntervalDays(int recurringIntervalDays) { this.recurringIntervalDays = recurringIntervalDays; }

    /**
     * Polymorphism: Overrides the base logic to reset deadlines 
     * in addition to calculating priority.
     */
    @Override
    public double calculateDynamicPriority() {
        if (LocalDate.now().isAfter(deadline)) {
            // Reset the deadline once passed
            deadline = deadline.plusDays(recurringIntervalDays);
        }
        
        long daysUntilDeadline = ChronoUnit.DAYS.between(LocalDate.now(), deadline);
        if (daysUntilDeadline == 0) {
            return basePriority + 30.0; // Due today
        } else {
            return basePriority + (5.0 / daysUntilDeadline);
        }
    }
}
