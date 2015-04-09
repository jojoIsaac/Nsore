/**
 * 
 */
package com.appsol.apps.projectcommunicate.model;

import java.util.ArrayList;
import java.util.List;

import com.appsol.apps.projectcommunicate.classes.Connector;

import android.R.string;
import android.database.Cursor;

/**
 * @author off
 *
 */
public class OtherBookmarks {

	/**
	 * 
	 */
	public static final String TBNAME="otherbookmarks",TITLE="title",ID="id"
			,TYPEID="type_ID",TYPE="type",resourceJson="devotionContent";
	
	private String title;
	private String id,typeid,type;
	private String resource;
	
	public final static  String TYPE_SUBSCRIBED_CHURCH="SC";
	public final static String TYPE_SUBSCRIBED_CHURCH_BRANCH="SBC";
	
	public void setResource(String resource) {
		this.resource = resource;
	}
	public String getResource() {
		return resource;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public String getTypeid() {
		return typeid;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTitle() {
		return title;
	}
	public void setTypeid(String typeid) {
		this.typeid = typeid;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	public String getId() {
		return id;
	}
	public String getDid() {
		return typeid;
	}
	public void setDid(String did) {
		this.typeid = did;
	}
	public OtherBookmarks() {
		// TODO Auto-generated constructor stub
	}
	public static boolean checkAlreadyBookedMarked(String ID,String type)
	{
		List<OtherBookmarks> bookmarks= OtherBookmarks.getBookMarks(Connector.dbhelper,type,type);
		boolean alreadyExist=false;
		boolean newBookmark=true;
		if(bookmarks.size()>0)
		{
			
			
			for (OtherBookmarks b : bookmarks) {
				if(b.getDid().equalsIgnoreCase(ID))
				{
					alreadyExist= true;
					newBookmark=false;
				}
			}
			
		}
		
		
		if(newBookmark && !alreadyExist)
		{
			return false;
		}
		else
			return  true;
		
		
	}
	
	
	public static boolean checkAlreadyBookedMarked(String ID,List<OtherBookmarks> bookmarks)
	{
		//List<OtherBookmarks> bookmarks= OtherBookmarks.getBookMarks(Connector.dbhelper,type);
		boolean alreadyExist=false;
		boolean newBookmark=true;
		if(bookmarks.size()>0)
		{
			
			
			for (OtherBookmarks b : bookmarks) {
				if(b.getDid().equalsIgnoreCase(ID))
				{
					alreadyExist= true;
					newBookmark=false;
				}
			}
			
		}
		
		
		if(newBookmark && !alreadyExist)
		{
			return false;
		}
		else
			return  true;
		
		
	}
	
	
	
	public static List<OtherBookmarks> getBookMarks(DBHelper db,String type,String type2)
	{
		Cursor cursor= db.getOtherBookMarks(type,type2);
		List<OtherBookmarks> bookmarks= new ArrayList<OtherBookmarks>();
		int cindex=-1;
		if(cursor!=null)
		{
			 while (cursor.moveToNext()) {
				cindex=  cursor.getColumnIndex(ID);
				OtherBookmarks bookmark= new OtherBookmarks();
				
				bookmark.setId(cursor.getString(cindex));
				cindex=cursor.getColumnIndex(TITLE);
				//System.out.println(cursor.getString(cindex));
				bookmark.setTitle(cursor.getString(cindex));
				
				cindex= cursor.getColumnIndex(resourceJson);
				bookmark.setResource(cursor.getString(cindex));
				
				
				
				cindex= cursor.getColumnIndex(TYPE);
				bookmark.setType(cursor.getString(cindex));
				cindex= cursor.getColumnIndex(TYPEID);
				bookmark.setTypeid(cursor.getString(cindex));
				
				cindex= cursor.getColumnIndex(resourceJson);
				bookmark.setResource(cursor.getString(cindex));
				bookmarks.add(bookmark);
				//devotionJson
			}
		}
		return bookmarks;
	}
	
	

}
