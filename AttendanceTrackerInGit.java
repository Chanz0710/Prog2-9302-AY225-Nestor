import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.Arrays;
import java.util.List;

public class AttendanceTrackerInGit {
    
    // Main frame for the application
    private JFrame frame;
    
    // Text fields for user input and display
    private JTextField nameField;
    private JComboBox<String> courseComboBox;
    private JTextField yearField;
    private JTextField timeInField;
    private JTextField eSignatureField;
    
    // Valid courses offered at UPHSD Molino
    private static final List<String> VALID_COURSES = Arrays.asList(
        // Business and Hospitality Management
        "BS in Hospitality Management",
        "BS in Tourism Management",
        "BS in Accountancy",
        "BS in Business Administration",
        "BS in Entrepreneurship",
        
        // Medical and Allied Health
        "BS in Medical Technology",
        "BS in Nursing",
        "BS in Occupational Therapy",
        "BS in Pharmacy",
        "BS in Physical Therapy",
        "BS in Radiologic Technology",
        
        // Education
        "Bachelor of Elementary Education",
        "Bachelor of Secondary Education",
        "Bachelor in Physical Education",
        
        // Engineering
        "BS in Computer Engineering",
        "BS in Electrical Engineering",
        "BS in Industrial Engineering",
        "BS in Electronics Engineering",
        
        // Computer Studies
        "BS in Computer Science",
        "BS in Information Technology",
        
        // Others
        "BS in Criminology",
        "BS in Architecture",
        
        // Associate Programs
        "Associate in Hotel & Restaurant Management",
        
        // Technical-Vocational Courses
        "Bartending NC II",
        "Bread and Pastry NC II",
        "Cookery NC II",
        "Food and Beverages Services NC II",
        "Housekeeping NC II",
        "Caregiving NC II",
        "Consumer Electronics Servicing NC II",
        "Shielded Metal Arc Welding (SMAW) NC II",
        
        // Senior High School Strands
        "Senior High - ABM",
        "Senior High - STEM",
        "Senior High - HUMSS",
        "Senior High - TVL"
    );
    
    /**
     * Constructor - Initializes the attendance tracker application
     */
    public AttendanceTrackerInGit() {
        initializeUI();
    }
    
