import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Project {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new OnlineExamLoginPage();
        });
    }

    static class OnlineExamLoginPage extends JFrame {
        private static final String DB_URL = "jdbc:mysql://localhost:3306/quiz";
        private static final String USERNAME = "root";
        private static final String PASSWORD = "himaja2123";

        public OnlineExamLoginPage() {
            setTitle("Online Examination");
            setSize(400, 300);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);

            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JLabel titleLabel = new JLabel("Online Examination", JLabel.CENTER);
            titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            panel.add(titleLabel, gbc);

            JButton loginButton = new JButton("Login");
            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.CENTER;
            panel.add(loginButton, gbc);

            loginButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showUserTypeButtons();
                    dispose();
                }
            });

            add(panel);
            setVisible(true);
        }

        private void showUserTypeButtons() {
            // Create a new frame for user type selection
            JFrame userTypeFrame = new JFrame("Select User Type");
            userTypeFrame.setSize(400, 300);
            userTypeFrame.setLocationRelativeTo(this);
    
            // Create panel and layout
            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;
    
            // Login as Admin button
            JButton adminLoginButton = new JButton("Login as Admin");
            gbc.gridx = 0;
            gbc.gridy = 0;
            panel.add(adminLoginButton, gbc);
    
            // Login as Student button
            JButton studentLoginButton = new JButton("Login as Student");
            gbc.gridx = 1;
            gbc.gridy = 0;
            panel.add(studentLoginButton, gbc);
    
            // Add action listeners to the buttons
            adminLoginButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    userTypeFrame.dispose(); // Close the user type frame
                    adminShowLoginDialog();
                }
            });
    
            studentLoginButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    userTypeFrame.dispose(); // Close the user type frame
                    StudentDuty sd=new StudentDuty();
                    sd.studentShowLoginDialog();
                }
            });
    
            // Add panel to the frame
            userTypeFrame.add(panel);
    
            // Make the frame visible
            userTypeFrame.setVisible(true);
        }

        private Connection establishConnection() {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                return DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
                return null;
            }
        }

        private void adminShowLoginDialog() {
            JDialog loginDialog = new JDialog(this, "Login as Admin", true);
            loginDialog.setSize(400, 300);
            loginDialog.setLocationRelativeTo(this);

            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JLabel adminuserLabel = new JLabel("Username:");
            gbc.gridx = 0;
            gbc.gridy = 0;
            panel.add(adminuserLabel, gbc);

            JTextField userTextField = new JTextField(20);
            gbc.gridx = 1;
            gbc.gridy = 0;
            panel.add(userTextField, gbc);

            JLabel adminpasswordLabel = new JLabel("Password:");
            gbc.gridx = 0;
            gbc.gridy = 1;
            panel.add(adminpasswordLabel, gbc);

            JPasswordField passwordField = new JPasswordField(20);
            gbc.gridx = 1;
            gbc.gridy = 1;
            panel.add(passwordField, gbc);

            JButton adminloginButton = new JButton("Login");
            gbc.gridx = 1;
            gbc.gridy = 2;
            panel.add(adminloginButton, gbc);

            JLabel messageLabel = new JLabel();
            gbc.gridx = 1;
            gbc.gridy = 3;
            panel.add(messageLabel, gbc);

            adminloginButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String username = userTextField.getText();
                    String password = new String(passwordField.getPassword());
                    
                    if (adminValidateLogin(username, password)) {
                        dispose();
                        adminDuties();
                    } else {
                        showIncorrectLoginDialog();
                    }
                }
            });

            loginDialog.add(panel);
            loginDialog.setVisible(true);
        }

        void showIncorrectLoginDialog() {
            // Create a new JDialog
            JDialog dialog = new JDialog((Frame) null, "Login Error", true);
            dialog.setSize(300, 150);
            dialog.setLocationRelativeTo(null);
            
            // Create a panel to hold the message and button
            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;
    
            // Add the message label
            JLabel messageLabel = new JLabel("Incorrect username or password", JLabel.CENTER);
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            panel.add(messageLabel, gbc);
    
            // Add the OK button
            JButton okButton = new JButton("OK");
            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.CENTER;
            panel.add(okButton, gbc);
    
            // Add action listener to the OK button
            okButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dialog.dispose();
                }
            });
    
            // Add the panel to the dialog
            dialog.add(panel);
            
            // Make the dialog visible
            dialog.setVisible(true);
        
        }
        private boolean adminValidateLogin(String username, String password) {
            try (Connection connection = establishConnection()) {
                // Create a statement
                Statement statement = connection.createStatement();
                // Execute a query
                ResultSet resultSet = statement.executeQuery("SELECT * from login");
                while(resultSet.next()) {
                    String dbUsername = resultSet.getString("username");
                    String dbPassword = resultSet.getString("password");
                    if (username.equals(dbUsername) && password.equals(dbPassword)) {
                        return true; // Login successful
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false; // Login failed
        }
        

    void adminDuties() {
        JFrame frame = new JFrame("Admin Duties");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Button 1
        JButton button1 = new JButton("Add Admin");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(button1, gbc);

        // Button 2
        JButton button2 = new JButton("Add Student");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(button2, gbc);

        // Button 3
        JButton button3 = new JButton("Add Question");
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(button3, gbc);

        JButton button4 = new JButton("Set Timer");
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(button4, gbc);

        // Add action listeners for each button
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createAddAdminPage();
            }
        });

        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createAddStudentPage();
            }
        });

        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddQuestions aq=new AddQuestions();
                aq.createInitialPage();
            }
        });

        button4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createExamTimerSetupPage1();
            }
        });

        frame.add(panel);
        frame.setVisible(true);
    }
     
    public void createAddAdminPage() {
        // Create the main frame
        JFrame frame = new JFrame("Add New Admin");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);

        // Create the panel to hold the components
        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponentsAdmin(panel, frame);

        // Set the frame visibility to true
        frame.setVisible(true);
    }

    public void createAddStudentPage() {
        // Create the main frame
        JFrame frame = new JFrame("Add New Student");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);

        // Create the panel to hold the components
        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponentsStudent(panel, frame);

        // Set the frame visibility to true
        frame.setVisible(true);
    }

    private void placeComponentsStudent(JPanel panel, JFrame frame) {
        panel.setLayout(null);

        // Create JLabel for username
        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(10, 20, 80, 25);
        panel.add(userLabel);

        // Create text field for username
        JTextField userText = new JTextField(20);
        userText.setBounds(150, 20, 165, 25);
        panel.add(userText);

        // Create JLabel for password
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(10, 50, 80, 25);
        panel.add(passwordLabel);

        // Create text field for password
        JPasswordField passwordText = new JPasswordField(20);
        passwordText.setBounds(150, 50, 165, 25);
        panel.add(passwordText);

        // Create JLabel for confirm password
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordLabel.setBounds(10, 80, 120, 25);
        panel.add(confirmPasswordLabel);

        // Create text field for confirm password
        JPasswordField confirmPasswordText = new JPasswordField(20);
        confirmPasswordText.setBounds(150, 80, 165, 25);
        panel.add(confirmPasswordText);

        // Create the Add Student button
        JButton addStudentButton = new JButton("Add Student");
        addStudentButton.setBounds(150, 120, 120, 25);
        panel.add(addStudentButton);

        // Create the Back button
        JButton backButton = new JButton("Back");
        backButton.setBounds(150, 150, 120, 25);
        panel.add(backButton);

        // Add action listener to the Add Student button
        addStudentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userText.getText();
                String password = new String(passwordText.getPassword());
                String confirmPassword = new String(confirmPasswordText.getPassword());

                if (validateInput(username, password, confirmPassword)) {
                    addStudentToDatabase(username, password, panel);
                }
            }

            private boolean validateInput(String username, String password, String confirmPassword) {
                if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "All fields are required", "Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }

                if (!password.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(panel, "Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }

                return true;
            }
        });

        // Add action listener to the Back button
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                // You can navigate to the previous screen here
            }
        });
    }

    private void addStudentToDatabase(String username, String password, JPanel panel) {
        // Database connection parameters
        String url = "jdbc:mysql://localhost:3306/quiz";
        String user = "root";
        String dbPassword = "himaja2123";

        String query = "INSERT INTO students (username, password) VALUES (?, ?)";

        try (Connection con = DriverManager.getConnection(url, user, dbPassword);
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, username);
            pst.setString(2, password);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(panel, "Student added successfully");

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(panel, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void placeComponentsAdmin(JPanel panel, JFrame frame) {
        panel.setLayout(null);

        // Create JLabel for username
        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(10, 20, 80, 25);
        panel.add(userLabel);

        // Create text field for username
        JTextField userText = new JTextField(20);
        userText.setBounds(150, 20, 165, 25);
        panel.add(userText);

        // Create JLabel for password
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(10, 50, 80, 25);
        panel.add(passwordLabel);

        // Create text field for password
        JPasswordField passwordText = new JPasswordField(20);
        passwordText.setBounds(150, 50, 165, 25);
        panel.add(passwordText);

        // Create JLabel for confirm password
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordLabel.setBounds(10, 80, 120, 25);
        panel.add(confirmPasswordLabel);

        // Create text field for confirm password
        JPasswordField confirmPasswordText = new JPasswordField(20);
        confirmPasswordText.setBounds(150, 80, 165, 25);
        panel.add(confirmPasswordText);

        // Create the Add Admin button
        JButton addAdminButton = new JButton("Add Admin");
        addAdminButton.setBounds(150, 120, 120, 25);
        panel.add(addAdminButton);

        // Create the Back button
        JButton backButton = new JButton("Back");
        backButton.setBounds(150, 150, 120, 25);
        panel.add(backButton);

        // Add action listener to the Add Admin button
        addAdminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userText.getText();
                String password = new String(passwordText.getPassword());
                String confirmPassword = new String(confirmPasswordText.getPassword());

                if (validateInput(username, password, confirmPassword)) {
                    addAdminToDatabase(username, password, panel);
                }
            }

            private boolean validateInput(String username, String password, String confirmPassword) {
                if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "All fields are required", "Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }

                if (!password.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(panel, "Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }

                return true;
            }
        });

        // Add action listener to the Back button
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                // You can navigate to the previous screen here
            }
        });
    }

    private void addAdminToDatabase(String username, String password, JPanel panel) {
        // Database connection parameters
        String url = "jdbc:mysql://localhost:3306/quiz";
        String user = "root";
        String dbPassword = "himaja2123";

        String query = "INSERT INTO login (username, password) VALUES (?,?)";

        try (Connection con = DriverManager.getConnection(url, user, dbPassword);
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, username);
            pst.setString(2, password);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(panel, "Admin added successfully");

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(panel, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }
    int timer;
    public void createExamTimerSetupPage() {
        JFrame frame = new JFrame("Exam Timer Setup");
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);

        JLabel hoursLabel = new JLabel("Hours:");
        hoursLabel.setBounds(30, 30, 80, 25);
        frame.add(hoursLabel);

        JTextField hoursField = new JTextField();
        hoursField.setBounds(120, 30, 50, 25);
        frame.add(hoursField);

        JLabel minutesLabel = new JLabel("Minutes:");
        minutesLabel.setBounds(30, 60, 80, 25);
        frame.add(minutesLabel);

        JTextField minutesField = new JTextField();
        minutesField.setBounds(120, 60, 50, 25);
        frame.add(minutesField);

        JButton startButton = new JButton("Set Timer");
        startButton.setBounds(100, 130, 100, 25);
        frame.add(startButton);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int hours = Integer.parseInt(hoursField.getText());
                int minutes = Integer.parseInt(minutesField.getText());
                
               timer = hours * 3600 + minutes * 60;
                System.out.println("Total seconds: " + timer);
            }
        });

        frame.setVisible(true);
    }

    private static final String DB_URLt = "jdbc:mysql://localhost:3306/timer";
    private static final String DB_USERt = "root";
    private static final String DB_PASSWORDt = "himaja2123";

    public void createExamTimerSetupPage1() {
        JFrame frame = new JFrame("Exam Timer Setup");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);

        JLabel hoursLabel = new JLabel("Hours:");
        hoursLabel.setBounds(120, 50, 80, 25);
        frame.add(hoursLabel);

        JTextField hoursField = new JTextField();
        hoursField.setBounds(200, 50, 50, 25);
        frame.add(hoursField);

        JLabel minutesLabel = new JLabel("Minutes:");
        minutesLabel.setBounds(120, 80, 80, 25);
        frame.add(minutesLabel);

        JTextField minutesField = new JTextField();
        minutesField.setBounds(200, 80, 50, 25);
        frame.add(minutesField);

        JLabel secondsLabel = new JLabel("Seconds:");
        secondsLabel.setBounds(120, 110, 80, 25);
        frame.add(secondsLabel);

        JTextField secondsField = new JTextField();
        secondsField.setBounds(200, 110, 50, 25);
        frame.add(secondsField);

        JButton startButton = new JButton("Set Timer");
        startButton.setBounds(130, 150, 100, 25);
        frame.add(startButton);

        JButton backButtonTimer = new JButton("back");
        backButtonTimer.setBounds(130, 190, 100, 25);
        frame.add(backButtonTimer);

        backButtonTimer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                adminDuties();
            }
        });

    
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int hours = Integer.parseInt(hoursField.getText());
                int minutes = Integer.parseInt(minutesField.getText());
                int seconds = Integer.parseInt(secondsField.getText());

                int totalSeconds = hours * 3600 + minutes * 60 + seconds;

                // Delete previous timer from the database
                deletePreviousTimerFromDatabase();

                // Save the new timer value to the database
                saveTimerToDatabase(totalSeconds);

                JOptionPane.showMessageDialog(frame, "Timer set successfully!");
                // For demonstration, let's just print the new total seconds
                System.out.println("New total seconds: " + totalSeconds);
            }
        });

        frame.setVisible(true);
    }

    private void deletePreviousTimerFromDatabase() {
        try (Connection connection = DriverManager.getConnection(DB_URLt, DB_USERt, DB_PASSWORDt);
             PreparedStatement statement = connection.prepareStatement("DELETE FROM timer")) {
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database errors
        }
    }

    private void saveTimerToDatabase(int totalSeconds) {
        try (Connection connection = DriverManager.getConnection(DB_URLt, DB_USERt, DB_PASSWORDt);
             PreparedStatement statement = connection.prepareStatement("INSERT INTO timer (seconds) VALUES (?)")) {
            statement.setInt(1, totalSeconds);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database errors
        }
    }

   /* public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ExamTimerSetup().createExamTimerSetupPage();
            }
        });
    }/* */
    
}
}
class AddQuestions extends JFrame {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/questions";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "himaja2123";

