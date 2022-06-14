package EncryptedDiary.app;

import javax.swing.*;
import java.awt.event.*;
import javax.swing.plaf.metal.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import EncryptedDiary.app.CustomComponents.PaginatedList;

public class DiaryEditorPage extends JFrame implements ActionListener{

    private User currentUser;
    private SQLDatabaseConnection sqlConn = new SQLDatabaseConnection();

    private JTextArea textComponent; // Text component
    private JFrame editorFrame; // Frame

    private JFrame listFrame;

    private String currentDocumentName = "Untitled";

    DiaryEditorPage(User currentUser) {
        this.currentUser = currentUser;

        this.sqlConn.openConnection();

        this.editorFrame = new JFrame("Diary Editor"); // creating a new JFrame

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel"); // setting metal look and feel

            MetalLookAndFeel.setCurrentTheme(new OceanTheme()); // setting theme to ocean
        }
        catch (Exception ignored){} // Just failing silently

        this.textComponent = new JTextArea(); // Text component

        JMenuBar mb = new JMenuBar(); // Creating a menu bar

        JMenu m1 = new JMenu("File"); // Create a menu for menu

        JMenuItem menuItem1 = new JMenuItem("New"); // Creating menu items for text editor
        JMenuItem menuItem2 = new JMenuItem("Open");
        JMenuItem menuItem3 = new JMenuItem("Save");
        JMenuItem menuItem4 = new JMenuItem("Print");

        menuItem1.addActionListener(this); // Adding action listeners for menu items
        menuItem2.addActionListener(this);
        menuItem3.addActionListener(this);
        menuItem4.addActionListener(this);

        m1.add(menuItem1); // Appending menu items to end of given menu m1
        m1.add(menuItem2);
        m1.add(menuItem3);
        m1.add(menuItem4);

        JMenu m2 = new JMenu("Edit");

        JMenuItem menuItem5 = new JMenuItem("cut"); // Create menu items
        JMenuItem menuItem6 = new JMenuItem("copy");
        JMenuItem menuItem7 = new JMenuItem("paste");

        menuItem5.addActionListener(this); // Add action listener
        menuItem6.addActionListener(this);
        menuItem7.addActionListener(this);

        m2.add(menuItem5);
        m2.add(menuItem6);
        m2.add(menuItem7);

        JMenuItem mc = new JMenuItem("close");

        mc.addActionListener(this);

        mb.add(m1);
        mb.add(m2);
        mb.add(mc);

        this.editorFrame.setJMenuBar(mb);
        this.editorFrame.add(this.textComponent);
        this.editorFrame.setSize(500, 500);

