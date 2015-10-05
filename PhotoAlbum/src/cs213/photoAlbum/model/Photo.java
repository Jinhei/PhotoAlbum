/* Photo.java
 * 
 * CS 213
 * Nicholas Fong 	(140006363)
 * Jeffrey Kang 	(139000087)
 * 2/18/2015
 */

package cs213.photoAlbum.model;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;

import javax.swing.ImageIcon;

import cs213.photoAlbum.util.Tag;
import cs213.photoAlbum.util.Utils;

/**
 * The class Photo contains methods for storing filename, caption, date/time, and tags 
 * as well as the ability to edit photos
 * @author jeffkang93
 *
 */
public class Photo implements Serializable{
	
	/** Photo's Filename*/
	public String fileName = "";	
	
	/** Photo's Caption*/
	public String caption = "";
	
	/**List of Tags */
	public List<Tag> tagList = null;
	
	/** Variable of type Calendar that holds data 
	 * for when the photo was last modified*/
	public Calendar lastModified = null;
	
	/** File associated with photo */
	public File file = null;
	
	/** Thumbnail for photo */
	public ImageIcon thumb = null;
	
	/** Scaled image */ 
	public ImageIcon image = null;
	
	/**
	 * Creates a photo object that takes in the filename, caption and last modified date
	 * @param fileName - file name of the photo being created
	 * @param caption - caption(if any) of the photo being created
	 * @param last_modified - last modified date of a photo
	 */
	public Photo(String fileName, String caption, Calendar last_modified, File file){
	
		this.fileName = fileName;
		this.caption = caption;
		this.lastModified = last_modified;
		this.tagList = new ArrayList<Tag>();
		this.file = file;
		this.thumb = Utils.getThumbnail(file);
		this.image = Utils.getScaledPicture(file);
	}

	/**
	 * Edits the filename of a photo
	 * @param FileName - String that holds the name of the photo
	 */
	public void editFileName(String FileName){
		this.fileName = FileName;
		
	}
	/**
	 * Edits a caption(if any) of a photo
	 * @param Caption - String that holds a caption of the photo
	 * */
	public void editCaption(String Caption){
		this.caption = Caption;
		
	}
	
	/**
	 * Adds a tag from a photo.
	 * @param tag - tag to be added
	 * @return true if tag was added successfully
	 */
	
	public boolean addTag(Tag tag){
		
		return tagList.add(tag);
	}
	
	/**
	 * Deletes a tag from a photo.
	 * @param tag - tag to be deleted
	 * @return true if tag was deleted successfully
	 */
	public boolean deleteTag(Tag tag){
		
		return tagList.remove(tag);
		
	}
	
	/**
	 * Checks a tag to see if it is equal to another tag.
	 * @param tag - tag to be checked
	 * @return true if tag is unique, false if it matches another tag
	 */
	public boolean checkTag(Tag tag){
		
		return tagList.contains(tag);		
	
	}
	
	/**
	 * Displays a list of tags in the photo
	 * @return List<Tag>, null if a list does not exist
	 */
	public List<Tag> listTag(){
		return tagList;
	}
	
	/**
	 * @Override
	 */
	public String toString() {
		return fileName;
	}
}
