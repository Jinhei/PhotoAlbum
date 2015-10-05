/* Controller.java
 * 
 * CS 213
 * Nicholas Fong 	(140006363)
 * Jeffrey Kang 	(139000087)
 * 2/18/2015
 */

package cs213.photoAlbum.control;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.swing.DefaultListModel;

import cs213.photoAlbum.model.Album;
import cs213.photoAlbum.model.Photo;
import cs213.photoAlbum.model.User;
import cs213.photoAlbum.util.AlbumDetails;
import cs213.photoAlbum.util.PhotoDetails;
import cs213.photoAlbum.util.Tag;
import cs213.photoAlbum.util.Utils;

/**
 * The Controller class does all data processing in the program
 * except for storage of objects.
 * @author Jinhei
 * @author jeffkang93
 */
public class Controller {
	
	/** Logged-in USER */
	public User USER = null;
	
	/** USER list **/
	public DefaultListModel<User> USER_LIST = null;
	
	/**
	 * Creates a new Controller for the logged-in USER
	 */
	public Controller(DefaultListModel<User> userList) {
		this.USER_LIST = userList;
	}
	
	/**
	 * Adds a new USER
	 * @param name - full name of the USER
	 * @param id - unique userID of the USER
	 * @return true if the USER was successfully created, false 
	 * if USER is already in use.
	 */
	public boolean addUser(String name, String id) {
		// TODO check if USER exists yet
		//User USER = new User(name, id);
		return true;
	}
	
	/**
	 * Deletes a USER
	 * @param userID - USER ID of the USER to be deleted
	 * @return true if the USER was successfully deleted, false if 
	 * USER did not exist.
	 */
	public boolean deleteUser(String userID) {
		return true;
	}
	
	/**
	 * Returns a list of users
	 * @return A list of users if there are users, null otherwise.
	 */
	public List<User> getUserList (){
		return null;
	}
	
	/**
	 * Checks if an album with name albumName already exists in the 
	 * USER's album list, then adds album if it does not.
	 * @param albumName - name of album to be added 
	 * @return true if album was added to the album list, false if album 
	 * with name albumName already existed in USER's album list.  
	 */
	public boolean createAlbum(String albumName){
		if (USER == null)
			Utils.debug("createAlbum(): user is null");
		// Return false if album is already in the album list
	//	if (Utils.getAlbumFromList(albumName, USER.getAlbumList()) != null){
		//	return false;
		//} else { // else add and return true
		
		for(int i = 0; i < USER.albumList.size(); i++){
			if(USER.albumList.elementAt(i).name.equals(albumName)){
				return false;				
			}
		}
		
			USER.addAlbum(new Album(albumName));
			return true;
		//}
	}
	
	/**
	 * Checks if an album with name albumName already exists in the USER's 
	 * album list, then removes the album if it does.
	 * @param albumName - name of album to be deleted
	 * @return true if album was removed, false if album did not exist
	 */
	public boolean deleteAlbum(String albumName){
		Album album = Utils.getAlbumFromList(albumName, USER.getAlbumList());
		// Return false if album is not in album list
		if (album == null) {
			return false;
		} else { // else delete and return true
			USER.deleteAlbum(album);
			return true;
		}
	}
	
	/**
	 * Checks if an album with name oldName already exists in the USER's
	 * album list, then renames the album to newName if it does.  
	 * @param oldName - name of the album to be renamed
	 * @param newName - new name of renamed album
	 * @return true if album was renamed, false if album did not exist
	 */
	
	public boolean renameAlbum(String oldName, String newName){
		Album album = Utils.getAlbumFromList(oldName, USER.getAlbumList());
		// Return false if album is not in album list
		if (album == null) {
			return false;
		} else { // else delete and add renamed album and return true
			USER.deleteAlbum(album);
			album.name = newName;
			USER.addAlbum(album);
			return true;
		}
		
		
	}
	