        this.editorFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.editorFrame.setVisible(true);
    }


    /**
     * Abstract method defined by the ActionListener interface that defines responses to different buttons in the
     * DiaryEditorPage being pressed.
     * @param e - The ActionEvent triggered via the press of a button
     */
    @Override
    // If a button is pressed this method is called
    public void actionPerformed(ActionEvent e) {
        String s = e.getActionCommand();

        if (s.equals("cut"))
            onCut();

        else if (s.equals("copy"))
            onCopy();

        else if (s.equals("paste"))
            onPaste();

        else if (s.equals("Save"))
            onSave();

        else if (s.equals("Print"))
            onPrint();

        else if (s.equals("Open"))
            onOpen();

        else if (s.equals("New"))
            onNew();

        else if (s.equals("close"))
            onClose();
    }


    /**
     * Wrapper method to used "cut" the text in the textComponent in the JTextArea
     */
    public void onCut(){
        this.textComponent.cut();
    }


    /**
     * Wrapper method used to "copy" the text in the textComponent in the JTextArea
     */
    public void onCopy(){
        this.textComponent.copy();
    }

    /**
     * Wrapper method used to "paste" given text in the textComponent in the JTextArea
     */
    public void onPaste(){
        this.textComponent.paste();
    }

    /**
     *
     * @return
     */
    private void updateDocument(String documentName) throws Exception{ // Will try to update the existing document
        String query = String.format("UPDATE userDocuments SET userDocumentContents = 'Contents Updated' WHERE userID" +
                " = %d AND userDocumentName = '%s'", this.currentUser.getUserID(), documentName);
        boolean executedSuccessfully = this.sqlConn.executeSQLUpdate(query);
        if (executedSuccessfully){
            JOptionPane.showMessageDialog(this, "Success in" +
                    " updating this document.");
        }
        if (!executedSuccessfully){
            JOptionPane.showMessageDialog(this, "There was a problem in" +
                    " updating this document.");
        }
    }

    /**
     *
     * @return
     */
    private void createDocument(String documentName) throws Exception{ // Will try to create a new document
        String query = String.format("INSERT INTO userDocuments (userDocumentName, userDocumentContents," +
                " encryptionMethod, decryptionMethod, userID) VALUES ('%s', '%s', '%s', '%s', %d)", documentName,
                "Contents", "Encrypt", "Decrypt", this.currentUser.getUserID());
        boolean executedSuccessfully = this.sqlConn.executeSQLUpdate(query);
        if (executedSuccessfully){
            JOptionPane.showMessageDialog(this, "Success in" +
                    " creating this document.");
        }
        if (!executedSuccessfully){
            JOptionPane.showMessageDialog(this, "There was a problem in" +
                    " creating this document.");
        }
    }

    /**
     *
     */
    public void onSave(){ // TODO: Keep writing logic for saving BLOB file to database
        if (this.currentDocumentName.equals("Untitled"))
            this.currentDocumentName = JOptionPane.showInputDialog(this.editorFrame, "Enter name of file");

        String query = String.format("SELECT userDocumentName FROM userDocuments where userID = %d AND " +
                "userDocumentName = '%s'", this.currentUser.getUserID(), this.currentDocumentName);
        ArrayList<Object []> results = this.sqlConn.executeSQLQuery(query);
        try {
            if (results.size() > 0){
                updateDocument(this.currentDocumentName);
            }
            else{
                createDocument(this.currentDocumentName);
            }
        }
        catch(Exception ex){
            JOptionPane.showMessageDialog(this, "An error occurred while trying to conduct" +
                    " this operation.");
        }
    }

    /**
     * Wrapper method used to print the text in the textComponent of the JTextArea
     */
    public void onPrint(){
        try {
            // print the file
            this.textComponent.print();
        }
        catch (Exception evt) {
            JOptionPane.showMessageDialog(this.editorFrame, evt.getMessage());
        }
    }

    /**
     * Private method used to retrieve the list of results when the query in executed in SQL
     * @return results - the results of the SQL query executed
     */
    private ArrayList<Object []> getUserFileNames() {
        String query = String.format("SELECT userDocumentName, userDocumentContents FROM userDocuments " +
                "WHERE userDocuments.userID = %d", this.currentUser.getUserID());
        ArrayList<Object []> results = this.sqlConn.executeSQLQuery(query);
        return results;
    }

    /**
     * Private method used to retrieve the list of results when the query in executed in SQL
     * @return results - the results of the SQL query executed
     */
    private ArrayList<Object []> getUserFileContents() {
        String query = String.format("SELECT userDocumentContents FROM userDocuments WHERE userDocuments.userID = %d",
                this.currentUser.getUserID());
        ArrayList<Object []> results = this.sqlConn.executeSQLQuery(query);
        return results;
    }

    /**
     * Method used display the names of files that the user has created
     */
    public void onOpen(){
        ArrayList<Object []> queryResults = this.getUserFileNames();

        String [] fileNames = new String[queryResults.size()];
        for (int i = 0; i < queryResults.size(); i++){
            fileNames[i] = (String) queryResults.get(i)[0];
        }

        PaginatedList myList = new PaginatedList(this, new JList(fileNames), 10);
        this.listFrame = new JFrame("frame");
        listFrame.setSize(400,400);
        listFrame.add(myList);
        listFrame.setVisible(true);
        listFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    public void disposeListFrame(){
        this.listFrame.setVisible(false);
    }

    /**
     * Wrapper method used to create a new document
     */
    public void onNew(){
        this.currentDocumentName = "Untitled";
        this.textComponent.setText("");
    }

    /**
     * Private method used to close currently open resources
     */
    private void closeOpenResources(){
        this.sqlConn.closeConnection();
    }

    /**
     * Method used to dispose of DiaryEditorPage & calls closeOpenResources(), which closes open resources
     */
    public void onClose(){
        closeOpenResources();
        this.editorFrame.setVisible(false);
        System.exit(0);
    }

    /**
     *
     */
    public static void EncryptPageContents() {
        ;
    }

    /**
     *
     */
    public static void DecryptPageContents() {
        ;
    }
}
