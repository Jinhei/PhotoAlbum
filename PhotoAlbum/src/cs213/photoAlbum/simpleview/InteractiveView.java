package cs213.photoAlbum.simpleview;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cs213.photoAlbum.control.Controller;
import cs213.photoAlbum.model.Album;
import cs213.photoAlbum.model.Photo;
import cs213.photoAlbum.model.User;
import cs213.photoAlbum.util.AlbumDetails;
import cs213.photoAlbum.util.PhotoDetails;
import cs213.photoAlbum.util.Tag;
import cs213.photoAlbum.util.Utils;

public class InteractiveView {
	
	/** logged in user **/
	public String userID = "";
	
	/** list of users **/
	public List<User> userList = null;
	
	/** Controller for this user **/
	public Controller controller = null;
	
	/** system in reader **/
	public Scanner scanner = null;
	
	/** parsing scanner **/
	public Scanner parser = null;
	
	/** date formatter **/
	private SimpleDateFormat sdf = null;
	
	/** Quotation mark pattern**/
	private final static Pattern pattern = Pattern.compile("(\\S+:\"[^\"]+\",?|[^\"]\\S*|\".*?\")\\s*");
	
	private final static String oldpattern = "(\\S+:\"[^\"]+\",?|[^\"]\\S*|\".*?\")\\s*";
	
	/**
	 * Creates InteractiveView object with the given controller
	 * @param controller - controller of the interactive view
	 */
	public InteractiveView(Controller controller) {
		this.userID = controller.USER.id;
		this.userList = Utils.defaultListModelToList(controller.USER_LIST);
		this.controller = controller;
		scanner = new Scanner(System.in);
		sdf = new SimpleDateFormat("MM/dd/yyyy-HH:mm:ss");
	}
	
