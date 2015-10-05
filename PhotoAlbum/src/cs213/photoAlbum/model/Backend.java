/* Backend.java
 * 
 * CS 213
 * Nicholas Fong 	(140006363)
 * Jeffrey Kang 	(139000087)
 * 2/18/2015
 */
package cs213.photoAlbum.model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;

import cs213.photoAlbum.util.Utils;



/**
 * The Backend class stores and retrieves from user's data directory
 * @author jeffkang93
 *
 */
public class Backend implements Serializable {
	
	public DefaultListModel <User> OLD_USER_LIST= null;
	
	public DefaultListModel<User> USER_LIST = null;
	
	public Backend(){
		OLD_USER_LIST = new DefaultListModel<User>();
		USER_LIST = new DefaultListModel<User>();
	}

	/** 
	 * Reads a User and the data attributed with the user from storage
	 * @param UserID - user ID of the user to be read
	 * @return User object read from storage. Returns null if no object was read
	 */
	public User readUser(String UserID){		
		Utils.debug("Entering Backend.readUser() with userID "+UserID+"...");
		UserID = "../data/"+UserID+".ser";
		User user = null;
		
		boolean flag = true;
		try {
			InputStream fis = new FileInputStream(UserID);
			ObjectInput input = new ObjectInputStream(fis);
			try {
				user = (User) input.readObject();
				//USER_LIST.add(user);
				
			} catch (Exception e) {
				Utils.info("errormessage error :"+e.toString());
				flag = false;
			} finally {
				input.close();
				fis.close();
			}
		} catch (Exception e) {
			Utils.info("errormessage error :"+e.toString());
			flag = false;
		}
		
		Utils.debug("Exiting Backend.readUser() with success = "+flag+"...");
		// if no exceptions, return user, else return null
		if (flag)
			return user;
		else return null;
		
	}
	/**
	 * Loads a list of the userIDs stored that is stored in memory
	 * @return a list of strings that holds the UserIDs. returns null
	 * if no list was found
	 */
	public List<String> loadUserListID(){
		Utils.debug("Entering Backend.loadUserListID()...");
		List <String> list = null;
		
		File path = new File("../data/");
		if(!path.exists())
			path.mkdir();
		
		try{
			InputStream fis = new FileInputStream("../data/UserList.txt");
			//InputStream fis = new FileInputStream("../data/testuser.ser");
			ObjectInput input = new ObjectInputStream(fis);
			try{
				list = (List<String>) input.readObject();
			} catch (Exception e){
				Utils.info("errormessage error :"+e.toString());
			} finally {
				input.close();
				fis.close();
			}
		} catch (Exception e){
			Utils.info("errormessage errors :"+e.toString());
		}
		
		Utils.debug("Exiting Backend.loadUserListID()...");
		return list;
		
	}
	
	
	/**
	 * Writes User data to storage from memory 
	 * @param user - user object of the user to be written
	 * @return true if user is successfully written to storage
	 */
	public boolean writeUser(User user){
		Utils.debug("Entering Backend.writeUser() with user "+user.id+"...");
		
		String UserID = user.id;
		UserID = "../data/"+UserID+".ser";
		
		boolean booleanFlag = true;
		try {
			OutputStream fos = new FileOutputStream(UserID);
			ObjectOutput output = new ObjectOutputStream(fos);
			try {
				
				output.writeObject(user);
				//USER_LIST.add(user);
			} catch (Exception e) {
				Utils.info("errormessage error :"+e.toString());
				booleanFlag = false;
			} finally {
				output.close();
				fos.close();
			}
		} catch (Exception e) {
			Utils.info("errormessage error :"+e.toString());
			booleanFlag = false;
			
		}
		Utils.debug("Exiting Backend.writeUser() with success = "+booleanFlag);
		return booleanFlag; 
		
	}
	/**
	 * stores a list of user ids to memory
	 * @param userList - a list of user ids that need to be saved
	 * @return true if successful, false otherwise
	 */
	public boolean saveUserListID(List <String> userList){		//list of user IDs
		Utils.debug("Entering Backend.saveUserListID()...");
		boolean booleanFlag = true;
		try {
			OutputStream fos = new FileOutputStream("../data/UserList.txt");
			ObjectOutput output = new ObjectOutputStream(fos);
			try {
				output.writeObject(userList);
			} catch(Exception e){
				Utils.info("errormessage error :" + e.toString());
				booleanFlag = false;
			
			} finally{
				output.close();
				fos.close();
			}
		}catch (Exception e){
			Utils.info("errormessage error: " + e.toString());
			booleanFlag = false;
		}			
		
		Utils.debug("Exiting Backend.saveUserListID() with success = "+booleanFlag+"...");
		return booleanFlag;
	}


