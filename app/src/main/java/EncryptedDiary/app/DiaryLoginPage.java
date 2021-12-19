package EncryptedDiary.app;

//Importing all necessary Packages
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DiaryLoginPage extends JFrame implements ActionListener{

    private Container container;
    private JLabel userLabel;
    private JLabel passwordLabel;
    private JTextField userTextField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton resetButton;
    private JCheckBox showPassword;

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
    }

    public void setLayoutManager() {
        //Setting layout manager of container to null
        container.setLayout(null);
    }

    public void setLocationAndSize() {
        // Setting location and size of each components using setBounds() method
        userLabel.setBounds(50,150,100,30);
        passwordLabel.setBounds(50,220,100,30);
        userTextField.setBounds(150,150,150,30);
        passwordField.setBounds(150,220,150,30);
        showPassword.setBounds(150,250,150,30);
        loginButton.setBounds(50,300,100,30);
        resetButton.setBounds(200,300,100,30);
    }

    public void addComponentsToContainer() {
        // Adding each component to the container
        this.container.add(userLabel);
        this.container.add(passwordLabel);
        this.container.add(userTextField);
        this.container.add(passwordField);
        this.container.add(showPassword);
        this.container.add(loginButton);
        this.container.add(resetButton);
    }

    public void addActionEvent() {
        // Adding action listener to components
        loginButton.addActionListener(this);
        resetButton.addActionListener(this);
        showPassword.addActionListener(this);
    }

    private static boolean onLoginButtonPress(String username, String password) {
        if (username.equalsIgnoreCase("matthew") && password.equalsIgnoreCase("killeen"))
            return true;
        return false;
    }

    private void onResetButtonPress() {
        userTextField.setText("");
        passwordField.setText("");
    }

    private void onShowPasswordButtonPress() {
        if (showPassword.isSelected())
            passwordField.setEchoChar((char) 0);
        else
            passwordField.setEchoChar('*');
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Login button press response
        if (e.getSource() == loginButton){
            String userText = userTextField.getText();
            String passwordText = passwordField.getText();

            boolean validLogin = onLoginButtonPress(userText, passwordText);
            if (validLogin)
                JOptionPane.showMessageDialog(this, "Login Successful");
            else
                JOptionPane.showMessageDialog(this, "Invalid Username or Password");
        }

        else if (e.getSource() == resetButton)
            onResetButtonPress(); // Reset button functionality

        else
            onShowPasswordButtonPress();
    }

    public static void main(String [] args){
        DiaryLoginPage myPage = new DiaryLoginPage(); // creating new login page object
        myPage.setTitle("User Login");
        myPage.setVisible(true); // able to actually see the login page frame
        myPage.setBounds(10,10,370,600); // setting x and y coordinates as well as width and height
        myPage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myPage.setResizable(true); // determines whether user can reshape frame
    }
}