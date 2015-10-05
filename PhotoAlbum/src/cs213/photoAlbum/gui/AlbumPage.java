package cs213.photoAlbum.gui;

import java.awt.*;
import java.io.File;

import javax.swing.*;

import cs213.photoAlbum.model.Album;
import cs213.photoAlbum.model.Photo;
import cs213.photoAlbum.model.User;
import cs213.photoAlbum.util.Utils;

/**
 * Class containing the Open Album page in the GUI
 * @author Nicholas Fong and Jeffrey Kang
 */
public class AlbumPage extends JFrame{
	
	/** User object */
	public User user;
	
	/** Album shown */
	public Album album;
	
	/** JList displaying photos */
	public JList<Photo> photoList;
	
	/** Container showing photo information */
	public JTextArea photoDetails;
	
	/** Add Photo button */
	public JButton addPhoto;
	
	/** Remove Photo button */
	public JButton removePhoto;
	
	/** Caption Photo button */
	public JButton recaptionPhoto;
	
	/** Change Album button */
	public JButton moveToAlbum;
	
	/** Display photo Photo button */
	public JButton displayPhoto;
	
	/** Input Field  */
	public JTextField input;
	
	/** Add Tag button */
	public JButton addTag;
	
	/** Delete Tag button */
	public JButton deleteTag;
	
	/** Error message area */
	public JTextArea error;
	
	/** Displayed Picture */
	public JLabel picture;
	
	/** Previous Photo button */
	public JButton previous;
	
	/** Next Photo button */
	public JButton next;
	
	/** Return button */
	public JButton cancel;
	
	/** Frame for the window */
	public JFrame frame;
	
	/** Currently displayed photo index */
	public int index;
	
	/**
	 * Constructor for Album Page
	 * @param user - logged in user
	 * @param album - album displayed
	 */
	public AlbumPage (User user, Album album) {
		this.user = user;
		this.album = album;
		frame = new JFrame(album.name);
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
		con.gridx = 0;
		con.gridwidth = 6;
		con.insets = new Insets(2,2,2,2);
		pane.add(new JLabel("Photos"), con);
		
		con.gridwidth = 4;
		con.gridheight = 9;
		con.gridx = 0;
		con.gridy = 1;
		photoList = new JList<Photo>(album.photos);
		photoList.setCellRenderer(new PhotoListCellRenderer());
		photoList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); 
		photoList.setVisibleRowCount(9);
		JScrollPane scrollPanel = new JScrollPane();
		scrollPanel.setViewportView(photoList);
		scrollPanel.setPreferredSize(new Dimension(200,220));
		pane.add(scrollPanel, con);
		
		con.gridwidth = 2;
		con.gridheight = 4;
		con.gridx = 4;
		photoDetails = new JTextArea("\n");
		photoDetails.setEditable(false);
		photoDetails.setPreferredSize(new Dimension(170, 800));
		photoDetails.setLineWrap(true);
		JScrollPane photoPanel = new JScrollPane();
		photoPanel.setViewportView(photoDetails);
		photoPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		photoPanel.setPreferredSize(new Dimension(200,100));
		pane.add(photoPanel, con);
		
		con.gridheight = 1;
		con.gridy = 5;
		addPhoto = new JButton("Add Photo");
		pane.add(addPhoto, con);
		
		con.gridy = 6;
		removePhoto = new JButton("Remove Photo");
		pane.add(removePhoto, con);
		
		con.gridy = 7;
		recaptionPhoto = new JButton("Set Caption");
		pane.add(recaptionPhoto, con);
		
		con.gridy = 8;
		moveToAlbum = new JButton("Move to Album");
		pane.add(moveToAlbum, con);
		
		con.gridy = 9;
		displayPhoto = new JButton("Display Photo");
		pane.add(displayPhoto, con);
		
		con.gridx = 0;
		con.gridy = 10;
		con.gridwidth = 4;
		input = new JTextField();
		pane.add(input, con);
		
		con.gridx = 4;
		con.gridwidth = 1;
		addTag = new JButton("Add Tag");
		addTag.setToolTipText("type:value");
		pane.add(addTag, con);
		
		con.gridx = 5;
		deleteTag = new JButton("Delete Tag");
		deleteTag.setToolTipText("type:value");
		pane.add(deleteTag, con);
		
		con.gridx = 0;
		con.gridy = 11;
		con.gridwidth = 6;
		error = new JTextArea("");
		error.setEditable(false);
		error.setOpaque(false);
		//error.setBorder(BorderFactory.createLineBorder(Color.black));
		pane.add(error, con);
		
		con.gridy = 12;
		con.gridheight = 4;
		con.fill = GridBagConstraints.NONE;
		picture = new JLabel(new ImageIcon());
		this.index = 0;
		pane.add(picture, con);
		
		con.fill = GridBagConstraints.BOTH;
		con.gridheight = 1;
		con.gridwidth = 1;
		con.gridy = 16;
		previous = new JButton(" < ");
		pane.add(previous, con);
		
		con.gridx = 1;
		next = new JButton(" > ");
		pane.add(next, con);
		
		con.gridwidth = 2;
		con.gridx = 4;
		cancel = new JButton("Cancel");
		pane.add(cancel, con);
		
		frame.pack();
		frame.setVisible(true);
	}
	
	private class PhotoListCellRenderer extends DefaultListCellRenderer {

	    PhotoListCellRenderer() {
	    }

	    @Override
	    public Component getListCellRendererComponent(
	            JList list,
	            Object value,
	            int index,
	            boolean selected,
	            boolean expanded) {

	        Photo p = (Photo) value;
	        String content = p.fileName;
	        if (!p.caption.equals("")){
	        	content = content + " - " + p.caption;
	        }
	        JLabel label = (JLabel)super.getListCellRendererComponent(
					list,
					content,
					index,
					selected,
					expanded);
	        label.setIcon(p.thumb);

	        if (selected) {
	            label.setBackground(java.awt.Color.LIGHT_GRAY);
	            label.setForeground(list.getSelectionForeground());
	        } else {
	            label.setBackground(list.getBackground());
	            label.setForeground(list.getForeground());
	        }

	        return label;
	    }
		
	}
}
