

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class SchedulerGUI extends JFrame {
    private SchedulerManager scheduler;
    private TaskDAO taskDAO;
    private DefaultTableModel tableModel;
    private JTable taskTable;

    public SchedulerGUI(SchedulerManager scheduler, TaskDAO taskDAO) {
        this.scheduler = scheduler;
        this.taskDAO = taskDAO;

        // Apply a modern look and feel (Nimbus)
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // Fallback to system default if Nimbus is not available
        }

        setTitle("Smart Task Scheduler");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null); // Center on screen
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(240, 244, 248)); // Soft distinct background

        // Save hook when closing window
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    taskDAO.saveTasks(scheduler.getAllTasks());
                    System.out.println("Tasks saved successfully via GUI.");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(SchedulerGUI.this, 
                        "Error saving tasks before exit: " + ex.getMessage(), 
                        "Save Error", JOptionPane.ERROR_MESSAGE);
                }
                System.exit(0);
            }
        });

        // North: Title
        JLabel titleLabel = new JLabel("Smart Task Scheduler", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(44, 62, 80));
        titleLabel.setBorder(new EmptyBorder(20, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Center: Task Table
        String[] columns = {"Task ID", "Title", "Type", "Deadline", "Base Priority", "Dynamic Priority", "Interval"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // read-only table
            }
        };
        taskTable = new JTable(tableModel);
        taskTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        taskTable.setRowHeight(30);
        taskTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        taskTable.getTableHeader().setBackground(new Color(52, 152, 219));
        taskTable.getTableHeader().setForeground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(taskTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        // South: Action Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(240, 244, 248));
        buttonPanel.setBorder(new EmptyBorder(10, 0, 20, 0));

        JButton btnAddOneTime = createStyledButton("Add One-Time Task", new Color(46, 204, 113));
        JButton btnAddRecurring = createStyledButton("Add Recurring Task", new Color(155, 89, 182));
        JButton btnViewNext = createStyledButton("View Next Highest Priority Task", new Color(230, 126, 34));
        JButton btnDelete = createStyledButton("Delete Task", new Color(231, 76, 60));

        btnAddOneTime.addActionListener(e -> openAddTaskDialog(false));
        btnAddRecurring.addActionListener(e -> openAddTaskDialog(true));
        btnViewNext.addActionListener(e -> viewNextHighestPriorityTask());
        btnDelete.addActionListener(e -> deleteSelectedTask());

        buttonPanel.add(btnAddOneTime);
        buttonPanel.add(btnAddRecurring);
        buttonPanel.add(btnViewNext);
        buttonPanel.add(btnDelete);

        add(buttonPanel, BorderLayout.SOUTH);

        // Initial Data Load
        refreshTable();
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(220, 45));
        return button;
    }

    private void refreshTable() {
        tableModel.setRowCount(0); // clear existing
        List<Task> tasks = scheduler.getAllTasks();
        
        // Sorting it visually to match priority
        tasks.sort((t1, t2) -> Double.compare(t2.calculateDynamicPriority(), t1.calculateDynamicPriority()));

        for (Task t : tasks) {
            String type = t instanceof RecurringTask ? "Recurring" : "One-Time";
            String interval = t instanceof RecurringTask ? String.valueOf(((RecurringTask) t).getRecurringIntervalDays()) + " days" : "N/A";
            
            tableModel.addRow(new Object[]{
                t.getTaskId(),
                t.getTitle(),
                type,
                t.getDeadline().toString(),
                String.format("%.1f", t.getBasePriority()),
                String.format("%.2f", t.calculateDynamicPriority()),
                interval
            });
        }
    }

    private void openAddTaskDialog(boolean isRecurring) {
        JDialog dialog = new JDialog(this, "Add " + (isRecurring ? "Recurring" : "One-Time") + " Task", true);
        dialog.setSize(400, isRecurring ? 450 : 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridLayout(isRecurring ? 5 : 4, 2, 10, 20));
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JTextField txtId = new JTextField();
        JTextField txtTitle = new JTextField();
        JTextField txtDeadline = new JTextField("YYYY-MM-DD");
        JTextField txtPriority = new JTextField();
        JTextField txtInterval = new JTextField();

        formPanel.add(new JLabel("Task ID:"));
        formPanel.add(txtId);
        formPanel.add(new JLabel("Title:"));
        formPanel.add(txtTitle);
        formPanel.add(new JLabel("Deadline (YYYY-MM-DD):"));
        formPanel.add(txtDeadline);
        formPanel.add(new JLabel("Base Priority (1-10):"));
        formPanel.add(txtPriority);

        if (isRecurring) {
            formPanel.add(new JLabel("Interval (Days):"));
            formPanel.add(txtInterval);
        }

        dialog.add(formPanel, BorderLayout.CENTER);

        JButton saveButton = createStyledButton("Save Task", new Color(52, 152, 219));
        saveButton.setPreferredSize(new Dimension(150, 40));
        saveButton.addActionListener(e -> {
            try {
                String id = txtId.getText();
                String title = txtTitle.getText();
                LocalDate deadline = LocalDate.parse(txtDeadline.getText());
                
                if (deadline.isBefore(LocalDate.now())) {
                    JOptionPane.showMessageDialog(dialog, "Error: Deadline cannot be in the past.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                double basePriority = Double.parseDouble(txtPriority.getText());

                Task task;
                if (isRecurring) {
                    int interval = Integer.parseInt(txtInterval.getText());
                    task = new RecurringTask(id, title, deadline, basePriority, interval);
                } else {
                    task = new OneTimeTask(id, title, deadline, basePriority);
                }

                scheduler.addTask(task);
                refreshTable();
                dialog.dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error adding task: " + ex.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel btnPanel = new JPanel();
        btnPanel.add(saveButton);
        btnPanel.setBorder(new EmptyBorder(0, 0, 15, 0));
        dialog.add(btnPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private void viewNextHighestPriorityTask() {
        Task nextTask = scheduler.peekNextTask();
        if (nextTask == null) {
            JOptionPane.showMessageDialog(this, "No tasks scheduled.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String details = "Next Task to Complete:\n\n" +
                         "ID: " + nextTask.getTaskId() + "\n" +
                         "Title: " + nextTask.getTitle() + "\n" +
                         "Deadline: " + nextTask.getDeadline().toString() + "\n" +
                         "Dynamic Priority: " + String.format("%.2f", nextTask.calculateDynamicPriority());

        JOptionPane.showMessageDialog(this, details, "Highest Priority Task", JOptionPane.INFORMATION_MESSAGE);
    }

    private void deleteSelectedTask() {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a task from the table to delete.", "No Task Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String taskId = (String) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete task ID: " + taskId + "?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (scheduler.removeTask(taskId)) {
                JOptionPane.showMessageDialog(this, "Task deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete task. It might no longer exist.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
