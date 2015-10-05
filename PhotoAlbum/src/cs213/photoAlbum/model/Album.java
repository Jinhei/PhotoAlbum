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
import javax.swing.ImageIcon;

/**
 * The Album class contains methods to add, delete, and 
 * recaption photos. 
 * @author Jinhei
 */
public class Album implements Serializable {
	/** Album name (unique per user) */
	public String name = "";
	
	/** List of photos in album */
	public DefaultListModel<Photo> photos = null;
	
	/**
	 * Creates a new album with the given name
	 * @param name - album name (unique per user) 
	 */
	public Album(String name) {
		this.name = name;
		this.photos = new DefaultListModel<Photo>();
	}
	
	/**
	 * Adds a photo to the album.
	 * @param photo - photo to be added
	 * @return true if photo was added successfully
	 */
	public boolean addPhoto(Photo photo) {
		photos.addElement(photo);
		return true;
	}
	
	/**
	 * Checks a photo if it is the same as any other photo in the album.
	 * @param photo - photo to be checked
	 * @return true if the photo being check is unique, false if it already exists
	 */
	public boolean checkPhoto(Photo photo) {
		return photos.contains(photo);
	}
	
	/**
	 * Deletes a photo from the album 
	 * @param photo - photo to be deleted
	 * @return true if photo was deleted successfully
	 */
	public boolean deletePhoto(Photo photo) {
		return photos.removeElement(photo);
	}
	
	/**
	 * Returns the list of photos
	 * @return List of Photo
	 */
	public DefaultListModel<Photo> getPhotoList() {
		return photos;
	}
	
	/**
	 * @Override 
	 */
	public String toString() {
		return name;
	}
}
