import exceptions.InvalidTaskDataException;

import javax.swing.SwingUtilities;
import java.io.IOException;
import java.util.List;

public class Main {
    private static final String DATA_FILE = "data/tasks.csv";

    public static void main(String[] args) {
        SchedulerManager scheduler = new SchedulerManager();
        TaskDAO taskDAO = new FileTaskDAO(DATA_FILE);

        // Load existing tasks
        try {
            List<Task> loadedTasks = taskDAO.loadTasks();
            for (Task t : loadedTasks) {
                scheduler.addTask(t);
            }
            System.out.println("Loaded " + loadedTasks.size() + " tasks from storage.");
        } catch (IOException | InvalidTaskDataException | exceptions.DeadlineConflictException e) {
            System.out.println("Error loading tasks: " + e.getMessage());
        }

        // Launch GUI application
        SwingUtilities.invokeLater(() -> {
            SchedulerGUI gui = new SchedulerGUI(scheduler, taskDAO);
            gui.setVisible(true);
        });
    }
}
