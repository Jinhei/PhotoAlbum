/* Tag.java
 * 
 * CS 213
 * Nicholas Fong 	(140006363)
 * Jeffrey Kang 	(139000087)
 * 2/18/2015
 */

package cs213.photoAlbum.util;
import java.io.Serializable;

/**
 * The class Tag stores a type and value
 * constructor for a tag that is needed for a list TagList 
 * @author jeffkang93
 */
public class Tag implements Serializable{
	
	/**A tag's type*/
	public String tagType = "";
	
	/**A tag's value*/
	public String tagValue = "";	
	
	/**
	 * Creates a photo's tag data 
	 * @param type - A Tag's Type
	 * @param value - A Tag's Value 
	 */
	public Tag(String type, String value){
		tagType = type;
		tagValue = value;	
	}
	
	@Override
	public String toString(){
		return ""+tagType+":"+tagValue;
	}

}