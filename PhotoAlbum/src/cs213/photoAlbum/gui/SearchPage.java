package cs213.photoAlbum.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import cs213.photoAlbum.model.Album;
import cs213.photoAlbum.model.Photo;
import cs213.photoAlbum.model.User;
import cs213.photoAlbum.util.Utils;
/**Class containing the Search Page window
 * @author Nicholas Fong
 * @author Jeffrey Kang
 * */
public class SearchPage {
	/**User object for the SearchPage*/
	public User user;
	/**JList object holding a list of photos to be displayed*/
	public JList<Photo> photoJList;
	/**DefaultListModel <Photo> object */
	public DefaultListModel<Photo> photoDLM;
	/**JTextArea object holding photoDetails*/
	public JTextArea photoDetails;
	/**JButton object for the dateRange*/
	public JButton dateRange;
	/**JButton object for the tag type Value*/
	public JButton typeValue;
	/**JButton object for recaptioning a photo*/
	public JButton recaptionPhoto;
	/**JButton object for creating an album*/
	public JButton createAlbum;
	/**JButton object for displaying a photo*/
	public JButton displayPhoto;
	/**JButton object for adding a tag to a photo*/
	public JButton addTag;
	/**JButton object for deleting a tag*/
	public JButton deleteTag;
	/**JTextField object for reading an input*/
	public JTextField input;
	/**JTextField object for displaying an error*/
	public JTextArea error;
	/**JLabel object for labeling a picture*/
	public JLabel picture;
	/**an integer used to keep track of a location*/
	public int index;
	/**JButton object used to go to the previous photo*/
	public JButton previous;
	/**JButton object used to go to the next photo */
	public JButton next;
	/**JButton object used to cancel the window and go to the AlbumPage window*/
	public JButton cancel;
	/**JFrame object used to hold the frame of he SearchPage frame*/
	public JFrame frame;
	
	public SearchPage (User user) {
		this.user = user;
		frame = new JFrame("Search Photos");
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
		photoDLM = new DefaultListModel<Photo>();
		photoJList = new JList<Photo>(photoDLM);
		photoJList.setCellRenderer(new PhotoListCellRenderer());
		photoJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); 
		photoJList.setVisibleRowCount(9);
		JScrollPane scrollPanel = new JScrollPane();
		scrollPanel.setViewportView(photoJList);
		scrollPanel.setPreferredSize(new Dimension(200,220));
		pane.add(scrollPanel, con);
		
		con.gridwidth = 2;
		con.gridheight = 4;
		con.gridx = 4;
		photoDetails = new JTextArea("\n");
		photoDetails.setEditable(false);
		photoDetails.setPreferredSize(new Dimension(170, 800));
		JScrollPane photoPanel = new JScrollPane();
		photoPanel.setViewportView(photoDetails);
		photoPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		photoPanel.setPreferredSize(new Dimension(200,100));
		pane.add(photoPanel, con);
		
		con.gridheight = 1;
		con.gridy = 5;
		dateRange = new JButton("Search By Date Range");
		pane.add(dateRange, con);
		
		con.gridy = 6;
		typeValue = new JButton("Search By Type Value");
		pane.add(typeValue, con);
		
		con.gridy = 7;
		recaptionPhoto = new JButton("Set caption");
		pane.add(recaptionPhoto, con);
		
		con.gridy = 8;
		createAlbum = new JButton("Create Album From Photos");
		pane.add(createAlbum, con);
		
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
		pane.add(addTag, con);
		
		con.gridx = 5;
		deleteTag = new JButton("Delete Tag");
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
		con.gridwidth = 6;
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
	
	class PhotoListCellRenderer extends DefaultListCellRenderer {

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
