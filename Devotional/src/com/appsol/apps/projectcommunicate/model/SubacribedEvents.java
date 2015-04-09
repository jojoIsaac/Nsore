package com.appsol.apps.projectcommunicate.model;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;

public class SubacribedEvents {

	public static final String TBNAME = "user_Events", TITLE = "title", ID = "id",
			EVENTJSON = "eventJson", DATE = "date", DAY = "day",EVENTID="event_id",
			DATE_STRING = "dateString";
	private String title;
	private String id, content, date;
	private String day, date_string;
	private String eventID;
	private String eventJson;
	
	public void setEventID(String eventID) {
		this.eventID = eventID;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	public void setEventJson(String eventJson) {
		this.eventJson = eventJson;
	}
	public String getEventJson() {
		return eventJson;
	}
	
	
	
	
	public String getEventID() {
		return eventID;
	}
	

	public void setDate_string(String date_string) {
		this.date_string = date_string;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getDate_string() {
		return date_string;
	}

	public String getDay() {
		return day;
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

	public String getcontent() {
		return content;
	}

	public void setcontent(String content) {
		this.content = content;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDate() {
		return date;
	}

	public SubacribedEvents() {
		// TODO Auto-generated constructor stub
	}

	
	public static List<SubacribedEvents> getNotes(DBHelper db) {
		Cursor cursor = db.getevents();
		List<SubacribedEvents> bookmarks = new ArrayList<SubacribedEvents>();
		int cindex = -1;
		if (cursor != null) {
			while (cursor.moveToNext()) {
				cindex = cursor.getColumnIndex(ID);
				SubacribedEvents bookmark = new SubacribedEvents();

				bookmark.setId(cursor.getString(cindex));
				cindex = cursor.getColumnIndex(TITLE);
				// System.out.println(cursor.getString(cindex));
				bookmark.setTitle(cursor.getString(cindex));
//				cindex = cursor.getColumnIndex(DATE);
//				bookmark.setDate(cursor.getString(cindex));
				cindex = cursor.getColumnIndex(EVENTJSON);
				bookmark.setEventJson(cursor.getString(cindex));
				
				cindex = cursor.getColumnIndex(DATE_STRING);
				bookmark.setDate_string(cursor.getString(cindex));
				
				cindex = cursor.getColumnIndex(EVENTID);
				bookmark.setEventID(cursor.getString(cindex));
				
				bookmarks.add(bookmark);
			}
		}
		return bookmarks;
	}
	
	
}
