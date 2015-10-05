package cs213.photoAlbum.util;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;

import cs213.photoAlbum.model.Album;
import cs213.photoAlbum.model.Photo;
import cs213.photoAlbum.model.User;

/**
 * Utility functions
 * @author Jinhei
 *
 */
public class Utils {
	
	/**
	 * Returns Album with the given name from the album list
	 * @param name - name of album
	 * @param albumList - List of albums
	 * @return Album with the given name if it exists in the list, 
	 * otherwise returns null.
	 */
	public static Album getAlbumFromList (String name, DefaultListModel<Album> albumList) {
		List<Album>listOfAlbums = Utils.defaultListModelToList(albumList);
		for (Album album : listOfAlbums) {
			if (album.name.equals(name))
				return album;
		}
		
		return null;
	}
	
	/**
	 * Returns Photo with the given name from the album list
	 * @param filename - name of photo
	 * @param photoList - List of photos
	 * @return Album with the given name if it exists in the list, 
	 * otherwise returns null.
	 */
	public static Photo getPhotoFromList (String filename, DefaultListModel<Photo> photoList) {
		
		List<Photo> listOfPhotos = Utils.defaultListModelToList(photoList);
		for (Photo photo : listOfPhotos) {
			if (photo.fileName.equals(filename))
				return photo;
		}
		
		return null;
	}
	
	/**
	 * Returns User with the given userID from userList
	 * @param userID - user ID of the user
	 * @param userList - List of users
	 * @return User with the given userID if it exists in the list,
	 * else returns null
	 */
	public static User getUserFromList (String userID, DefaultListModel<User> userList) {
		List<User> UL = Utils.defaultListModelToList(userList);
		for(User user : UL) {
			if (user.id.equals(userID)){
				Utils.debug("Utils.getUserFromList(): returning user for user "+userID);
				return user;
			}
		}
		
		Utils.debug("Utils.getUserFromList(): returning null for user "+userID);
		return null;
	}
	
	/**
	 * Logs message to system.out if debug == true
	 * Info Level
	 * @param message - message to be logged
	 */
	public static void info(String message) {
		boolean INFO = false;
		
		if(INFO) {
			System.out.println("     INFO: "+message);
		}
	}
	/**
	 * Checks for quotation marks around a string
	 * @param string - string that needs to be checked
	 * @return - return true if quotations exist, false otherwise
	 */
	public static boolean checkForQuotation(String string){
		Utils.debug(string);
		if (string.startsWith("\"") && string.endsWith("\"")){
			
			return true;
		}
		else{
			return false;
		}
		
	}
	

	/**
	 * Logs message to system.out if debug == true
	 * Debug level
	 * @param message - message to be logged
	 */
	public static void debug(String message) {
		boolean DEBUG = false;
		
		if(DEBUG)
			System.out.println("     DEBUG: "+message);
	}
	
	
	
	/**
	 * Reads a String input, tokenizes by quotes, and returns a list of string
	 * @param input - the string to be parsed and broken up
	 * @return returns a list of string that is broken up by quotes
	 */
	public static List<String> getQuoted(String input){
		List<String> output = new ArrayList<String>();
		Utils.debug("getQuoted input length = "+input.length());
		for(int i = 0; i < input.length(); i++){
			if(input.charAt(i) == '\"'){
				Utils.debug("getQuoted found left quotation mark");
				int temp = 0;
				for(int j = i+1; j < input.length(); j++){
					Utils.debug("getQuoted has j="+j+" with char "+input.charAt(j));
					if(input.charAt(j) == '\"'){
						Utils.debug("getQuoted found right quotation mark");
						String s = input.substring(i, j+1);
						Utils.debug("getQuoted found string "+s);
						output.add(s);
						temp = j;
						break;
					}
				}
				if(temp != 0)
					i = temp + 1;
			}
		}
		
		return output;
	}
	/**
	 * Gets the name of a file by parsing the filename
	 * @param fileName
	 * @return returns a String that is the name of a path
	 */
	public static String getNameFromPath(String fileName){
		String cname = "";
		try {
			cname = new File(fileName).getCanonicalPath();
		} catch (IOException e) {
			return "";
		}
		String[] group = cname.split("\\\\");
		String name = group[group.length - 1];
		return name;
	}
	/**
	 * gets a scaled picture image
	 * @param picture - picture to be scaled
	 * @return - returns a scaled down ImageIcon object
	 */
	public static ImageIcon getScaledPicture(File picture){
		try {
			int width = 400;
			ImageIcon fullPic = new ImageIcon(ImageIO.read(picture));
			int origH = fullPic.getIconHeight();
			int origW = fullPic.getIconWidth();
			
			int newH = origH*width/origW;
			return new ImageIcon(ImageIO.read(picture).getScaledInstance(width, newH, Image.SCALE_SMOOTH));
		} catch (IOException e) {
			System.out.println("Error scaling picture: "+e.toString());
		}
		return null;
	}
	/**
	 * gets a smaller scaled size of the picture
	 * @param picture - picture to be scaled
	 * @return - returns a very scaled down ImageICon object
	 */
	public static ImageIcon getThumbnail(File picture){
		try {
			return new ImageIcon(ImageIO.read(picture).getScaledInstance(15, 15, Image.SCALE_SMOOTH));
		} catch (IOException e) {
			System.out.println("Error scaling picture: "+e.toString());
		}
		return null;
	}
	/**
	 * changes a DefaultListModel object to a list object
	 * @param list - List object to be transformed
	 * @return - returns a List object of type T
	 */
	public static <T> List<T> defaultListModelToList(DefaultListModel<T> list){
		List<T> returnList = new ArrayList<T>();
		int size = list.size();
		for(int i = 0; i < size ; i++){
			returnList.add(i, list.elementAt(i));
		}
		return returnList;		
	}
	/**
	 * changes a list object to a DefaultListModel object 
	 * @param list - DefaultListModel object to be transformed
	 * @return - returns a DefaultListModel object 
	 */
	public static <T> DefaultListModel<T> listToDefaultListModel(List<T> list){
		DefaultListModel<T> returnListModel = new DefaultListModel<T>();
		int size = list.size();
		for(int i = 0; i < size ; i++){
			returnListModel.add(i, list.get(i));
		}
		return returnListModel;		
	}
	
}
