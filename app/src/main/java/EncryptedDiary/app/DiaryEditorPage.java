package EncryptedDiary.app;

import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.awt.event.*;
import javax.swing.plaf.metal.*;
import javax.swing.text.*;

public class DiaryEditorPage extends JFrame implements ActionListener{

    JTextArea t; // Text component
    JFrame f; // Frame

    DiaryEditorPage() {
        f = new JFrame("Diary Editor"); // creating a new JFrame

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel"); // setting metal look and feel

            MetalLookAndFeel.setCurrentTheme(new OceanTheme()); // setting theme to ocean
        }
        catch (Exception ex){} // Just failing silently

        t = new JTextArea(); // Text component

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

        f.setJMenuBar(mb);
        f.add(t);
        f.setSize(500, 500);
        f.show();
    }


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


    public void onCut(){
        t.cut();
    }


    public void onCopy(){
        t.copy();
    }


    public void onPaste(){
        t.paste();
    }


    public void onSave(){
        JFileChooser j = new JFileChooser("f:"); // Create an object of JFileChooser class

        int r = j.showSaveDialog(null); // Invoke the showSaveDialog function to show the save dialog

        if (r == JFileChooser.APPROVE_OPTION) {

            // Set the label to the path of the selected directory
            File fi = new File(j.getSelectedFile().getAbsolutePath());

            try {
                // Create a file writer
                FileWriter wr = new FileWriter(fi, false);

                // Create buffered writer to write
                BufferedWriter w = new BufferedWriter(wr);

                w.write(t.getText());

                w.flush();
                w.close();
            }

            catch (Exception evt) {
                JOptionPane.showMessageDialog(f, evt.getMessage());
            }
        }

        else
            JOptionPane.showMessageDialog(f, "the user cancelled the operation");
    }


    public void onPrint(){
        try {
            // print the file
            t.print();
        }
        catch (Exception evt) {
            JOptionPane.showMessageDialog(f, evt.getMessage());
        }
    }


    public void onOpen(){
        // Create an object of JFileChooser class
        JFileChooser j = new JFileChooser("f:");

        // Invoke the showsOpenDialog function to show the save dialog
        int r = j.showOpenDialog(null);

        // If the user selects a file
        if (r == JFileChooser.APPROVE_OPTION) {
            // Set the label to the path of the selected directory
            File fi = new File(j.getSelectedFile().getAbsolutePath());

            try {
                // String
                String s1 = "";

                // File Reader
                FileReader fr = new FileReader(fi);

                // Buffered Reader
                BufferedReader br = new BufferedReader(fr);

                // Initialize s1
                s1 = br.readLine();

                // Take the input from the file
                while ((s1 = br.readLine()) != null) {
                    s1 = s1 + "\n" + s1;
                }

                // Set the text
                t.setText(s1);
            }

            catch(Exception evt){
                JOptionPane.showMessageDialog(f, evt.getMessage());
            }
        }

        // If the user cancelled the operation
        else
            JOptionPane.showMessageDialog(f, "the user cancelled the operation");
    }


    public void onNew(){
        t.setText("");
    }


    public void onClose(){
        f.setVisible(false);
        System.exit(0);
    }


    //TODO: Think about how to add encryption and decryption, should it be before text is sent to database?

    public static void EncryptPageContents() {
        ;
    }


    public static void DecryptPageContents() {
        ;
    }
}
