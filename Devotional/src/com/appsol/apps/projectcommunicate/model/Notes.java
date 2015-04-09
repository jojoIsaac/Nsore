package com.appsol.apps.projectcommunicate.model;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;

public class Notes {

	public static final String TBNAME = "notes", TITLE = "title", ID = "id",
			CONTENT = "content", DATE = "date", DAY = "day",
			DATE_STRING = "dateString";

	private String title;
	private String id, content, date;
	private String day, date_string;

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

	public Notes() {
		// TODO Auto-generated constructor stub
	}

	public static List<Notes> getNotes(DBHelper db) {
		Cursor cursor = db.getNotes();
		List<Notes> bookmarks = new ArrayList<Notes>();
		int cindex = -1;
		if (cursor != null) {
			while (cursor.moveToNext()) {
				cindex = cursor.getColumnIndex(ID);
				Notes bookmark = new Notes();

				bookmark.setId(cursor.getString(cindex));
				cindex = cursor.getColumnIndex(TITLE);
				// System.out.println(cursor.getString(cindex));
				bookmark.setTitle(cursor.getString(cindex));
				cindex = cursor.getColumnIndex(DATE);
				bookmark.setDate(cursor.getString(cindex));
				cindex = cursor.getColumnIndex(CONTENT);
				bookmark.setcontent(cursor.getString(cindex));
				
				cindex = cursor.getColumnIndex(DATE_STRING);
				bookmark.setDate_string(cursor.getString(cindex));
				
				cindex = cursor.getColumnIndex(DAY);
				bookmark.setDay(cursor.getString(cindex));
				
				bookmarks.add(bookmark);
			}
		}
		return bookmarks;
	}

}