    private int numberOfQuestions;
    private int currentQuestionIndex;

    public void createInitialPage() {
        setTitle("Question Entry");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
    
        JLabel label = new JLabel("Enter the number of questions:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(label, gbc);
    
        JTextField textField = new JTextField(10);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(textField, gbc);
    
        JButton submitButton = new JButton("Submit");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(submitButton, gbc);
    
        add(panel);
        setVisible(true);
    
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                numberOfQuestions = Integer.parseInt(textField.getText());
                currentQuestionIndex = 0;
                deleteExistingQuestions();
                dispose();
                openQuestionEntryPage();
            }
        });
    }
    
    private void openQuestionEntryPage() {
        JFrame frame = new JFrame("Question Entry Page");
        frame.setSize(700, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel questionLabel = new JLabel("Question:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(questionLabel, gbc);

        JTextField questionField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(questionField, gbc);

        JLabel optionALabel = new JLabel("Option A:");
        gbc.gridy = 1;
        panel.add(optionALabel, gbc);

        JTextField optionAField = new JTextField(30);
        gbc.gridy = 2;
        panel.add(optionAField, gbc);

        JLabel optionBLabel = new JLabel("Option B:");
        gbc.gridy = 3;
        panel.add(optionBLabel, gbc);

        JTextField optionBField = new JTextField(30);
        gbc.gridy = 4;
        panel.add(optionBField, gbc);

        JLabel optionCLabel = new JLabel("Option C:");
        gbc.gridy = 5;
        panel.add(optionCLabel, gbc);

        JTextField optionCField = new JTextField(30);
        gbc.gridy = 6;
        panel.add(optionCField, gbc);

        JLabel optionDLabel = new JLabel("Option D:");
        gbc.gridy = 7;
        panel.add(optionDLabel, gbc);

        JTextField optionDField = new JTextField(30);
        gbc.gridy = 8;
        panel.add(optionDField, gbc);

        JLabel correctOptionLabel = new JLabel("Correct Option:");
        gbc.gridy = 9;
        panel.add(correctOptionLabel, gbc);

        JTextField correctOptionField = new JTextField(30);
        gbc.gridy = 10;
        panel.add(correctOptionField, gbc);

        JButton nextButton = new JButton("Next");
        gbc.gridy = 11;
        panel.add(nextButton, gbc);

        frame.add(panel);
        frame.setVisible(true);

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String question = questionField.getText();
                String optionA = optionAField.getText();
                String optionB = optionBField.getText();
                String optionC = optionCField.getText();
                String optionD = optionDField.getText();
                String correctOption = correctOptionField.getText();

                storeQuestionInDatabase(question, optionA, optionB, optionC, optionD, correctOption);

                currentQuestionIndex++;

                if (currentQuestionIndex < numberOfQuestions) {
                    clearFields(questionField, optionAField, optionBField, optionCField, optionDField, correctOptionField);
                } else {
                    frame.dispose();
                    JOptionPane.showMessageDialog(null, "All questions added successfully!");
                }
            }
        });
    }

    private void clearFields(JTextField... fields) {
        for (JTextField field : fields) {
            field.setText("");
        }
    }

    private void deleteExistingQuestions() {
        try (Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM questions");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting existing questions: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void storeQuestionInDatabase(String question, String optionA, String optionB, String optionC, String optionD, String correctOption) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
             PreparedStatement statement = connection.prepareStatement("INSERT INTO questions (qsn, option_a, option_b, option_c, option_d, correct_option) VALUES (?, ?, ?, ?, ?, ?)")) {
            statement.setString(1, question);
            statement.setString(2, optionA);
            statement.setString(3, optionB);
            statement.setString(4, optionC);
            statement.setString(5, optionD);
            statement.setString(6, correctOption);
            statement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error storing question: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

class StudentDuty extends JFrame{
    public void studentShowLoginDialog() {
        JDialog loginDialog = new JDialog(this, "Login as Student", true);
        loginDialog.setSize(400, 300);
        loginDialog.setLocationRelativeTo(this);
    
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
    
        JLabel adminuserLabel = new JLabel("Username:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(adminuserLabel, gbc);
    
        JTextField userTextField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(userTextField, gbc);
    
        JLabel adminpasswordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(adminpasswordLabel, gbc);
    
        JPasswordField passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(passwordField, gbc);
    
        JButton adminloginButton = new JButton("Login");
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(adminloginButton, gbc);
    
        JLabel messageLabel = new JLabel();
        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(messageLabel, gbc);
    
        adminloginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userTextField.getText();
                String password = new String(passwordField.getPassword());
                
                if (studentValidateLogin(username, password)) {
                    dispose();
                    setupUI();
                } else {
                    showIncorrectLoginDialog();
                }
            }
        });
    
        loginDialog.add(panel);
        loginDialog.setVisible(true);
    }
    
    void showIncorrectLoginDialog() {
        // Create a new JDialog
        JDialog dialog = new JDialog((Frame) null, "Login Error", true);
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(null);
        
        // Create a panel to hold the message and button
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
    
        // Add the message label
        JLabel messageLabel = new JLabel("Incorrect username or password", JLabel.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(messageLabel, gbc);
    
        // Add the OK button
        JButton okButton = new JButton("OK");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(okButton, gbc);
    
        // Add action listener to the OK button
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
    
        // Add the panel to the dialog
        dialog.add(panel);
        
        // Make the dialog visible
        dialog.setVisible(true);
    
    }
    private boolean studentValidateLogin(String username, String password) {
        try (Connection connection = establishConnection()) {
            // Create a statement
            Statement statement = connection.createStatement();
            // Execute a query
            ResultSet resultSet = statement.executeQuery("SELECT * from students");
            while(resultSet.next()) {
                String dbUsername = resultSet.getString("username");
                String dbPassword = resultSet.getString("password");
                if (username.equals(dbUsername) && password.equals(dbPassword)) {
                    return true; // Login successful
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Login failed
    }
    private static final String DB_URL = "jdbc:mysql://localhost:3306/quiz";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "himaja2123";
    private Connection establishConnection() {
        try {
            return DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error connecting to database: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public void setupUI() {
        setTitle("Quiz Instructions");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Instructions Label
        JLabel instructionsLabel = new JLabel("<html><h1>Instructions for the Quiz</h1>"
                + "<ul>"
                + "<li>The quiz consists of multiple-choice questions (MCQs).</li>"
                + "<li>Each question will have four options: A, B, C, and D.</li>"
                + "<li>Only one option is correct for each question.</li>"
                + "<li>You will have a limited time to complete the quiz.</li>"
                + "<li>Ensure you answer all the questions within the given time.</li>"
                + "</ul></html>");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(instructionsLabel, gbc);

        // Checkbox to confirm reading instructions
        JCheckBox readInstructionsCheckBox = new JCheckBox("I have read and understood the instructions");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(readInstructionsCheckBox, gbc);

        // Start Exam Button
        JButton startExamButton = new JButton("Click Here to Start Exam");
        startExamButton.setEnabled(false);  // Disabled initially
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(startExamButton, gbc);

        // Action listener for checkbox
        readInstructionsCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startExamButton.setEnabled(readInstructionsCheckBox.isSelected());
            }
        });

        // Action listener for start button
        startExamButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("exam stated");
                dispose();
                QuizApplication quizApp = new QuizApplication();
            }
        });

        add(panel);
        setVisible(true);

        
    }
    
    
}


