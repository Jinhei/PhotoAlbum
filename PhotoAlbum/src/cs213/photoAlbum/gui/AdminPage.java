package cs213.photoAlbum.gui;

import java.awt.*;

import javax.swing.*;

import cs213.photoAlbum.model.User;

/**
 * Class containing the Administrator Page window
 * @author Nicholas Fong
 */
public class AdminPage extends JFrame{ 
	/**Jlist object displaying users*/
	public JList<User> userList;
	/**JButton object for creating a user*/
	public JButton createUser;
	/**Jbutton object for deleting a user*/
	/**object*/
	public JButton deleteUser;
	/**Jbutton object for logging ou*/
	public JButton logout;
	/**JTextField object for userID*/
	public JTextField userID;
	/**JTextField object for userName*/
	public JTextField userName;
	/**JTextArea object ifor error*/
	public JTextArea error;
	/**JFrame object that is the frame for the AdminPage frame*/
	public JFrame frame;
	
	/**
	 * Constructor for AdminPage
	 * @param list - list of users to display 
	 */
	public AdminPage(DefaultListModel<User> list){
		frame = new JFrame("Admin");
		//frame.setPreferredSize(new Dimension(300, 600));
		//frame.setLocationRelativeTo(null);
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//frame.addWindowListener(new SongLibListener());
		frame.setLayout(new BorderLayout());
		//frame.setResizable(false);
		Container pane = frame.getContentPane();
		pane.setLayout(new GridBagLayout());
		//panel.setPreferredSize(new Dimension(580,280));
		GridBagConstraints con = new GridBagConstraints();
		
		// label
		con.insets = new Insets(2,5,2,5);
		con.anchor = GridBagConstraints.CENTER;
		con.gridwidth = 2;
		con.gridx = 0;
		con.gridy = 0;
		JLabel listLabel = new JLabel("List of Users");
		pane.add(listLabel, con);
		
		// userlist
		con.fill = GridBagConstraints.HORIZONTAL;
		con.gridheight = 6;
		con.gridy = 1;
		userList = new JList<User>(list);
		userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); 
		userList.setVisibleRowCount(6);
		JScrollPane scrollPanel = new JScrollPane();
		scrollPanel.setViewportView(userList);
		scrollPanel.setPreferredSize(new Dimension(200,150));
		pane.add(scrollPanel, con);
		
		con.gridheight = 1;
		con.gridy = 7;
		pane.add(MidPanel(), con);
		con.gridy = 8;
		
		// Error button
		con.gridwidth = 1;
		error = new JTextArea("");
		error.setEditable(false);
		error.setOpaque(false);
		pane.add(error, con);
		
		// Logout button
		con.fill = GridBagConstraints.NONE;
		con.anchor = GridBagConstraints.LAST_LINE_END;
		con.ipadx = 1;
		con.ipady = 1;
		con.gridx = 1;
		logout = new JButton("Logout");
		pane.add(logout, con);
		
		frame.pack();
		frame.setVisible(true);
	}
	
	/**
	 * Panel containing text field for string entry and
	 * create/delete user buttons
	 * @return JPanel for the middle of the window
	 */
	private JPanel MidPanel(){
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints con = new GridBagConstraints();
		con.fill = GridBagConstraints.BOTH;
		
		panel.add(new JLabel("User ID: "), con);
		
		con.gridy = 1;
		panel.add(new JLabel("User Name: "), con);
		
		con.gridwidth = 3;
		con.gridx = 1;
		con.gridy = 0;		
		userID = new JTextField();
		panel.add(userID, con);
		
		con.gridy = 1;
		userName = new JTextField();
		panel.add(userName, con);
		
		con.gridwidth = 2;
		con.gridx = 0;
		con.gridy = 2;
		createUser = new JButton ("Create User");
		panel.add(createUser, con);
		
		con.gridx = 2;
		deleteUser = new JButton("Delete User");
		panel.add(deleteUser, con);
		
		return panel;
	}
	
	/**
	 * Panel containing logout button
	 * @return JPanel for logout button on bottom of window
	 */
	private JPanel BotPanel(){
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints con = new GridBagConstraints();
		
		// Error button
		con.fill = GridBagConstraints.VERTICAL;
		con.gridx = 0;
		con.gridy = 0;
		error = new JTextArea(" ");
		error.setOpaque(false);
		panel.add(error, con);
		
		// Logout button
		con.fill = GridBagConstraints.VERTICAL;
		con.anchor = GridBagConstraints.LAST_LINE_END;
		con.ipadx = 1;
		con.ipady = 1;
		con.gridx = 1;
		con.gridy = 0;
		logout = new JButton("Logout");
		panel.add(logout, con);
		
		return panel;
	}
	
}
