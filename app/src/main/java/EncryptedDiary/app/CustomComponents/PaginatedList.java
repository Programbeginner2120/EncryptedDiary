package EncryptedDiary.app.CustomComponents;//package EncryptedDiary.app.CustomComponents;

import EncryptedDiary.app.DiaryEditorPage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * A paginated list. Only displays a specific number of rows
 * and allows you to page backwards and forwards through the list
 * with the help of a toolbar.
 * ALL CREDIT GOES TO STACKOVERFLOW ANSWER AT https://stackoverflow.com/questions/4613840/can-i-paginate-a-jlist
 */

public class PaginatedList extends JPanel {

    private DiaryEditorPage parentFrame;
    private String chosenDocumentName;

    private final int pageSize;
    private final JList list;
    private final ListModel model;

    private final int lastPageNum;
    private int currPageNum;
    private JLabel countLabel;
    private JButton first, prev, next, last;
    private JButton confirm;
    private JButton cancel;

    /**
     * @param list the jlist
     * @param pageSize the number of rows visible in the jlist
     */
    public PaginatedList(DiaryEditorPage parentFrame, JList list, int pageSize) {
        super();
        this.pageSize = pageSize;
        this.list = list;
        this.model = list.getModel();

        this.chosenDocumentName = null;

        this.parentFrame = parentFrame;

        //work out how many pages there are
        this.lastPageNum = model.getSize() / pageSize + (model.getSize() % pageSize != 0 ? 1 : 0);
        this.currPageNum = 1;

        setLayout(new BorderLayout());
        countLabel = new JLabel();
        add(countLabel, BorderLayout.NORTH);
        add(list, BorderLayout.CENTER);
        add(createControls(), BorderLayout.SOUTH);
        updatePage();
    }

    private JPanel createControls() {
        first = new JButton(new AbstractAction("<<") {
            public void actionPerformed(ActionEvent e) {
                currPageNum = 1;
                updatePage();
            }
        });

        prev = new JButton(new AbstractAction("<") {
            public void actionPerformed(ActionEvent e) {
                if (--currPageNum <= 0)
                    currPageNum = 1;
                updatePage();
            }
        });

        next = new JButton(new AbstractAction(">") {
            public void actionPerformed(ActionEvent e) {
                if (++currPageNum > lastPageNum)
                    currPageNum = lastPageNum;
                updatePage();

            }
        });

        last = new JButton(new AbstractAction(">>") {
            public void actionPerformed(ActionEvent e) {
                currPageNum = lastPageNum;
                updatePage();
            }
        });

        confirm = new JButton(new AbstractAction("Ok") {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(list.getSelectedIndex());
            }
        });

        cancel = new JButton(new AbstractAction("Cancel") {
            @Override
            public void actionPerformed(ActionEvent e) {
                parentFrame.disposeListFrame();
            }
        });

        JPanel bar = new JPanel(new GridLayout(1, 4));
        bar.add(first);
        bar.add(prev);
        bar.add(next);
        bar.add(last);
        bar.add(confirm);
        bar.add(cancel);
        return bar;
    }

    private void disposePage(){
        this.getParent().getParent().setVisible(false);
    }

    private void updatePage() {

        //replace the list's model with a new model containing
        //only the entries in the current page.
        final DefaultListModel page = new DefaultListModel();
        final int start = (currPageNum - 1) * pageSize;
        int end = start + pageSize;
        if (end >= model.getSize()) {
            end = model.getSize();
        }
        for (int i = start; i < end; i++) {
            page.addElement(model.getElementAt(i));
        }
        list.setModel(page);

        //update the label
        countLabel.setText("Page " + currPageNum + "/" + lastPageNum);

        // update buttons
        final boolean canGoBack = currPageNum != 1;
        final boolean canGoFwd = currPageNum != lastPageNum;
        first.setEnabled(canGoBack);
        prev.setEnabled(canGoBack);
        next.setEnabled(canGoFwd);
        last.setEnabled(canGoFwd);
    }
}