class QuizApplication extends JFrame {
    public static final String DB_URL = "jdbc:mysql://localhost:3306/questions";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "himaja2123";

    public static final String DB_URL2 = "jdbc:mysql://localhost:3306/timer";
    public static final String USERNAME2 = "root";
    public static final String PASSWORD2 = "himaja2123";

    private int currentQuestionIndex = 0;
    private int score = 0;
    private List<Question> questions;
    private JLabel questionLabel;
    private JRadioButton[] optionButtons;
    private ButtonGroup buttonGroup;
    private JLabel timerLabel;
    private Timer timer;
   private int timeRemaining; // Total time for quiz in seconds 

    public QuizApplication() {
        setupUI();
    }

    private void setupUI() {
        setTitle("Quiz Page");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    
        // Retrieve timer value from the database
        int timerInSeconds = retrieveTimerFromDatabase();
    
        questions = fetchQuestionsFromDatabase();
    
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
    
        // Timer Label
        timerLabel = new JLabel("Time remaining: " + formatTime(timerInSeconds));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(timerLabel, gbc);
    
        // Question Label
        questionLabel = new JLabel();
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        panel.add(questionLabel, gbc);
    
        // Option Buttons
        optionButtons = new JRadioButton[4];
        buttonGroup = new ButtonGroup();
        for (int i = 0; i < 4; i++) {
            optionButtons[i] = new JRadioButton();
            gbc.gridy = 2 + i;
            gbc.gridwidth = 1;
            panel.add(optionButtons[i], gbc);
            buttonGroup.add(optionButtons[i]);
        }
    
        // Next Button
        JButton nextButton = new JButton("Next");
        gbc.gridy = 6;
        gbc.gridx = 1;
        panel.add(nextButton, gbc);
    
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkAnswer();
                currentQuestionIndex++;
                if (currentQuestionIndex < questions.size()) {
                    displayQuestion();
                } else {
                    timer.stop();
                    showScorePage();
                }
            }
        });
    
        add(panel);
        setVisible(true);
    
        startTimer(timerInSeconds); // Start timer with retrieved value
        displayQuestion();
    }

    public void startTimer(int initialTimeInSeconds) {
        timeRemaining = initialTimeInSeconds;
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeRemaining--;
                timerLabel.setText("Time remaining: " + formatTime(timeRemaining));
    
                if (timeRemaining <= 0) {
                    timer.stop();
                    JOptionPane.showMessageDialog(QuizApplication.this, "Time is up! Your score is: " + score);
                    showScorePage();
                }
            }
        });
        timer.start();
    }
    
    public String formatTime(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
    
    
    public int retrieveTimerFromDatabase() {
        int timerInSeconds = 0;
        try (Connection connection = DriverManager.getConnection(DB_URL2, USERNAME2, PASSWORD2);
             PreparedStatement statement = connection.prepareStatement("SELECT seconds FROM timer")) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                timerInSeconds = resultSet.getInt("seconds");
                System.out.println("Timer value retrieved from database: " + timerInSeconds);
            } else {
                // Default timer value if not found in database
                timerInSeconds = 300; // 5 minutes
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error retrieving timer value from database: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return timerInSeconds;
    }
     

    private List<Question> fetchQuestionsFromDatabase() {
        List<Question> questions = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM questions")) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String questionText = resultSet.getString("qsn");
                String optionA = resultSet.getString("option_a");
                String optionB = resultSet.getString("option_b");
                String optionC = resultSet.getString("option_c");
                String optionD = resultSet.getString("option_d");
                String correctOption = resultSet.getString("correct_option");
                questions.add(new Question(questionText, optionA, optionB, optionC, optionD, correctOption));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching questions: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return questions;
    }

    private void displayQuestion() {
        if (currentQuestionIndex < questions.size()) {
            Question currentQuestion = questions.get(currentQuestionIndex);
            questionLabel.setText("Q" + (currentQuestionIndex + 1) + ": " + currentQuestion.getQuestionText());
            optionButtons[0].setText(currentQuestion.getOptionA());
            optionButtons[1].setText(currentQuestion.getOptionB());
            optionButtons[2].setText(currentQuestion.getOptionC());
            optionButtons[3].setText(currentQuestion.getOptionD());
            buttonGroup.clearSelection();
        }
    }

    private void startTimer() {
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeRemaining--;
                int minutes = timeRemaining / 60;
                int seconds = timeRemaining % 60;
                timerLabel.setText(String.format("Time remaining: %02d:%02d", minutes, seconds));

                if (timeRemaining <= 0) {
                    timer.stop();
                    JOptionPane.showMessageDialog(QuizApplication.this, "Time is up! Your score is: " + score);
                    showScorePage();
                }
            }
        });
        timer.start();
    }

    private void checkAnswer() {
        String selectedOption = null;
        for (int i = 0; i < 4; i++) {
            if (optionButtons[i].isSelected()) {
                selectedOption = optionButtons[i].getText();
                break;
            }
        }
        String correctOption = questions.get(currentQuestionIndex).getCorrectOption();
        if (selectedOption != null && selectedOption.equals(correctOption)) {
            score++;
        }
    }

    private void showScorePage() {
        // Dispose the current quiz page
        dispose();

        // Create a new JFrame for the score page
        JFrame scoreFrame = new JFrame("Quiz Completed");
        scoreFrame.setSize(400, 200);
        scoreFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        scoreFrame.setLocationRelativeTo(null);

        JLabel scoreLabel = new JLabel("Your score is: " + score);
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Serif", Font.BOLD, 20));

        scoreFrame.add(scoreLabel);
        scoreFrame.setVisible(true);
    }

    public static class Question {
        private String questionText;
        private String optionA;
        private String optionB;
        private String optionC;
        private String optionD;
        private String correctOption;

        public Question(String questionText, String optionA, String optionB, String optionC, String optionD, String correctOption) {
            this.questionText = questionText;
            this.optionA = optionA;
            this.optionB = optionB;
            this.optionC = optionC;
            this.optionD = optionD;
            this.correctOption = correctOption;
        }

        public String getQuestionText() {
            return questionText;
        }

        public String getOptionA() {
            return optionA;
        }

        public String getOptionB() {
            return optionB;
        }

        public String getOptionC() {
            return optionC;
        }

        public String getOptionD() {
            return optionD;
        }

        public String getCorrectOption() {
            return correctOption;
        }
    }
}
