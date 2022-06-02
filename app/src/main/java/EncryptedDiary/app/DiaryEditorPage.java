package EncryptedDiary.app;

import javax.swing.*;
import java.awt.event.*;
import javax.swing.plaf.metal.*;
import java.util.ArrayList;
import EncryptedDiary.app.CustomComponents.PaginatedList;

public class DiaryEditorPage extends JFrame implements ActionListener{

    private User currentUser;
    private SQLDatabaseConnection sqlConn = new SQLDatabaseConnection();

    private JTextArea textComponent; // Text component
    private JFrame editorFrame; // Frame

    DiaryEditorPage(User currentUser) {
        this.currentUser = currentUser;

        this.sqlConn.openConnection();

        this.editorFrame = new JFrame("Diary Editor"); // creating a new JFrame

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel"); // setting metal look and feel

            MetalLookAndFeel.setCurrentTheme(new OceanTheme()); // setting theme to ocean
        }
        catch (Exception ex){} // Just failing silently

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


//    public void onSave(){
//        JFileChooser j = new JFileChooser("f:"); // Create an object of JFileChooser class
//
//        int r = j.showSaveDialog(null); // Invoke the showSaveDialog function to show the save dialog
//
//        if (r == JFileChooser.APPROVE_OPTION) {
//
//            // Set the label to the path of the selected directory
//            File fi = new File(j.getSelectedFile().getAbsolutePath());
//
//            try {
//                // Create a file writer
//                FileWriter wr = new FileWriter(fi, false);
//
//                // Create buffered writer to write
//                BufferedWriter w = new BufferedWriter(wr);
//
//                w.write(t.getText());
//
//                w.flush();
//                w.close();
//            }
//
//            catch (Exception evt) {
//                JOptionPane.showMessageDialog(f, evt.getMessage());
//            }
//        }
//
//        else
//            JOptionPane.showMessageDialog(f, "the user cancelled the operation");
//    }

    /**
     *
     * @return
     */
    private boolean updateDocument(){ // Will try to update the existing document
        return false;
    }

    /**
     *
     * @return
     */
    private boolean createDocument(){ // Will try to create a new document
        return false;
    }

    /**
     *
     */
    public void onSave(){ // TODO: Keep writing logic for saving BLOB file to database
        try{
            updateDocument();
        }
        catch (Exception ex){
            createDocument();
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

//    public void onOpen(){ // may need for later, for now just commenting out
//        // Create an object of JFileChooser class
//        JFileChooser j = new JFileChooser("f:");
//
//        // Invoke the showsOpenDialog function to show the save dialog
//        int r = j.showOpenDialog(null);
//
//        // If the user selects a file
//        if (r == JFileChooser.APPROVE_OPTION) {
//            try {
//                Path path = Paths.get(j.getSelectedFile().getAbsolutePath());
//                List<String> lines = Files.readAllLines(path);
//
//                // Set the text
//                String res = String.join("\n", lines);
//                t.setText(res);
//            }
//
//            catch(Exception evt){
//                JOptionPane.showMessageDialog(f, evt.getMessage());
//            }
//        }
//
//        // If the user cancelled the operation
//        else
//            JOptionPane.showMessageDialog(f, "the user cancelled the operation");
//    }

    /**
     * Private method used to retrieve the list of results when the query in executed in SQL
     * @return results - the results of the SQL query executed
     */
    private ArrayList<Object []> getUserFiles() {
        String query = String.format("SELECT userDocumentName, userDocumentContents FROM userDocuments " +
                "WHERE userDocuments.userID = %d", this.currentUser.getUserID());
        ArrayList<Object []> results = this.sqlConn.executeSQLQuery(query);
        return results;
    }

    /**
     * Method used display the names of files that the user has created
     */
    public void onOpen(){
        ArrayList<Object []> userInfo = this.getUserFiles();
        PaginatedList myList = new PaginatedList(new JList(new String[] {"Monday", "Tuesday"}), 10);
        JFrame frame = new JFrame("frame");
        frame.setSize(400,400);
        frame.add(myList);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    /**
     * Wrapper method used to create a new document
     */
    public void onNew(){
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
