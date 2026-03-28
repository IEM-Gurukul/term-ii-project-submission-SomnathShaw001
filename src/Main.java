import exceptions.InvalidTaskDataException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final String DATA_FILE = "data/tasks.csv";

    public static void main(String[] args) {
        SchedulerManager scheduler = new SchedulerManager();
        TaskDAO taskDAO = new FileTaskDAO(DATA_FILE);
        Scanner scanner = new Scanner(System.in);

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

        boolean running = true;
        while (running) {
            System.out.println("\n--- Smart Task Scheduler ---");
            System.out.println("1. Add a One-Time Task");
            System.out.println("2. Add a Recurring Task");
            System.out.println("3. View Next Highest Priority Task");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    addOneTimeTask(scanner, scheduler);
                    break;
                case "2":
                    addRecurringTask(scanner, scheduler);
                    break;
                case "3":
                    viewNextTask(scheduler);
                    break;
                case "4":
                    running = false;
                    saveAndExit(scheduler, taskDAO);
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
        scanner.close();
    }

    private static void addOneTimeTask(Scanner scanner, SchedulerManager scheduler) {
        try {
            System.out.print("Enter Task ID: ");
            String id = scanner.nextLine();
            System.out.print("Enter Title: ");
            String title = scanner.nextLine();
            System.out.print("Enter Deadline (YYYY-MM-DD): ");
            LocalDate deadline = LocalDate.parse(scanner.nextLine());
            
            if (deadline.isBefore(LocalDate.now())) {
                System.out.println("Error: Deadline cannot be in the past.");
                return;
            }

            System.out.print("Enter Base Priority (1-10): ");
            double basePriority = Double.parseDouble(scanner.nextLine());

            OneTimeTask task = new OneTimeTask(id, title, deadline, basePriority);
            scheduler.addTask(task);
            System.out.println("One-time task added successfully.");
        } catch (exceptions.DeadlineConflictException e) {
            System.out.println("Conflict Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error adding task: " + e.getMessage());
        }
    }

    private static void addRecurringTask(Scanner scanner, SchedulerManager scheduler) {
        try {
            System.out.print("Enter Task ID: ");
            String id = scanner.nextLine();
            System.out.print("Enter Title: ");
            String title = scanner.nextLine();
            System.out.print("Enter Initial Deadline (YYYY-MM-DD): ");
            LocalDate deadline = LocalDate.parse(scanner.nextLine());
            
            if (deadline.isBefore(LocalDate.now())) {
                System.out.println("Error: Deadline cannot be in the past.");
                return;
            }

            System.out.print("Enter Base Priority (1-10): ");
            double basePriority = Double.parseDouble(scanner.nextLine());
            System.out.print("Enter Recurring Interval in Days: ");
            int interval = Integer.parseInt(scanner.nextLine());

            RecurringTask task = new RecurringTask(id, title, deadline, basePriority, interval);
            scheduler.addTask(task);
            System.out.println("Recurring task added successfully.");
        } catch (exceptions.DeadlineConflictException e) {
            System.out.println("Conflict Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error adding task: " + e.getMessage());
        }
    }

    private static void viewNextTask(SchedulerManager scheduler) {
        Task nextTask = scheduler.peekNextTask();
        if (nextTask == null) {
            System.out.println("No tasks scheduled.");
        } else {
            System.out.println("Next Task: [" + nextTask.getTaskId() + "] " + nextTask.getTitle() + 
                               " | Deadline: " + nextTask.getDeadline() + 
                               " | Dynamic Priority: " + String.format("%.2f", nextTask.calculateDynamicPriority()));
        }
    }

    private static void saveAndExit(SchedulerManager scheduler, TaskDAO taskDAO) {
        try {
            taskDAO.saveTasks(scheduler.getAllTasks());
            System.out.println("Tasks saved successfully. Exiting...");
        } catch (IOException e) {
            System.out.println("Error saving tasks before exit: " + e.getMessage());
        }
    }
}
