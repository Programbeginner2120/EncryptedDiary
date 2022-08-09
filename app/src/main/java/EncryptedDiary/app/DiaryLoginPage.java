package EncryptedDiary.app;

//Importing all necessary Packages
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.function.Supplier;

public class DiaryLoginPage extends JFrame implements ActionListener{

    private SQLDatabaseConnection sqlConn = new SQLDatabaseConnection();

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
        addActionEvents();

        constructLoginPage();
        openNeededResources();
    }

    /**
     * Private method used to open needed resources for the running of the DiaryLoginPage
     */
    private void openNeededResources(){ this.sqlConn.openConnectionObject(); }

    /**
     * Private method used to close all resources open after this page is disposed
     */
    public void freeOpenResources() {
        this.sqlConn.openConnectionObject();
        this.sqlConn = null;
    }

    /**
     * Private method used to construct the DiaryLoginPage login page
     */
    private void constructLoginPage(){
        this.setTitle("User Login");
        //this.setVisible(true); // able to actually see the login page frame
        this.setBounds(10,10,370,600); // setting x and y coordinates as well as width and height
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setResizable(false); // determines whether user can reshape frame
    }

    /**
     * Private method used to deconstruct the DiaryLoginPage login page, freeing up memory of the application
     */
    private void deconstructLoginPage(){
        this.freeOpenResources();
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
    private void addActionEvents() {
        // Adding action listener to components
        loginButton.addActionListener(this);
        resetButton.addActionListener(this);
        showPassword.addActionListener(this);
    }

    //*** END OF FRONT END DESIGN ***

    /**
     * Private static method to validate the username passed into the DiaryLoginPage. If username is valid, method
     * returns true, else returns false. If an exception occurs, a RuntimeException is thrown.
     * @param username - String representing the inputted username by the user
     * @return - Returns true for valid username, false for invalid username
     */
    private boolean validateUsername(String username){
        String query = String.format("SELECT * FROM Users WHERE username = '%s'", username);
        List<Object []> results = this.sqlConn.executeSQLQuery(query);
        return results.size() > 0;
    }

    /**
     * Private static method to validate the password passed into the DiaryLoginPage. If password is valid, method
     * returns true, else returns false. If an exception occurs, a RuntimeException is thrown.
     * @param password - String representing the inputted password by the user
     * @return Returns true for valid password, false for invalid password
     */
    private boolean validatePassword(String password){
        String query = String.format("SELECT * FROM Users WHERE passHash = '%s'",
                Integer.toString(password.hashCode()));
        List<Object []> results = this.sqlConn.executeSQLQuery(query);
        return results.size() > 0;
    }

    /**
     * Private method used to validate username and password credentials on press of loginButton
     * @param username - Username inputted by user
     * @param password - Password inputted by user
     * @return userInfo - ArrayList of object arrays that contain results from SQL query
     */
    private List<Object []> onLoginButtonPress(String username, String password) {
        boolean validUsername = validateUsername(username);
        boolean validPassHash = validatePassword(password);
        if (validUsername && validPassHash) {
            List<Object []> userInfo = this.sqlConn.executeSQLQuery(String.format(
                    "SELECT userID, username FROM Users WHERE username = '%s'", username));
            return userInfo;
        }
        else {
            return null;
        }
    }

    /**
     * Private method used to clear all text in login text areas when resetButton is pressed
     */
    private void onResetButtonPress() {
        userTextField.setText("");
        passwordField.setText("");
    }

    /**
     * Private method used to show or hide the password text area when showPassword is pressed
     */
    private void onShowPasswordButtonPress() {
        if (showPassword.isSelected())
            passwordField.setEchoChar((char) 0);
        else
            passwordField.setEchoChar('•');
    }

    /**
     * Abstract method implemented from ActionListener interface that responds to different buttons in DiaryLoginPage
     * being pressed
     * @param e - The ActionEvent triggered via the press of a button
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // Login button press response
        if (e.getSource() == loginButton){
            String userText = userTextField.getText();
            String passwordText = String.valueOf(passwordField.getPassword());

            List<Object []> userInfo = this.onLoginButtonPress(userText, passwordText);
            if (userInfo != null) {
                JOptionPane.showMessageDialog(this, "Login Successful");
                this.deconstructLoginPage();
                moveToNewDiaryEditorPage(new User((Integer) userInfo.get(0)[0],
                        (String) userInfo.get(0)[1]));
//                moveToNewPage(new User(Integer.parseInt(userInfo.get(0)[0]), userInfo.get(0)[1]));
            }
            else if (userInfo == null){
                JOptionPane.showMessageDialog(this, "Invalid Username or Password");
            }
            else{
                // TODO: PUT ANOTHER STATEMENT HERE WHEN CLEANING UP CODE TO HANDLE NON-CREDENTIAL RELATED ERRORS
            }
        }
        else if (e.getSource() == resetButton)
            onResetButtonPress(); // Reset button functionality
        else
            onShowPasswordButtonPress();
    }

    private void moveToNewPage(Object o, User user) {
    }

    /**
     * Private method used to move from the DiaryLoginPage to the DiaryEditorPage. Requires a validated User
     * @param user - An instance of the User class which contains logged-in user's information
     */
    private static <T> void moveToNewDiaryEditorPage(User user){
        DiaryEditorPage editorPage = new DiaryEditorPage(user);
    }
}

//TODO: IMPLEMENT JUNIT TESTING BEFORE PROCEEDING WITH MORE CODE SO THAT I KNOW THE FOUNDATIONAL FUNCTIONALITIES
// WORK BEFORE MOVING ON

