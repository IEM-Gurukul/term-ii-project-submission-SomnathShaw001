import java.time.LocalDate;

/**
 * Abstraction: This abstract class defines the common blueprint for all types of tasks.
 * It uses protected fields to allow subclasses access (Inheritance) while hiding them from external classes (Encapsulation).
 */
public abstract class Task {
    protected String taskId;
    protected String title;
    protected LocalDate deadline;
    protected double basePriority;

    /** Constructor to initialize the base properties of a task. */
    public Task(String taskId, String title, LocalDate deadline, double basePriority) {
        this.taskId = taskId;
        this.title = title;
        this.deadline = deadline;
        this.basePriority = basePriority;
    }

    // Encapsulation: Getters and setters for controlled access to variables.
    public String getTaskId() { return taskId; }
    public void setTaskId(String taskId) { this.taskId = taskId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public LocalDate getDeadline() { return deadline; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }
    public double getBasePriority() { return basePriority; }
    public void setBasePriority(double basePriority) { this.basePriority = basePriority; }

    /**
     * Polymorphism: Abstract method to be overridden by subclasses 
     * to provide specific priority logic.
     */
    public abstract double calculateDynamicPriority();
}