	/**
	 * Returns a list of the USER's albums, start date, and end date of photos taken in 
	 * the album
	 * @return a list of photo details or <b>null</b> if no photos exist. 
	 */
	public List<AlbumDetails> listAlbums() {
		List<AlbumDetails> detailList = new ArrayList<AlbumDetails>();
		
		DefaultListModel<Album> albumList1 = USER.getAlbumList(); 
		List<Album> albumList = Utils.defaultListModelToList(albumList1);
		// initialize start and end to first photo in first album of albumList
		Calendar startDate = null;
		Calendar endDate = null;
		
		// get date range
		for (Album album : albumList) {
			List<Photo> photoList = Utils.defaultListModelToList(album.getPhotoList());
			startDate = null;
			endDate = null;
			for (Photo photo : photoList) {
				Calendar photoDate = photo.lastModified;
				
				if (startDate == null) {
					startDate = photoDate;
				} if (endDate == null) {
					endDate = photoDate;
				} else {
					if (startDate.compareTo(photoDate) > 0)
						startDate = photoDate;
					if (endDate.compareTo(photoDate) < 0)
						endDate = photoDate;
				}
			}
			detailList.add(new AlbumDetails(album, startDate, endDate));
		}
		
		return detailList;
	}
	
	/**
	 * Returns a list of the photos in the specified album
	 * @param albumName - name of the album  to be listed
	 * @return List of the album's photos. Returns null if 
	 * the album was not found
	 */
	public DefaultListModel<Photo> listPhotos(String albumName) {
		Album album = Utils.getAlbumFromList(albumName, USER.albumList);
		
		// if albumName was not in USER's album list, return null
		if (album == null)
			return null;
		else return album.getPhotoList();
	}
	
	/**
	 * If no photo with name fileName already exists in
	 * the album, then it is added with the given caption.
	 * @param fileName - file name of photo to be added 
	 * @param caption - caption for the photo to be added
	 * @param albumName - album name of the photo to be added
	 * @return 1 if photo was added to album, 0 if 
	 * photo was previously in album, -1 if specified album
	 * does not exist, -2 if photo file does not exist
	 */
	public int addPhoto(String fileName, String caption, String albumName){
		File p = new File(fileName);
		if(!p.exists() || p.isDirectory()){
			Utils.debug("Contoller.addPhoto() returns file DNE for photo "+fileName);
			return -2;
		}
		
		String name = Utils.getNameFromPath(fileName);
		
		boolean photoExists = false;
		Photo oldPhoto = null;
		
		// check if photo already exists
		
		List<Album> listOfAlbums = Utils.defaultListModelToList(USER.albumList);
		
		
		for (Album album : listOfAlbums) {
			
			List<Photo> listOfPhotos = Utils.defaultListModelToList(album.photos);
			for (Photo photo : listOfPhotos) {
				if (photo.fileName.equals(name)){
					oldPhoto = photo;
					photoExists = true;
				}
			}
		}
		
		DefaultListModel<Photo> photoList = listPhotos(albumName);
		if (photoList == null){
			Utils.debug("Controller.addPhoto() returns album DNE for photo "+fileName);
			return -1;
		} else {
			Utils.debug("addPhoto(): got photoList "+photoList.toString());
		}
		
		if (Utils.getPhotoFromList(name, photoList) != null){
			Utils.debug("Controller.addPhoto() returns file already exists in album for photo "+fileName+" in album "+albumName);
			return 0;
		} else {
			
			for (Album album : listOfAlbums) {
				if (album.name.equals(albumName)){
					List<Photo> listOfPhotos = Utils.defaultListModelToList(album.photos);
					for (Photo photo : listOfPhotos) {
						if (photo.fileName.equals(name)){
							Utils.debug("Controller.addPhoto() returns file already exists in album for photo "+fileName+" in album "+albumName);
							return 0; 
						}
					}
					if (photoExists) {
						if (!caption.equals("")){
							recaptionPhoto(name, caption);
							oldPhoto.caption = caption;
						}
						album.photos.addElement(oldPhoto);
						Utils.debug("Controller.addPhoto() successful for photo "+fileName);
						return 1;
					} else {
						Calendar lastMod = Calendar.getInstance();
						lastMod.setTimeInMillis(p.lastModified());
						lastMod.set(Calendar.MILLISECOND, 0);
						
						album.photos.addElement(new Photo(name, caption, lastMod, p));
						Utils.debug("Controller.addPhoto() successful for photo "+fileName);
						return 1;
					}
				}
			}
			Utils.debug("Contoller.addPhoto() returns album DNE for photo "+fileName+" at end");
			return -1;
		}
	}
	
