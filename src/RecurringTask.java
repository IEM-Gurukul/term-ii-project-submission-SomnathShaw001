import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class RecurringTask extends Task {
    private int recurringIntervalDays;

    public RecurringTask(String taskId, String title, LocalDate deadline, double basePriority, int recurringIntervalDays) {
        super(taskId, title, deadline, basePriority);
        this.recurringIntervalDays = recurringIntervalDays;
    }

    public int getRecurringIntervalDays() {
        return recurringIntervalDays;
    }

    public void setRecurringIntervalDays(int recurringIntervalDays) {
        this.recurringIntervalDays = recurringIntervalDays;
    }

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
