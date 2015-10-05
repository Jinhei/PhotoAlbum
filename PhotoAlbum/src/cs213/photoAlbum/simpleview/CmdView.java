package cs213.photoAlbum.simpleview;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JWindow;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import cs213.photoAlbum.control.Controller;
import cs213.photoAlbum.gui.AdminPage;
import cs213.photoAlbum.gui.LoginPage;
import cs213.photoAlbum.model.*;
import cs213.photoAlbum.util.PhotoDetails;
import cs213.photoAlbum.util.Utils;
import cs213.photoAlbum.gui.*;

/**
 * View for Photo Album GUI
 * @author Nicholas Fong and Jeffrey Kang
 *
 */
public class CmdView {
	

	LoginPage LOGIN_PAGE;
	AdminPage ADMIN_PAGE;
	UserPage USER_PAGE;
	AlbumPage ALBUM_PAGE;
	SearchPage SEARCH_PAGE;
	DefaultListModel<User> USER_LIST;
	Backend backend;
	Controller CONTROLLER;
	
	/**
	 * Constructor for Command View Object
	 */
	public CmdView(){
		backend = new Backend();		
		try {
			USER_LIST = backend.startSession();
			CONTROLLER = new Controller(USER_LIST);
			LOGIN_PAGE = new LoginPage();
			setLoginListeners();
		} catch (Exception e) {
			backend.saveSession();
			JFrame frame = new JFrame("Error");
			frame.setVisible(true);
			frame.setResizable(false);
			frame.setLocation(null);
			frame.add(new JLabel("An unexpected error has occured: \n"
					+e.toString()+"\n"
					+"Please restart the program."));
			JButton ok = new JButton("OK");
			frame.add(ok);
			ok.addActionListener(new OkListener());
			frame.addWindowListener(new OkListener());
			frame.pack();
		}
	}
	
	private class OkListener implements ActionListener, WindowListener {
		@Override
		public void windowActivated(WindowEvent arg0) {
		}

		@Override
		public void windowClosed(WindowEvent arg0) {
		}

		@Override
		public void windowClosing(WindowEvent arg0) {
			System.exit(1);
		}

		@Override
		public void windowDeactivated(WindowEvent arg0) {
		}

		@Override
		public void windowDeiconified(WindowEvent arg0) {
		}

		@Override
		public void windowIconified(WindowEvent arg0) {
		}

		@Override
		public void windowOpened(WindowEvent arg0) {
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(1);
		}
		
	}
	/**
	 *Prints out a list of users 
	 *@param userList - list of users to be printed
	 *@return - prints out list of users and returns true if possible
	 */
	public static boolean listusers(DefaultListModel<User> userList){
		Utils.debug("CmdView.listusers():");
		
		if (userList.isEmpty()){
			System.out.println("no users found");
			return false;
		}
		List<User> listOfUsers = Utils.defaultListModelToList(userList);
		for(User user: listOfUsers){
			System.out.println(user.id);
		}
		
		return true;
		
	}

