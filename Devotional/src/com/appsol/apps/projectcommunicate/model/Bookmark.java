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
public class Bookmark {

	/**
	 * 
	 */
	public static final String TBNAME="bookmarks",TITLE="title",ID="id"
			,DID="DID",CONTENT="content",DGID="daily_guide_id",devotionJson="devotionContent";
	
	private String title;
	private String id,did,daily_guide_id;
	private String content,devotion;
	public void setDevotion(String devotion) {
		this.devotion = devotion;
	}
	public String getDevotion() {
		return devotion;
	}
	public void setDaily_guide_id(String daily_guide_id) {
		this.daily_guide_id = daily_guide_id;
	}
	public String getDaily_guide_id() {
		return daily_guide_id;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getContent() {
		return content;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTitle() {
		return title;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getId() {
		return id;
	}
	public String getDid() {
		return did;
	}
	public void setDid(String did) {
		this.did = did;
	}
	
	public Bookmark() {
		// TODO Auto-generated constructor stub
	}
	public static boolean checkAlreadyBookedMarked(String DID)
	{
		List<Bookmark> bookmarks= Bookmark.getBookMarks(Connector.dbhelper);
		boolean alreadyExist=false;
		boolean newBookmark=true;
		if(bookmarks.size()>0)
		{
			
			
			for (Bookmark b : bookmarks) {
				if(b.getDid().equalsIgnoreCase(DID))
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
	public static List<Bookmark> getBookMarks(DBHelper db)
	{
		Cursor cursor= db.getBookMarks();
		List<Bookmark> bookmarks= new ArrayList<Bookmark>();
		int cindex=-1;
		if(cursor!=null)
		{
			 while (cursor.moveToNext()) {
				cindex=  cursor.getColumnIndex(ID);
				Bookmark bookmark= new Bookmark();
				
				bookmark.setId(cursor.getString(cindex));
				cindex=cursor.getColumnIndex(TITLE);
				//System.out.println(cursor.getString(cindex));
				bookmark.setTitle(cursor.getString(cindex));
				cindex=cursor.getColumnIndex(DID);
				bookmark.setDid(cursor.getString(cindex));
				cindex=cursor.getColumnIndex(CONTENT);
				bookmark.setContent(cursor.getString(cindex));
				cindex= cursor.getColumnIndex(DGID);
				bookmark.setDaily_guide_id(cursor.getString(cindex));
				cindex= cursor.getColumnIndex(devotionJson);
				bookmark.setDevotion(cursor.getString(cindex));
				bookmarks.add(bookmark);
				//devotionJson
			}
		}
		return bookmarks;
	}
	String verse;
	public String getVerse() {
		// TODO Auto-generated method stub
		return verse;
	}
	public void setVerse(String verse) {
		this.verse = verse;
	}
	public String getDevotionjson() {
		return devotionJson;
	}
	
	

}