	/**
	 * Moves a photo with name fileName from and old album 
	 * to its new album.
	 * @param fileName - name of the photo to be moved
	 * @param oldAlbumName - name of album to be moved from
	 * @param newAlbumName - name of album to be moved to
	 * @return 1 if photo was moved, 0 if photo did 
	 * not exist in old album, -1 old album did not exist,
	 * -2 new album did not exist, -3 if photo is already in 
	 * the new album
	 */
	public int movePhoto(String fileName, String oldAlbumName, String newAlbumName) {
		DefaultListModel<Photo> oldPhotoList = listPhotos(oldAlbumName);
		// oldAlbumName DNE
		if (oldPhotoList == null)
			return -1;
		
		DefaultListModel<Photo> newPhotoList = listPhotos(newAlbumName);
		// newAlbumName DNE
		if (newPhotoList == null)
			return  -2;
		
		// return false if photo does not exist in old or exists already in new album
		if (Utils.getPhotoFromList(fileName, oldPhotoList) == null){
			return 0;
		} else if (Utils.getPhotoFromList(fileName, newPhotoList) != null){
			return -3;
		} else {
			Photo foundPhoto = null;
			Album oldAlbum = null;
			// looks for photo, then removes+adds photo
			List<Album> listOfAlbums = Utils.defaultListModelToList(USER.albumList);
			for (Album album : listOfAlbums) {
				if (album.name.equals(oldAlbumName)){
					List<Photo> listOfPhotos = Utils.defaultListModelToList(album.photos);
					for (Photo photo : listOfPhotos) {
						if (photo.fileName.equals(fileName)){
							oldAlbum = album;
							foundPhoto = photo;
						}
					}
					
				}
			}
			
			if(foundPhoto == null)
				return 0;
			
			Album newAlbum = Utils.getAlbumFromList(newAlbumName, USER.getAlbumList());
			if (newAlbum == null)
				return -2;
			oldAlbum.deletePhoto(foundPhoto);
			newAlbum.addPhoto(foundPhoto);
			return 1;
		}
	}
	
	/**
	 * If a photo with name fileName exists in the album, 
	 * it is removed.
	 * @param fileName - file name of photo to be removed
	 * @param albumName - album name of the photo to be added
	 * @return 1 if photo was removed from album, 0 
	 * if photo was not previously in album, -1 if album does 
	 * not exist
	 */
	public int removePhoto(String fileName, String albumName){
		DefaultListModel<Photo> photoList = listPhotos(albumName);
		// album albumName DNE in USER
		if (photoList == null)
			return -1;
		
		// return false if photo does not exist in album
		if (Utils.getPhotoFromList(fileName, photoList) == null){
			return 0;
		} else {
			// looks for photo, then removes photo
			List<Album> listOfAlbums = Utils.defaultListModelToList(USER.albumList);
			for (Album album : listOfAlbums) {
				if (album.name.equals(albumName)){
					List<Photo> listOfPhotos = Utils.defaultListModelToList(album.photos);
					for (Photo photo : listOfPhotos) {
						if (photo.fileName.equals(fileName)){
							album.deletePhoto(photo);
							return 1;
						}
					}
					
					// file DNE in album
					return 0;
				}
			}
			//no album albumName in album list
			return -1;
		}
	}
	
