package EncryptedDiary.app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * A paginated list. Only displays a specific number of rows
 * and allows you to page backwards and forwards through the list
 * with the help of a toolbar.
 * CREDIT GOES TO STACKOVERFLOW ANSWER AT https://stackoverflow.com/questions/4613840/can-i-paginate-a-jlist for the initial
 * template
 */

public class PaginatedList extends JPanel {

    private JFrame parentFrame;
    private JFrame previousFrame;
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
    public PaginatedList(JFrame parentFrame, DiaryEditorPage previousFrame, JList list, int pageSize) {
        super();
        this.previousFrame = previousFrame;
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
                int indexOfDocument = list.getSelectedIndex();
                if (indexOfDocument >= 0){
                    String documentName = (String) list.getModel().getElementAt(list.getSelectedIndex());
                    ((DiaryEditorPage)previousFrame).setCurrentDocumentName(documentName);
                    ((DiaryEditorPage)previousFrame).setCurrentDocumentIndex(list.getSelectedIndex());
                    ((DiaryEditorPage)previousFrame).disposeListFrame(parentFrame);
                    ((DiaryEditorPage)previousFrame).processFileOpen(documentName);
                }
            }
        });

        cancel = new JButton(new AbstractAction("Cancel") {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((DiaryEditorPage)previousFrame).disposeListFrame(parentFrame);
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

////TODO: NEED TO CLEAN THIS PAGE'S FUNCTIONALITY UP AND MAKE IT MORE SUSTAINABLE
////TODO: MAKE THIS PAGE'S FUNCTIONALITY MORE GENERIC / REUSABLE