	/**
	 * Creates an album based on the name scanned
	 */
	public void createAlbum(){
		String albumName = scanner.nextLine();
		List<String> group = Utils.getQuoted(albumName);
		
		albumName = group.get(0);
		System.out.println("album name = " + albumName);
		boolean  quotationCheck= Utils.checkForQuotation(albumName); //check for quotes*/
		if (quotationCheck == true){
			albumName = albumName.substring(1,albumName.length()-1);	//remove quotes
			if (albumName == null || albumName.equals("")){
				System.out.println("Invalid album name. please try again.");
			} else {
				if(controller.createAlbum(albumName)) {
					System.out.println("created album for user "+userID);
				} else {
					System.out.println("album exists for user "+userID);
				}
			}
	
		}
		else{
			System.out.println("Invalid Entry, Album Name needs quotation marks");
		}
	}
	/**
	 * Deletes an album based on the name scanned
	 */
	public void deleteAlbum() {

		String albumName = scanner.nextLine();
		List<String> group = new ArrayList<String>();
		
		boolean quotationCheck = Utils.checkForQuotation(albumName);
		
		if (quotationCheck == true){
			albumName = albumName.substring(1,albumName.length()-1);
			if (albumName == null || albumName.equals("")){
				System.out.println("Invalid album name. please try again.");
			} else {
				if(controller.deleteAlbum(albumName)) {
					System.out.println("deleted album for user "+userID);
				} else {
					System.out.println("album does not exist for user "+userID);
				}
			}
	
		}
		else{
			System.out.println("Invalid Entry, Album Name needs quotation marks");
		}
	}
	/**
	 * Lists all albums that exist in the user
	 */
	public void listAlbums(){
		List<AlbumDetails> detailList = controller.listAlbums();
		
		if (detailList.isEmpty()) {
			System.out.println("No albums exist for user "+userID);
		} else {
			System.out.println("Albums for user "+userID);
			
			// print out album details
			for (AlbumDetails ad : detailList) {
				// no photos in album, thus no dates
				if (ad.album.photos.isEmpty()){
					System.out.println(ad.album.name+" number of photos: 0");
				} else {
					System.out.println(ad.album.name+" number of photos: "+ad.album.photos.size()+
							", "+sdf.format(ad.start.getTime())+" - "+sdf.format(ad.end.getTime()));
				}
			}
		}
		
	}
	/**
	 * Lists all photos in an album
	 */
	public void listPhotos() {
		
		String albumName = scanner.nextLine();
		if(albumName.equals("")){
			System.out.println("Improper usage.");
			return;
		}
		albumName = albumName.substring(1);	//removed first space character
		boolean quotationCheck = Utils.checkForQuotation(albumName); //check for quotes
		
		if (quotationCheck == true){
			albumName = albumName.substring(1,albumName.length()-1);	//remove quotes
			
			List<Photo> photoList = Utils.defaultListModelToList(controller.listPhotos(albumName));
			
			// null/empty check, then print out list 
			if (photoList == null) {
				System.out.println("Album "+albumName+" does not exist");
			} else if (photoList.isEmpty()) {
				System.out.println("Album "+albumName+" is empty");
			} else {
				for (Photo photo : photoList) {
					System.out.println(photo.fileName+" - "+sdf.format(photo.lastModified.getTime()));
				}
			}
	
		}
		else{
			System.out.println("Invalid Entry, Album Name needs quotation marks.");
		}
	}
	/**
	 * Reads a File Name, Caption, and Album and creates a photo
	 */
	public void addPhoto(){
		String line = scanner.nextLine();
		List<String> group = Utils.getQuoted(line);
		if (group.size() != 3){
			System.out.println("Input is incorrect");
		}
		
		String name = group.get(0);
		String caption = group.get(1);
		String album = group.get(2);
		
		if(Utils.checkForQuotation(name) == false || Utils.checkForQuotation(caption) == false || Utils.checkForQuotation(album) == false){
			System.out.println("Proper quotation required.");
			return;			
		}
		
		name = name.replaceAll("^\"|\"$", "");
		caption = caption.replaceAll("^\"|\"$", "");
		album = album.replaceAll("^\"|\"$", "");
		
		
		int result = controller.addPhoto(name, caption, album);
		String[] pgroup = name.split("\\\\");
		String pname = pgroup[pgroup.length - 1];
		if (result == -1){
			System.out.println("Album "+album+" does not exist");
		} else if(result == 0) {
			System.out.println("Photo "+pname+" already exists in album "+album);
		} else if (result == -2) {
			System.out.println("File "+name+" does not exist");
		}else {
			Album a = Utils.getAlbumFromList(album, controller.USER.albumList);
			Photo p = a.photos.get(a.photos.size()-1);
			System.out.println("Added photo "+p.fileName+":");
			System.out.println(p.caption+
					" - Album: "+a.name);
		}
	}
	/**
	 * Reads a File Name, Old Album, and New Album and moves a photo
	 */
	public void movePhoto() {
		String line = scanner.nextLine();
		List<String> group = Utils.getQuoted(line);
		if (group.size() != 3){
			System.out.println("Input is incorrect");
		}
		
		String fileName= group.get(0);
		String oldAlbum = group.get(1);
		String newAlbum = group.get(2);
		
		if(Utils.checkForQuotation(fileName) == false || Utils.checkForQuotation(oldAlbum) == false || Utils.checkForQuotation(newAlbum) == false){
			System.out.println("Proper quotation required.");
			return;			
		}
		
		fileName = fileName.replaceAll("^\"|\"$", "");
		oldAlbum = oldAlbum.replaceAll("^\"|\"$", "");
		newAlbum = newAlbum.replaceAll("^\"|\"$", "");
		
		
		
		
		int result = controller.movePhoto(fileName, oldAlbum, newAlbum);
		
		if (result == -1) {
			System.out.println("Album "+oldAlbum+" does not exist");
		} else if (result == -2) {
			System.out.println("Album "+newAlbum+" does not exist");
		} else if (result == -3) {
			System.out.println("Photo "+fileName+" already exists in album "+newAlbum);
		} else if (result == 0) {
			System.out.println("Photo "+fileName+" does not exist in album "+oldAlbum);
		} else if (result == 1) {
			System.out.println("Moved photo "+fileName+":");
			System.out.println(fileName+" - from album "+oldAlbum+" to album "+newAlbum);
		}
	}
	/**
	 * Reads a File Name, Caption, and Album and deletes a photo
	 */
	public void removePhoto() {
		String line = scanner.nextLine();
		List<String> group = Utils.getQuoted(line);
		if (group.size() != 2){
			System.out.println("Input is incorrect");
		}
		
		String name = group.get(0);
		String caption = group.get(1);
		String album = group.get(2);
		
		if(Utils.checkForQuotation(name) == false || Utils.checkForQuotation(caption) == false || Utils.checkForQuotation(album) == false){
			System.out.println("Proper quotation required.");
			return;			
		}
		
		name = name.replaceAll("^\"|\"$", "");
		caption = caption.replaceAll("^\"|\"$", "");
		album = album.replaceAll("^\"|\"$", "");
		
		int result = controller.removePhoto(name, album);
		if (result == -1) {
			System.out.println("Album "+album+" does not exist");
		} else if (result == 0) {
			System.out.println("Photo "+name+" does not exist in album "+album);
		} else if (result == 1) {
			System.out.println("Removed photo :");
			System.out.println(name+" - from album "+album);
		}
	}
	
