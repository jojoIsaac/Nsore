package com.appsol.apps.projectcommunicate.classes;

import github.ankushsachdeva.emojicon.EmojiconEditText;
import github.ankushsachdeva.emojicon.EmojiconsPopup;
import github.ankushsachdeva.emojicon.EmojiconGridView.OnEmojiconClickedListener;
import github.ankushsachdeva.emojicon.EmojiconsPopup.OnEmojiconBackspaceClickedListener;
import github.ankushsachdeva.emojicon.EmojiconsPopup.OnSoftKeyboardOpenCloseListener;
import github.ankushsachdeva.emojicon.emoji.Emojicon;

import java.io.File;
import android.annotation.SuppressLint;
import android.app.backup.BackupAgentHelper;
import android.app.backup.FileBackupHelper;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupWindow.OnDismissListener;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.appsol.apps.devotional.AppController;
import com.appsol.apps.devotional.ChurchGroupFeedComment;
import com.appsol.apps.devotional.DevotionDiscussions;
import com.appsol.apps.devotional.R;
import com.appsol.apps.devotional.NsoreUserDetailsActivity;
import com.appsol.apps.projectcommunicate.model.Bookmark;
import com.appsol.apps.projectcommunicate.model.ChurchGroupFeeds;
import com.appsol.apps.projectcommunicate.model.DBHelper;
//import com.classes.Favorite;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.model.BitmapDescriptorFactory;
//import com.google.android.gms.maps.model.CameraPosition;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.MarkerOptions;
import com.appsol.apps.projectcommunicate.model.NsoreDevotionalUser;
import com.appsol.volley.classes.ExtHttpClientStack;
import com.appsol.volley.classes.SslHttpClient;
import com.appsol.volley.classes.Volley;


@SuppressLint("InlinedApi") public class Connector {
	// URLS Used to access the Server
	public final static String parentURL = "https://nsoredevotional.com/";
	public final static String groupFeedUrl=Connector.parentURL+"group_feeds_attachments/";
	public final static String ChurchLogoURL = "https://nsoredevotional.com/church_logos/";
	public final static String churchGrouplogo="https://nsoredevotional.com/group_logos/thumbs/";//thumbs
	public final static String URL = "https://nsoredevotional.com/mobile/operations.php"; // URL  to access  information  from the  Database.
	public static String imageURL = "https://nsoredevotional.com/member_photos/thumbs/";// Getting
																						// userImages
	public static String eventBannerURL = "https://nsoredevotional.com/event_banners/thumbs/"; // Getting
																								// the
																								// events
																								// Banners
	public static String leadersImages = "https://nsoredevotional.com/leaders_photos/"; // Getting
																						// the
																						// events
																						// Banners
	public static String churchBanner = "https://nsoredevotional.com/church_banners/"; // Getting
																						// the
																						// events
																						// Banners
	public static String faq_icons = "https://nsoredevotional.com/faq_icons/";// Stores
																				// the
																				// FAQ
																				// ICONS
	public static String member_cover_photosURL= "https://nsoredevotional.com/member_cover_photos/";
	public static SharedPreferences myPrefs; // Global Shared preference
	public static String prefernceName = "USER_DATA_STATS";// The Name of the
															// shared preference
	// Preference labels for saving data using the SharedPreferences myPrefs
	public static final String pref_DAILY_GUIDE_ID = "DAILY_GUIDE_ID";
	public static final String pref_DAILY_GUIDE_LESSON = "DAILY_GUIDE_LESSON";
	public static final String pref_USER_ID = "REG_USER_ID";
	public static final String pref_server_date = "DATE_SERVER";
	public static final String pref_Name = "Name";
	public static final String pref_BranchCount = "branch_Count";
	public static final String pref_ChurchDetails = "ChurchDetails";

	public static String pref_Phone = "phone";
	public static String pref_Email = "email";
	public static String pref_UserName = "UName";
	private static final String CHURCH_ID = "CHURCH_ID";
	private static final String CHURCH_NAME = "CHURCH_NAME";
	private static final String IS_LOGGED_IN = "IS_LOGGED_IN",
			REG_TIME = "REG_TIME";
	private static final String SPLASH_DONE = "splash_is_done";
	private static final String BRANCH_ID = "branch_id";
	private static final String BRANCHES_JSON = "branches";
	private static final String USERDP = "user_dp";
	private static final String TODAYDEVOTIONS = "today_devotion";
	private static String date_fetch = "date_fetch";
	private static String devotionStatus = "DSTATUS";

	public static Context context;
	public static double currentLat;
	public static double currentlong;
	public static String TempTel = "";
	public static String ID = "";
	// public static Favorite respondedDriver=null;
	public static boolean checkresponses, cancelChecks = true;
	// Folder for data Storage on users phone
	public static String AppFolder = "NSORE";
	//
	public static DBHelper dbhelper;
	public static String regID = "", contact = "";
	public static String Imagename = "";
	public static String LID = "", DID = "", title = "", message = "",
			temp_lid = "", memoryVerse = "";