    /**
     * Initializes the user interface components
     */
    private void initializeUI() {
        // Create the main frame
        frame = new JFrame("UPHSD Molino Attendance Tracker");
        frame.setSize(600, 450);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // Center the window on screen
        
        // Create main panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Create title label
        JLabel titleLabel = new JLabel("UPHSD Molino Attendance Tracking System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(139, 0, 0)); // Maroon color
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Create form panel for input fields
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(5, 2, 10, 15));
        
        // Attendance Name field
        JLabel nameLabel = new JLabel("Attendance Name:");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        nameField = new JTextField(20);
        nameField.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(nameLabel);
        formPanel.add(nameField);
        
        // Course dropdown field
        JLabel courseLabel = new JLabel("Course/Program:");
        courseLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Create sorted array for dropdown
        String[] coursesArray = VALID_COURSES.toArray(new String[0]);
        Arrays.sort(coursesArray); // Sort alphabetically
        
        courseComboBox = new JComboBox<>(coursesArray);
        courseComboBox.setFont(new Font("Arial", Font.PLAIN, 12));
        courseComboBox.setEditable(true); // Allow typing custom course
        formPanel.add(courseLabel);
        formPanel.add(courseComboBox);
        
        // Year Level field
        JLabel yearLabel = new JLabel("Year Level:");
        yearLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        String[] yearLevels = {"1st Year", "2nd Year", "3rd Year", "4th Year", "5th Year"};
        yearField = new JTextField(10);
        yearField.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Create a combo box for year levels
        JComboBox<String> yearComboBox = new JComboBox<>(yearLevels);
        yearComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        yearComboBox.addActionListener(e -> {
            yearField.setText((String) yearComboBox.getSelectedItem());
        });
        
        formPanel.add(yearLabel);
        formPanel.add(yearComboBox);
        
        // Time In field (auto-populated with current date/time)
        JLabel timeInLabel = new JLabel("Time In:");
        timeInLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        timeInField = new JTextField(20);
        timeInField.setFont(new Font("Arial", Font.PLAIN, 14));
        timeInField.setEditable(false); // Make read-only
        timeInField.setBackground(Color.WHITE);
        
        // Get current date and time and format it
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = currentDateTime.format(formatter);
        timeInField.setText(formattedDateTime);
        
        formPanel.add(timeInLabel);
        formPanel.add(timeInField);
        
        // E-Signature field (auto-generated unique identifier)
        JLabel eSignatureLabel = new JLabel("E-Signature:");
        eSignatureLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        eSignatureField = new JTextField(20);
        eSignatureField.setFont(new Font("Arial", Font.PLAIN, 11));
        eSignatureField.setEditable(false); // Make read-only
        eSignatureField.setBackground(Color.WHITE);
        
        // Generate unique e-signature using UUID
        String eSignature = UUID.randomUUID().toString();
        eSignatureField.setText(eSignature);
        
        formPanel.add(eSignatureLabel);
        formPanel.add(eSignatureField);
        
        // Add form panel to main panel
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // Create button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        // Submit button
        JButton submitButton = new JButton("Submit Attendance");
        submitButton.setFont(new Font("Arial", Font.BOLD, 14));
        submitButton.setPreferredSize(new Dimension(180, 35));
        submitButton.setBackground(new Color(139, 0, 0)); // Maroon color
        submitButton.setForeground(Color.WHITE);
        submitButton.addActionListener(e -> submitAttendance());
        buttonPanel.add(submitButton);
        
        // Clear button
        JButton clearButton = new JButton("Clear");
        clearButton.setFont(new Font("Arial", Font.PLAIN, 14));
        clearButton.setPreferredSize(new Dimension(100, 35));
        clearButton.addActionListener(e -> clearFields());
        buttonPanel.add(clearButton);
        
        // View Valid Courses button
        JButton viewCoursesButton = new JButton("View Valid Courses");
        viewCoursesButton.setFont(new Font("Arial", Font.PLAIN, 12));
        viewCoursesButton.setPreferredSize(new Dimension(160, 35));
        viewCoursesButton.addActionListener(e -> showValidCourses());
        buttonPanel.add(viewCoursesButton);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add main panel to frame
        frame.add(mainPanel);
        
        // Make the frame visible
        frame.setVisible(true);
    }
    
