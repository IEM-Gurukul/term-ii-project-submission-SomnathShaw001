import java.io.IOException;
import java.util.List;

public interface TaskDAO {
    void saveTasks(List<Task> tasks) throws IOException;
    List<Task> loadTasks() throws IOException;
}