	/**
	 * Reads a File Name, Tag Type, and Tag Value and creates a tag for the associated photo
	 */
	public void addTag(){
		
		String string = scanner.nextLine();
		if (string.equals("")){
			System.out.println("Improper usage");
		}
		string = string.substring(1,string.length());
		String [] group = (string.split(" ", 2));
		String string2 = group[1];
		String [] group2 = string2.split(":",2);
		
		if(Utils.checkForQuotation(group[0]) == false || Utils.checkForQuotation(group2[1]) == false){
			System.out.println("Invalid Entry, File Name and Tag Value need to be in quotation marks");
			return;			
		}
		
		String fileName = group[0].replaceAll("^\"|\"$", "");
		String tagType = group2[0].replaceAll("^\"|\"$", "");
		String tagValue = group2[1].replaceAll("^\"|\"$", "");
		
		int result = controller.addTag(fileName, tagType, tagValue);
		
		if (result == 0){
			System.out.println("Tag already exists for " + fileName +" "+ tagType + ":" + tagValue );
		} else if(result == -1) {
			System.out.println("Photo "+fileName+" does not exist");
		} else if(result == 1){
			System.out.println("Added tag: " + fileName +" "+ tagType + ":" + tagValue);
		}
		
	}
	/**
	 * Reads a File Name, Tag Type, and Tag Value and creates a tag for the associated photo
	 */
	public void deleteTag(){
		
		String string = scanner.nextLine();
		if (string.equals("")){
			System.out.println("Improper usage");
		}
		string = string.substring(1,string.length());
		String [] group = (string.split(" ", 2));
		String string2 = group[1];
		String [] group2 = string2.split(":",2);
		
		if(Utils.checkForQuotation(group[0]) == false || Utils.checkForQuotation(group2[1]) == false){
			System.out.println("Invalid Entry, File Name and Tag Value need to be in quotation marks");
			return;			
		}
		
		String fileName = group[0].replaceAll("^\"|\"$", "");
		String tagType = group2[0].replaceAll("^\"|\"$", "");
		String tagValue = group2[1].replaceAll("^\"|\"$", "");
		
		int result = controller.deleteTag(fileName, tagType, tagValue);
		
		if (result == 0){
			System.out.println("Tag does not exist for " + fileName +" "+ tagType + ":" + tagValue );
		} else if(result == -1) {
			System.out.println("Photo "+fileName+" does not exist");
		} else if(result == 1){
			System.out.println("Deleted tag: " + fileName +" "+ tagType + ":" + tagValue);
		}
		
	}
	/**
	 * Reads a File Name, and lists all relevant information including photo file name, album, date, caption and tags
	 */
	public void listPhotoInfo(){
		String string = scanner.nextLine();
		
		if(Utils.checkForQuotation(string) == false){
			System.out.println("Invalid Entry, File Name needs to be in quotation marks");
			return;			
		}
		
		String fileName = string.substring(2,string.length()-1);
		
		
		PhotoDetails photoObject = controller.listPhotoInfo(fileName);
		if (photoObject == null){
			System.out.println(fileName + " does not exist");
		}
		else{
			System.out.println("Photo file name: " + fileName);
			System.out.println("Album: " + photoObject.albums.toString().substring(1 , photoObject.albums.toString().length()-1));
			System.out.println("Date: " + sdf.format(photoObject.photo.lastModified.getTime()));
			System.out.println("Caption: " + photoObject.photo.caption);
			System.out.println("Tags: ");
			for(Tag tag:photoObject.photo.tagList){
				System.out.println(tag.tagType + ":" + tag.tagValue);
			}
	
		}
	}
	/**
	 *Reads in a start date and an end date
	 *and finds all photos that are within the range in chronological order
	 */
	public void getPhotoByDate(){
		
		String string = scanner.nextLine();
		String [] group = (string.split(" ", 3));	
		if (group.length != 3) {
			System.out.println("Improper usage");
		}
		
		String startDate = group[1].replaceAll("^\"|\"$", "");
		String endDate = group[2].replaceAll("^\"|\"$", "");
		
		
		
		List <PhotoDetails> photoDetailsList = controller.getPhotosByDate(startDate, endDate);
		
		if(photoDetailsList == null){
			System.out.println("Invalid date");
		}
		
		System.out.println("Photos for user "+ controller.USER.id+ " in range " + startDate+ " to " + endDate);
		for (int i = 0; i < photoDetailsList.size(); i++){
			//System.out.println(photoDetailsList.get(i).photo.fileName);
			System.out.println(photoDetailsList.get(i).photo.caption + " - Album: " + photoDetailsList.get(i).albums.toString().substring(1, photoDetailsList.get(i).albums.toString().length()-1) +" - Date: " + sdf.format(photoDetailsList.get(i).photo.lastModified.getTime()));
		}
		
		
	}
	/**
	 * Reads in a list of tags and finds all photos that have those tags
	 * in chronological order
	 */
	public void getPhotosByTag(){
		
		String string = scanner.nextLine(); // entire string of search tags
		if (string.equals("")){
			System.out.println("Improper usage");
		}
		string = string.substring(1, string.length());
		String [] group = string.split(", ");	//string array of search tags		
		
		List<PhotoDetails> photoList = controller.getPhotosByTag(group);
		if (photoList == null){
			System.out.println("Improper usage");
			return;
		}
		System.out.println("Photos for user "+ controller.USER.id+ " with tags " + string);
		
		for (int i = 0; i < photoList.size(); i++){
			//System.out.println(photoDetailsList.get(i).photo.fileName);
			System.out.println(photoList.get(i).photo.caption + " - Album: " + photoList.get(i).albums.toString().substring(1, photoList.get(i).albums.toString().length()-1) +" - Date: " + sdf.format(photoList.get(i).photo.lastModified.getTime()));
		}
		
	}
	/**
	 * Logs out user and ends the sessions
	 */
	public void logout(){
		
		controller.endInteractiveMode();
		scanner.close();
	}
	/**
	 * Runs the interactive mode
	 */
	public void run() {
		boolean running = true;
		while(running){
			//try{
				switch(scanner.next()){
					case "createAlbum":
						createAlbum();
						break;
					case "deleteAlbum":
						deleteAlbum();
						break;
					case "listAlbums":
						listAlbums();
						break;
					case "listPhotos":
						listPhotos();
						break;
					case "addPhoto":
						addPhoto();
						break;
					case "movePhoto":
						movePhoto();
						break;
					case "removePhoto":
						removePhoto();
						break;
					case "addTag":	
						addTag();
						break;
					case "deleteTag":
						deleteTag();
						break;
					case "listPhotoInfo":
						listPhotoInfo();
						break;
					case "getPhotosBy"
							+ "":
						getPhotoByDate();
						break;					
					case "getPhotosByTag":
						getPhotosByTag();
						break;
					case "logout":
						logout();
						running = false;
						break;
					default:
						System.out.println("Please enter a valid command");
						scanner.nextLine();
						break;
			/*}
			} catch (Exception e) {
				Utils.debug("Error: "+e.toString());
				System.out.println("Command usage incorrect.");*/
			}
		}
	}
}
