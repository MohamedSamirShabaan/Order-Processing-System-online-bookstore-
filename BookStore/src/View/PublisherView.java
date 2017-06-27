package View;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import controller.Controller;
import model.Publisher;

public class PublisherView extends JFrame {
	
	
	private JTextField nameField;
	private JTextField addressField;
	private JTextField phoneField;
	
	private Controller controller;
	private JButton saveButton;
	private Publisher publisher;
	private boolean create;
	private JFrame frame = this;
	
	public PublisherView(Publisher publisher, boolean create){
		
		this.publisher = publisher;
		this.create = create;
		
		controller = Controller.getInstance();
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    this.setSize(new Dimension(500,250));
    this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
    getContentPane().setLayout(null);
    this.setTitle("Publisher");
		getContentPane().setLayout(null);
		
		int x1 = 10 , x2  = 10+200+10 , y = 10 , w = 200 , h = 35 ;
		JLabel nameLabel = new JLabel("Publisher Name: ");
		getContentPane().add(nameLabel);
		nameLabel.setBounds(x1, y, w, h);
		nameField = new JTextField();
		getContentPane().add(nameField);
		nameField.setBounds(x2, y, w, h);
		y+=(h+5);
		
		JLabel addressLabel = new JLabel("Addressess: ");
		getContentPane().add(addressLabel);
		addressLabel.setBounds(x1, y, w, h);
		
		addressField = new JTextField();
		getContentPane().add(addressField);
		addressField.setBounds(x2, y, w, h);
		y+=(h+5);
		
		JLabel phoneLabel = new JLabel("Phone Numbers: ");
		getContentPane().add(phoneLabel);
		phoneLabel.setBounds(x1, y, w, h);
		
		phoneField = new JTextField();
		getContentPane().add(phoneField);
		phoneField.setBounds(x2, y, w, h);
		y+=(h+5);
		
		saveButton = new JButton("Save");
		saveButton.setBounds(x1, y, w, h);
		getContentPane().add(saveButton);
		saveButton.addActionListener(new saveAction());
		y+=(h+5);
		
		if(!create){
			update(publisher);
		}
	}
	
	private void update(Publisher publisher){
		nameField.setText(publisher.getName());
		addressField.setText(publisher.getAddresses().toString());
		phoneField.setText(publisher.getPhoneNumbers().toString());
	}
	
	private class saveAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			Publisher temp = new Publisher(nameField.getText().toLowerCase());
			temp.setAddresses(getElements(addressField.getText().toLowerCase()));
			temp.setPhoneNumbers(getElements(phoneField.getText().toLowerCase()));
			if(create){
				/// call create publisher
				//if faild update by book else update by temp & book = temp
				if(controller.addPublisher(temp)){
					publisher = temp;
					JOptionPane.showMessageDialog(null, "Publisher Added" );
					frame.dispose();
				}
				update(publisher);
			}else{
				/// call edit publisher
			}
		}
		
		private ArrayList<String> getElements(String elemetns){
			String []allelemetns = elemetns.split("\\s*\\,\\s*");
			ArrayList<String> list = new ArrayList<String>();
			for(String element : allelemetns){
				list.add(element);
			}
			return list;
		}
		
	}
	
}
