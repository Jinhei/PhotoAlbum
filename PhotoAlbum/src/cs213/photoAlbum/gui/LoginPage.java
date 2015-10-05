package cs213.photoAlbum.gui;

import java.awt.*;

import javax.swing.*;
/**
 * Class containing the Login Page for the GUI
 * @author Nicholas Fong and Jeffrey Kang
 */
public class LoginPage extends JFrame{ 
	/**JButton object used to login*/
	public JButton login;
	/**JButton object used to create a user*/
	public JButton createUser;
	/**JTextField object used to read inputs*/
	public JTextField inputField;
	/**JTextArea object used to display an error*/
	public JTextArea error;
	/**JFrame object that is used to open the LoginPage window*/
	public JFrame frame;
	
	public LoginPage(){
		frame = new JFrame("Login");
		frame.setResizable(false);
		//frame.setPreferredSize(new Dimension(300,160));
		JPanel panel = new JPanel();
		frame.setMinimumSize(new Dimension(280,90));
		panel.setLayout(new GridLayout(4,1));
		//panel.setMinimumSize(new Dimension(280,180));
		panel.add(TopPanel());
		inputField = new JTextField();
		panel.add(inputField);
		
		login = new JButton("Login");
		panel.add(login);
		
		error = new JTextArea("");
		error.setOpaque(false);
		error.setEditable(false);
		panel.add(error);
		
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
	}
	

	private JPanel TopPanel(){
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints con = new GridBagConstraints();
		
		con.fill = GridBagConstraints.VERTICAL;
		con.ipady = 5;
		con.gridx = 0;
		con.gridy = 0;
		panel.add(new JLabel("Login"));
		
		return panel;
	}
	
	private JPanel BotPanel(){
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints con = new GridBagConstraints();
				
		con.fill = GridBagConstraints.VERTICAL;
		con.insets = new Insets(0,2,0,2);
		con.ipady = 5;
		con.gridx = 0;
		con.gridy = 0;
		login = new JButton("Login");
		panel.add(login, con);
		
		
		// Error button
		con.fill = GridBagConstraints.VERTICAL;
		con.gridwidth = 2;
		con.gridx = 0;
		con.gridy = 1;
		
		
		return panel;
	}
	
}