	private class UserPageCreateAlbumListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e){
			String inputText = USER_PAGE.input.getText();
			
			if(inputText.isEmpty()){
				Toolkit.getDefaultToolkit().beep();
				USER_PAGE.error.setText("Please enter an album name.");
				return;
			}
			
			//System.out.println(inputText);
			boolean bool = CONTROLLER.createAlbum(inputText);
			
			
			if (bool == false){
				Toolkit.getDefaultToolkit().beep();
				USER_PAGE.error.setText("Album already exists.");
			}
			else{
				USER_PAGE.error.setText("");
				USER_PAGE.input.setText("");
				USER_PAGE.albumList.setSelectedIndex(0);
			}				
			
		}
	}
	
	private class UserPageDeleteAlbumListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e){
			USER_PAGE.error.setText("");
			if (USER_PAGE.albumList.getSelectedIndex() < 0){
				Toolkit.getDefaultToolkit().beep();
				return;
			}
			Album selectedAlbum = USER_PAGE.albumList.getSelectedValue();
			if (selectedAlbum == null){
				Toolkit.getDefaultToolkit().beep();
				USER_PAGE.error.setText("Album does not exist");
			}
			else{
				String selectedText = selectedAlbum.name;
				CONTROLLER.deleteAlbum(selectedText);
				USER_PAGE.error.setText("");
				if(!USER_PAGE.user.albumList.isEmpty())
					USER_PAGE.albumList.setSelectedIndex(0);
			}
		}
	}

	private class UserPageRenameAlbumListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e){
			String newName = USER_PAGE.input.getText();
			if (newName.isEmpty()){
				Toolkit.getDefaultToolkit().beep();
				USER_PAGE.error.setText("Please enter an album name");
				return;
			}
			
			Album oldAlbum = USER_PAGE.albumList.getSelectedValue();
			
			if(oldAlbum == null){
				Toolkit.getDefaultToolkit().beep();
				USER_PAGE.error.setText("Please select an album");
				return;
			}
			else{
				String oldName = oldAlbum.name;
				boolean result =  CONTROLLER.renameAlbum(oldName, newName);
				if (!result){
					Toolkit.getDefaultToolkit().beep();
					USER_PAGE.error.setText("Album with given name already exists");
					return;
				}
				
				USER_PAGE.error.setText("");
				USER_PAGE.input.setText("");
				USER_PAGE.albumList.clearSelection();
				USER_PAGE.albumList.setSelectedIndex(0);
			}	
		}
	}	
	private class UserPageOpenPhotosListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e){
			
			Album openAlbumPage = USER_PAGE.albumList.getSelectedValue();
			if(openAlbumPage == null){
				Toolkit.getDefaultToolkit().beep();
				USER_PAGE.error.setText("Please select an album");
			}
			else{
				ALBUM_PAGE = new AlbumPage(USER_PAGE.user,openAlbumPage);	
				setAlbumListeners();
				USER_PAGE.frame.setVisible(false);
				USER_PAGE.error.setText("");
			}
		}
	}
	private class UserPageSearchPhotosListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e){
			
			SEARCH_PAGE = new SearchPage(USER_PAGE.user);
			setSearchPageListeners();
			USER_PAGE.frame.setVisible(false);
			USER_PAGE.error.setText("");
		}
	}
	
	private class UserDetailListener implements ListSelectionListener {

		@Override
		public void valueChanged(ListSelectionEvent e) {
			int index = USER_PAGE.albumList.getSelectedIndex();
			if (index != -1){ 
				DefaultListModel<Photo> photos = CONTROLLER.listPhotos(USER_PAGE.user.albumList.get(index).name);
				int listSize = photos.getSize();
				if (listSize > 0){
					String content = "";
					SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy-HH:mm:ss");
					for(int i = 0; i < listSize; i++){
						Photo p = photos.get(i);
						content += ""+p.fileName+" - "+sdf.format(p.lastModified.getTime())+"\n";
					}
					USER_PAGE.albumDetails.setText(content);
					USER_PAGE.frame.pack();
				} else {
					USER_PAGE.albumDetails.setText("\n");
				}
			} else {
				USER_PAGE.albumDetails.setText("\n");
			}
		}
	}
	
	private class UserPageLogoutListener implements ActionListener, WindowListener{
		@Override
		public void actionPerformed(ActionEvent e){
			backend.saveSession();
			USER_PAGE.frame.dispose();
			LOGIN_PAGE.frame.setVisible(true);
			LOGIN_PAGE.inputField.setText("");
		}

		@Override
		public void windowActivated(WindowEvent e) {
		}

		@Override
		public void windowClosed(WindowEvent e) {
		}

		@Override
		public void windowClosing(WindowEvent e) {
			backend.saveSession();
			System.exit(0);
			
		}

		@Override
		public void windowDeactivated(WindowEvent e) {
		}

		@Override
		public void windowDeiconified(WindowEvent e) {
		}

		@Override
		public void windowIconified(WindowEvent e) {
		}

		@Override
		public void windowOpened(WindowEvent e) {			
		}
	}
	void setUserPageListeners(){
		USER_PAGE.createAlbum.addActionListener(new UserPageCreateAlbumListener());
		USER_PAGE.deleteAlbum.addActionListener(new UserPageDeleteAlbumListener());
		USER_PAGE.renameAlbum.addActionListener(new UserPageRenameAlbumListener());
		USER_PAGE.openAlbum.addActionListener(new UserPageOpenPhotosListener());
		USER_PAGE.searchPhotos.addActionListener(new UserPageSearchPhotosListener());
		USER_PAGE.logout.addActionListener(new UserPageLogoutListener());
		USER_PAGE.frame.addWindowListener(new UserPageLogoutListener());
		USER_PAGE.albumList.getSelectionModel().addListSelectionListener(new UserDetailListener());
	}
	
	private class LoginListener implements WindowListener, ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent e){
			String inputText = LOGIN_PAGE.inputField.getText();
			if(inputText.equals("admin")){
				ADMIN_PAGE = new AdminPage(USER_LIST);
				setAdminListeners();
				LOGIN_PAGE.frame.setVisible(false);
				LOGIN_PAGE.error.setText("");
			}
			else{
				if(CONTROLLER.login(inputText)){
					User user = Utils.getUserFromList(inputText, USER_LIST);
					USER_PAGE = new UserPage(user);	
					setUserPageListeners();
					LOGIN_PAGE.frame.setVisible(false);
					LOGIN_PAGE.error.setText("");
				}
				else {
					LOGIN_PAGE.error.setText("User does not exist. Please try again");
				}
				LOGIN_PAGE.frame.pack();
			}			
		}
		
		public void windowClosing(WindowEvent e) {
			backend.saveSession();
			System.exit(0);
		}

		@Override
		public void windowActivated(WindowEvent e) {
		}

		@Override
		public void windowClosed(WindowEvent e) {
		}

		@Override
		public void windowDeactivated(WindowEvent e) {
		}

		@Override
		public void windowDeiconified(WindowEvent e) {
		}

		@Override
		public void windowIconified(WindowEvent e) {
		}

		@Override
		public void windowOpened(WindowEvent e) {			
		}
		
	}
	
	void setAdminListeners(){
		ADMIN_PAGE.createUser.addActionListener(new AdminCreateUserListener());
		ADMIN_PAGE.deleteUser.addActionListener(new AdminDeleteUserListener());
		ADMIN_PAGE.logout.addActionListener(new AdminLogoutListener());
		ADMIN_PAGE.frame.addWindowListener(new AdminLogoutListener());
	}
	
	private class AdminCreateUserListener implements ActionListener, DocumentListener{
		
		/**
		 * Checks if the name and id fields are non-empty, then adds the user
		 * to the list		 * 
		 * @param e
		 */ @Override
		public void actionPerformed(ActionEvent e){
			String id = ADMIN_PAGE.userID.getText();
			String name = ADMIN_PAGE.userName.getText();
			
			if (name.isEmpty() || id.isEmpty()) { // check if user and id fields are filled
				Toolkit.getDefaultToolkit().beep();
				ADMIN_PAGE.error.setText("All fields required.");
				ADMIN_PAGE.pack();
			} else if (Utils.getUserFromList(id, USER_LIST) != null){ // check if user is already in list
				Toolkit.getDefaultToolkit().beep();
				ADMIN_PAGE.error.setText("User already exists.");
				ADMIN_PAGE.pack();
			} else {
				ADMIN_PAGE.userID.setText("");
				ADMIN_PAGE.userName.setText("");
				User user = new User(id, name);
				
				USER_LIST.addElement(user);
				
				// Clear errors on successful add
				ADMIN_PAGE.error.setText("");
				ADMIN_PAGE.pack();
				
				//if(!USER_LIST.isEmpty())
				//	ADMIN_PAGE.userList.setSelectedIndex(0);
			}
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
		}

	}
	
	private class AdminDeleteUserListener implements ActionListener{
		
		/**
		 * Checks if song is selected, then deletes it and selects a new one.
		 * @param e
		 */ @Override
		public void actionPerformed(ActionEvent e){
			ADMIN_PAGE.error.setText("");
			
			int index = ADMIN_PAGE.userList.getSelectedIndex();
			
			// If song is selected in list, delete 
			if(index != -1){
				USER_LIST.remove(index);
			} else {
				ADMIN_PAGE.error.setText("Please select a user");
			}
		}			
	}
	
	private class AdminLogoutListener implements ActionListener, WindowListener{
		
		/**
		 * Checks if song is selected, then deletes it and selects a new one.
		 * @param e
		 */ @Override
		public void actionPerformed(ActionEvent e){
			ADMIN_PAGE.frame.dispose();
			LOGIN_PAGE.frame.setVisible(true);
			LOGIN_PAGE.inputField.setText("");
			
		}

		@Override
		public void windowActivated(WindowEvent e) {
		}

		@Override
		public void windowClosed(WindowEvent e) {
		}

		@Override
		public void windowClosing(WindowEvent e) {
			backend.saveSession();
			System.exit(0);
		}

		@Override
		public void windowDeactivated(WindowEvent e) {
		}

		@Override
		public void windowDeiconified(WindowEvent e) {
		}

		@Override
		public void windowIconified(WindowEvent e) {
		}

		@Override
		public void windowOpened(WindowEvent e) {
		}
			
	}

	
	void setLoginListeners(){
		LoginListener listener = new LoginListener();
		LOGIN_PAGE.login.addActionListener(listener);
		LOGIN_PAGE.frame.addWindowListener(listener);
	}
	
	
	void setAlbumListeners(){
		ALBUM_PAGE.addPhoto.addActionListener(new AlbumAddPhotoListener());
		ALBUM_PAGE.removePhoto.addActionListener(new AlbumRemovePhotoListener());
		ALBUM_PAGE.recaptionPhoto.addActionListener(new AlbumRecaptionPhotoListener());
		ALBUM_PAGE.moveToAlbum.addActionListener(new AlbumMoveToAlbumListener());
		ALBUM_PAGE.displayPhoto.addActionListener(new AlbumDisplayPhotoListener());
		ALBUM_PAGE.addTag.addActionListener(new AlbumAddTagListener());
		ALBUM_PAGE.deleteTag.addActionListener(new AlbumDeleteTagListener());
		ALBUM_PAGE.previous.addActionListener(new AlbumPreviousPhotoListener());
		ALBUM_PAGE.next.addActionListener(new AlbumNextPhotoListener());
		ALBUM_PAGE.photoList.getSelectionModel().addListSelectionListener(new AlbumDetailListener());
		ALBUM_PAGE.cancel.addActionListener(new AlbumCancelListener());
		ALBUM_PAGE.frame.addWindowListener(new AlbumCancelListener());
	}
	
	private class AlbumAddPhotoListener implements ActionListener{
		
		/**
		 * adds photo to album
		 * @param e
		 */ @Override
		public void actionPerformed(ActionEvent e){
			String fileName = ALBUM_PAGE.input.getText();
			File p = null;
			String name = Utils.getNameFromPath(fileName);
			
			if (fileName.isEmpty()) { // check if pathname is filled
				Toolkit.getDefaultToolkit().beep();
				ALBUM_PAGE.error.setText("File name required.");
			} else if (name.equals("")) {
				Toolkit.getDefaultToolkit().beep();
				ALBUM_PAGE.error.setText("Please enter a valid photo.");
			} else if (Utils.getPhotoFromList(name, ALBUM_PAGE.album.photos) != null){ // check if user is already in list
				Toolkit.getDefaultToolkit().beep();
				ALBUM_PAGE.error.setText("Photo already exists in album.");
			} else {
				ALBUM_PAGE.input.setText("");
				int result = CONTROLLER.addPhoto(fileName, "", ALBUM_PAGE.album.name);
				switch (result) {
				case 0: Toolkit.getDefaultToolkit().beep();
					ALBUM_PAGE.error.setText("Photo already exists in album.");
					break;
				case -1: Toolkit.getDefaultToolkit().beep();
					ALBUM_PAGE.error.setText("Invalid album.");
					break;
				case -2: Toolkit.getDefaultToolkit().beep();
					ALBUM_PAGE.error.setText("Please enter a valid photo.");
					break;
				default: 
					ALBUM_PAGE.error.setText("");
					break;
				}
			}
			if(!ALBUM_PAGE.album.photos.isEmpty())
				ALBUM_PAGE.photoList.setSelectedIndex(0);
			ALBUM_PAGE.frame.pack();
		}
	}
	
	private class AlbumRemovePhotoListener implements ActionListener{
		
		/**
		 * Checks if photo is selected, then deletes it 
		 * @param e
		 */ @Override
		public void actionPerformed(ActionEvent e){
			ALBUM_PAGE.error.setText("");
			
			int index = ALBUM_PAGE.photoList.getSelectedIndex();
			
			// If song is selectC:\Users\jeffkang93\Pictures\RUBAYBE.jpged in list, delete 
			if(index != -1){
				Photo p = ALBUM_PAGE.album.photos.get(index);
				CONTROLLER.removePhoto(p.fileName, ALBUM_PAGE.album.name);
				ALBUM_PAGE.picture.setIcon(new ImageIcon());
				if(!ALBUM_PAGE.album.photos.isEmpty())
					ALBUM_PAGE.photoList.setSelectedIndex(0);
			} else {
				Toolkit.getDefaultToolkit().beep();
				ALBUM_PAGE.error.setText("Please select a photo.");
			}
		}
			
	}
	
	private class AlbumRecaptionPhotoListener implements ActionListener{
		
		/**
		 * Checks if photo is selected, then recaptions it 
		 * @param e
		 */ @Override
		public void actionPerformed(ActionEvent e){
			String caption = ALBUM_PAGE.input.getText();
			
			ALBUM_PAGE.error.setText("");
			
			int index = ALBUM_PAGE.photoList.getSelectedIndex();
			
			// If song is selected in list, delete 
			if(index != -1){
				Photo p = ALBUM_PAGE.album.photos.get(index);
				CONTROLLER.recaptionPhoto(p.fileName, caption);
				if(!ALBUM_PAGE.album.photos.isEmpty()){
					ALBUM_PAGE.photoList.clearSelection();;
					ALBUM_PAGE.photoList.setSelectedIndex(index);
				}
				ALBUM_PAGE.input.setText("");
			} else {
				Toolkit.getDefaultToolkit().beep();
				ALBUM_PAGE.error.setText("Please select a photo.");
			}
			ALBUM_PAGE.frame.pack();
		}
			
	}
	
	private class AlbumMoveToAlbumListener implements ActionListener{
		
		/**
		 * moves photo to another album
		 * @param e
		 */ @Override
		public void actionPerformed(ActionEvent e){
			int index = ALBUM_PAGE.photoList.getSelectedIndex();
			Photo p = null;
				
			// If song is selected in list, delete 
			if(index != -1){
				p = ALBUM_PAGE.album.photos.get(index);
			} else {
				Toolkit.getDefaultToolkit().beep();
				ALBUM_PAGE.error.setText("Please select a photo.");
				return;
			}
			
			String newAlbum = ALBUM_PAGE.input.getText();
			Album na = Utils.getAlbumFromList(newAlbum, ALBUM_PAGE.user.albumList);
			
			if (newAlbum.isEmpty()) { // check if pathname is filled
				Toolkit.getDefaultToolkit().beep();
				ALBUM_PAGE.error.setText("New album required.");
				return;
			} else if (newAlbum.equals(ALBUM_PAGE.album.name)){
				Toolkit.getDefaultToolkit().beep();
				ALBUM_PAGE.error.setText("New album required.");
				return;
			} else if (na == null) {
				Toolkit.getDefaultToolkit().beep();
				ALBUM_PAGE.error.setText("Please enter valid album.");
				return;
			} else 	{
				ALBUM_PAGE.input.setText("");
				
				Photo r = Utils.getPhotoFromList(p.fileName, ALBUM_PAGE.album.getPhotoList());
				if (r != null){ 
					Toolkit.getDefaultToolkit().beep();
					ALBUM_PAGE.error.setText("Photo already exists in album.");
					return;
				}else {
					ALBUM_PAGE.album.photos.remove(index);
					Utils.getAlbumFromList(newAlbum, ALBUM_PAGE.user.albumList).addPhoto(p);
					ALBUM_PAGE.input.setText("");
					ALBUM_PAGE.error.setText("");
				}
				if(!ALBUM_PAGE.album.photos.isEmpty()){
					ALBUM_PAGE.photoList.clearSelection();
					ALBUM_PAGE.photoList.setSelectedIndex(0);
				}
			}
		}
	}
	
	private class AlbumDisplayPhotoListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			int index = ALBUM_PAGE.photoList.getSelectedIndex();
			Photo p = null;
				
			// If song is selected in list, delete 
			if(index != -1){
				p = ALBUM_PAGE.album.photos.get(index);
			} else {
				Toolkit.getDefaultToolkit().beep();
				ALBUM_PAGE.error.setText("Please select a photo.");
				return;
			}
			
			ALBUM_PAGE.picture.setIcon(p.image);
			ALBUM_PAGE.index = index;
			ALBUM_PAGE.error.setText("");
			ALBUM_PAGE.frame.pack();
		}	
	}
	
	private class AlbumPreviousPhotoListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (ALBUM_PAGE.photoList.getSelectedIndex() == -1)
				return;
			
			ALBUM_PAGE.error.setText("");
			if(ALBUM_PAGE.index < 1){
				return;
			} else {
				ALBUM_PAGE.index--;
				Photo p = ALBUM_PAGE.album.photos.get(ALBUM_PAGE.index);
				ALBUM_PAGE.picture.setIcon(p.image);
			}
			ALBUM_PAGE.frame.pack();	
		}	
	}
	
	private class AlbumNextPhotoListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (ALBUM_PAGE.photoList.getSelectedIndex() == -1)
				return;
			
			ALBUM_PAGE.error.setText("");
			if (ALBUM_PAGE.album.photos.size() <= (ALBUM_PAGE.index+1)){
				return;
			} else {
				ALBUM_PAGE.index = ALBUM_PAGE.index + 1;
				Photo p = ALBUM_PAGE.album.photos.get(ALBUM_PAGE.index);
				ALBUM_PAGE.picture.setIcon(p.image);
			}
			ALBUM_PAGE.frame.pack();
		}
	}
	
	private class AlbumCancelListener implements ActionListener, WindowListener {

		@Override
		public void windowActivated(WindowEvent arg0) {
		}

		@Override
		public void windowClosed(WindowEvent arg0) {
		}

		@Override
		public void windowClosing(WindowEvent arg0) {
			backend.saveSession();
			System.exit(0);
		}

		@Override
		public void windowDeactivated(WindowEvent arg0) {
		}

		@Override
		public void windowDeiconified(WindowEvent arg0) {
		}

		@Override
		public void windowIconified(WindowEvent arg0) {
		}

		@Override
		public void windowOpened(WindowEvent arg0) {
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			ALBUM_PAGE.frame.dispose();
			USER_PAGE.albumList.clearSelection();
			USER_PAGE.frame.setVisible(true);
		}
		
	}
	
	private class AlbumDetailListener implements ListSelectionListener {

		@Override
		public void valueChanged(ListSelectionEvent e) {
			int index = ALBUM_PAGE.photoList.getSelectedIndex();
			if (index != -1){ 
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy-HH:mm:ss");
				Photo p = ALBUM_PAGE.album.photos.get(index);
				PhotoDetails pd = CONTROLLER.listPhotoInfo(p.fileName);
				String albumString = pd.albums.toString();
				String tagString = pd.photo.tagList.toString(); 
				String content = "Name: "+pd.photo.fileName+"\n" 
						+"Album(s): "+albumString.substring(1, albumString.length()-1)+"\n"
						+"Date: "+sdf.format(pd.photo.lastModified.getTime())+"\n"
						+"Caption: "+pd.photo.caption+"\n"
						+"Tags: "+tagString.substring(1, tagString.length()-1);
				ALBUM_PAGE.photoDetails.setText(content);
				ALBUM_PAGE.frame.pack();
			} else {
				ALBUM_PAGE.photoDetails.setText("\n");
			}
		}
	}
	
	private class AlbumAddTagListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String input = ALBUM_PAGE.input.getText();
			String [] group = input.split(":",2);
			if (group.length != 2) {
				Toolkit.getDefaultToolkit().beep();
				ALBUM_PAGE.error.setText("Please enter valid type:value pair.");
				return;
			}
			String type = group[0];
			String value = group[1];
			
			
			
			int index = ALBUM_PAGE.photoList.getSelectedIndex();
			if(index != -1){
				Photo p = ALBUM_PAGE.album.photos.get(index);
				CONTROLLER.addTag(p.fileName, type, value);
				ALBUM_PAGE.error.setText("");
				ALBUM_PAGE.input.setText("");
				ALBUM_PAGE.photoList.clearSelection();;
				ALBUM_PAGE.photoList.setSelectedIndex(index);
			} else {
				Toolkit.getDefaultToolkit().beep();
				ALBUM_PAGE.error.setText("Please select a photo.");
				return;
			}
		}
	}
	
	private class AlbumDeleteTagListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String input = ALBUM_PAGE.input.getText();
			String [] group = input.split(":",2);
			if (group.length != 2) {
				Toolkit.getDefaultToolkit().beep();
				ALBUM_PAGE.error.setText("Please enter valid type:value pair.");
				return;
			}
			String type = group[0];
			String value = group[1];
			
			int index = ALBUM_PAGE.photoList.getSelectedIndex();
			if(index != -1){
				Photo p = ALBUM_PAGE.album.photos.get(index);
				CONTROLLER.deleteTag(p.fileName, type, value);
				ALBUM_PAGE.error.setText("");
				ALBUM_PAGE.input.setText("");
				ALBUM_PAGE.photoList.clearSelection();;
				ALBUM_PAGE.photoList.setSelectedIndex(index);
			} else {
				Toolkit.getDefaultToolkit().beep();
				ALBUM_PAGE.error.setText("Please select a photo.");
				return;
			}
		}
	}
	
	private class SearchPageDateListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			
			String date = SEARCH_PAGE.input.getText();						
			
			String []  group = (date.split(" ", 2));	
			
			if(group.length != 2){
				SEARCH_PAGE.error.setText("Invalid date entry: Please enter only two dates \nseparated by a space in the form MM/dd/yyyy-HH:mm:ss");
				SEARCH_PAGE.frame.pack();
				return;
			}
			List<PhotoDetails> pd = CONTROLLER.getPhotosByDate(group[0], group[1]);
			
			if(pd == null){
				SEARCH_PAGE.error.setText("Invalid date: dates must be formatted MM/dd/yyyy-HH:mm:ss");
				SEARCH_PAGE.frame.pack();
				return;
			}
			else if (pd.isEmpty()){
				SEARCH_PAGE.error.setText("No photos were found that matched the criteria.");
				SEARCH_PAGE.frame.pack();
				return;
			}
			else{
				SEARCH_PAGE.error.setText("");
				SEARCH_PAGE.input.setText("");
				SEARCH_PAGE.frame.pack();
				SEARCH_PAGE.photoDLM.clear();								
				for(PhotoDetails p : pd){
					SEARCH_PAGE.photoDLM.addElement(p.photo);					
				}
				
				if(!SEARCH_PAGE.photoDLM.isEmpty())
					SEARCH_PAGE.photoJList.setSelectedIndex(0);
			}
		}
		
	}
	
	private class SearchByTypeValue implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e){
			String typeValue = SEARCH_PAGE.input.getText();
			String [] group = (typeValue.split(","));
			List<PhotoDetails> lpd = CONTROLLER.getPhotosByTag(group);
			
			if (lpd.isEmpty()){
				SEARCH_PAGE.error.setText("No photos were found that matched the criteria.");
			}
			else{
				SEARCH_PAGE.error.setText("");
				SEARCH_PAGE.input.setText("");
				SEARCH_PAGE.photoDLM.clear();								
				for(PhotoDetails p : lpd){
					SEARCH_PAGE.photoDLM.addElement(p.photo);					
				}
				
				if(!SEARCH_PAGE.photoDLM.isEmpty())
					SEARCH_PAGE.photoJList.setSelectedIndex(0);
			}
			
		}
	}
	
	
	private class SearchDisplayPhotoListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			int index = SEARCH_PAGE.photoJList.getSelectedIndex();
			Photo p = null;
				
			// If song is selected in list, delete 
			if(index != -1){
				p = SEARCH_PAGE.photoDLM.get(index);
			} else {
				Toolkit.getDefaultToolkit().beep();
				SEARCH_PAGE.error.setText("Please select a photo.");
				return;
			}
			
			SEARCH_PAGE.picture.setIcon(p.image);
			SEARCH_PAGE.index = index;
			SEARCH_PAGE.error.setText("");
			SEARCH_PAGE.frame.pack();
		}	
	}
	
	private class SearchPreviousPhotoListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (SEARCH_PAGE.photoJList.getSelectedIndex() == -1)
				return;
			
			SEARCH_PAGE.error.setText("");
			if(SEARCH_PAGE.index < 1){
				return;
			} else {
				SEARCH_PAGE.index--;
				Photo p = SEARCH_PAGE.photoDLM.get(SEARCH_PAGE.index);
				SEARCH_PAGE.picture.setIcon(p.image);
			}
			SEARCH_PAGE.frame.pack();	
		}	
	}
	
	private class SearchNextPhotoListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (SEARCH_PAGE.photoJList.getSelectedIndex() == -1)
				return;
			
			SEARCH_PAGE.error.setText("");
			if (SEARCH_PAGE.photoDLM.size() <= (SEARCH_PAGE.index+1)){
				return;
			} else {
				SEARCH_PAGE.index = SEARCH_PAGE.index + 1;
				Photo p = SEARCH_PAGE.photoDLM.get(SEARCH_PAGE.index);
				SEARCH_PAGE.picture.setIcon(p.image);
			}
			SEARCH_PAGE.frame.pack();
		}
	}
	
	private class SearchCancelListener implements ActionListener, WindowListener {

		@Override
		public void windowActivated(WindowEvent arg0) {
		}

		@Override
		public void windowClosed(WindowEvent arg0) {
		}

		@Override
		public void windowClosing(WindowEvent arg0) {
			backend.saveSession();
			System.exit(0);
		}

		@Override
		public void windowDeactivated(WindowEvent arg0) {
		}

		@Override
		public void windowDeiconified(WindowEvent arg0) {
		}

		@Override
		public void windowIconified(WindowEvent arg0) {
		}

		@Override
		public void windowOpened(WindowEvent arg0) {
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			USER_PAGE.frame.setVisible(true);
			SEARCH_PAGE.frame.dispose();
		}
		
	}
	
	private class SearchDetailListener implements ListSelectionListener {

		@Override
		public void valueChanged(ListSelectionEvent e) {
			int index = SEARCH_PAGE.photoJList.getSelectedIndex();
			if (index != -1){ 
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy-HH:mm:ss");
				Photo p = SEARCH_PAGE.photoDLM.get(index);
				PhotoDetails pd = CONTROLLER.listPhotoInfo(p.fileName);
				String albumString = pd.albums.toString();
				String tagString = pd.photo.tagList.toString(); 
				String content = "Name: "+pd.photo.fileName+"\n" 
						+"Album(s): "+albumString.substring(1, albumString.length()-1)+"\n"
						+"Date: "+sdf.format(pd.photo.lastModified.getTime())+"\n"
						+"Caption: "+pd.photo.caption+"\n"
						+"Tags: "+tagString.substring(1, tagString.length()-1);
				SEARCH_PAGE.photoDetails.setText(content);
				SEARCH_PAGE.frame.pack();
			} else {
				SEARCH_PAGE.photoDetails.setText("\n");
			}
		}
	}
	
	private class SearchAddTagListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String input = SEARCH_PAGE.input.getText();
			String [] group = input.split(":",2);
			if (group.length != 2) {
				Toolkit.getDefaultToolkit().beep();
				SEARCH_PAGE.error.setText("Please enter valid type:value pair.");
				return;
			}
			String type = group[0];
			String value = group[1];
			
			int index = SEARCH_PAGE.photoJList.getSelectedIndex();
			if(index != -1){
				Photo p = SEARCH_PAGE.photoDLM.get(index);
				CONTROLLER.addTag(p.fileName, type, value);
				SEARCH_PAGE.error.setText("");
				SEARCH_PAGE.input.setText("");
				SEARCH_PAGE.photoJList.clearSelection();
				SEARCH_PAGE.photoJList.setSelectedIndex(index);
			} else {
				Toolkit.getDefaultToolkit().beep();
				SEARCH_PAGE.error.setText("Please select a photo.");
				return;
			}
		}
	}
	
	private class SearchDeleteTagListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String input = SEARCH_PAGE.input.getText();
			String [] group = input.split(":",2);
			if (group.length != 2) {
				Toolkit.getDefaultToolkit().beep();
				SEARCH_PAGE.error.setText("Please enter valid type:value pair.");
				return;
			}
			String type = group[0];
			String value = group[1];
			
			int index = SEARCH_PAGE.photoJList.getSelectedIndex();
			if(index != -1){
				Photo p = SEARCH_PAGE.photoDLM.get(index);
				CONTROLLER.deleteTag(p.fileName, type, value);
				SEARCH_PAGE.error.setText("");
				SEARCH_PAGE.input.setText("");
				SEARCH_PAGE.photoJList.clearSelection();
				SEARCH_PAGE.photoJList.setSelectedIndex(index);
			} else {
				Toolkit.getDefaultToolkit().beep();
				SEARCH_PAGE.error.setText("Please select a photo.");
				return;
			}
		}
	}	
	
	private class SearchCreateAlbum implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			String album = SEARCH_PAGE.input.getText();
			
			if (album.isEmpty()) { // check if pathname is filled
				Toolkit.getDefaultToolkit().beep();
				SEARCH_PAGE.error.setText("New album name required.");
			} else {
				boolean result = CONTROLLER.createAlbum(album);
				if(!result){
					Toolkit.getDefaultToolkit().beep();
					SEARCH_PAGE.error.setText("Album with given name already exists.");
				} else {
					int listSize = SEARCH_PAGE.photoDLM.getSize();
					for (int i = 0; i < listSize; i++){
						Utils.getAlbumFromList(album, CONTROLLER.USER.albumList).addPhoto(SEARCH_PAGE.photoDLM.get(i));
					}
					SEARCH_PAGE.error.setText("Album created.");
					SEARCH_PAGE.input.setText("");
				}
			}
		}
	}
	
	private class SearchRecaptionPhotoListener implements ActionListener{
		
		/**
		 * Checks if photo is selected, then recaptions it 
		 * @param e
		 */ @Override
		public void actionPerformed(ActionEvent e){
			String caption = SEARCH_PAGE.input.getText();
			
			SEARCH_PAGE.error.setText("");
			
			int index = SEARCH_PAGE.photoJList.getSelectedIndex();
			
			// If song is selected in list, delete 
			if(index != -1){
				Photo p = SEARCH_PAGE.photoDLM.get(index);
				CONTROLLER.recaptionPhoto(p.fileName, caption);
				if(!SEARCH_PAGE.photoDLM.isEmpty()){
					SEARCH_PAGE.photoJList.clearSelection();;
					SEARCH_PAGE.photoJList.setSelectedIndex(index);
				}
				SEARCH_PAGE.input.setText("");
			} else {
				Toolkit.getDefaultToolkit().beep();
				ALBUM_PAGE.error.setText("Please select a photo.");
			}
			ALBUM_PAGE.frame.pack();
		}
			
	}
	
	void setSearchPageListeners(){
		SEARCH_PAGE.cancel.addActionListener(new SearchCancelListener());
		SEARCH_PAGE.dateRange.addActionListener(new SearchPageDateListener());
		SEARCH_PAGE.typeValue.addActionListener(new SearchByTypeValue());
		SEARCH_PAGE.createAlbum.addActionListener(new SearchCreateAlbum());
		SEARCH_PAGE.displayPhoto.addActionListener(new SearchDisplayPhotoListener());
		SEARCH_PAGE.next.addActionListener(new SearchNextPhotoListener());
		SEARCH_PAGE.previous.addActionListener(new SearchPreviousPhotoListener());
		SEARCH_PAGE.addTag.addActionListener(new SearchAddTagListener());
		SEARCH_PAGE.deleteTag.addActionListener(new SearchDeleteTagListener());
		SEARCH_PAGE.frame.addWindowListener(new SearchCancelListener());
		SEARCH_PAGE.photoJList.getSelectionModel().addListSelectionListener(new SearchDetailListener());
		SEARCH_PAGE.recaptionPhoto.addActionListener(new SearchRecaptionPhotoListener());
	}
	
	/**
	 * The main method
	 * @param args -the input arguements from command line
	 */
	public static void main(String[] args) {
		new CmdView();
	}
}