	public static boolean fromBookMark = false;

	public static class Config {
		public static final boolean DEVELOPER_MODE = false;
	}

	public static class Extra {
		public static final String IMAGES = "com.nostra13.example.universalimageloader.IMAGES";
		public static final String IMAGE_POSITION = "com.nostra13.example.universalimageloader.IMAGE_POSITION";
	}   

	// Call this function with the Devotion object to access the comment Page
	public static void handleComment(Devotion devotion) {
		Intent commentIntent = new Intent(context, DevotionDiscussions.class);
		commentIntent.putExtra("title", devotion.getTitle());
		commentIntent.putExtra("lesson_ID", devotion.getLesseonid());
		commentIntent.putExtra("day", devotion.getDevotionday());
		commentIntent.putExtra("date", devotion.getNormalDevotionDate());
		commentIntent.putExtra("rawdate", devotion.getRawDate());
		commentIntent.putExtra("title", devotion.getTitle());
		context.startActivity(commentIntent);

	}
	
	
	public static void handleGroupFeedComment(ChurchGroupFeeds feeds) {
		Intent commentIntent = new Intent(context, ChurchGroupFeedComment.class);
		commentIntent.putExtra("title", feeds.getTitle());
		commentIntent.putExtra("feedid", feeds.getFeedID());
		commentIntent.putExtra("content",feeds.getDescription());
		commentIntent.putExtra("date", feeds.getDate_sent());
		//commentIntent.putExtra("rawdate", feeds.getRawDate());
		commentIntent.putExtra("title", feeds.getTitle());
		commentIntent.putExtra("UID", feeds.getMemberID());
		commentIntent.putExtra("name", feeds.getName());
		commentIntent.putExtra("churchname", feeds.getChurchname());
		commentIntent.putExtra("churchid", feeds.getChurchID());
		commentIntent.putExtra("userdevice", feeds.getDeviceID());
		commentIntent.putExtra("membersince", feeds.getMemberSince());
		commentIntent.putExtra("connected", feeds.getIsConnected());
		commentIntent.putExtra("userdp", feeds.getProfilePic());
		commentIntent.putExtra("cover", feeds.getUserCover());
		commentIntent.putExtra("mutual", feeds.getMutualFriends());

		commentIntent.putExtra("about",feeds.getAboutUser());
		context.startActivity(commentIntent);

	}
	

	public final static String faceAccountUsedAlready = "FacebookAlready";

	// public static final String
	public static void setfacebookAlreadyLoggedIn(String value) {
		Editor edits = Connector.myPrefs.edit();
		edits.putString(faceAccountUsedAlready, value);

		edits.commit();
	}

	public static String getFaceaccountusedalready() {
		String val = Connector.myPrefs.getString(faceAccountUsedAlready, "");
		return val;
	}
	private PopupWindow pwindo;
	private static JSONObject objects;
	public static void handleUserProfile(NsoreDevotionalUser nsoreUser,
			Context context,Boolean isActivity) {
		
		if(!nsoreUser.getMemberID().equalsIgnoreCase(Connector.getUserId()))
		{
		
		if(!isActivity)
		{
			//onShowPopup(user);
			Intent commentIntent = new Intent(Connector.context, NsoreUserDetailsActivity.class);
		
			commentIntent.putExtra("UID", nsoreUser.getMemberID());
			commentIntent.putExtra("name", nsoreUser.getName());
			commentIntent.putExtra("churchname", nsoreUser.getChurchname());
			commentIntent.putExtra("churchid", nsoreUser.getChurchID());
			commentIntent.putExtra("userdevice", nsoreUser.getDeviceID());
			commentIntent.putExtra("membersince", nsoreUser.getMemberSince());
			commentIntent.putExtra("connected", nsoreUser.getIsConnected());
			commentIntent.putExtra("userdp", nsoreUser.getProfilePic());
			commentIntent.putExtra("cover", nsoreUser.getUserCover());
			commentIntent.putExtra("mutual", nsoreUser.getMutualFriends());

			commentIntent.putExtra("about", nsoreUser.getAboutUser());
			//UserprofileDetailsFragment.nsoreUser= nsoreUser;
			Connector.context.startActivity(commentIntent);
			
		}
		else
		{
			onShowPopup(nsoreUser);
		}
		}
	}


	
	   @SuppressLint("NewApi") 
	   private static void onShowPopup(final NsoreDevotionalUser user){
		final PopupWindow popWindow;
		ImageLoader imageLoader = com.appsol.volley.classes.ImageCacheManager.getInstance().getImageLoader();
		
	   LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    
	   // inflate the custom popup layout
	   final View inflatedView = layoutInflater.inflate(R.layout.userprofile, null,false);
	   
	   CircularNetworkImageView imgprofile= (CircularNetworkImageView)inflatedView.findViewById(R.id.imgprofile);
	   TextView txt_name = (TextView) inflatedView.findViewById(R.id.txt_name);
	   TextView txtphone =(TextView) inflatedView.findViewById(R.id.txtphone);
	   TextView txtemail =(TextView) inflatedView.findViewById(R.id.txtemail);
	   TextView txttime=(TextView) inflatedView.findViewById(R.id.txttime);
	   Button btnconnect =(Button) inflatedView.findViewById(R.id.btnconnect);
	// find the ListView in the popup layout
	   int mDeviceHeight;
	    
	   // get device size
	   Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
	   final Point size = new Point();
	   display.getSize(size);
	   mDeviceHeight = size.y;	    
	   // set height depends on the device size
	   popWindow = new PopupWindow(inflatedView, size.x - 50,size.y - 300, true );
	   if(user!=null)
	   {
		   txt_name.setText(user.getName());
		   txtphone.setText(user.getChurchname());
		   txtemail.setText(user.getMutualFriends());
		   txttime.setText(user.getMemberSince());
		   
		   String url = Connector.imageURL + user.getProfilePic(); 
		   if (imageLoader == null)
	            imageLoader = com.appsol.volley.classes.ImageCacheManager.getInstance().getImageLoader();
			imgprofile.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.mobile));
	        if(!url.equalsIgnoreCase(Connector.imageURL))
			{
	        	imgprofile.setImageUrl(Connector.imageURL+user.getProfilePic(), imageLoader);
			}
	 