	/**
	 * If a photo with name fileName exists in the album, 
	 * its caption is changed.
	 * @param fileName - file name of photo to change caption
	 * @param caption - new caption for photo
	 * @return true if photo caption was successfully changed, 
	 * false if no photo with name fileName is in album
	 */
	public boolean recaptionPhoto(String fileName, String caption){	
		// flag if photo has been recaptioned
		boolean recaptioned = false;
		

		// recaptions all matching photos
		List<Album> listOfAlbums = Utils.defaultListModelToList(USER.albumList);	
		
		for (Album album : listOfAlbums) {
			List<Photo> listOfPhotos = Utils.defaultListModelToList(album.photos);
			for (Photo photo : listOfPhotos) {
				if (photo.fileName.equals(fileName)){
					photo.caption = caption;
					recaptioned = true;
				}
			}
		}

		return recaptioned;
	}
	
	/**
	 * Adds a unique tag type-value pair to a photo. 
	 * @param fileName - name of the photo to be tagged
	 * @param tagType - type of the tag to be added
	 * @param tagValue - value of the tag to be added
	 * @return 1 if tag was added to the photo, -1 if photo does not exist, 0 if tag type-value already exists 
	 * if the tag type-value pair already existed.
	 */
	public int addTag(String fileName, String tagType, String tagValue) {
		Tag tag = new Tag(tagType, tagValue);
		String typeValue = tagType+":"+tagValue;
		// checks if tag type-value already exists
		
		List<Album> listOfAlbums = Utils.defaultListModelToList(USER.albumList);
		for (Album album : listOfAlbums) { 
			List<Photo> listOfPhotos = Utils.defaultListModelToList(album.photos);	
			for (Photo photo : listOfPhotos) {
				for(Tag t : photo.tagList){
					if (t.toString().equals(typeValue)){
						return 0;
					}
				}
			}
		}		
		// photo does not exist if -1
		int tagAdded = -1;
		
		// add tag to all matching photos
		List<Album> listOfAlbums1 = Utils.defaultListModelToList(USER.albumList);				
		for (Album album : listOfAlbums1) {
			List<Photo> listOfPhotos = Utils.defaultListModelToList(album.photos);
			for (Photo photo : listOfPhotos) {
				if (photo.fileName.equals(fileName)){
					photo.tagList.add(tag);
					tagAdded = 1;
					return tagAdded;
				} 
			}
		}

		return tagAdded;
	}
	
	/**
	 * Deletes a tag from a photo. 
	 * @param fileName - name of the photo to remove tag
	 * @param tagType - type of the tag to be removed
	 * @param tagValue - value of the tag to be removed
	 * @return 1 if tag was deleted from photo, 0 if 
	 * if the tag type-value pair does not exist in the  photo
	 * -1 if the photo does not exist
	 */
	public int deleteTag(String fileName, String tagType, String tagValue) {
		// checks if tag type-value does not exist
		Tag tag = new Tag(tagType, tagValue);
		
		boolean photoFound = false;
		
		List<Album> listOfAlbums = Utils.defaultListModelToList(USER.albumList);	
		for (Album album : listOfAlbums) { 
			
			List<Photo> listOfPhotos = Utils.defaultListModelToList(album.photos);
			for (Photo photo : listOfPhotos) {
				if(photo.fileName.equals(fileName))
					photoFound = true;
			}
		}
		if(photoFound == false)
			return -1;
		
		// tag not found yet
		int tagDeleted = 0;
		
		// deletes all matching tags on photo
		List<Album> listOfAlbums1 = Utils.defaultListModelToList(USER.albumList);
		for (Album album : listOfAlbums1) {
			List<Photo> listOfPhotos = Utils.defaultListModelToList(album.photos);
			for (Photo photo : listOfPhotos) {
				if (photo.fileName.equals(fileName)){
					for(int i = 0; i < photo.tagList.size(); i++){
						Tag t = photo.tagList.get(i);
						if (t.tagType.equals(tagType) && t.tagValue.equals(tagValue)){
							tagDeleted = 1;
							photo.tagList.remove(i);
						}
					}
				}
			}
		}

		return tagDeleted;
	}
	
