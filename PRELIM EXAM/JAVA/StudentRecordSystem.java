// Student Record System - Nestor, Chance Darwin S. 25-1855-855
// CRUD Challenge Implementation - Java Swing

// RUN IT IN TERMINAL (type "java StudentRecordSystem")

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class StudentRecordSystem extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    
    // Input fields
    private JTextField txtStudentID, txtFirstName, txtLastName;
    private JTextField txtLab1, txtLab2, txtLab3;
    private JTextField txtPrelim, txtAttendance;
    
    private JButton btnAdd, btnDelete;

    public StudentRecordSystem() {
        // Set frame title with programmer identifier
        setTitle("Student Record System - Nestor, Chance Darwin S. 25-1855-855");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 700);
        setLocationRelativeTo(null);

        // Initialize components
        initializeComponents();
        
        // Load data from CSV
        loadDataFromCSV();
        
        setVisible(true);
    }

    private void initializeComponents() {
        // Main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Input panel at the top
        JPanel inputPanel = createInputPanel();
        
        // Table setup
        String[] columnNames = {
            "Student ID", "First Name", "Last Name", 
            "Lab Work 1", "Lab Work 2", "Lab Work 3", 
            "Prelim Exam", "Attendance Grade"
        };
        
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };
        
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(25);
        table.getTableHeader().setReorderingAllowed(false);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(table);

        // Add components to main panel
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Add main panel to frame
        add(mainPanel);
    }

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout(10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "Student Information"
        ));
        inputPanel.setBackground(Color.WHITE);

        // Create form panel with GridBagLayout for better control
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Initialize text fields
        txtStudentID = new JTextField(15);
        txtFirstName = new JTextField(15);
        txtLastName = new JTextField(15);
        txtLab1 = new JTextField(15);
        txtLab2 = new JTextField(15);
        txtLab3 = new JTextField(15);
        txtPrelim = new JTextField(15);
        txtAttendance = new JTextField(15);

        // Row 0: Student ID and First Name
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Student ID:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtStudentID, gbc);
        
        gbc.gridx = 2;
        formPanel.add(new JLabel("First Name:"), gbc);
        gbc.gridx = 3;
        formPanel.add(txtFirstName, gbc);

        // Row 1: Last Name and Lab Work 1
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Last Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtLastName, gbc);
        
        gbc.gridx = 2;
        formPanel.add(new JLabel("Lab Work 1:"), gbc);
        gbc.gridx = 3;
        formPanel.add(txtLab1, gbc);

        // Row 2: Lab Work 2 and Lab Work 3
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Lab Work 2:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtLab2, gbc);
        
        gbc.gridx = 2;
        formPanel.add(new JLabel("Lab Work 3:"), gbc);
        gbc.gridx = 3;
        formPanel.add(txtLab3, gbc);

        // Row 3: Prelim Exam and Attendance
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Prelim Exam:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtPrelim, gbc);
        
        gbc.gridx = 2;
        formPanel.add(new JLabel("Attendance:"), gbc);
        gbc.gridx = 3;
        formPanel.add(txtAttendance, gbc);

        // Button panel at the bottom of input section
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        
        btnAdd = new JButton("Add Record");
        btnDelete = new JButton("Delete Record");
        
        btnAdd.setPreferredSize(new Dimension(150, 30));
        btnDelete.setPreferredSize(new Dimension(150, 30));
        
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnDelete);

        // Add button listeners
        btnAdd.addActionListener(e -> addRecord());
        btnDelete.addActionListener(e -> deleteRecord());

        // Add panels to input panel
        inputPanel.add(formPanel, BorderLayout.CENTER);
        inputPanel.add(buttonPanel, BorderLayout.SOUTH);

        return inputPanel;
    }

    private void loadDataFromCSV() {
        try (BufferedReader br = new BufferedReader(new FileReader("class_records.csv"))) {
            String line;
            boolean isFirstLine = true;
            
            while ((line = br.readLine()) != null) {
                // Skip header line
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                
                // Split the CSV line
                String[] data = line.split(",");
                
                // Add row to table if we have enough columns
                if (data.length >= 8) {
                    Object[] rowData = new Object[8];
                    for (int i = 0; i < 8; i++) {
                        rowData[i] = data[i];
                    }
                    tableModel.addRow(rowData);
                }
            }
            
            JOptionPane.showMessageDialog(this, 
                "Successfully loaded " + tableModel.getRowCount() + " records from class_records.csv",
                "Data Loaded", 
                JOptionPane.INFORMATION_MESSAGE);
                
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                "Error reading class_records.csv: " + e.getMessage() + 
                "\nPlease ensure the file is in the same directory as the program.",
                "File Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addRecord() {
        // Get all input values
        String studentID = txtStudentID.getText().trim();
        String firstName = txtFirstName.getText().trim();
        String lastName = txtLastName.getText().trim();
        String lab1 = txtLab1.getText().trim();
        String lab2 = txtLab2.getText().trim();
        String lab3 = txtLab3.getText().trim();
        String prelim = txtPrelim.getText().trim();
        String attendance = txtAttendance.getText().trim();

        // Validation - check if all fields are filled
        if (studentID.isEmpty() || firstName.isEmpty() || lastName.isEmpty() ||
            lab1.isEmpty() || lab2.isEmpty() || lab3.isEmpty() ||
            prelim.isEmpty() || attendance.isEmpty()) {
            
            JOptionPane.showMessageDialog(this,
                "Please fill in all fields!",
                "Input Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validate that grades are numbers
        try {
            int lab1Grade = Integer.parseInt(lab1);
            int lab2Grade = Integer.parseInt(lab2);
            int lab3Grade = Integer.parseInt(lab3);
            int prelimGrade = Integer.parseInt(prelim);
            int attendanceGrade = Integer.parseInt(attendance);
            
            // Check if grades are in valid range (0-100)
            if (lab1Grade < 0 || lab1Grade > 100 ||
                lab2Grade < 0 || lab2Grade > 100 ||
                lab3Grade < 0 || lab3Grade > 100 ||
                prelimGrade < 0 || prelimGrade > 100 ||
                attendanceGrade < 0 || attendanceGrade > 100) {
                
                JOptionPane.showMessageDialog(this,
                    "All grades must be between 0 and 100!",
                    "Input Error",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Add new row to table
            Object[] rowData = {
                studentID, firstName, lastName,
                lab1, lab2, lab3,
                prelim, attendance
            };
            tableModel.addRow(rowData);
            
            // Clear all input fields
            clearInputFields();
            
            JOptionPane.showMessageDialog(this,
                "Record added successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
                
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "All grades must be valid numbers!",
                "Input Error",
                JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteRecord() {
        int selectedRow = table.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a row to delete",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete this record?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            tableModel.removeRow(selectedRow);
            JOptionPane.showMessageDialog(this,
                "Record deleted successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void clearInputFields() {
        txtStudentID.setText("");
        txtFirstName.setText("");
        txtLastName.setText("");
        txtLab1.setText("");
        txtLab2.setText("");
        txtLab3.setText("");
        txtPrelim.setText("");
        txtAttendance.setText("");
    }

    public static void main(String[] args) {
        // Use SwingUtilities to ensure thread safety
        SwingUtilities.invokeLater(() -> new StudentRecordSystem());
    }
}