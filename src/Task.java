import java.time.LocalDate;

public abstract class Task {
    protected String taskId;
    protected String title;
    protected LocalDate deadline;
    protected double basePriority;

    public Task(String taskId, String title, LocalDate deadline, double basePriority) {
        this.taskId = taskId;
        this.title = title;
        this.deadline = deadline;
        this.basePriority = basePriority;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public double getBasePriority() {
        return basePriority;
    }

    public void setBasePriority(double basePriority) {
        this.basePriority = basePriority;
    }

    public abstract double calculateDynamicPriority();
}
