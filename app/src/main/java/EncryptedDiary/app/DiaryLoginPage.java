package EncryptedDiary.app;

//Importing all necessary Packages
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class DiaryLoginPage extends JFrame implements ActionListener{

    private Container container;
    private JLabel userLabel;
    private JLabel passwordLabel;
    private JTextField userTextField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton resetButton;
    private JCheckBox showPassword;

    /**
     * Constructor for the DiaryLoginPage class, sets up layout and functionality of page via calling private
     * methods
     */
    public DiaryLoginPage() {
         this.container = getContentPane();
         this.userLabel = new JLabel("USERNAME");
         this.passwordLabel = new JLabel("PASSWORD");
         this.userTextField = new JTextField();
         this.passwordField = new JPasswordField();
         this.loginButton = new JButton("LOGIN");
         this.resetButton = new JButton("RESET");
         this.showPassword = new JCheckBox("Show Password");

        //Calling setLayoutMethod inside constructor
        setLayoutManager();
        setLocationAndSize();
        addComponentsToContainer();
        addActionEvent();

        constructLoginPage();
    }

    /**
     * Private method used to construct the DiaryLoginPage login page
     */
    private void constructLoginPage(){
        this.setTitle("User Login");
        this.setVisible(true); // able to actually see the login page frame
        this.setBounds(10,10,370,600); // setting x and y coordinates as well as width and height
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setResizable(false); // determines whether user can reshape frame
    }

    /**
     * Private method used to deconstruct the DiaryLoginPage login page, freeing up memory of the application
     */
    private void deconstructLoginPage(){
        this.dispose();
    }

    /**
     * Private wrapper method for setting the layout manager of the container of the DiaryLoginPage
     */
    private void setLayoutManager() {
        //Setting layout manager of container to null
        container.setLayout(null);
    }

    /**
     * Private method to set the sizes of the buttons and labels present in the DiaryLoginPage
     */
    private void setLocationAndSize() {
        // Setting location and size of each components using setBounds() method
        userLabel.setBounds(50,150,100,30);
        passwordLabel.setBounds(50,220,100,30);
        userTextField.setBounds(150,150,150,30);
        passwordField.setBounds(150,220,150,30);
        showPassword.setBounds(150,250,150,30);
        loginButton.setBounds(50,300,100,30);
        resetButton.setBounds(200,300,100,30);
    }

    /**
     * Private method for adding the different components to the component of the DiaryLoginPage
     */
    private void addComponentsToContainer() {
        // Adding each component to the container
        this.container.add(userLabel);
        this.container.add(passwordLabel);
        this.container.add(userTextField);
        this.container.add(passwordField);
        this.container.add(showPassword);
        this.container.add(loginButton);
        this.container.add(resetButton);
    }

    /**
     * Private method for adding action listeners to different buttons of the DiaryLoginPage, enabling
     * the application to listen and respond to user input
     */
    private void addActionEvent() {
        // Adding action listener to components
        loginButton.addActionListener(this);
        resetButton.addActionListener(this);
        showPassword.addActionListener(this);
    }

    //*** END OF FRONT END DESIGN ***

    /**
     * Private static method to validate the username passed into the DiaryLoginPage. If username is valid, method
     * returns true, else returns false. If an exception occurs, a RuntimeException is thrown.
     * @param conn - A Connection object enabling interaction with the SQL Server database
     * @param username - String representing the inputted username by the user
     * @return - Returns true for valid username, false for invalid username
     */
    private static boolean validateUsername(Connection conn, String username){
        PreparedStatement ps;
        try{
            ps = conn.prepareStatement("SELECT * FROM Users WHERE username = ?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
        catch (SQLException ex){
            JOptionPane.showMessageDialog(null, "Something went wrong when trying " +
                    "to connect to the network. Please try again later.");
            throw new RuntimeException();
        }
    }

    /**
     * Private static method to validate the password passed into the DiaryLoginPage. If password is valid, method
     * returns true, else returns false. If an exception occurs, a RuntimeException is thrown.
     * @param conn - A Connection object enabling interaction with the SQL Server database
     * @param password - String representing the inputted password by the user
     * @return Returns true for valid password, false for invalid password
     */
    private static boolean validatePassword(Connection conn, String password){
        PreparedStatement ps;
        try{
            ps = conn.prepareStatement("SELECT * FROM Users WHERE passHash = ?");
            ps.setString(1, Integer.toString(password.hashCode()));
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
        catch (Exception ex){
            JOptionPane.showMessageDialog(null, "Something went wrong when trying " +
                    "to connect to the network. Please try again later.");
            throw new RuntimeException();
        }
    }

    /**
     *
     * @param username
     * @param password
     * @return
     */
    private String onLoginButtonPress(String username, String password) {
        Connection conn = SQLDatabaseConnection.openConnection();
        try{
            boolean validUsername = validateUsername(conn, username);
            boolean validPassHash = validatePassword(conn, password);
            if (validUsername && validPassHash) {
                SQLDatabaseConnection.closeConnection(conn);
                return "SUCCESS";
            }
            return "FAILURE";
        }

        catch (Exception ex){
            return "ERROR";
        }
    }

    private void onResetButtonPress() {
        userTextField.setText("");
        passwordField.setText("");
    }

    private void onShowPasswordButtonPress() {
        if (showPassword.isSelected())
            passwordField.setEchoChar((char) 0);
        else
            passwordField.setEchoChar('•');
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Login button press response
        if (e.getSource() == loginButton){
            String userText = userTextField.getText();
            String passwordText = String.valueOf(passwordField.getPassword());

            String loginStringResult = this.onLoginButtonPress(userText, passwordText);
            if (loginStringResult.equals("SUCCESS")) {
                JOptionPane.showMessageDialog(this, "Login Successful");
                this.deconstructLoginPage();
                DiaryEditorPage editorPage = new DiaryEditorPage();
            }
            else if (loginStringResult.equals("FAILURE"))
                JOptionPane.showMessageDialog(this, "Invalid Username or Password");
//            else if (loginStringResult.equals("ERROR"))
//                ;
        }

        else if (e.getSource() == resetButton)
            onResetButtonPress(); // Reset button functionality

        else
            onShowPasswordButtonPress();
    }
}

//TODO: IMPLEMENT JUNIT TESTING BEFORE PROCEEDING WITH MORE CODE SO THAT I KNOW THE FOUNDATIONAL FUNCTIONALITIES
// WORK BEFORE MOVING ON

//TODO: FIGURE OUT WHY YOU CAN'T REPEATEDLY OPEN AND CLOSE NEW CONNECTIONS TO THE DATABASE WHEN ATTEMPTING TO LOGIN

