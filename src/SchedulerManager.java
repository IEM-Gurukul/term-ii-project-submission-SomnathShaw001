import exceptions.InvalidTaskDataException;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;

public class SchedulerManager {
    private PriorityQueue<Task> taskQueue;

    public SchedulerManager() {
        // Sort in descending order of priority, so highest priority is at the head
        this.taskQueue = new PriorityQueue<>(Comparator.comparingDouble(Task::calculateDynamicPriority).reversed());
    }

    public void addTask(Task t) throws InvalidTaskDataException {
        if (t == null || t.getTaskId() == null || t.getTitle() == null || t.getDeadline() == null) {
            throw new InvalidTaskDataException("Task fields cannot be null.");
        }
        taskQueue.offer(t);
    }

    public boolean removeTask(String id) {
        if (id == null) return false;
        
        // PriorityQueue doesn't have an efficient remove by ID, so we find and remove
        for (Task t : taskQueue) {
            if (id.equals(t.getTaskId())) {
                taskQueue.remove(t);
                return true;
            }
        }
        return false;
    }

    public Task getNextTask() {
        return taskQueue.poll();
    }
    
    public Task peekNextTask() {
        return taskQueue.peek();
    }
    
    public List<Task> getAllTasks() {
        return new ArrayList<>(taskQueue);
    }
}
