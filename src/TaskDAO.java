import java.io.IOException;
import java.util.List;

/**
 * Abstraction: Interface representing a Data Access Object (DAO).
 * It decouples the application logic from the persistence implementation.
 */
public interface TaskDAO {
    /** Abstract method for saving tasks. */
    void saveTasks(List<Task> tasks) throws IOException;
    /** Abstract method for loading tasks. */
    List<Task> loadTasks() throws IOException;
}
