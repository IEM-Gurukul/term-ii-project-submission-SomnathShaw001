import exceptions.InvalidTaskDataException;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulation: Manages the collection of tasks internally and restricts 
 * access to through predefined methods, protecting the taskQueue.
 */
public class SchedulerManager {
    // Encapsulation: Private collection
    private PriorityQueue<Task> taskQueue;

    /** Constructor initializes the queue with a custom generic comparator. */
    public SchedulerManager() {
        this.taskQueue = new PriorityQueue<>(Comparator.comparingDouble(Task::calculateDynamicPriority).reversed());
    }

    /**
     * Exception Handling: Throws a custom exception to maintain robust application state.
     */
    public void addTask(Task t) throws InvalidTaskDataException, exceptions.DeadlineConflictException {
        if (t == null || t.getTaskId() == null || t.getTitle() == null || t.getDeadline() == null) {
            throw new InvalidTaskDataException("Task fields cannot be null.");
        }
        
        for (Task existing : taskQueue) {
            if (existing.getDeadline().equals(t.getDeadline()) && existing.getTitle().equals(t.getTitle())) {
                throw new exceptions.DeadlineConflictException("A task with the same title ('" + t.getTitle() + "') and deadline already exists.");
            }
        }
        
        taskQueue.offer(t);
    }

    /** Method to remove tasks manually using an iteration over the encapsulated queue. */
    public boolean removeTask(String id) {
        if (id == null) return false;
        for (Task t : taskQueue) {
            if (id.equals(t.getTaskId())) {
                taskQueue.remove(t);
                return true;
            }
        }
        return false;
    }

    /** Method to retrieve and remove the top task. */
    public Task getNextTask() { return taskQueue.poll(); }
    
    /** Method to return the top task without removing it. */
    public Task peekNextTask() { return taskQueue.peek(); }
    
    /** Method to expose queue elements safely without breaking encapsulation. */
    public List<Task> getAllTasks() { return new ArrayList<>(taskQueue); }
}
