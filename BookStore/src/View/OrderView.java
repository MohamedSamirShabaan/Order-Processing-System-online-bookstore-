package View;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import controller.Controller;
import model.Order;

public class OrderView extends JFrame {

	private JTextField IDField;
	private JTextField ISBNField;
	private JTextField quantityField;
	
	private Controller controller;
	private JButton saveButton;
	private Order order;
	private boolean create;
	private JFrame frame = this;

	public OrderView(Order order, boolean create){
		this.order = order;
		this.create = create;
		controller = Controller.getInstance();
	  getContentPane().setLayout(null);
    getContentPane().setLayout(null);
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    this.setSize(new Dimension(500,300));
    this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
    this.setTitle("Order");
    
		getContentPane().setLayout(null);
		
		int x1 = 10 , x2  = 10+200+10 , y = 10 , w = 200 , h = 35 ;
		
		JLabel IDLabel = new JLabel("OrderId: ");
		getContentPane().add(IDLabel);
		IDLabel.setBounds(x1, y, w, h);
		
		IDField = new JTextField();
		getContentPane().add(IDField);
		IDField.setBounds(x2, y, w, h);
		y+=(h+5);
		
		JLabel ISBNLabel = new JLabel("ISBN: ");
		getContentPane().add(ISBNLabel);
		ISBNLabel.setBounds(x1, y, w, h);
		
		ISBNField = new JTextField();
		getContentPane().add(ISBNField);
		ISBNField.setBounds(x2, y, w, h);
		y+=(h+5);
		
		JLabel quantityLabel = new JLabel("Quantity: ");
		getContentPane().add(quantityLabel);
		quantityLabel.setBounds(x1, y, w, h);
		
		quantityField = new JTextField();
		getContentPane().add(quantityField);
		quantityField.setBounds(x2, y, w, h);
		y+=(h+5);
		
		saveButton = new JButton("Save");
		saveButton.setBounds(x1, y, w, h);
		getContentPane().add(saveButton);
		saveButton.addActionListener(new saveAction());
		y+=(h+5);
		
		if(!create){
			update(order);
		}
	}
	
	private void update(Order order){
		IDField.setText(String.valueOf(order.getOrderId()));
		ISBNField.setText(String.valueOf(order.getISBN()));
		quantityField.setText(String.valueOf(order.getQuantity()));
	}   
	
	private class saveAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			Order temp = new Order();	//// it's Error 
			temp.setOrderId(Integer.valueOf(IDField.getText().toLowerCase()));
			temp.setISBN(Integer.valueOf(ISBNField.getText().toLowerCase()));
			temp.setQuantity(Integer.valueOf(quantityField.getText().toLowerCase()));
			if(create){
				/// call create order
				if(controller.placeOrder(temp)){
					order = temp;
					JOptionPane.showMessageDialog(null, "Order Added" );
          frame.dispose();
				}
				update(order);
			}else{
				/// call edit order
//				if(controller.editOrder(temp)){
//					order = temp;
//				}
//				update(order);
			}
		}
		
	}
	
}