    /**
     * Validates if the entered course is in the list of valid UPHSD Molino courses
     * @param course The course to validate
     * @return true if valid, false otherwise
     */
    private boolean isValidCourse(String course) {
        // Trim and check if course exists in the valid courses list (case-insensitive)
        String trimmedCourse = course.trim();
        for (String validCourse : VALID_COURSES) {
            if (validCourse.equalsIgnoreCase(trimmedCourse)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Handles the submit attendance action
     * Validates input and displays confirmation message
     */
    private void submitAttendance() {
        String name = nameField.getText().trim();
        String course = (String) courseComboBox.getSelectedItem();
        String year = yearField.getText().trim();
        
        // Validate that required fields are filled
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(frame,
                "Please enter your name.",
                "Missing Name",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (course == null || course.trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame,
                "Please select or enter a course/program.",
                "Missing Course",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Validate course against UPHSD Molino offerings
        if (!isValidCourse(course)) {
            // Show error dialog with course validation failure
            int option = JOptionPane.showOptionDialog(frame,
                "ERROR: Invalid Course/Program!\n\n" +
                "\"" + course + "\" is not offered at UPHSD Molino.\n\n" +
                "Please select a valid course from the dropdown list\n" +
                "or click 'View Valid Courses' to see all available programs.",
                "Invalid Course Error",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.ERROR_MESSAGE,
                null,
                new Object[]{"View Valid Courses", "OK"},
                "OK");
            
            // If user clicks "View Valid Courses", show the list
            if (option == 0) {
                showValidCourses();
            }
            return;
        }
        
        // Display success message with attendance details
        String message = String.format(
            "✓ Attendance Recorded Successfully!\n\n" +
            "Name: %s\n" +
            "Course/Program: %s\n" +
            "Year Level: %s\n" +
            "Time In: %s\n" +
            "E-Signature: %s\n\n" +
            "Thank you for using UPHSD Molino Attendance System!",
            name, course, year, timeInField.getText(), eSignatureField.getText()
        );
        
        JOptionPane.showMessageDialog(frame,
            message,
            "Attendance Submitted",
            JOptionPane.INFORMATION_MESSAGE);
        
        // Optionally clear fields after successful submission
        clearFields();
    }
    
    /**
     * Displays a dialog showing all valid courses offered at UPHSD Molino
     */
    private void showValidCourses() {
        // Create a formatted list of courses by category
        StringBuilder courseList = new StringBuilder();
        courseList.append("Valid Courses/Programs at UPHSD Molino:\n\n");
        
        courseList.append("═══ BUSINESS & HOSPITALITY ═══\n");
        courseList.append("• BS in Hospitality Management\n");
        courseList.append("• BS in Tourism Management\n");
        courseList.append("• BS in Accountancy\n");
        courseList.append("• BS in Business Administration\n");
        courseList.append("• BS in Entrepreneurship\n\n");
        
        courseList.append("═══ MEDICAL & ALLIED HEALTH ═══\n");
        courseList.append("• BS in Medical Technology\n");
        courseList.append("• BS in Nursing\n");
        courseList.append("• BS in Occupational Therapy\n");
        courseList.append("• BS in Pharmacy\n");
        courseList.append("• BS in Physical Therapy\n");
        courseList.append("• BS in Radiologic Technology\n\n");
        
        courseList.append("═══ EDUCATION ═══\n");
        courseList.append("• Bachelor of Elementary Education\n");
        courseList.append("• Bachelor of Secondary Education\n");
        courseList.append("• Bachelor in Physical Education\n\n");
        
        courseList.append("═══ ENGINEERING ═══\n");
        courseList.append("• BS in Computer Engineering\n");
        courseList.append("• BS in Electrical Engineering\n");
        courseList.append("• BS in Industrial Engineering\n");
        courseList.append("• BS in Electronics Engineering\n\n");
        
        courseList.append("═══ COMPUTER STUDIES ═══\n");
        courseList.append("• BS in Computer Science\n");
        courseList.append("• BS in Information Technology\n\n");
        
        courseList.append("═══ OTHER PROGRAMS ═══\n");
        courseList.append("• BS in Criminology\n");
        courseList.append("• BS in Architecture\n\n");
        
        courseList.append("═══ SENIOR HIGH SCHOOL ═══\n");
        courseList.append("• Senior High - ABM\n");
        courseList.append("• Senior High - STEM\n");
        courseList.append("• Senior High - HUMSS\n");
        courseList.append("• Senior High - TVL\n\n");
        
        courseList.append("═══ TECHNICAL-VOCATIONAL ═══\n");
        courseList.append("• Bartending NC II\n");
        courseList.append("• Bread and Pastry NC II\n");
        courseList.append("• Cookery NC II\n");
        courseList.append("• And more...\n");
        
        // Create text area for scrollable display
        JTextArea textArea = new JTextArea(courseList.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setCaretPosition(0);
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 400));
        
        JOptionPane.showMessageDialog(frame,
            scrollPane,
            "UPHSD Molino Valid Courses",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Clears all input fields and regenerates time and e-signature
     */
    private void clearFields() {
        nameField.setText("");
        courseComboBox.setSelectedIndex(0);
        yearField.setText("");
        
        // Regenerate current time
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = currentDateTime.format(formatter);
        timeInField.setText(formattedDateTime);
        
        // Regenerate e-signature
        String eSignature = UUID.randomUUID().toString();
        eSignatureField.setText(eSignature);
        
        // Set focus back to name field
        nameField.requestFocus();
    }
    
    /**
     * Main method - Entry point of the application
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        // Use SwingUtilities to ensure thread safety
        SwingUtilities.invokeLater(() -> {
            new AttendanceTrackerInGit();
        });
    }
}
