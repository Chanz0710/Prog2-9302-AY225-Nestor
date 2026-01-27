import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PrelimGradeCalculator extends JFrame {
    // Input fields
    private JTextField attendanceField;
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
        setSize(600, 550);
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
        inputPanel.setLayout(new GridLayout(4, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Student Information"));
        
        inputPanel.add(new JLabel("Number of Attendances:"));
        attendanceField = new JTextField();
        inputPanel.add(attendanceField);
        
        inputPanel.add(new JLabel("Lab Work 1 Grade:"));
        lab1Field = new JTextField();
        inputPanel.add(lab1Field);
        
        inputPanel.add(new JLabel("Lab Work 2 Grade:"));
        lab2Field = new JTextField();
        inputPanel.add(lab2Field);
        
        inputPanel.add(new JLabel("Lab Work 3 Grade:"));
        lab3Field = new JTextField();
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
        
        outputArea = new JTextArea(12, 40);
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
    
    private class CalculateListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                // Get input values
                double attendance = Double.parseDouble(attendanceField.getText().trim());
                double lab1 = Double.parseDouble(lab1Field.getText().trim());
                double lab2 = Double.parseDouble(lab2Field.getText().trim());
                double lab3 = Double.parseDouble(lab3Field.getText().trim());
                
                // Validate inputs
                if (attendance < 0 || lab1 < 0 || lab1 > 100 || 
                    lab2 < 0 || lab2 > 100 || lab3 < 0 || lab3 > 100) {
                    JOptionPane.showMessageDialog(PrelimGradeCalculator.this,
                        "Please enter valid values:\n" +
                        "- Attendance: non-negative number\n" +
                        "- Lab grades: 0-100",
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Calculate Lab Work Average
                double labWorkAverage = (lab1 + lab2 + lab3) / 3.0;
                
                // Calculate Class Standing
                double classStanding = (attendance * 0.40) + (labWorkAverage * 0.60);
                
                // Calculate required Prelim Exam scores
                // Corrected formula: Prelim Grade = (Prelim Exam × 0.30) + (Class Standing × 0.70)
                // For Passing (75): 75 = (Prelim Exam × 0.30) + (Class Standing × 0.70)
                // Prelim Exam = (75 - Class Standing × 0.70) / 0.30
                double requiredForPass = (75 - (classStanding * 0.70)) / 0.30;
                double requiredForExcellent = (100 - (classStanding * 0.70)) / 0.30;
                
                // Build output
                StringBuilder output = new StringBuilder();
                output.append("═══════════════════════════════════════════════════\n");
                output.append("           PRELIM GRADE CALCULATION RESULTS\n");
                output.append("═══════════════════════════════════════════════════\n\n");
                
                output.append("INPUT VALUES:\n");
                output.append(String.format("  Attendance Score: %.2f\n", attendance));
                output.append(String.format("  Lab Work 1: %.2f\n", lab1));
                output.append(String.format("  Lab Work 2: %.2f\n", lab2));
                output.append(String.format("  Lab Work 3: %.2f\n\n", lab3));
                
                output.append("COMPUTED VALUES:\n");
                output.append(String.format("  Lab Work Average: %.2f\n", labWorkAverage));
                output.append(String.format("  Class Standing: %.2f\n\n", classStanding));
                
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
                
                if (requiredForPass <= 0) {
                    output.append("  ★ Congratulations! You have already secured\n");
                    output.append("    a passing grade based on your Class Standing.\n");
                    output.append("    Even with 0 on the Prelim Exam, you will pass!\n");
                } else if (requiredForPass > 100) {
                    output.append("  ⚠ Unfortunately, it is mathematically impossible\n");
                    output.append("    to achieve a passing grade of 75.\n");
                    output.append("    Maximum possible Prelim Grade: ");
                    double maxGrade = (100 * 0.30) + (classStanding * 0.70);
                    output.append(String.format("%.2f\n", maxGrade));
                } else {
                    output.append(String.format("  • You need to score %.2f or higher on the\n", requiredForPass));
                    output.append("    Prelim Exam to pass the Prelim period.\n");
                }
                
                if (requiredForExcellent <= 0) {
                    output.append("  ★ You have already achieved an excellent standing!\n");
                } else if (requiredForExcellent > 100) {
                    output.append("  • Achieving an excellent grade (100) is not possible\n");
                    output.append("    based on your current Class Standing.\n");
                } else if (requiredForExcellent <= 100 && requiredForPass <= 100) {
                    output.append(String.format("  • To achieve excellent standing (100), you need\n"));
                    output.append(String.format("    to score %.2f on the Prelim Exam.\n", requiredForExcellent));
                }
                
                output.append("\n═══════════════════════════════════════════════════");
                
                outputArea.setText(output.toString());
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(PrelimGradeCalculator.this,
                    "Please enter valid numeric values in all fields.",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private class ClearListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            attendanceField.setText("");
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