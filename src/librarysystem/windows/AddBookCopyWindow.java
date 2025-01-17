package librarysystem.windows;

import business.Book;
import business.BookCopy;
import controller.ControllerInterface;
import controller.SystemController;
import exception.LibrarySystemException;
import librarysystem.LibWindow;
import utils.FirstRowBackgroundRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;


public class AddBookCopyWindow extends JPanel implements LibWindow {

    private static final long serialVersionUID = 1L;
    public static final AddBookCopyWindow INSTANCE = new AddBookCopyWindow();
    ControllerInterface ci = new SystemController();
    private boolean isInitialized = false;

    private JPanel mainPanel;
    private JLabel isbnLabel;
    private JTextField isbnTxtField;
    private JButton addBookCopyBtn;
    private JTable bookCopyTable;

    private String[] columnNames = {"No", "ISBN", "Title", "Copy Num", "Available"};

    //Singleton class
    private AddBookCopyWindow() {
        super(new BorderLayout());
        init();
    }

    @Override
    public boolean isInitialized() {
        return isInitialized;
    }

    @Override
    public void isInitialized(boolean val) {
        isInitialized = val;
    }

    public void init() {
        mainPanel = new JPanel();
        mainPanel.setLayout(null);
        defineMainPanel();
        add(mainPanel);
        isInitialized(true);
        setSize(621, 450);
    }

    public void defineMainPanel() {
        mainPanel = new JPanel();
        FlowLayout fl = new FlowLayout(FlowLayout.CENTER, 25, 25);
        mainPanel.setLayout(fl);

        isbnLabel = new JLabel("ISBN");
        mainPanel.add(isbnLabel);

        isbnTxtField = new JTextField(20);
        mainPanel.add(isbnTxtField);

        addBookCopyBtn = new JButton("Add Book Copy");
        addBookCopyButtonListener(addBookCopyBtn);
        mainPanel.add(addBookCopyBtn);

        defineBookCopyTable();
    }

    private void defineBookCopyTable() {
        DefaultTableModel defaultTableModel = new DefaultTableModel(columnNames, 0);
        bookCopyTable = new JTable(defaultTableModel);
        bookCopyTable.getColumnModel().getColumn(0).setPreferredWidth(20);
        bookCopyTable.setEnabled(false);
        bookCopyTable.setBounds(30, 40, 200, 200);

        JScrollPane sp = new JScrollPane(bookCopyTable);
        sp.setPreferredSize(new Dimension(480, 300));
        mainPanel.add(sp);
    }

    private String getIsbnValue() {
        return isbnTxtField.getText().trim();
    }

    public void clearFields() {
        isbnTxtField.setText("");
    }

    private void addBookCopyButtonListener(JButton butn) {
        butn.addActionListener(evt -> {
            try {
                Book book = ci.addNewBookCopy(getIsbnValue());
                populateTableData(book);
            } catch (LibrarySystemException e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        });
    }

    private void populateTableData(Book book) {
        DefaultTableModel defaultTableModel = clearTable();
        int no = 0;
        for (int i = book.getCopies().length - 1; i >= 0; i--) {
            no++;
            BookCopy bookCopy = book.getCopies()[i];
            Object[] rowData = new Object[]{no, book.getIsbn(), book.getTitle(), bookCopy.getCopyNum(), bookCopy.isAvailable() ? "Yes" : "No"};
            defaultTableModel.addRow(rowData);
        }

        bookCopyTable.setModel(defaultTableModel);
        setFirstRowColor();
        bookCopyTable.updateUI();
    }

    public DefaultTableModel clearTable() {
        DefaultTableModel defaultTableModel = (DefaultTableModel) bookCopyTable.getModel();
        defaultTableModel.getDataVector().removeAllElements();
        return defaultTableModel;
    }

    private void setFirstRowColor() {
        bookCopyTable.setDefaultRenderer(Object.class, new FirstRowBackgroundRenderer());
    }

    public JTable getBookCopyTable() {
        return bookCopyTable;
    }
}
