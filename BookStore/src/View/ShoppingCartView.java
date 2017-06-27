package View;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

import controller.Controller;
import model.Book;
import model.User;

public class ShoppingCartView extends JFrame {
	
	private JTable table;
	
	private JFrame frame = this;
	private final String [] buttons = {"Buy", "Delete Book", "Delete All"};
	private ActionListener[] actions;
	private User user;
	private Vector<String> columnNames = new Vector<String>();
	private Vector<Vector<String>> data = new Vector<Vector<String>>();
	
	private Controller controller;
	
	public ShoppingCartView(User user) {
	
		this.user = user;
		getContentPane().setLayout(null);
		
		controller = Controller.getInstance();
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	    this.setSize(new Dimension(1200,500));
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		this.setTitle("Order");
		
		table = new JTable();
		if(user != null){
			LinkedHashMap<Book,Integer> books = user.getShoppingCart();
			updateTable(books);
		}
		
		table.setSurrendersFocusOnKeystroke(true);
		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		//table.setBounds(12, 285, 320, -250);
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        table.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setVisible(true);
        scrollPane.setBounds(20,30,950,700);
        getContentPane().add(scrollPane);

		
		
		actions = new ActionListener[3];
		actions[0] = new BuyAction();
		actions[1] = new DeleteBookAction();
		actions[2] = new DeleteAllAction();
		
		int x = 1000, y = 33, w = 150, h = 25;
		for(int i = 0 ; i < 3 ; i++){
			JButton button = new JButton(buttons[i]);
			button.setBounds(x, y+=(h+5), w, h);
			button.addActionListener(actions[i]);
			getContentPane().add(button);
		}
		
			
	}
	
	private void updateTable(LinkedHashMap<Book,Integer> books){
		columnNames.clear();
		data.clear();
	  columnNames.add("ISBN");
		columnNames.add("Title");
		columnNames.add("Publisher");
		columnNames.add("Year");
		columnNames.add("Price");
		columnNames.add("#Copies");
		columnNames.add("Threshold");
		columnNames.add("Category");
		columnNames.add("Quantity");
		columnNames.add("Authers");
		
		
		double count = 0;
		for(Entry<Book, Integer> entry : books.entrySet()){
			Vector<String> tuple = new Vector<String>();
			Book book = entry.getKey();
			int quantity = entry.getValue();
			count += quantity*book.getPrice();
			tuple.add(String.valueOf(book.getISBN()));
			tuple.add(book.getTitle());
			tuple.add(book.getPublisherName());
			tuple.add(book.getYear());
			tuple.add(String.valueOf(book.getPrice()));
			tuple.add(String.valueOf(book.getNumberOfCopies()));
			tuple.add(String.valueOf(book.getThreshold()));
			tuple.add(book.getCategory());
			tuple.add(String.valueOf(quantity));
			tuple.add(book.getAuthors().toString());
			data.add(tuple);
		}
		
		Vector<String> tuple = new Vector<String>();
		tuple.add("");
		tuple.add("Total price");
		tuple.add("");
		tuple.add("");
		tuple.add(String.valueOf(count));
		tuple.add("");
		tuple.add("");
		tuple.add("");
		tuple.add("");
		tuple.add("");
		data.add(tuple);
		
		DefaultTableModel model = new DefaultTableModel(data, columnNames);
		table.setModel(model);
		
	}
	
	private class BuyAction implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			String cartNumber = JOptionPane.showInputDialog(frame, "Enter your Credit Card number: ");
			String date = JOptionPane.showInputDialog(frame, "Enter your cart due date like this dd-MMM-yyyy");
			
			DateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
			String timeStamp = new SimpleDateFormat("dd-MMM-yyyy").format(Calendar.getInstance().getTime());
			java.util.Date dueDate = null, now = null;
			
			try {
				dueDate = (java.util.Date) formatter.parse(date);
				now = (java.util.Date) formatter.parse(timeStamp);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				
			}
			if(dueDate != null &&  now != null && dueDate.after(now)){
				controller.checkout();
				frame.dispose();
				//JOptionPane.showMessageDialog(null, "Buy Done :D" , "Buy Done", JOptionPane.INFORMATION_MESSAGE);
			}else{
				JOptionPane.showMessageDialog(null, "invaled Due date!!\ncheck you due date cart!!" , "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	private class DeleteBookAction implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
		  int index = table.getSelectedRow();
		  if(index >= 0 && index < table.getRowCount() ){
		      String ss = (String) table.getValueAt(index, 0);
  		    if(ss != null && !ss.equals("")){
  		      Integer ISBN = Integer.valueOf(ss);
  		      if(ISBN != null){
  		        user.removeBookFromShoppingCart(ISBN);
              updateTable(user.getShoppingCart());
  		      }else{
  		        JOptionPane.showMessageDialog(null, "can't delete this row!!" , "Error", JOptionPane.ERROR_MESSAGE);
  		      }
  		    }else{
  	        JOptionPane.showMessageDialog(null, "can't delete this row!!" , "Error", JOptionPane.ERROR_MESSAGE);
  		    }
		  }else{
        JOptionPane.showMessageDialog(null, "invaild index to delete!!" , "Error", JOptionPane.ERROR_MESSAGE);
		  }
			
		}
	}
	private class DeleteAllAction implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			user.removeAll();
			updateTable(user.getShoppingCart());
		}
	}
}