	/**
	 * Returns a PhotoDetails object detailing the photo
	 * @param fileName - name of the photo to be detailed
	 * @return photo details of the photo, null if photo
	 * does not exist.
	 */
	public PhotoDetails listPhotoInfo(String fileName) {
		List<String> albumsList = new ArrayList<String>();
		Photo photo = null;
		
		// checks all albums for photo, then adds album name to albumsList
		List<Album> listOfAlbums = Utils.defaultListModelToList(USER.albumList);
		for (Album album : listOfAlbums) {
			if (Utils.getPhotoFromList(fileName, album.getPhotoList()) != null){
				albumsList.add(album.name);
				photo = Utils.getPhotoFromList(fileName, album.getPhotoList());
			}
		}
		
		// return false if in no albums
		if (albumsList.isEmpty())
			return null;
		else return new PhotoDetails(photo, albumsList);
	}
	
	/**
	 * retrieve all photos taken within a given range of dates, in chronological order
	 * @param start_date - A string that holds the start date for photos to be retrieved
	 * @param end_date - A String that holds the end date for photos to be retrieved
	 * @return - A list of PhotoDetails; returns null if dates are improperly formatted
	 */
	public List<PhotoDetails> getPhotosByDate(String start_date, String end_date){	
		List <PhotoDetails> photoList = new ArrayList<PhotoDetails>();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy-HH:mm:ss");
		sdf.setLenient(false);
		
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		start.setLenient(false);
		end.setLenient(false);
		
		try {
			start.setTime(sdf.parse(start_date));
			end.setTime(sdf.parse(end_date));
			
			start.getTime();
			end.getTime();
			end.set(Calendar.MILLISECOND, 1);
		} catch (Exception e) {
			return null;
		}
		List<Album> listOfAlbums = Utils.defaultListModelToList(USER.getAlbumList());
		for (Album album : listOfAlbums) {
			List<Photo> listOfPhotos = Utils.defaultListModelToList(album.getPhotoList());
			for (Photo photo : listOfPhotos){
				if (photo.lastModified.compareTo(start) >= 0 && photo.lastModified.compareTo(end) <= 0){
					PhotoDetails photoDetail = listPhotoInfo(photo.fileName); 
					
					if (!photoList.contains(photoDetail))
						photoList.add(photoDetail);
				}
			}
		}
		
		if (photoList.isEmpty()){
			return photoList;
		}
		
		PhotoDetails temp = photoList.get(0);
		List<PhotoDetails> sortedList = new ArrayList<PhotoDetails>();
		for(int i = 0; i < photoList.size(); i++) {		//sort photolist 
			temp = photoList.get(i);	
			for(int j = i+1; j < photoList.size(); j++){
				PhotoDetails temp2 = photoList.get(j);
				if(temp.photo.lastModified.compareTo(temp2.photo.lastModified) > 0){
					temp = temp2;
					photoList.remove(j);
					photoList.add(j, temp);
				}
			}
			boolean contains = false;
			for(PhotoDetails pd : sortedList){
				if(pd.photo.fileName.equals(temp.photo.fileName))
					contains = true;
			}
			if(!contains)
				sortedList.add(temp);
			
		}
		
		return sortedList;
	}
	
