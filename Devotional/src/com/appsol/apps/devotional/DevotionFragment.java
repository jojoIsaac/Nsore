package com.appsol.apps.devotional;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.StringRequest;
import com.appsol.apps.projectcommunicate.classes.Connector;
import com.appsol.apps.projectcommunicate.classes.Devotion;
import com.appsol.apps.projectcommunicate.model.Bookmark;
import com.appsol.apps.projectcommunicate.model.DBHelper;
import com.appsol.volley.classes.ExtHttpClientStack;
import com.appsol.volley.classes.SslHttpClient;
import com.appsol.volley.classes.Volley;

//import com.android.volley.toolbox.Volley;
/**
 * A simple {@link Fragment} subclass. Use the
 * {@link DevotionFragment#newInstance} factory method to create an instance of
 * this fragment.
 * 
 */
public class DevotionFragment extends Fragment {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";
	protected static final String TAG = "Devotion-Page";
	static Devotion Bdevotion;

	// TODO: Rename and change types of parameters

	private static String DID;
	private static String LID;
	private static String Title;
	private static String summary;
	private static String msg;
	private static String day;
	private static String month;
	private static String readingplan;
	private static String verse;
	private static String prayer;
	private static String day_name;
	private static String loadeddate;
	private static String rawDate;
	private View rootView;
	ProgressBar progressbarLoad;
	TextView txtdevotionstatus;
	String devotionalert="Please wait. Loading Devotion";
	TextView txtreflection, txtfreadings, txtprayer, txtTitle, txtdate,
	txtreading;
LinearLayout lviewnodevotion, linearLayout1, layoutdate, layoutmessage,
	layout1, layout2, layout3, layoutreflex, layoutprayer,linearContent;
private TextView txt1;
private String devotiondate;
private Button bookmarkBtn;
private JSONObject object;
private static LinearLayout layoutBookmark;
public static String memoryVerse="";
static String URL_FEED = "https://nsoredevotional.com/mobile/eventsFetch.php";
	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment DevotionFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static DevotionFragment newInstance(String param1, String param2) {
		DevotionFragment fragment = new DevotionFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}

