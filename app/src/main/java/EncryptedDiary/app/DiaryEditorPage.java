package EncryptedDiary.app;

import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class DiaryEditorPage extends JFrame implements ActionListener{

    private User currentUser;
    private SQLDatabaseConnection sqlConn = new SQLDatabaseConnection();

    private DiaryCipher diaryCipher;

    private String currentDocumentName = "Untitled";
    private int currentDocumentIndex = -1;

    private JTextArea textComponent; // Text component

    private void instantiateDiaryCipher(){
        try {
            this.diaryCipher = new DiaryCipher();
        }
        catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public DiaryEditorPage(User currentUser) {
        super("Diary Editor Frame");

        this.currentUser = currentUser;

        this.sqlConn.openConnectionObject();

        this.instantiateDiaryCipher(); // instantiate diary cipher object

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

        this.setJMenuBar(mb);
        this.add(this.textComponent);
        this.setSize(500, 500);

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.setVisible(true);
    }

    public void setCurrentDocumentName(String currentDocumentName){
        this.currentDocumentName = currentDocumentName;
    }

    public void setCurrentDocumentIndex(int currentDocumentIndex){
        this.currentDocumentIndex = currentDocumentIndex;
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
            this.onCut();

        else if (s.equals("copy"))
            this.onCopy();

        else if (s.equals("paste"))
            this.onPaste();

        else if (s.equals("Save"))
            this.onSave();

        else if (s.equals("Print"))
            this.onPrint();

        else if (s.equals("Open"))
            this.onOpen();

        else if (s.equals("New"))
            this.onNew();

        else if (s.equals("close"))
            this.onClose();
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
     */
    public void onSave(){ // TODO: Keep writing logic for saving BLOB file to database
        if (this.currentDocumentName.equals("Untitled"))
            this.currentDocumentName = JOptionPane.showInputDialog(this, "Enter name of file");

        String query = String.format("SELECT userDocumentName FROM userDocuments where userID = %d AND " +
                "userDocumentName = '%s'", this.currentUser.getUserID(), this.currentDocumentName);
        List<Object []> results = this.sqlConn.executeSQLQuery(query);
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
            JOptionPane.showMessageDialog(this, evt.getMessage());
        }
    }

    void disposeListFrame(JFrame paginatedListFrame){
        paginatedListFrame.dispose();
    }

    private void instantiatePaginatedList(String [] fileNames){
        JFrame paginatedListFrame = new JFrame("File Selection Frame");
        JPanel paginatedList = new PaginatedList(paginatedListFrame, this, new JList(fileNames),
                10);
        paginatedListFrame.setSize(400,400);
        paginatedListFrame.add(paginatedList);
        paginatedListFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        paginatedListFrame.setVisible(true);
    }

//    private void writeContentsToTextArea(){
//        List<String[]> fileContents = this.getUserFileContents();
//        this.textComponent.setText(Arrays.stream(fileContents.get(0)).collect(Collectors.joining()));
//    }

    private void writeSecretKeyToDatabase(){

    }

    //TODO: IT SEEMS THAT ENCRYPTED CONTENTS GET MODIFIED IN THE PROCESS OF GOING FROM JAVA TO SQL AND / OR SQL TO JAVA,
    // NEED TO FIGURE OUT HOW TO HANDLE THIS ISSUE

    public void processFileOpen(String chosenDocumentName){
        if (!this.currentDocumentName.equals("Untitled")){
            String query = "SELECT userDocumentContents FROM userDocuments WHERE userId = ? AND userDocumentName = ?";
            byte [] contentBytes = (byte []) this.sqlConn.executeSQLQuery(query, this.currentUser.getUserID(),
                    chosenDocumentName).get(0)[0];


            query = "SELECT secretKey FROM userDocuments WHERE userId = ? AND userDocumentName = ?";
            byte [] secretKeyBytes = (byte []) this.sqlConn.executeSQLQuery(query, this.currentUser.getUserID(),
                    chosenDocumentName).get(0)[0];


            this.diaryCipher.setMyKey(new SecretKeySpec(secretKeyBytes, 0, secretKeyBytes.length,
                    "AES"));

            String fileContents = this.diaryCipher.decryptString(contentBytes);
            this.textComponent.setText(fileContents);
        }
    }

    /**
     * Method used display the names of files that the user has created
     */
    public void onOpen(){
        List<Object []> queryResults = this.getUserFileNames();

        String [] fileNames = new String[queryResults.size()];
        for (int i = 0; i < queryResults.size(); i++){
            fileNames[i] = (String) queryResults.get(i)[0];
        }

        this.instantiatePaginatedList(fileNames);


        // TODO: IMPLEMENT SOME WAY TO TRANSFER FILE CONTENTS
    }

    /**
     * Wrapper method used to create a new document
     */
    public void onNew(){
        this.currentDocumentName = "Untitled";
        this.textComponent.setText("");
    }

    /**
     * Method used to dispose of DiaryEditorPage & calls closeOpenResources(), which closes open resources
     */
    public void onClose(){
        freeOpenResources();
        this.dispose();
    }

    /**
     *
     * @return
     */
    private void updateDocument(String documentName) throws Exception{ // Will try to update the existing document

        String fileContents = this.textComponent.getText();
        byte [] encryptedContents = this.diaryCipher.encryptText(fileContents);
        byte [] secretKeyBytes = this.diaryCipher.getMyKey().getEncoded();

        String query = String.format("UPDATE userDocuments SET userDocumentContents = ?, secretKey = ? WHERE userID" +
                " = %d AND userDocumentName = '%s'", this.currentUser.getUserID(), documentName);

        boolean executedSuccessfully = this.sqlConn.executeSQLUpdate(query, encryptedContents, secretKeyBytes);

        if (executedSuccessfully){
            JOptionPane.showMessageDialog(this, "Success in" +
                    " updating this document.");
        }
        else {
            JOptionPane.showMessageDialog(this, "There was a problem in" +
                    " updating this document.");
        }
    }

    /**
     *
     * @return
     */
    private void createDocument(String documentName) throws Exception{ // Will try to create a new document

        String fileContents = this.textComponent.getText();
        byte [] encryptedContents = this.diaryCipher.encryptText(fileContents);
        byte [] secretKeyBytes = this.diaryCipher.getMyKey().getEncoded();

        String query = String.format("INSERT INTO userDocuments (userDocumentName, userDocumentContents, secretKey," +
                        " userID) VALUES ('%s', ?, ?, %d)", documentName, this.currentUser.getUserID());

        boolean executedSuccessfully = this.sqlConn.executeSQLUpdate(query, encryptedContents,
                secretKeyBytes);
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
     * Private method used to retrieve the list of results when the query in executed in SQL
     * @return results - the results of the SQL query executed
     */
    private List<Object []> getUserFileNames() {
        String query = String.format("SELECT userDocumentName, userDocumentContents FROM userDocuments " +
                "WHERE userDocuments.userID = %d", this.currentUser.getUserID());
        List<Object []> results = this.sqlConn.executeSQLQuery(query);
        return results;
    }

    /**
     * Private method used to retrieve the list of results when the query in executed in SQL
     * @return results - the results of the SQL query executed
     */
    private List<Object []> getUserFileContents() {
        String query = String.format("SELECT userDocumentContents FROM userDocuments WHERE userDocuments.userID = %d",
                this.currentUser.getUserID());
        List<Object []> results = this.sqlConn.executeSQLQuery(query);
        return results;
    }

    public void freeOpenResources() {
        this.sqlConn.openConnectionObject();
        this.sqlConn = null;
    }

    private void deconstructLoginPage(){
        this.freeOpenResources();
        this.dispose();
    }
}

//TODO: CLEAN UP CONTENTS, MAKE STRUCTURE MORE MODULAR
