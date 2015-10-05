package cs213.photoAlbum.gui;

import java.awt.*;

import javax.swing.*;

import cs213.photoAlbum.model.Album;
import cs213.photoAlbum.model.User;

/**
 * Class containing the User Page for the GUI
 * @author Nicholas Fong and Jeffrey Kang
 */
public class UserPage extends JFrame {

	/** Logged in user */
	public User user;
	
	/** Displayed list of users */
	public JList<Album> albumList;
	
	/** Information about selected album */
	public JTextArea albumDetails;
	
	/** Create Album Button */
	public JButton createAlbum;
	
	/** Delete Album Button */
	public JButton deleteAlbum;
	
	/** Rename Album Button */
	public JButton renameAlbum;
	
	/** Open Album Button */
	public JButton openAlbum;
	
	/** Open Search Photos Page Button */
	public JButton searchPhotos;
	
	/** Logout Button */
	public JButton logout;
	
	/** Input field */
	public JTextField input;
	
	/** Error message area */
	public JTextArea error;
	
	/** Frame for this window */
	public JFrame frame;
	
	/**
	 * Constructor for User Page
	 * @param user - logged in user
	 */
	public UserPage (User user) {
		this.user = user;
		frame = new JFrame(user.name);
		//frame.setMinimumSize(new Dimension(300, 200));
		frame.setVisible(true);
		//frame.setLocationRelativeTo(null);
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//frame.addWindowListener(new SongLibListener());
		//frame.setLayout(new BorderLayout());
		frame.setResizable(false);
		Container pane = frame.getContentPane();
		pane.setLayout(new GridBagLayout());
		GridBagConstraints con = new GridBagConstraints();
		
		con.fill = GridBagConstraints.BOTH;
		con.gridx = 1;
		con.gridwidth = 6;
		con.insets = new Insets(2,2,2,2);
		pane.add(new JLabel("Albums"), con);
		
		con.gridwidth = 4;
		con.gridheight = 8;
		con.gridx = 0;
		con.gridy = 1;
		albumList = new JList<Album>(user.albumList);
		albumList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); 
		albumList.setVisibleRowCount(8);
		JScrollPane scrollPanel = new JScrollPane();
		scrollPanel.setViewportView(albumList);
		scrollPanel.setPreferredSize(new Dimension(200,220));
		pane.add(scrollPanel, con);
		
		con.gridwidth = 2;
		con.gridheight = 4;
		con.gridx = 4;
		albumDetails = new JTextArea("\n");
		albumDetails.setEditable(false);
		albumDetails.setPreferredSize(new Dimension(170, 800));
		JScrollPane albumPanel = new JScrollPane();
		albumPanel.setViewportView(albumDetails);
		albumPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		albumPanel.setPreferredSize(new Dimension(200,100));
		pane.add(albumPanel, con);
		
		con.gridheight = 1;
		con.gridy = 5;
		createAlbum = new JButton("Create Album");
		pane.add(createAlbum, con);
		
		con.gridy = 6;
		deleteAlbum = new JButton("Delete Album");
		pane.add(deleteAlbum, con);
		
		con.gridy = 7;
		renameAlbum = new JButton("Rename Album");
		pane.add(renameAlbum, con);
		
		con.gridy = 8;
		openAlbum = new JButton("Open Album");
		pane.add(openAlbum, con);
		
		con.gridx = 0;
		con.gridy = 9;
		con.gridwidth = 6;
		input = new JTextField();
		pane.add(input, con);
		
		con.gridwidth = 3;
		con.gridy = 10;
		searchPhotos = new JButton("Search Photos");
		pane.add(searchPhotos, con);
		
		con.gridx = 3;
		logout = new JButton("Logout");
		pane.add(logout, con);
		
		con.gridwidth = 6;
		con.gridx = 0;
		con.gridy = 11;
		error = new JTextArea("");
		error.setEditable(false);
		error.setOpaque(false);
		pane.add(error, con);
		
		frame.pack();
		frame.setVisible(true);
	}
}
