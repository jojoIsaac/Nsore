package com.appsol.apps.projectcommunicate.model;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.appsol.apps.projectcommunicate.classes.Connector;

import android.app.backup.BackupAgentHelper;
import android.app.backup.FileBackupHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

	private static final String DBNAME = "NSORE";
	private static String createBookTable = "CREATE TABLE " + Bookmark.TBNAME
			+ "( " + Bookmark.ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + ","
			+ Bookmark.CONTENT + " text " + "," + Bookmark.DID + " INTEGER ,"
			+ Bookmark.TITLE + " text ," + Bookmark.DGID + " text,"
			+ Bookmark.devotionJson + " text )";

	private static String createOtherBookTable = "CREATE TABLE "
			+ OtherBookmarks.TBNAME + "( " + OtherBookmarks.ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT" + "," + OtherBookmarks.TITLE
			+ " text " + "," + OtherBookmarks.TYPEID + " text,"
			+ OtherBookmarks.TYPE + " text ," + OtherBookmarks.resourceJson
			+ " text )";

	private static String createNotesTable = "CREATE TABLE " + Notes.TBNAME
			+ "( " + Notes.ID + " INTEGER PRIMARY KEY " + " AUTOINCREMENT , "
			+ Notes.DATE + " text " + "," + Notes.CONTENT + " text ,"
			+ Notes.TITLE + " text, " + Notes.DATE_STRING + " text ,"
			+ Notes.DAY + " text  )";
	
	
	private static String createEventsTable = "CREATE TABLE " + SubacribedEvents.TBNAME
			+ "( " + SubacribedEvents.ID + " INTEGER PRIMARY KEY " + " AUTOINCREMENT , "
			+ SubacribedEvents.DATE + " text " + "," + SubacribedEvents.EVENTJSON + " text ,"
			+SubacribedEvents.TITLE + " text, " + SubacribedEvents.DATE_STRING + " text ,"
			+ SubacribedEvents.EVENTID + " text  )";
	
	
	private static final int DATABASE_VERSION = 1;
	Context context;

	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, DBNAME, factory, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	public DBHelper(Context x) {
		// TODO Auto-generated constructor stub
		super(x, DBNAME, null, DATABASE_VERSION);
		context = x;
	}

	public void deleteAlldata() {

		SQLiteDatabase arg0 = getWritableDatabase();
		arg0.execSQL("DROP TABLE IF EXISTS " + Bookmark.TBNAME);
		arg0.execSQL("DROP TABLE IF EXISTS " + Notes.TBNAME);
		arg0.execSQL("DROP TABLE IF EXISTS " + OtherBookmarks.TBNAME);
		arg0.execSQL("DROP TABLE IF EXISTS " + SubacribedEvents.TBNAME);

		onCreate(arg0);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		Log.v("DB", "Creating DB");
		try {

			db.execSQL(createBookTable);
			db.execSQL(createNotesTable);
			db.execSQL(createOtherBookTable);
			db.execSQL(createEventsTable);
		} catch (SQLiteException e) {
			Log.v("DB", e.getMessage());
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		Log.w("DB", "Upgrading the database from version " + arg1 + " to "
				+ arg2 + ", This will destroy all data");
		arg0.execSQL("DROP TABLE IF EXISTS " + Bookmark.TBNAME);
		arg0.execSQL("DROP TABLE IF EXISTS " + Notes.TBNAME);
		onCreate(arg0);
	}

	public long addBookMark(Bookmark b) {
		ContentValues content = new ContentValues();
		content.put(Bookmark.CONTENT, b.getContent());
		content.put(Bookmark.DID, b.getDid());
		content.put(Bookmark.TITLE, b.getTitle());
		content.put(Bookmark.DGID, b.getDaily_guide_id());
		content.put(Bookmark.devotionJson, b.getDevotion());
		SQLiteDatabase sdb = getWritableDatabase();
		long s = sdb.insert(Bookmark.TBNAME, Bookmark.TITLE, content);
		return s;
	}

	
	
	
	
	
	
	
	
	public boolean deleteBookmark(String MID) {
		SQLiteDatabase db = getWritableDatabase();
		String[] whereArgs = new String[] { MID };

		int results = db.delete(Bookmark.TBNAME, Bookmark.ID + " = ?",
				whereArgs);

		return (results > 0);
	}
	
	
	
	public boolean deleteOtherBookmark(String MID) {
		SQLiteDatabase db = getWritableDatabase();
		String[] whereArgs = new String[] { MID };

		int results = db.delete(OtherBookmarks.TBNAME, OtherBookmarks.TYPEID + " = ?",
				whereArgs);

		return (results > 0);
	}

	public boolean deleteNote(String MID) {
		SQLiteDatabase db = getWritableDatabase();
		String[] whereArgs = new String[] { MID };

		int results = db.delete(Notes.TBNAME, Notes.ID + " = ?", whereArgs);

		return (results > 0);
	}

	public boolean deleteEvent(String MID) {
		SQLiteDatabase db = getWritableDatabase();
		String[] whereArgs = new String[] { MID };

		int results = db.delete(SubacribedEvents.TBNAME, SubacribedEvents.EVENTID + " = ?", whereArgs);

		return (results > 0);
	}
	
	public Cursor getNotes() {

		Cursor cursor;
		SQLiteDatabase sdb = getReadableDatabase();
		String cols[] = new String[] { Notes.ID, Notes.CONTENT, Notes.TITLE,
				Notes.DATE, Notes.DATE_STRING,Notes.DAY };
		cursor = sdb.query(Notes.TBNAME, cols, null, null, null, null, Notes.ID
				+ " DESC ");

		return cursor;
	}
	public Cursor getevents() {

		Cursor cursor;
		SQLiteDatabase sdb = getReadableDatabase();
		String date= Connector.getdate("MMM d");
		//Log.e("DATE",date);
		String[] whereArgs = new String[] { date };
		String cols[] = new String[] { SubacribedEvents.ID, SubacribedEvents.EVENTJSON, SubacribedEvents.TITLE,
				SubacribedEvents.EVENTID,SubacribedEvents.DATE_STRING };
		cursor = sdb.query(SubacribedEvents.TBNAME, cols,SubacribedEvents.DATE_STRING+ " = ?", whereArgs, null, null, SubacribedEvents.ID
				+ " DESC ");
		

		
		return cursor;
	}
	public Cursor getBookMarks() {
		Cursor cursor = null;
		
		try
		{
			SQLiteDatabase sdb = getReadableDatabase();
			String cols[] = new String[] { Bookmark.ID, Bookmark.DID,
					Bookmark.TITLE, Bookmark.CONTENT, Bookmark.DGID,
					Bookmark.devotionJson };
			cursor = sdb.query(Bookmark.TBNAME, cols, null, null, null, null,
					Bookmark.ID + " DESC ");
		}
		catch(Exception e)
		{
			
		}
		

		return cursor;
	}
	
	
	
	
	public long addOtherBookMark(OtherBookmarks b) {
		ContentValues content = new ContentValues();
		content.put(OtherBookmarks.TYPE, b.getType());
		content.put(OtherBookmarks.TITLE, b.getTitle());
		content.put(OtherBookmarks.TYPEID, b.getTypeid());
		content.put(OtherBookmarks.resourceJson, b.getResource());
		SQLiteDatabase sdb = getWritableDatabase();
		long s = sdb.insert(OtherBookmarks.TBNAME, OtherBookmarks.TITLE, content);
		return s;
	}
	
	
	
	public Cursor getOtherBookMarks(String type,String type2) {
		Cursor cursor = null;
		
		try
		{
			SQLiteDatabase sdb = getReadableDatabase();
			String cols[] = new String[] { OtherBookmarks.ID, OtherBookmarks.TYPEID,
					OtherBookmarks.TITLE,OtherBookmarks.TYPE,
					OtherBookmarks.resourceJson };
			String[] whereArgs = new String[] { type,type2 };
			cursor = sdb.query(OtherBookmarks.TBNAME, cols, OtherBookmarks.TYPE+ " = ? OR " + OtherBookmarks.TYPE+" = ? ", whereArgs, null, null,
					OtherBookmarks.ID + " DESC ");
			
		}
		catch(Exception e)
		{
			
		}
		

		return cursor;
	}

	
	public long addEvent(SubacribedEvents b) {
		ContentValues content = new ContentValues();

		
		
	content.put(SubacribedEvents.EVENTID, b.getEventID());
	content.put(SubacribedEvents.EVENTJSON, b.getEventJson());
	content.put(SubacribedEvents.DATE_STRING, b.getDate_string());
	content.put(SubacribedEvents.TITLE, b.getTitle());

		SQLiteDatabase sdb = getWritableDatabase();
		long s = sdb.insert(SubacribedEvents.TBNAME, SubacribedEvents.TITLE, content);
		return s;
	}
	
	
	
	public long addNote(Notes b) {
		ContentValues content = new ContentValues();

		SimpleDateFormat ft = new SimpleDateFormat("dd/MM/yyy");
		Date date = new Date();
		String c = b.getDate();

		c = ft.format(date);
		
		String day= Connector.getdate("E");
		String month = Connector.getdate("MMM d,yy");
b.setDate_string(month);
b.setDay(day);
		content.put(Notes.DATE, c);
		content.put(Notes.CONTENT, b.getcontent());
		content.put(Notes.TITLE, b.getTitle());

		content.put(Notes.DAY, b.getDay());
		content.put(Notes.DATE_STRING, b.getDate_string());

		SQLiteDatabase sdb = getWritableDatabase();
		long s = sdb.insert(Notes.TBNAME, Notes.TITLE, content);
		return s;
	}

	public long updateNote(Notes b) {
		ContentValues content = new ContentValues();
		SimpleDateFormat ft = new SimpleDateFormat("yyy-MM-dd");
		Date date = new Date();
		String c = b.getDate();
		String[] whereArgs = new String[] { b.getId() };

		// int results = db.delete(Notes.TBNAME, Notes.ID+" = ?", whereArgs) ;

		c = ft.format(date);
		String day= Connector.getdate("E");
		String month = Connector.getdate("MMM d,yy");
b.setDate_string(month);
b.setDay(day);
		content.put(Notes.DATE, c);
		content.put(Notes.CONTENT, b.getcontent());
		content.put(Notes.TITLE, b.getTitle());
//		content.put(Notes.DAY, b.getDay());
//		content.put(Notes.DATE_STRING, b.getDate_string());
		SQLiteDatabase sdb = getWritableDatabase();

		// long s=sdb.insert(Notes.TBNAME, Notes.TITLE, content);
		long s = sdb
				.update(Notes.TBNAME, content, Notes.ID + " = ?", whereArgs);
		return s;
	}
	public static class BackupAgent extends BackupAgentHelper {
		String DATABASE_NAME = DBNAME;
		String DATABASE_FILE_NAME = DBNAME+".db";
		 
		@Override
		public void onCreate() {
		FileBackupHelper dbs = new FileBackupHelper(this, DATABASE_FILE_NAME);
		addHelper("dbs", dbs);
		}
		 
		@Override
		public File getFilesDir() {
		File path = getDatabasePath(DATABASE_FILE_NAME);
		return path.getParentFile();
		}
		}
}