	else
		imgprofile.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.mobile));
	      Toast.makeText(Connector.context, user.getIsConnected(), Toast.LENGTH_LONG).show();
	        if(user.getIsConnected().equalsIgnoreCase("-2"))
	        {
	        	btnconnect.setText("Accept Request");
	        	user.setOperationReason("Accept Request");
	        }
	        else if(user.getIsConnected().equalsIgnoreCase("1"))
	        {
	        	
	        }
	      btnconnect.setOnClickListener( new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				handleServerRequest(user, popWindow);
			}

			
		});
	        	
	        
	   }
	   //Views
	   
	   
	 
	   // set a background drawable with rounders corners
	   popWindow.setBackgroundDrawable(Connector.context.getResources().getDrawable(R.drawable.fb_popup_bg));
	   // make it focusable to show the keyboard to enter in `EditText`
	   popWindow.setFocusable(true);
	   // make it outside touchable to dismiss the popup window
	   popWindow.setOutsideTouchable(true);
	    
	   // show the popup at bottom of the screen and set some margin at bottom ie,
	   popWindow.showAtLocation(inflatedView, Gravity.TOP, 0,150);
	   }

	   
		private static void handleServerRequest(final NsoreDevotionalUser user, PopupWindow popWindow) {
			
			String URL_FEED = "https://nsoredevotional.com/mobile/devotionHandler.php";
			StringRequest jsonReq = new StringRequest(Method.POST, URL_FEED,
					createMyReqSuccessListener(user,popWindow), createMyReqErrorListener()) {
				@Override
				protected Map<String, String> getParams() {
					Map<String, String> params = new HashMap<String, String>();
					params.put("CID", Connector.getChurchID());
					params.put("MID", user.getMemberID());
					params.put("UID", Connector.getUserId());
					params.put("reason", user.getOperationReason());
					
					return params;
				}
			};
			InputStream keyStore = Connector.context.getResources().openRawResource(R.raw.appsol);

			RequestQueue queue = Volley.newRequestQueue(Connector.context,
					new ExtHttpClientStack(new SslHttpClient(keyStore, "qwerty")));
			// Adding request to volley request queue
			queue.add(jsonReq);
		}
		
		private static Response.Listener<String> createMyReqSuccessListener(final NsoreDevotionalUser user,final PopupWindow  popWindow) {
			return new Response.Listener<String>() {

				@Override
				public void onResponse(String response) {
					if (!response.equalsIgnoreCase("No Testimony Shared.")) {
						//user.setServerResponse(response);
						//Toast.makeText(Connector.context, response, Toast.LENGTH_LONG).show();
						if(response.trim().contains("Friend Accepted"))
						{
							  //holder.btnConnect.setText("Send Request");
							    user.setOperationReason("Send Request");
							    user.setIsConnected("1");
							    
							    if( popWindow!=null)
							    {
							    	 popWindow.dismiss();
							    }
							   
						}
					}
				}
			};
		}

		private static Response.ErrorListener createMyReqErrorListener() {
			return new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					showErrorDialog(error.getMessage());
					Log.d("RES", error.getMessage());
				}
			};
		}
		private static void showErrorDialog(String error) {
			boolean mInError = true;

			AlertDialog.Builder b = new AlertDialog.Builder(Connector.context);
			b.setMessage("Error occured: " + error);
			b.show();
		}
	   
	   
	   
	   
	
	// Utility Functions
	public static void openDB(Context c) {
		Connector.dbhelper = new DBHelper(c);
	}

	public static void closeDB() {
		Connector.dbhelper.close();

	}

	public static boolean isInternetAvailable(Context c) {
		ConnectivityManager cm = (ConnectivityManager) c
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetWork = cm.getActiveNetworkInfo();
		return (activeNetWork != null && activeNetWork
				.isConnectedOrConnecting());
	}

	public static void createCall(String number, Context x) {
		if (number != "") {
			String numbers = "tel:" + number;
			Intent ints = new Intent(Intent.ACTION_CALL, Uri.parse(numbers));
			x.startActivity(ints);
		}
	}

	public static String getDate() {
		SimpleDateFormat ft = new SimpleDateFormat("dd/MM/yyy");
		Date date = new Date();
		String c = "";

		c = ft.format(date);

		return c;
	}

	public static String getDay() {
		SimpleDateFormat ft = new SimpleDateFormat("dd");
		Date date = new Date();
		String c = "";

		c = ft.format(date);

		return c;
	}

	public static String getYear() {
		SimpleDateFormat ft = new SimpleDateFormat("yyy");
		Date date = new Date();
		String c = "";

		c = ft.format(date);

		return c;
	}

	public static String getMonth() {
		SimpleDateFormat ft = new SimpleDateFormat("MM");
		Date date = new Date();
		String c = "";

		c = ft.format(date);

		return c;
	}

	public static String getdate(String format) {
		SimpleDateFormat ft = new SimpleDateFormat(format);
		Date date = new Date();
		String c = "";

		c = ft.format(date);

		return c;
	}

	// Functions Handling server interaction
	public static String processStream(InputStream ins) {
		// InputStream ins = response.getEntity().getContent();
		if (ins != null) {
			String line = "";
			StringBuffer sb = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					ins));
			try {
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return sb.toString();
		}
		return "";
	}

	public static String sendData(ArrayList<NameValuePair> parameters, Context c) {
		try {
			if (isInternetAvailable(c)) {
				HttpClient client = new DefaultHttpClient();
				HttpPost posts = new HttpPost(URL);

				posts.setEntity(new UrlEncodedFormEntity(parameters));

				HttpResponse response = client.execute(posts);

				if (response.getStatusLine().getStatusCode() == 200) {
					InputStream ins = response.getEntity().getContent();
					if (ins != null) {
						String line = "";
						StringBuffer sb = new StringBuffer();
						BufferedReader reader = new BufferedReader(
								new InputStreamReader(ins));
						while ((line = reader.readLine()) != null) {
							sb.append(line);
						}
						return sb.toString();
					}
				}
			} else {
				return "No Internet Connection";
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "";
	}

	/*
	 * Sets the font on all TextViews in the ViewGroup. Searches recursively for
	 * all inner ViewGroups as well. Just add a check for any other views you
	 * want to set as well (EditText, etc.)
	 */
	public static void setFont(ViewGroup group, Typeface font) {
		int count = group.getChildCount();
		View v;
		for (int i = 0; i < count; i++) {
			v = group.getChildAt(i);
			if (v instanceof TextView || v instanceof EditText
					|| v instanceof Button) {
				((TextView) v).setTypeface(font);
			} else if (v instanceof ViewGroup)
				setFont((ViewGroup) v, font);
		}
	}

	public static String sendDataForJson(ArrayList<NameValuePair> parameters,
			Context c) {
		try {
			if (isInternetAvailable(c)) {
				HttpClient client = new DefaultHttpClient();
				HttpPost posts = new HttpPost(URL);

				posts.setEntity(new UrlEncodedFormEntity(parameters));

				HttpResponse response = client.execute(posts);

				if (response.getStatusLine().getStatusCode() == 200) {
					InputStream ins = response.getEntity().getContent();
					if (ins != null) {
						String line = "";
						StringBuffer sb = new StringBuffer();
						BufferedReader reader = new BufferedReader(
								new InputStreamReader(ins));
						while ((line = reader.readLine()) != null) {
							sb.append(line);
						}
						return sb.toString();
					}
				}
			} else {
				return "No Internet Connection";
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "";
	}

	// functions to manipulate specific data in the Sharedpreference
	public static void setUserInfo(String name, String email,
			String phone_number, String username) {
		Editor edits = Connector.myPrefs.edit();
		edits.putString(pref_Name, name);

		edits.putString(pref_UserName, username);

		edits.putString(pref_Email, email);

		edits.putString(pref_Phone, phone_number);

		edits.commit();
	}

	public static void setUserName(String username) {
		Editor edits = Connector.myPrefs.edit();

		edits.putString(pref_UserName, username);

		edits.commit();
	}

	public static void setdevotionStatus(String s) {
		Editor edits = Connector.myPrefs.edit();
		edits.putString(devotionStatus, s);
		edits.commit();
	}

	public static String getdevotionalStatus() {
		String val = "";
		val = Connector.myPrefs.getString(devotionStatus, "Failed");
		return val;
	}

	// date

	public static List<String> getUserDetails() {
		List<String> user = new ArrayList<String>();
		String val = "";
		val = Connector.myPrefs.getString(pref_Name, "");
		user.add(val);
		val = Connector.myPrefs.getString(pref_UserName, "");
		user.add(val);
		val = Connector.myPrefs.getString(pref_Email, "");
		user.add(val);
		val = Connector.myPrefs.getString(pref_Phone, "");
		user.add(val);
		return user;
	}

	//

	public static void setBranchCount(Integer count) {
		// pref_BranchCount
		Editor edits = Connector.myPrefs.edit();
		edits.putInt(pref_BranchCount, count);
		edits.commit();

	}

	public static Integer getBranchCount() {
		int val = 0;
		val = Connector.myPrefs.getInt(pref_BranchCount, 0);
		return val;
	}

	public static void setBranch(String branch) {
		// pref_BranchCount
		Editor edits = Connector.myPrefs.edit();
		edits.putString(BRANCH_ID, branch);
		edits.commit();

	}

	public static String getBranch() {
		String val = "";
		val = Connector.myPrefs.getString(BRANCH_ID, "NOT_SET");
		return val;
	}

	public static void setUserdp(String userdp) {
		// pref_BranchCount
		Editor edits = Connector.myPrefs.edit();
		edits.putString(USERDP, userdp);
		edits.commit();

	}

	public static String getUserdp() {
		String val = "";
		val = Connector.myPrefs.getString(USERDP, "NOT_SET");
		return val;
	}

	public static void setDevotion(String userdp) {
		// pref_BranchCount
		Editor edits = Connector.myPrefs.edit();
		edits.putString(TODAYDEVOTIONS, userdp);
		edits.commit();

	}

	public static void setdevotionDate(String date) {
		Editor edits = Connector.myPrefs.edit();
		edits.putString(date_fetch, date);
		edits.commit();
	}

	public static String getdevotionDate() {
		String val = "";
		val = Connector.myPrefs.getString(date_fetch, "");
		return val;

	}

	public static String getdevotion() {
		String val = "";
		val = Connector.myPrefs.getString(TODAYDEVOTIONS, "NOT_SET");
		return val;
	}

	public static void setChurchJson(String churchdeatils) {
		// pref_BranchCount
		Editor edits = Connector.myPrefs.edit();
		edits.putString(pref_ChurchDetails, churchdeatils);
		edits.commit();

	}

	public static void setChurchImage(String imageName) {
		Editor edits = Connector.myPrefs.edit();
		edits.putString("CHURCH_LOGO", imageName);
		edits.commit();
	}

	public static String getChurchJson() {
		String val = "";
		val = Connector.myPrefs.getString(pref_ChurchDetails, "NOT_SET");
		return val;
	}

	public static void setBranches(String branch) {
		// pref_BranchCount
		Editor edits = Connector.myPrefs.edit();
		edits.putString(BRANCHES_JSON, branch);
		edits.commit();

	}

	public static String getBranches() {
		String val = "";
		val = Connector.myPrefs.getString(BRANCHES_JSON, "NOT_SET");
		return val;
	}

	static String CurrentID = "CURR_DEV";

	public static void setCURRDEV(String date) {
		Editor edits = Connector.myPrefs.edit();
		edits.putString(CurrentID, date);
		edits.commit();
	}

	public static String getCURRDEV() {
		String val = "";
		val = Connector.myPrefs.getString(CurrentID, getDate());
		return val;
	}

	public static void setServerDate(String date) {
		Editor edits = Connector.myPrefs.edit();
		edits.putString(pref_server_date, date);
		edits.commit();
	}

	public static String getServerTime() {
		String val = "";
		val = Connector.myPrefs.getString(pref_server_date, getDate());
		return val;
	}

	//
	public static void setRegTime() {
		Editor edits = Connector.myPrefs.edit();
		edits.putString(REG_TIME, getDate());
		edits.commit();
	}

	public static String getRegTime() {
		String val = "";
		val = Connector.myPrefs.getString(REG_TIME, getDate());
		return val;
	}

	// /

	public static void setIsSplashDone(boolean b) {
		Editor edits = Connector.myPrefs.edit();
		edits.putBoolean(SPLASH_DONE, b);
		edits.commit();
	}

	public static void setDailyGuide(Context c, String ID) {
		Editor edits = Connector.myPrefs.edit();
		edits.putString(pref_DAILY_GUIDE_ID, ID);
		edits.commit();
	}

	public static String getDialyGuide() {
		String val = "";
		val = Connector.myPrefs
				.getString(pref_DAILY_GUIDE_ID, "NO_GUIDE_FOUND");
		return val;
	}

	// Church name and ID
	public static void setisLoggedIn(Context c, boolean name) {
		Editor edits = Connector.myPrefs.edit();
		edits.putBoolean(IS_LOGGED_IN, name);
		edits.commit();
	}

	public static Boolean getisLOggedIn() {
		Boolean val = false;
		val = Connector.myPrefs.getBoolean(IS_LOGGED_IN, false);
		return val;
	}

	public static Boolean getisSplashDone() {
		Boolean val = false;
		val = Connector.myPrefs.getBoolean(SPLASH_DONE, false);
		return val;
	}

	public static void setChurchName(Context c, String name) {
		Editor edits = Connector.myPrefs.edit();
		edits.putString(CHURCH_NAME, name);
		edits.commit();
	}

	public static String getChurchName() {
		String val = "";
		val = Connector.myPrefs.getString(CHURCH_NAME, "NO_CHURCH_FOUND");
		return val;
	}

	public static void setChurchID(Context c, String ID) {
		Editor edits = Connector.myPrefs.edit();
		edits.putString(CHURCH_ID, ID);
		edits.commit();
	}

	public static String getChurchID() {
		String val = "";
		val = Connector.myPrefs.getString(CHURCH_ID, "NO_CHURCH_FOUND");
		return val;
	}

	public static String shareText(Context context2, String content) {
		String status = "";
		if (TextUtils.isEmpty(content)) {
			status = "NOT_SET";
		} else {
			Intent shareIntent = new Intent();
			shareIntent.setAction(Intent.ACTION_SEND);
			shareIntent.putExtra(Intent.EXTRA_TEXT, content);
			shareIntent.setType("text/*");
			context2.startActivity(shareIntent);
			status = "SHARED";
		}

		return status;

	}

	public static void setUserID(Context c, String ID) {
		Editor edits = Connector.myPrefs.edit();
		edits.putString(pref_USER_ID, ID);
		edits.commit();
	}

	public static String getUserId() {
		String val = "";
		val = Connector.myPrefs.getString(pref_USER_ID, "0");
		return val;
	}

	public static void setDailyGuideLesson(Context c, String ID) {
		Editor edits = Connector.myPrefs.edit();
		edits.putString(pref_DAILY_GUIDE_LESSON, ID);
		edits.commit();
	}

	public static String getDialyGuideLesson() {
		String val = "";
		val = Connector.myPrefs.getString(pref_DAILY_GUIDE_LESSON, "NO_LESSON");
		return val;
	}

	//

	public static void scaleImage(ImageView view, int boundBoxInDp, Context c) {
		// Get the ImageView and its bitmap
		Drawable drawing = view.getDrawable();
		Bitmap bitmap = ((BitmapDrawable) drawing).getBitmap();

		// Get current dimensions
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		// Determine how much to scale: the dimension requiring less scaling is
		// closer to the its side. This way the image always stays inside your
		// bounding box AND either x/y axis touches it.
		float xScale = ((float) boundBoxInDp) / width;
		float yScale = ((float) boundBoxInDp) / height;
		float scale = (xScale <= yScale) ? xScale : yScale;

		// Create a matrix for the scaling and add the scaling data
		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);

		// Create a new bitmap and convert it to a format understood by the
		// ImageView
		Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
				matrix, true);
		BitmapDrawable result = new BitmapDrawable(scaledBitmap);
		width = scaledBitmap.getWidth();
		height = scaledBitmap.getHeight();

		// Apply the scaled bitmap
		view.setImageDrawable(result);

		// Now change ImageView's dimensions to match the scaled image
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view
				.getLayoutParams();
		params.width = width;
		params.height = height;
		view.setLayoutParams(params);
	}

	public int dpToPx(int dp, Context c) {
		float density = c.getResources().getDisplayMetrics().density;
		return Math.round((float) dp * density);
	}

	public static Bitmap lessResolution(String filePath, int width, int height) {
		int reqHeight = width;
		int reqWidth = height;
		BitmapFactory.Options options = new BitmapFactory.Options();

		// First decode with inJustDecodeBounds=true to check dimensions
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeFile(filePath, options);
	}

	private static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {

		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

	public static Bitmap download_Image(String url, String destination) {

		Bitmap bmp = null;
		try {
			URL ulrn = new URL(url);
			HttpURLConnection con = (HttpURLConnection) ulrn.openConnection();
			InputStream is = con.getInputStream();
			bmp = BitmapFactory.decodeStream(is);
			if (null != bmp) {
				// imagepath= filePath+
				// "/"+Connector.AppFolder+"/church_logo.jpg";

				// FileOutputStream fout= new FileOutputStream(destination);
				ByteArrayOutputStream bstream = new ByteArrayOutputStream();

				bmp.compress(Bitmap.CompressFormat.JPEG, 70, bstream);
				File f = new File(destination);
				f.createNewFile();
				FileOutputStream fouts = new FileOutputStream(f);
				fouts.write(bstream.toByteArray());
				fouts.close();
				is.close();

				return bmp;

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return bmp;
	}

	public static class GridMonthAdapter extends BaseAdapter {
		private Context mContext;
		private String year = "";
		// Keep all Images in array
		public String[] mThumbIds = { "January", "Febuary", "March", "April",
				"May", "June", "July", "August", "September", "October",
				"November", "December" };

		// Constructor
		public GridMonthAdapter(Context c) {
			mContext = c;

		}

		@Override
		public int getCount() {
			return mThumbIds.length;
		}

		@Override
		public Object getItem(int position) {
			return mThumbIds[position];
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			LayoutInflater inflate = (LayoutInflater) Connector.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View imageView = inflate.inflate(R.layout.monthview, parent, false);
			TextView monthname = (TextView) imageView
					.findViewById(R.id.monthname);
			monthname.setText(getItem(position).toString());

			return imageView;
		}

	}

	public static Boolean DevotionBookmarkcheckAlreadyExist(String ID) {
		List<Bookmark> bookmarks = Bookmark.getBookMarks(Connector.dbhelper);
		boolean alreadyExist = false;
		boolean newBookmark = true;
		if (bookmarks.size() > 0) {

			for (Bookmark b : bookmarks) {
				if (b.getDid().equalsIgnoreCase(ID)) {
					alreadyExist = true;
					newBookmark = false;
				}
			}

		}

		if (newBookmark && !alreadyExist) {
			return true;
		} else
			return false;
	}

	public static void addBookMark(final Context c, final Bookmark bookmark,
			final View layoutBookmark) {
		AlertDialog.Builder build = new AlertDialog.Builder(c);
		Connector.dbhelper = new DBHelper(Connector.context);
		build.setTitle("Bookmark").setMessage("Add to Bookmarks")
				.setPositiveButton("Yes", new OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						// if (Connector.getDialyGuide().replace(" ",
						// "").length() > 0) {
						// if (!Connector.getDialyGuide()
						// .equalsIgnoreCase("0")
						// || !Connector.getDialyGuide()
						// .equalsIgnoreCase("NO_GUIDE_FOUND")) {

						List<Bookmark> bookmarks = Bookmark
								.getBookMarks(Connector.dbhelper);
						boolean alreadyExist = false;
						boolean newBookmark = true;
						if (bookmarks.size() > 0) {

							for (Bookmark b : bookmarks) {
								if (b.getDid().equalsIgnoreCase(
										bookmark.getDid())) {
									alreadyExist = true;
									newBookmark = false;
								}
							}

						}

						if (newBookmark && !alreadyExist) {
							// Bookmark bookmark = new Bookmark();
							// bookmark.setDid(Connector.LID);
							// bookmark.setTitle(Connector.title);
							// bookmark.setContent(Connector.message);
							// bookmark.setDaily_guide_id(Connector
							// .getDialyGuide());
							// bookmark.setDevotion(Connector
							// .getdevotion());

							Long s = Connector.dbhelper.addBookMark(bookmark);
							Connector.dbhelper.close();
							if (s > 0) {
								Toast.makeText(c, "Bookmark Created",
										Toast.LENGTH_LONG).show();
								layoutBookmark.setVisibility(View.GONE);
							} else {
								Toast.makeText(c, "Unable to Add Bookmark",
										Toast.LENGTH_LONG).show();
							}
						} else {
							Toast.makeText(c, "Bookmark Already Exists",
									Toast.LENGTH_LONG).show();
						}

						// }
						// else {
						// Toast.makeText(c, "Unable to Add Bookmark",
						// Toast.LENGTH_LONG).show();
						// }
						// }
					}

				}).setNegativeButton("No", new OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub

					}
				}).show();

	}

	// Lines for the Notepad
	public static class LineEditText extends EditText {
		// we need this constructor for LayoutInflater
		public LineEditText(Context context, AttributeSet attrs) {
			super(context, attrs);
			mRect = new Rect();
			mPaint = new Paint();
			mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
			// mPaint.setColor(Connector.context.getResources().getColor(R.color.note_lines));
			mPaint.setColor(Connector.context.getResources().getColor(
					R.color.note_lines));
		}

		private Rect mRect;
		private Paint mPaint;

		@Override
		protected void onDraw(Canvas canvas) {

			int height = getHeight();
			int line_height = getLineHeight();

			int count = height / line_height;

			if (getLineCount() > count)
				count = getLineCount();

			Rect r = mRect;
			Paint paint = mPaint;
			int baseline = getLineBounds(0, r);

			for (int i = 0; i < count; i++) {

				canvas.drawLine(r.left, baseline + 1, r.right, baseline + 1,
						paint);
				baseline += getLineHeight();

				super.onDraw(canvas);
			}

		}
	}

	public static class EmailFormatValidator {

		private Pattern pattern;
		private Matcher matcher;

		private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

		public EmailFormatValidator() {
			pattern = Pattern.compile(EMAIL_PATTERN);
		}

		public boolean validate(final String email) {

			matcher = pattern.matcher(email);
			return matcher.matches();

		}
	}
public static Integer processUsersListJson(ArrayAdapter<NsoreDevotionalUser> listAdapter,String response)
{
	  try {

			objects = new JSONObject(response.trim());
			if (objects != null) {
				JSONArray feedArray  = objects.getJSONArray("Friends");

				if (feedArray  != null) {
      
          //JSONArray feedArray = response.getJSONArray("feed");


//justClicked=199;
          for (int i = 0; i < feedArray.length(); i++) {
              JSONObject feedObj = (JSONObject) feedArray.get(i);

           NsoreDevotionalUser  item = new  NsoreDevotionalUser();
            /*
             name memberID comment_id timeStamp comment profilePic
             * 
             */
           
            item.setName(feedObj.getString("name"));
            item.setMemberID(feedObj.getString("memberID"));
         
            item.setEmail(feedObj.getString("email"));
            item.setPhone_number(feedObj.getString("phone"));
            //item.setCurrent(false);
			  item.setProfilePic(feedObj.getString("profilePic"));
			  item.setIsConnected(feedObj.getInt("isConnected")+"");
			  item.setFriendsCount(feedObj.getString("friends")+"");
			  item.setMutualFriends(feedObj.optString("mutualFriends",feedObj.optInt("mutualFriends", 0)+""));
			  item.setMemberSince(feedObj.getString("date_joined"));
			  item.setUserCover(feedObj.getString("cover"));
			  item.setChurchname(feedObj.getString("churchName"));
            item.setChurchID(feedObj.getString("CID"));
            item.setAboutUser(feedObj.getString("about-user"));
				
               
           //startAT = 
            
            listAdapter.add(item);
          }

          // notify data changes to list adapater
          
         //startAT= feedItems.size()+ 1;
          listAdapter.notifyDataSetChanged();
         return 1;
				}
				
			}
      } catch (JSONException e) {
         Log.d("ERR", e.getMessage());
          return -1;
      }
	  return -1;
}


public static String HandleVolleyerror(VolleyError error,Context context)
{
	
	String errorSatatement="";
	if(error!=null)
	{
		if( error instanceof  	TimeoutError || error instanceof NoConnectionError)
		{
			errorSatatement="No Active Internet Connection";
		}
		else if(error instanceof ParseError )
		{
			errorSatatement="";
		}
		else if(error instanceof ParseError )
		{
			errorSatatement="";
		}
		else if(error instanceof ServerError  )
		{
			errorSatatement="";
		}
		else if(error instanceof AuthFailureError  )
		{
			errorSatatement="";
		}
	}
	return errorSatatement;
	
}


PopupWindow popWindow;
@SuppressLint("NewApi") 
private  void onShowPopup(String errorMessage,Activity c){
	
	//ImageLoader imageLoader = com.appsol.volley.classes.ImageCacheManager.getInstance().getImageLoader();
	
LayoutInflater layoutInflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
// inflate the custom popup layout
final View inflatedView = layoutInflater.inflate(R.layout.fragment_create_note, null,false);
final EditText body= (EditText) inflatedView.findViewById(R.id.body);

//body.setText(prayer);

		
Button btnconnect =(Button) inflatedView.findViewById(R.id.btnsavenote);
Button btnCancel=(Button) inflatedView.findViewById(R.id.btnCancel);



// find the ListView in the popup layout
int mDeviceHeight;
 
// get device size
Display display = c.getWindowManager().getDefaultDisplay();
final Point size = new Point();
display.getSize(size);
mDeviceHeight = size.y;	    
// set height depends on the device size
popWindow = new PopupWindow(inflatedView, size.x - 50,size.y - 300, true );

//Views



// set a background drawable with rounders corners
popWindow.setBackgroundDrawable(Connector.context.getResources().getDrawable(R.drawable.flatlayout));
// make it focusable to show the keyboard to enter in `EditText`
popWindow.setFocusable(true);
// make it outside touchable to dismiss the popup window
popWindow.setOutsideTouchable(true);
 
// show the popup at bottom of the screen and set some margin at bottom ie,
popWindow.showAtLocation(inflatedView, Gravity.TOP, 0,150);

}



}
