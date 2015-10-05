/* AlbumDetails.java
 * 
 * CS 213
 * Nicholas Fong 	(140006363)
 * Jeffrey Kang 	(139000087)
 * 2/18/2015
 */

package cs213.photoAlbum.util;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import cs213.photoAlbum.model.Album;

/**
 * The AlbumDetails class contains a album and the list of albums that 
 * the Album appears in.
 * @author Jinhei
 */
public class AlbumDetails implements Serializable {
	
	/** Album that AlbumDetail is detailing */
	public Album album = null;
	
	/** start date of album **/
	public Calendar start = null;
	
	/** end date of album **/
	public Calendar end = null;
	
	/**
	 * Creates a AlbumDetail object consisting of a Album and 
	 * the albums it appears in.
	 * @param album - Album that is being detailed
	 * @param start - start date
	 * @param end - end date
	 */
	public AlbumDetails(Album album, Calendar start, Calendar end) {
		this.album = album;
		this.start = start;
		this.end = end;
	}
}
