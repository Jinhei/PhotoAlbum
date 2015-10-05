/* User.java
 * 
 * CS 213
 * Nicholas Fong 	(140006363)
 * Jeffrey Kang 	(139000087)
 * 2/18/2015
 */
package cs213.photoAlbum.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;

/**
 * The User class contains methods that add, delete, and rename 
 * albums in the user's album list.
 * @author Nicholas Fong
 */
public class User implements Serializable {
	/** User's unique user ID, used for login */
	public String id = "";
	
	/** User's full name */
	public String name = "";
	
	/** User's list of albums */
	public DefaultListModel<Album> albumList = null;
	
	/**
	 * Creates a new user with the given user ID and name.
	 * @param id - unique user ID (used for login)
	 * @param name - user's full name
	 */
	public User(String id, String name) {

		this.id = id;
		this.name = name;
		this.albumList = new DefaultListModel<Album>();
	}
	
	/**
	 * Adds album to the user's album list
	 * @param album - album to be added to the album list
	 * @return true if album was added
	 */
	public boolean addAlbum(Album album){
		
		albumList.addElement(album);
		return true;
		
	}
	
	/**
	 * Deletes album from user's album list
	 * @param album - album to be deleted from the album list
	 * @return true if album was deleted
	 */
	public boolean deleteAlbum(Album album){
		return albumList.removeElement(album);
	}
	
	/**
	 * Changes an album's name to a new name
	 * @param album - album to be renamed
	 * @param newName - new name for the album
	 * @return true if the album was renamed
	 */
	public boolean renameAlbum(Album album, String newName) {
		albumList.removeElement(album);
		album.name = newName;
		albumList.addElement(album);
		return true;
	}
	/**
	 * Gets a list of albums
	 * @return List<Album> if a list of albums exist or null if it does not exist
	 */
	public DefaultListModel<Album> getAlbumList(){
		return albumList;
		
	}
	
	public String toString(){
		return id;
	}
	
}
