/* PhotoDetails.java
 * 
 * CS 213
 * Nicholas Fong 	(140006363)
 * Jeffrey Kang 	(139000087)
 * 2/18/2015
 */

package cs213.photoAlbum.util;

import java.io.Serializable;
import java.util.List;

import cs213.photoAlbum.model.Photo;

/**
 * The PhotoDetails class contains a photo and the list of albums that 
 * the Photo appears in.
 * @author Jinhei
 */
public class PhotoDetails implements Serializable {
	
	/** Photo that PhotoDetail is detailing */
	public Photo photo = null;
	
	/** List of albums that the Photo appears in */
	public List<String> albums = null; 
	
	/**
	 * Creates a PhotoDetail object consisting of a Photo and 
	 * the albums it appears in.
	 * @param photo - Photo that is being detailed
	 * @param albums - List of albums that the Photo appears in
	 */
	public PhotoDetails(Photo photo, List<String> albums) {
		this.photo = photo;
		this.albums = albums;
	}
}
