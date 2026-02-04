import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.text.*;

public class PrelimGradeCalculator extends JFrame {
    // Input fields
    private JTextField attendanceField;
    private JTextField excusedAbsencesField;
    private JTextField lab1Field;
    private JTextField lab2Field;
    private JTextField lab3Field;
    
    // Output area
    private JTextArea outputArea;
    
    // Calculate button
    private JButton calculateButton;
    private JButton clearButton;
    
    public PrelimGradeCalculator() {
        setTitle("Prelim Grade Calculator");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Create main panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Title panel
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Prelim Grade Calculator");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titlePanel.add(titleLabel);
        
        // Input panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(5, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Student Information"));
        
        inputPanel.add(new JLabel("Number of Attendances (out of 5):"));
        attendanceField = createIntegerOnlyField();
        inputPanel.add(attendanceField);
        
        inputPanel.add(new JLabel("Excused Absences (if any):"));
        excusedAbsencesField = createIntegerOnlyField();
        excusedAbsencesField.setText("0");
        inputPanel.add(excusedAbsencesField);
        
        inputPanel.add(new JLabel("Lab Work 1 Grade (whole numbers only):"));
        lab1Field = createIntegerOnlyField();
        inputPanel.add(lab1Field);
        
        inputPanel.add(new JLabel("Lab Work 2 Grade (whole numbers only):"));
        lab2Field = createIntegerOnlyField();
        inputPanel.add(lab2Field);
        
        inputPanel.add(new JLabel("Lab Work 3 Grade (whole numbers only):"));
        lab3Field = createIntegerOnlyField();
        inputPanel.add(lab3Field);
        
        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        calculateButton = new JButton("Calculate");
        calculateButton.setPreferredSize(new Dimension(120, 35));
        calculateButton.addActionListener(new CalculateListener());
        
        clearButton = new JButton("Clear");
        clearButton.setPreferredSize(new Dimension(120, 35));
        clearButton.addActionListener(new ClearListener());
        
        buttonPanel.add(calculateButton);
        buttonPanel.add(clearButton);
        
        // Output panel
        JPanel outputPanel = new JPanel();
        outputPanel.setLayout(new BorderLayout());
        outputPanel.setBorder(BorderFactory.createTitledBorder("Results"));
        
        outputArea = new JTextArea(14, 40);
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(outputArea);
        outputPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Add panels to main panel
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout(10, 10));
        centerPanel.add(inputPanel, BorderLayout.NORTH);
        centerPanel.add(buttonPanel, BorderLayout.CENTER);
        centerPanel.add(outputPanel, BorderLayout.SOUTH);
        
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    // Create a text field that only accepts integers (no decimals)
    private JTextField createIntegerOnlyField() {
        JTextField field = new JTextField();
        ((AbstractDocument) field.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string != null && string.matches("\\d*")) {
                    super.insertString(fb, offset, string, attr);
                }
            }
            
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text != null && text.matches("\\d*")) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });
        return field;
    }
    
    private class CalculateListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                // Get input values
                int attendance = Integer.parseInt(attendanceField.getText().trim());
                int excusedAbsences = Integer.parseInt(excusedAbsencesField.getText().trim());
                int lab1 = Integer.parseInt(lab1Field.getText().trim());
                int lab2 = Integer.parseInt(lab2Field.getText().trim());
                int lab3 = Integer.parseInt(lab3Field.getText().trim());
                
                // Calculate absences automatically
                int absences = 5 - attendance;
                
                // Validate inputs
                if (attendance < 0 || attendance > 5 || excusedAbsences < 0 ||
                    excusedAbsences > absences || lab1 < 0 || lab1 > 100 || 
                    lab2 < 0 || lab2 > 100 || lab3 < 0 || lab3 > 100) {
                    JOptionPane.showMessageDialog(PrelimGradeCalculator.this,
                        "Please enter valid values:\n" +
                        "- Attendance: 0-5\n" +
                        "- Excused Absences: 0 to " + absences + " (total absences)\n" +
                        "- Lab grades: 0-100 (whole numbers only)",
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Calculate unexcused absences
                int unexcusedAbsences = absences - excusedAbsences;
                
                // Calculate attendance percentage (100% - 20% per unexcused absence)
                double attendancePercentage = 100.0 - (unexcusedAbsences * 20.0);
                
                // Check if student failed due to attendance (below 20% = 4 or more unexcused absences)
                boolean failedByAttendance = attendancePercentage < 20.0;
                
                // Calculate Lab Work Average
                double labWorkAverage = (lab1 + lab2 + lab3) / 3.0;
                
                // Calculate Class Standing (using attendance percentage)
                double classStanding = (attendancePercentage * 0.40) + (labWorkAverage * 0.60);
                
                // Calculate required Prelim Exam scores
                double requiredForPass = (75 - (classStanding * 0.70)) / 0.30;
                double requiredForExcellent = (100 - (classStanding * 0.70)) / 0.30;
                
                // Build output
                StringBuilder output = new StringBuilder();
                output.append("═══════════════════════════════════════════════════\n");
                output.append("           PRELIM GRADE CALCULATION RESULTS\n");
                output.append("═══════════════════════════════════════════════════\n\n");
                
                output.append("ATTENDANCE INFORMATION:\n");
                output.append(String.format("  Days Present: %d out of 5\n", attendance));
                output.append(String.format("  Total Absences: %d\n", absences));
                output.append(String.format("  Excused Absences: %d\n", excusedAbsences));
                output.append(String.format("  Unexcused Absences: %d\n", unexcusedAbsences));
                output.append(String.format("  Attendance Percentage: %.2f%%\n", attendancePercentage));
                output.append(String.format("  (100%% - %d unexcused × 20%%)\n\n", unexcusedAbsences));
                
                output.append("LAB WORK GRADES:\n");
                output.append(String.format("  Lab Work 1: %d\n", lab1));
                output.append(String.format("  Lab Work 2: %d\n", lab2));
                output.append(String.format("  Lab Work 3: %d\n\n", lab3));
                
                output.append("COMPUTED VALUES:\n");
                output.append(String.format("  Lab Work Average: %.2f\n", labWorkAverage));
                output.append(String.format("  Class Standing: %.2f\n\n", classStanding));
                
                // Check if failed by attendance first
                if (failedByAttendance) {
                    output.append("═══════════════════════════════════════════════════\n");
                    output.append("                 ⚠️  FAILED STATUS  ⚠️\n");
                    output.append("═══════════════════════════════════════════════════\n\n");
                    output.append("REMARKS:\n");
                    output.append(String.format("  ❌ AUTOMATIC FAILURE: Your attendance is %.2f%%\n", attendancePercentage));
                    output.append("     (below the required 20%).\n\n");
                    output.append(String.format("  You have %d unexcused absences out of 5 sessions.\n", unexcusedAbsences));
                    output.append("  Each unexcused absence deducts 20%% from attendance.\n\n");
                    output.append("  According to the attendance policy, having 4 or more\n");
                    output.append("  unexcused absences (less than 20%% attendance) results\n");
                    output.append("  in an automatic FAILING grade for the Prelim period,\n");
                    output.append("  regardless of exam and lab work scores.\n\n");
                    output.append("  ⚠️  You CANNOT pass this Prelim period.\n");
                } else {
                    output.append("REQUIRED PRELIM EXAM SCORES:\n");
                    
                    // Display required score for passing
                    if (requiredForPass <= 0) {
                        output.append("  To Pass (75): Already achieved\n");
                    } else if (requiredForPass > 100) {
                        output.append(String.format("  To Pass (75): Impossible (would need %.2f)\n", requiredForPass));
                    } else {
                        output.append(String.format("  To Pass (75): %.2f\n", requiredForPass));
                    }
                    
                    // Display required score for excellent
                    if (requiredForExcellent <= 0) {
                        output.append("  To Achieve Excellent (100): Already achieved\n\n");
                    } else if (requiredForExcellent > 100) {
                        output.append("  To Achieve Excellent (100): Impossible\n\n");
                    } else {
                        output.append(String.format("  To Achieve Excellent (100): %.2f\n\n", requiredForExcellent));
                    }
                    
                    output.append("REMARKS:\n");
                    output.append(String.format("  ✅ Attendance Status: PASSING (%.2f%%)\n\n", attendancePercentage));
                    
                    if (requiredForPass <= 0) {
                        output.append("  ⭐ Congratulations! You have already secured\n");
                        output.append("     a passing grade based on your Class Standing.\n");
                        output.append("     Even with 0 on the Prelim Exam, you will pass!\n");
                    } else if (requiredForPass > 100) {
                        output.append("  ⚠️  Unfortunately, it is mathematically impossible\n");
                        output.append("     to achieve a passing grade of 75.\n");
                        double maxGrade = (100 * 0.30) + (classStanding * 0.70);
                        output.append(String.format("     Maximum possible Prelim Grade: %.2f\n", maxGrade));
                    } else {
                        output.append(String.format("  • You need to score %.2f or higher on the\n", requiredForPass));
                        output.append("    Prelim Exam to pass the Prelim period.\n");
                    }
                    
                    if (requiredForExcellent <= 0) {
                        output.append("  ⭐ You have already achieved an excellent standing!\n");
                    } else if (requiredForExcellent > 100) {
                        output.append("  • Achieving an excellent grade (100) is not possible\n");
                        output.append("    based on your current Class Standing.\n");
                    } else if (requiredForExcellent <= 100 && requiredForPass <= 100) {
                        output.append(String.format("  • To achieve excellent standing (100), you need\n"));
                        output.append(String.format("    to score %.2f on the Prelim Exam.\n", requiredForExcellent));
                    }
                }
                
                output.append("\n═══════════════════════════════════════════════════");
                
                outputArea.setText(output.toString());
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(PrelimGradeCalculator.this,
                    "Please enter valid numeric values in all fields.\n" +
                    "Note: Only whole numbers are allowed (no decimals).",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private class ClearListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            attendanceField.setText("");
            excusedAbsencesField.setText("0");
            lab1Field.setText("");
            lab2Field.setText("");
            lab3Field.setText("");
            outputArea.setText("");
            attendanceField.requestFocus();
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                PrelimGradeCalculator calculator = new PrelimGradeCalculator();
                calculator.setVisible(true);
            }
        });
    }
}