	/**
	 * Deletes a the user with user ID userID from memory
	 * @param userID - user Id of user to be deleted
	 * @return true if user is successfully deleted
	 */
	
	public boolean deleteUser(String userID){
		Utils.debug("Entering Backend.deleteUser() with userID "+userID+"...");
		String name = userID;
		userID = "../data/"+userID+".ser";
		
		boolean booleanFlag = true;
		try {
			OutputStream fos = new FileOutputStream(userID);
			ObjectOutput output = new ObjectOutputStream(fos);
			try {
				fos.close();
				File f = new File(userID);
				f.delete();
			} catch (Exception e) {
				Utils.info("errormessage error :"+e.toString());	
				booleanFlag = false;
			} finally {
				output.close();

			}
		} catch (Exception e) {			
			Utils.info("errormessage error :"+e.toString());
			booleanFlag = false;
			
		} 
		
		Utils.debug("Exiting Backend.deleteUser() with return "+booleanFlag+"...");
		return booleanFlag;
		
	}
	
	/**
	 * Add a user with user ID userID
	 * @param userID - user Id of user to be added
	 * @param username - name of user to be added
	 * @return added User if user is successfully added, 
	 * else return null
	 */
	public User addUser(String userID, String username){
		Utils.debug("Entering Backend.addUser() with userID "+userID+" and username "+username+"...");
		if (Utils.getUserFromList(userID, USER_LIST) != null)
			return null;
		User user = new User(userID, username);
		
		// if user wrote correctly, return the user
		if(writeUser(user)){
			USER_LIST.addElement(user);
			Utils.debug("Exiting Backend.addUser() with return User...");
			return user;
		} else {
			Utils.debug("Exiting Backend.addUser() with return NULL");
			return null;
		}
	}
	
	/**
	 * removes a user with user ID userID
	 * @param userID - user Id of user to be added
	 * @return deletes users if they exist in userlist,
	 * else returns false
	 */
	public boolean removeUser(String userID){
		Utils.debug("Entering Backend.removeUser() with userID "+userID+"...");
		if (Utils.getUserFromList(userID, USER_LIST) == null){
			return false;
		}
		
		boolean done = deleteUser(userID);
		boolean r = false;
		if (done){
			Utils.debug("removeUser removing user");
			r = USER_LIST.removeElement(Utils.getUserFromList(userID, USER_LIST));
		}
		
		Utils.debug("Returning "+r+" from removeUser");
		return r;
	}
	
	/**
	 * Returns a list of user IDs.
	 * @return List of user IDs.
	 */
	public DefaultListModel<User> getList(){
		//Utils.debug("Backend.getList():");
		return this.USER_LIST;
	}
	
	/**
	 * starts a session when the program runs. Calls the loadUserListID method to retrieve the userlist of IDs stored in memory and converts it into a list of users
	 * @return a list of users
	 */
	public DefaultListModel<User> startSession(){
		Utils.debug("Entering Backend.startSession()...");
		List <String> listOfUserIds = null;
		DefaultListModel <User> listOfUsers = new DefaultListModel<User>();
		
		listOfUserIds = loadUserListID();
		if (listOfUserIds == null){
			listOfUserIds = new ArrayList<String>();
		}
		
		User temp;
		for (String UserID: listOfUserIds) {
			Utils.debug("Backend.startSession(): Reading user "+UserID+" from file...");
			temp = readUser(UserID);
			if(temp != null)
				listOfUsers.addElement(temp);
		}		
		
		OLD_USER_LIST = listOfUsers;
		List<User> O_U_L = Utils.defaultListModelToList(OLD_USER_LIST);
		for (User user : O_U_L) {
			Utils.debug("Backend.startSession(): Adding user "+user.id+" to USER_LIST");
			USER_LIST.addElement(user);
		}
		
		Utils.debug("Exiting Backend.startSession()...");
		return USER_LIST;
		
	}
	/**
	 * Saves the list of users as a userlist of ids into memory
	 */
	public void saveSession(){
		Utils.debug("Entering Backend.saveSession()...");
		List<String> listofUserIds = new ArrayList<String>();
		// delete old files
		List<User> O_U_L = Utils.defaultListModelToList(OLD_USER_LIST);
		for (User user: O_U_L){
			deleteUser(user.id);
			
		}
		//TODO might create a a bug with saving. Interchanging list to DefaultListModel may create issues
		// add new files
		List<User> O_U_L1 = Utils.defaultListModelToList(USER_LIST);
		for (User user: O_U_L1){
			writeUser(user);
			listofUserIds.add(user.id);
			Utils.debug("Backend.saveSession(): saving "+user.id);
		}
		saveUserListID(listofUserIds);
		Utils.debug("Exiting Backend.saveSession()...");
	}
	

	
}
