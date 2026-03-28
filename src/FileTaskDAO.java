import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FileTaskDAO implements TaskDAO {
    private final String filePath;

    public FileTaskDAO(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void saveTasks(List<Task> tasks) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Task t : tasks) {
                // Determine task type
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

    @Override
    public List<Task> loadTasks() throws IOException {
        List<Task> tasks = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            return tasks; // Return empty list if file doesn't exist
        }

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
