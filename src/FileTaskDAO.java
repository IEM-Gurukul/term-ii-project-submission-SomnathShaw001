import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Interface Implementation: Implements the TaskDAO abstraction for persistent file-based storage.
 */
public class FileTaskDAO implements TaskDAO {
    // Encapsulation: Private immutable field.
    private final String filePath;

    /** Constructor injecting the file path. */
    public FileTaskDAO(String filePath) {
        this.filePath = filePath;
    }

    /** Polymorphism: Concrete implementation of saveTasks using basic File IO. */
    @Override
    public void saveTasks(List<Task> tasks) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Task t : tasks) {
                String type = t instanceof RecurringTask ? "RECURRING" : "ONETIME";
                writer.write(type + "," + t.getTaskId() + "," + t.getTitle() + "," + 
                             t.getDeadline().toString() + "," + t.getBasePriority());
                if (t instanceof RecurringTask) {
                    writer.write("," + ((RecurringTask) t).getRecurringIntervalDays());
                }
                writer.newLine();
            }
        }
    }

    /** Polymorphism: Concrete implementation of loadTasks handling string parsing. */
    @Override
    public List<Task> loadTasks() throws IOException {
        List<Task> tasks = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) return tasks;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 5) continue;
                
                String type = parts[0];
                String id = parts[1];
                String title = parts[2];
                LocalDate deadline = LocalDate.parse(parts[3]);
                double priority = Double.parseDouble(parts[4]);

                if ("RECURRING".equals(type) && parts.length == 6) {
                    int interval = Integer.parseInt(parts[5]);
                    tasks.add(new RecurringTask(id, title, deadline, priority, interval));
                } else if ("ONETIME".equals(type)) {
                    tasks.add(new OneTimeTask(id, title, deadline, priority));
                }
            }
        }
        return tasks;
    }
}
