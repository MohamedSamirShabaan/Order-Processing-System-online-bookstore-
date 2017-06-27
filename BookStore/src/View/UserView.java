package View;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import controller.Controller;
import model.User;

public class UserView extends JFrame implements WindowListener{
	private JTextField userNameField;
	private JTextField passwordField;
	private JTextField lastNameField;
	private JTextField firstNameField;
	private JTextField emailField;
	private JTextField phoneField;
	private JTextField addressField;
	
	private JButton saveButton;
	private Controller controller;
	private User user;
	private boolean signup;
	private JFrame frame = this;
	
	public UserView(User user, boolean signup) {
		
		controller = Controller.getInstance();
		
		this.user = user;
		this.signup = signup;
		getContentPane().setLayout(null);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    this.setSize(new Dimension(500,450));
    this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
    this.setTitle("User Info");
    
		int x1 = 10 , x2  = 10+200+10 , y = 10 , w = 200 , h = 35 ;
		JLabel userNameLabel = new JLabel("User Name: ");
		userNameLabel.setBounds(x1, y, w, h);
		getContentPane().add(userNameLabel);
		userNameField = new JTextField();
		userNameField.setBounds(x2, y, w, h);
		getContentPane().add(userNameField);
		y+=(h+5);
		
		
		JLabel passwordLabel = new JLabel("Password: ");
		passwordLabel.setBounds(x1, y, w, h);
		getContentPane().add(passwordLabel);
		passwordField = new JPasswordField();
		passwordField.setBounds(x2, y, w, h);
		getContentPane().add(passwordField);
		y+=(h+5);
		
		JLabel firstNameLabel = new JLabel("First Name: ");
		firstNameLabel.setBounds(x1, y, w, h);
		getContentPane().add(firstNameLabel);
		firstNameField = new JTextField();
		firstNameField.setBounds(x2, y, w, h);
		getContentPane().add(firstNameField);
		y+=(h+5);
		
		JLabel lastNameLabel = new JLabel("Last Name: ");
		lastNameLabel.setBounds(x1, y, w, h);
		getContentPane().add(lastNameLabel);
		lastNameField = new JTextField();
		lastNameField.setBounds(x2, y, w, h);
		getContentPane().add(lastNameField);
		y+=(h+5);
		
		
		JLabel emailLabel = new JLabel("Email: ");
		emailLabel.setBounds(x1, y, w, h);
		getContentPane().add(emailLabel);
		emailField = new JTextField();
		emailField.setBounds(x2, y, w, h);
		getContentPane().add(emailField);
		y+=(h+5);
		
		JLabel phoneLabel = new JLabel("Phone: ");
		phoneLabel.setBounds(x1, y, w, h);
		getContentPane().add(phoneLabel);
		phoneField = new JTextField();
		phoneField.setBounds(x2, y, w, h);
		getContentPane().add(phoneField);
		y+=(h+5);
		
		JLabel addressLabel = new JLabel("Address: ");
		addressLabel.setBounds(x1, y, w, h);
		getContentPane().add(addressLabel);
		addressField = new JTextField();
		addressField.setBounds(x2, y, w, h);
		getContentPane().add(addressField);
		y+=(h+5);
		
		saveButton = new JButton("Save");
		saveButton.addActionListener(new saveAction());
		saveButton.setBounds(x1, y, w, h);
		getContentPane().add(saveButton);
		y+=(h+5);
		
		
		if(!signup){
			update(user);
		}
	}
	

	
	@Override
	public void windowOpened(WindowEvent e) {}
	@Override
	public void windowClosing(WindowEvent e) {
	  
	}
	@Override
	public void windowClosed(WindowEvent e) {
	  
	}
	@Override
	public void windowIconified(WindowEvent e) {}
	@Override
	public void windowDeiconified(WindowEvent e) {}
	@Override
	public void windowActivated(WindowEvent e) {}
	@Override
	public void windowDeactivated(WindowEvent e) {}
	
	private void update(User user){
		userNameField.setText(user.getUserName());
		passwordField.setText(user.getPassword());
		firstNameField.setText(user.getFirstName());
		lastNameField.setText(user.getLastName());
		emailField.setText(user.getEmail());
		phoneField.setText(user.getPhoneNumber());
		addressField.setText(user.getShippingAddress());
	}
	
	private class saveAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			User temp = new User();
			temp.setUserName(userNameField.getText().toLowerCase());
			temp.setPassword(passwordField.getText());
			temp.setFirstName(firstNameField.getText().toLowerCase());
			temp.setLastName(lastNameField.getText().toLowerCase());
			temp.setEmail(emailField.getText().toLowerCase());
			temp.setPhoneNumber(phoneField.getText().toLowerCase());
			temp.setShippingAddress(addressField.getText().toLowerCase());
			if(signup){
				if(controller.signup(temp)){
					user = temp;
          JOptionPane.showMessageDialog(null, "Signed Up" );
	        frame.dispose();
	        Login login = new Login();
	        login.setVisible(true);
				}else{
  				//update(user);
  				// if done show message success or failed
  			
				}
			}else{
				/// i need editInformation to return something to indicate if finish success or not
				if(controller.editInformation(temp)){
					user = temp;
          JOptionPane.showMessageDialog(null, "Edit Done" );
					frame.dispose();
				}
				update(user);
				// if done show message success or failed
				// if faild update by user else update by temp
			}
		}
		
	}
}
