package View;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

import controller.Controller;
import model.Book;

public class BookView extends JFrame implements WindowListener {
  private JTextField ISBNField;
  private JTextField titleField;
  private JTextField publisherField;
  private JTextField yearField;
  private JTextField priceField;
  private JTextField copiesField;
  private JTextField thresholdField;
  private JTextField categoryField;
  private JTextField authorsField;

  private JButton saveButton;
  private Controller controller;
  private Book book;
  private boolean create;
  private JFrame frame = this;

  public BookView(Book book, boolean create) {

    controller = Controller.getInstance();
    this.book = book;
    this.create = create;

    getContentPane().setLayout(null);
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    this.setSize(new Dimension(500, 550));
    this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2
        - this.getSize().height / 2);
    this.setTitle("Book");

    int x1 = 10, x2 = 10 + 200 + 10, y = 10, w = 200, h = 35;

    JLabel ISBNLabel = new JLabel("ISBN: ");
    getContentPane().add(ISBNLabel);
    ISBNLabel.setBounds(x1, y, w, h);

    ISBNField = new JTextField();
    getContentPane().add(ISBNField);
    ISBNField.setBounds(x2, y, w, h);
    y += (h + 5);

    JLabel titleLabel = new JLabel("Title: ");
    getContentPane().add(titleLabel);
    titleLabel.setBounds(x1, y, w, h);

    titleField = new JTextField();
    getContentPane().add(titleField);
    titleField.setBounds(x2, y, w, h);
    y += (h + 5);

    JLabel publisherLabel = new JLabel("Publisher: ");
    getContentPane().add(publisherLabel);
    publisherLabel.setBounds(x1, y, w, h);

    publisherField = new JTextField();
    getContentPane().add(publisherField);
    publisherField.setBounds(x2, y, w, h);
    y += (h + 5);

    JLabel yearLabel = new JLabel("Year: ");
    getContentPane().add(yearLabel);
    yearLabel.setBounds(x1, y, w, h);

    yearField = new JTextField();
    getContentPane().add(yearField);
    yearField.setBounds(x2, y, w, h);
    y += (h + 5);

    JLabel priceLabel = new JLabel("price: ");
    getContentPane().add(priceLabel);
    priceLabel.setBounds(x1, y, w, h);

    priceField = new JTextField();
    getContentPane().add(priceField);
    priceField.setBounds(x2, y, w, h);
    y += (h + 5);

    JLabel copiesLabel = new JLabel("# Cpoies: ");
    getContentPane().add(copiesLabel);
    copiesLabel.setBounds(x1, y, w, h);

    copiesField = new JTextField();
    getContentPane().add(copiesField);
    copiesField.setBounds(x2, y, w, h);
    y += (h + 5);

    JLabel thresholdLabel = new JLabel("Threshold: ");
    getContentPane().add(thresholdLabel);
    thresholdLabel.setBounds(x1, y, w, h);

    thresholdField = new JTextField();
    getContentPane().add(thresholdField);
    thresholdField.setBounds(x2, y, w, h);
    y += (h + 5);

    JLabel categoryLabel = new JLabel("Category: ");
    getContentPane().add(categoryLabel);
    categoryLabel.setBounds(x1, y, w, h);

    categoryField = new JTextField();
    getContentPane().add(categoryField);
    categoryField.setBounds(x2, y, w, h);
    y += (h + 5);

    JLabel authorLabel = new JLabel("Authors: ");
    getContentPane().add(authorLabel);
    authorLabel.setBounds(x1, y, w, h);

    authorsField = new JTextField();
    getContentPane().add(authorsField);
    authorsField.setBounds(x2, y, w, h);
    y += (h + 5);

    saveButton = new JButton("Save");
    saveButton.setBounds(x1, y, w, h);
    getContentPane().add(saveButton);
    saveButton.addActionListener(new saveAction());
    y += (h + 5);
    if (!create) {
      update(book);
    }
  }

  @Override
  public void windowOpened(WindowEvent e) {
  }

  @Override
  public void windowClosing(WindowEvent e) {
  }

  @Override
  public void windowClosed(WindowEvent e) {
  }

  @Override
  public void windowIconified(WindowEvent e) {
  }

  @Override
  public void windowDeiconified(WindowEvent e) {
  }

  @Override
  public void windowActivated(WindowEvent e) {
  }

  @Override
  public void windowDeactivated(WindowEvent e) {
  }

  private void update(Book book) {
    ISBNField.setText(String.valueOf(book.getISBN()));
    titleField.setText(book.getTitle());
    publisherField.setText(book.getPublisherName());
    yearField.setText(book.getYear());
    priceField.setText(String.valueOf(book.getPrice()));
    copiesField.setText(String.valueOf(book.getNumberOfCopies()));
    thresholdField.setText(String.valueOf(book.getThreshold()));
    categoryField.setText(book.getCategory());
    String ss = book.getAuthors().toString();
    authorsField.setText(ss.substring(1, ss.length() - 1));
  }

  private class saveAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
      // TODO Auto-generated method stub
      Book temp = new Book();
      temp.setISBN(Integer.valueOf(ISBNField.getText().toLowerCase()));
      temp.setTitle(titleField.getText().toLowerCase());
      temp.setPublisherName(publisherField.getText().toLowerCase());
      temp.setYear(yearField.getText().toLowerCase());
      temp.setPrice(Double.valueOf(priceField.getText().toLowerCase()));
      temp.setNumberOfCopies(Integer.valueOf(copiesField.getText()
          .toLowerCase()));
      temp.setThreshold(Integer.valueOf(thresholdField.getText().toLowerCase()));
      temp.setCategory(categoryField.getText().toLowerCase());
      temp.setAuthors(getAuthors(authorsField.getText().toLowerCase()));
      if (create) {
        if (controller.addBook(temp)) {
          book = temp;
          JOptionPane.showMessageDialog(null, "Book Added");
          frame.dispose();
        }
        // update(book);
        // if faild update by book else update by temp & book = temp
      } else {
        // / call edit book
        if (controller.modifyBook(temp)) {
          book = temp;
          JOptionPane.showMessageDialog(null, "Book Modified");
          frame.dispose();
        }
        // update(book);
        // if faild update by book else update by temp & book = temp
      }
    }

    private ArrayList<String> getAuthors(String authers) {
      String[] allAuthers = authers.split("\\s*\\,\\s*");
      ArrayList<String> list = new ArrayList<String>();
      for (String auther : allAuthers) {
        list.add(auther);
      }
      return list;
    }

  }

}