	public DevotionFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setHasOptionsMenu(true);
	}

	void loadDevotion()
	{
		StringRequest jsonReq = new StringRequest(Method.POST, URL_FEED,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						VolleyLog.d(TAG, "Response: " + response.toString());
						if (response != null) {
							parseJsonFeed(response.trim());
						}
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						progressbarLoad.setVisibility(View.GONE);

						VolleyLog.d(TAG, "Error: " + error.getMessage() + "XXX"
								+ error.toString());
					}
				}) {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				// params.put("reason", "Members Testimonies");
				params.put("CID", Connector.getChurchID());
				params.put("MID", Connector.getUserId());
				params.put("reason", "Get Lesson");

				return params;
			}
		};
		InputStream keyStore = getResources().openRawResource(R.raw.appsol);

		RequestQueue queue = Volley.newRequestQueue(getActivity(),
				new ExtHttpClientStack(new SslHttpClient(keyStore, "qwerty")));
		// Adding request to volley request queue
		jsonReq.setRetryPolicy(new DefaultRetryPolicy(5000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		queue.add(jsonReq);
	}
	protected void parseJsonFeed(String trim) {
		// TODO Auto-generated method stub
		//Toast.makeText(getActivity(), trim, Toast.LENGTH_LONG).show();
		if (!trim.trim().contains("Sorry No Devotions Found") && !trim.trim().contains("NOT_SET")) {

			
				
				try {

					object = new JSONObject(trim);
					if (object != null) {
						JSONArray feedArray = object.getJSONArray("Devotion");

						if (feedArray != null) {

							// JSONArray feedArray = response.getJSONArray("feed");

							for (int i = 0; i < feedArray.length(); i++) {
								JSONObject object  = (JSONObject) feedArray.get(i);
				
				if (object != null) {
					
					
					layoutprayer.setVisibility(View.VISIBLE);
					lviewnodevotion.setVisibility(View.GONE);
					linearContent.setVisibility(View.VISIBLE);
					// linearLayout1.setVisibility(View.GONE);
					layoutdate.setVisibility(View.VISIBLE);
					layoutmessage.setVisibility(View.VISIBLE);
					layout1.setVisibility(View.VISIBLE);
					layout2.setVisibility(View.VISIBLE);
					layoutreflex.setVisibility(View.VISIBLE);
					layoutBookmark.setVisibility(View.VISIBLE);
					
					
					 DID = object.optString("DID");
					 LID = object.optString("LID");
					 Title = object.optString("title");
					 summary = object.optString("summary");
					 msg = object.optString("Msg");
					 day = object.optString("DN");
					 month = object.getString("MN");
					 readingplan = object
							.optString("readingplan");
					 verse = object.optString("verse");
					 memoryVerse= verse;
				     prayer=object.optString("prayer");
					 day_name= object.optString("day_name");
					 loadeddate= object.optString("loadeddate");
					 rawDate=object.optString("rawDate");
					 String dates = object.optString("RD");
					 Bdevotion= new Devotion();
						Bdevotion.setCurrent(false);
					    Bdevotion.setRawDate(rawDate);
					   Bdevotion.setId(object.getInt("LID") + "");
							Bdevotion.setDevotionJson(object.toString());
							Bdevotion.setContent(msg);
							Bdevotion.setTitle(Title);
							Bdevotion.setPrayer(prayer);
							Bdevotion.setVerse(verse);
							Bdevotion.setReadingPlan(readingplan);
							Bdevotion.setDevotiondate(day + " "
									+ month);
							Bdevotion.setDevotionday(day_name);
							Bdevotion.setNormalDevotionDate(loadeddate);
							Bdevotion.setSummary(summary);
							Bdevotion.setDevotion(object.toString());
							Bdevotion.setLesseonid(LID);
							Bdevotion.setDaily_guide_id(DID);
							Bdevotion.setCurrentDevotionID(object.optString("CurrentID"));
							Bdevotion.setChurchName(object.getString("churchName"));
							Bdevotion.setDevotiondate(object.getString("D") + " "
									+ object.getString("MN"));
							
							
							
							String date= object.optString("date");
							Connector.setServerDate(date);
							txtfreadings.setText(readingplan + "");
							txtreflection.setText(msg);
							txtprayer.setText(prayer);
							txtdate.setText(dates );
							txtreading.setText(verse);
							txtTitle.setText(Title);
							
							Connector.dbhelper = new DBHelper(getActivity());
							//Connector.openDB(getActivity());
							//bookmarks= Bookmark.getBookMarks(Connector.dbhelper);
							Boolean isBookmarked= Bookmark.checkAlreadyBookedMarked(LID);
							if(isBookmarked)
							{
								layoutBookmark.setVisibility(View.GONE);
							}
							Connector.dbhelper.close();
					 
					 
				}
							}}}
			}catch(Exception e)
			{
				
			}
				}
		else
		{
			linearContent.setVisibility(View.GONE);
			lviewnodevotion.setVisibility(View.VISIBLE);
			layoutBookmark.setVisibility(View.GONE);
			progressbarLoad.setVisibility(View.GONE);
			txtdevotionstatus.setText("Sorry No Lesson Set for Today");
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
		// Inflate the menu; this adds items to the action bar if it is present.
		inflater.inflate(R.menu.devotionaldetailmenu, menu);
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.mn_Share) {
			if(Bdevotion!=null && !TextUtils.isEmpty(Bdevotion.getLesseonid())&& !TextUtils.isEmpty(Bdevotion.getVerse()))
			{
				String data=Connector.getChurchName()+"" +"\n" +
						"\n "+Bdevotion.getTitle()+"\n" +"\n" +
							"Memory Verse: "+Bdevotion.getVerse()+"\n" +"\n" +
									"- Register and access more at: " +
									""+Connector.parentURL;
				Connector.shareText(getActivity(), data);
			}
			
			return true;
		}
		else if(id==R.id.mn_comment)
		{
			if(Bdevotion!=null && !TextUtils.isEmpty(Bdevotion.getLesseonid())&& !TextUtils.isEmpty(Bdevotion.getVerse()))
			{
			Connector.handleComment(Bdevotion);
			}
		}
		else if(id==R.id.mn_add_note)
		{
			
			if(Bdevotion!=null)
			{
			Intent ints= new Intent(getActivity(), CreateNoteActivity.class);
			ints.putExtra("TITLE", Bdevotion.getTitle());
			CreateNoteActivity.title_= Bdevotion.getTitle();
			startActivity(ints);
			}
			
			 
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		Connector.myPrefs = getActivity().getSharedPreferences(
				Connector.prefernceName, Context.MODE_PRIVATE);
		//Bdevotion= new Devotion();
		devotiondate = Connector.getdevotionDate();
		// Inflate the layout for this fragment
		rootView= inflater.inflate(R.layout.fragment_devotion_detail, container, false);
		progressbarLoad=(ProgressBar) rootView.findViewById(R.id.progressbarLoad);
		progressbarLoad.setProgress(0);
		txtdevotionstatus=(TextView) rootView.findViewById(R.id.txtdevotionstatus);
		txt1 = (TextView) rootView.findViewById(R.id.txt1);
		txt1.setText(Connector.getChurchName() + " Daily Devotion");
		lviewnodevotion = (LinearLayout) rootView.findViewById(R.id.layout6);
		layoutBookmark = (LinearLayout) rootView
				.findViewById(R.id.layoutBookmark);
		linearContent=(LinearLayout) rootView.findViewById(R.id.linearContent);
		layout1 = (LinearLayout) rootView.findViewById(R.id.layout1);
		layout2 = (LinearLayout) rootView.findViewById(R.id.layout2);
		layoutprayer = (LinearLayout) rootView.findViewById(R.id.layoutprayer);
		layoutmessage = (LinearLayout) rootView
				.findViewById(R.id.layoutmessage);
		layoutreflex = (LinearLayout) rootView.findViewById(R.id.layoutreflex);
		layoutdate = (LinearLayout) rootView.findViewById(R.id.layouthead);

		txtreflection = (TextView) rootView.findViewById(R.id.txtreflection);
		txtfreadings = (TextView) rootView.findViewById(R.id.txtfreadings);
		txtprayer = (TextView) rootView.findViewById(R.id.txtprayer);
		txtTitle = (TextView) rootView.findViewById(R.id.txtTitle);
		txtdate = (TextView) rootView.findViewById(R.id.txtdate);
		txtreading = (TextView) rootView.findViewById(R.id.txtreading);
		//Hide All Layouts
		progressbarLoad.setVisibility(View.GONE);
//		if (devotiondate.equalsIgnoreCase(Connector.getdate("dd-MM-yyy"))) {
//			processdevotionJson(Connector.getdevotion());
//		} else {
			linearContent.setVisibility(View.GONE);
			lviewnodevotion.setVisibility(View.VISIBLE);
			layoutBookmark.setVisibility(View.GONE);
			txtdevotionstatus.setText(devotionalert);
			//(new fetchLesson()).execute();
		//}
			
			if (Connector.myPrefs.getString("CHURCH_ID", "").length() > 0
					&& !Connector.getChurchID().equalsIgnoreCase(
							"NO_CHURCH_FOUND")) {
			loadDevotion();
			}
		bookmarkBtn= (Button) rootView.findViewById(R.id.bookmarkBtn);
		bookmarkBtn.setOnClickListener( new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				bookmarkBtnClick(v);
			}
		});
		return rootView;
	}
	
	
	public void bookmarkBtnClick(View view) {
		Bookmark bookmark = new Bookmark();
		bookmark.setDid(Bdevotion.getLesseonid());
		bookmark.setTitle(Bdevotion.getTitle());
		bookmark.setContent(Bdevotion.getContent());
		bookmark.setDaily_guide_id(Bdevotion.getDaily_guide_id());
		bookmark.setDevotion(Bdevotion.getDevotionJson());
		Connector.addBookMark(this.getActivity(),bookmark,layoutBookmark);
	}
	
	
	
	class fetchLesson extends AsyncTask<Void, Integer,  String>
	{

		
		private int myProgressCount=-1;
		private String response=null;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			if(progressbarLoad!=null)
				 progressbarLoad.setProgress(0);
			     myProgressCount = 0;
			super.onPreExecute();
		}
		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			
			if(progressbarLoad!=null)
			   progressbarLoad.setProgress(values[0]);
			super.onProgressUpdate(values);
		}
		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			if (Connector.myPrefs.getString("CHURCH_ID", "").length() > 0
					&& !Connector.getChurchID().equalsIgnoreCase(
							"NO_CHURCH_FOUND")) {
				response= Devotion.getCurrentDevotion(getActivity());
				while (response == null) {
	                   myProgressCount++;
	                   /**
	                    * Runs on the UI thread after publishProgress(Progress...) is
	                    * invoked. The specified values are the values passed to
	                    * publishProgress(Progress...).
	                    *
	                    * Parameters values The values indicating progress.
	                    */

	                   publishProgress(myProgressCount);
	                   SystemClock.sleep(100);
	             }
				return response;
			}
			return "NOT_SET";
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			processdevotionJson(result);
			super.onPostExecute(result);
		}
		
	}
	
	
	
	
	//Process the JSon data obtain from the server Request
	// processes the Jsondata representing the devotion for the day.
	public void processdevotionJson(String result) {
		Log.e("PR",result);
		// TODO Auto-generated method stub
		if (result.equalsIgnoreCase("NOT_SET")) {
			// lviewnodevotion,linearLayout1,layoutdate,layoutmessage,layout1,layout2,layout3,layoutreflex
			linearContent.setVisibility(View.GONE);
			lviewnodevotion.setVisibility(View.VISIBLE);
			layoutBookmark.setVisibility(View.GONE);
			progressbarLoad.setVisibility(View.GONE);
			txtdevotionstatus.setText("Sorry No Lesson Set for Today");

		} else {
		
			JSONObject object;

			try {
				object = new JSONObject(result);
				if (object != null) {
					/*
					 * daily_guide.`daily_guide_id` as 'DID',
					 * `daily_guide_lesson_id` as 'LID',`topic` as
					 * 'T',SUBSTRING(`message`,1,60) as 'M', `message` as
					 * 'Msg',`day` as 'D',`month` as 'MN',`reading_plan` as 'RP'
					 * ,`verse` as 'V'
					 */
					//Log.e("POLL", result);
					String status = object.getString("status");
					Connector.setdevotionStatus(status);
					String DID = object.optString("DID");
					String LID = object.optString("LID");
					String Title = object.optString("T");
					String m = object.optString("M");
					String msg = object.optString("Msg");
					String day = object.optString("DD");
					String month = object.optString("MN");
					String readingplan = object.optString("RP");
					String dates = object.optString("RD");
					String verse = object.optString("V");
					String prayer = object.optString("prayer");
				
					String date= object.optString("date");
					Connector.setServerDate(date);
					txtfreadings.setText(readingplan + "");
					txtreflection.setText(msg);
					txtprayer.setText(prayer);
					txtdate.setText(dates );
					txtreading.setText(verse);
					txtTitle.setText(Title);
					if (status.equalsIgnoreCase("Failed")) {
						layoutprayer.setVisibility(View.GONE);
						lviewnodevotion.setVisibility(View.VISIBLE);
						
						// linearLayout1.setVisibility(View.GONE);
						layoutdate.setVisibility(View.GONE);
						layoutmessage.setVisibility(View.GONE);
						layout1.setVisibility(View.GONE);
						layout2.setVisibility(View.GONE);
						layoutreflex.setVisibility(View.GONE);
						layoutBookmark.setVisibility(View.GONE);

					} else {
						
						layoutprayer.setVisibility(View.VISIBLE);
						lviewnodevotion.setVisibility(View.GONE);
						linearContent.setVisibility(View.VISIBLE);
						// linearLayout1.setVisibility(View.GONE);
						layoutdate.setVisibility(View.VISIBLE);
						layoutmessage.setVisibility(View.VISIBLE);
						layout1.setVisibility(View.VISIBLE);
						layout2.setVisibility(View.VISIBLE);
						layoutreflex.setVisibility(View.VISIBLE);
						layoutBookmark.setVisibility(View.VISIBLE);
						memoryVerse= verse;
						
					}

					Connector.message = m;
					Connector.title = Title;
					Connector.LID = LID;
					Connector.DID = DID;
					Connector.memoryVerse= verse;
					Connector.setDevotion(result);
					Connector.setdevotionDate(Connector.getdate("dd-MM-yyy"));
					//devotions.add(devotion)
					Connector.dbhelper = new DBHelper(getActivity());
					//Connector.openDB(getActivity());
					//bookmarks= Bookmark.getBookMarks(Connector.dbhelper);
					Boolean isBookmarked= Bookmark.checkAlreadyBookedMarked(LID);
					if(isBookmarked)
					{
						layoutBookmark.setVisibility(View.GONE);
					}
					Connector.dbhelper.close();
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

}