	/**
	 * retrieves all photos with the given tags in chronological order
	 * @param tag - A tag type or value 
	 * @return - A list of PhotoDetails in chronological order. returns 
	 * null if value field is improperly formatted.
	 */
	public List<PhotoDetails> getPhotosByTag(String[] tag){
		List<PhotoDetails> photoList = new ArrayList<PhotoDetails>();
		
		for(int i = 0 ; i < tag.length; i++ ){
			List<PhotoDetails> tempPhotoList = new ArrayList<PhotoDetails>();
			String T = tag[i];
			while(T.startsWith(" ")){
				T = T.substring(1);
			}
			// type-value or just value
			if (T.contains(":")){
				String[] tGroup = T.split(":", 2);
				String type = tGroup[0];
				String name = tGroup[1];
				
				tempPhotoList = getPhotosByTagTypeValue(type, name);
			}
			else{
				Utils.debug("getPhotosByTag() found type: "+T);
				String name = T;
				
				tempPhotoList = getPhotosByTagValue(name);
			}
			for(PhotoDetails pd : tempPhotoList) {
				boolean contains = false;
				for (PhotoDetails plpd : photoList){
					if(plpd.photo.fileName.equals(pd.photo.fileName))
						contains = true;
				}
				if(!contains)
					photoList.add(pd);
			}
		}
		
		for(int i = 1; i < photoList.size(); i++) {		//sort photolist 
			Calendar temp = photoList.get(i).photo.lastModified;
			int ptr = i;
			for(int j = i-1 ; j >= 0; j--){
				if (photoList.get(j).photo.lastModified.compareTo(temp)>0){
				photoList.get(j+1).photo.lastModified = photoList.get(j).photo.lastModified;
					ptr = j;
				}								
				
			}
			
		}
		
		return photoList;
	}
	
	/**
	 * Method used by getPhotosByTag for searching only by value
	 * @param value - A String containing a tag value
	 * @return - A list of PhotoDetails of photos containing this tag value
	 */
	public List<PhotoDetails> getPhotosByTagValue(String value){
		List<PhotoDetails> photoList = new ArrayList<PhotoDetails>();
		List<Album> listOfAlbums = Utils.defaultListModelToList(USER.getAlbumList());		
		
		for (Album album : listOfAlbums){
			List<Photo> listOfPhotos = Utils.defaultListModelToList(album.getPhotoList());	
			for (Photo photo : listOfPhotos){
				for (Tag t : photo.tagList){
					if(t.tagValue.equals(value)){
						PhotoDetails photoDetail = listPhotoInfo(photo.fileName);
						if (!photoList.contains(photoDetail))
							photoList.add(photoDetail);
					}
				}
			}
		}
		
		return photoList;
	}
	/**
	 * Method used by getPhotosByTag for searching by type/value pair
	 * @param type - A String containing a tag type
	 * @param value - A String containing a tag value
	 * @return - A list of PhotoDetails of photos containing this type and value
	 */
	public List<PhotoDetails> getPhotosByTagTypeValue(String type, String value){
		List<PhotoDetails> photoList = new ArrayList<PhotoDetails>();
		
		List<Album> listOfAlbums = Utils.defaultListModelToList(USER.getAlbumList());
		
		
		for (Album album : listOfAlbums){
			List<Photo> listOfPhotos = Utils.defaultListModelToList(album.photos);
			for (Photo photo : listOfPhotos){
				for (Tag t : photo.tagList){
					if(t.tagType.equals(type) && t.tagValue.equals(value)){
						PhotoDetails photoDetail = listPhotoInfo(photo.fileName);
						if (!photoList.contains(photoDetail))
							photoList.add(photoDetail);
					}
				}
			}
		}
		
		return photoList;
	}
	
	/**
	 * Logs in specified USER
	 * @param userID - USER ID of USER to be logged  in
	 * @return - true is successfully logged in. False otherwise.
	 */
	public boolean login(String userID) {
		User user = Utils.getUserFromList(userID, USER_LIST);
		if (user == null)
			return false;
		
		this.USER = user;
		return true;
	}
	
	/**
	 * Ends the interactive mode
	 * @return - true if successfully able to exit the interactive mode, 
	 * false otherwise
	 */
	public boolean endInteractiveMode(){
		return true;
	}

}