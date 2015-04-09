package com.appsol.apps.devotional;

import org.json.JSONException;
import org.json.JSONObject;

import com.appsol.apps.devotional.DevotionFragment.fetchLesson;
import com.appsol.apps.projectcommunicate.classes.Connector;
import com.appsol.apps.projectcommunicate.classes.Devotion;
import com.appsol.apps.projectcommunicate.classes.SlideInAnimationHandler;
import com.appsol.apps.projectcommunicate.model.Bookmark;
import com.appsol.apps.projectcommunicate.model.DBHelper;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.os.Build;

public class DevotionDetailActivity extends ActionBarActivity {

	
	private String devotiondate;
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
	static Devotion Bdevotion;
	

	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Connector.myPrefs= getSharedPreferences(Connector.prefernceName, Context.MODE_PRIVATE);
        Connector.context=this;
		setContentView(R.layout.activity_devotion_detail);
		//setupfloatingMenu();
		//setupfloatingMenu1();
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.devotionaldetailmenu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.mn_Share) {
			if(Bdevotion!=null && !TextUtils.isEmpty(Bdevotion.getVerse()))
			{
				String data=Connector.getChurchName()+"" +"\n "+
						"\n "+Bdevotion.getTitle()+"\n" +"\n "+
							"Memory Verse: "+Bdevotion.getVerse()+"\n" +"\n "+
									"- Register and access more at: " +
									""+Connector.parentURL;
				Connector.shareText(this, data);
			}
			return true;
		}
		else if(id==R.id.mn_add_note)
		{
			Intent ints= new Intent(this, CreateNoteActivity.class);
			ints.putExtra("TITLE", Bdevotion.getTitle());
			CreateNoteActivity.title_=Bdevotion.getTitle();
			startActivity(ints);
			 
			return true;
		}
		else if(id==R.id.mn_comment)
		{
			Connector.handleComment(Bdevotion);
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		private View rootView;
		ProgressBar progressbarLoad;
		TextView txtdevotionstatus;
		String devotionalert="Please wait. Loading Devotion";
		TextView txtreflection, txtfreadings, txtprayer, txtTitle, txtdate,
		txtreading;
	LinearLayout lviewnodevotion, linearLayout1, layoutdate, layoutmessage,
		layout1, layout2, layout3, layoutreflex, layoutprayer,linearContent;
	Button bookmarkBtn;
	private TextView txt1;
	private static String Lesson;
private static String fromnew="No";
	private static LinearLayout layoutBookmark;
		public PlaceholderFragment() {
		}

		
		void setupfloatingMenu()
		{
			  // Our action button is this time just a regular view!
           
            ImageView fabContent = new ImageView(getActivity());
            fabContent.setImageDrawable(getResources().getDrawable(R.drawable.nsore_bg_logo));

            FloatingActionButton centerActionButton = new FloatingActionButton.Builder(getActivity())
                                                  .setTheme(FloatingActionButton.THEME_DARK)
                                                  .setContentView(fabContent)
                                                  .setPosition(FloatingActionButton.POSITION_BOTTOM_RIGHT)
                                                  .build();
            // Add some items to the menu. They are regular views as well!
            TextView a = new TextView(getActivity()); a.setText("a"); a.setBackgroundResource(android.R.drawable.btn_default_small);
            TextView b = new TextView(getActivity()); b.setText("b"); b.setBackgroundResource(android.R.drawable.btn_default_small);
            TextView c = new TextView(getActivity()); c.setText("c"); c.setBackgroundResource(android.R.drawable.btn_default_small);
            TextView d = new TextView(getActivity()); d.setText("d"); d.setBackgroundResource(android.R.drawable.btn_default_small);
            TextView e = new TextView(getActivity()); e.setText("e"); e.setBackgroundResource(android.R.drawable.btn_default_small);
            TextView f = new TextView(getActivity()); f.setText("f"); f.setBackgroundResource(android.R.drawable.btn_default_small);
            TextView g = new TextView(getActivity()); g.setText("g"); g.setBackgroundResource(android.R.drawable.btn_default_small);
            TextView h = new TextView(getActivity()); h.setText("h"); h.setBackgroundResource(android.R.drawable.btn_default_small);
            FrameLayout.LayoutParams tvParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            a.setLayoutParams(tvParams);
            b.setLayoutParams(tvParams);
            c.setLayoutParams(tvParams);
            d.setLayoutParams(tvParams);
            e.setLayoutParams(tvParams);
            f.setLayoutParams(tvParams);
            g.setLayoutParams(tvParams);
            h.setLayoutParams(tvParams);

            SubActionButton.Builder subBuilder = new SubActionButton.Builder(getActivity());

            FloatingActionMenu circleMenu = new FloatingActionMenu.Builder(getActivity())
                    .setStartAngle(0) // A whole circle!
                    .setEndAngle(360)
                    .setRadius(getResources().getDimensionPixelSize(R.dimen.radius_large))
                    .addSubActionView(a)
                    .addSubActionView(b)
                    .addSubActionView(c)
                    .addSubActionView(d)
                    .addSubActionView(e)
                    .addSubActionView(f)
                    .addSubActionView(g)
                    .addSubActionView(h)
                    .attachTo(centerActionButton)
                    .build();
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_devotion,
					container, false);
			
			//setupfloatingMenu();
			
			setupfloatingMenu1();
			
			
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
			lviewnodevotion.setVisibility(View.GONE);
			//Hide All Layouts
			progressbarLoad.setVisibility(View.GONE);
			//if (devotiondate.equalsIgnoreCase(Connector.getdate("dd-MM-yyy"))) {
			String json= getActivity().getIntent().getStringExtra("DEVOTION-JSON");
			
			fromnew= getActivity().getIntent().getStringExtra("From-New");
			try{
			String hideBookmark= getActivity().getIntent().getStringExtra("HIDE-BOOOKMARK");
			Lesson= getActivity().getIntent().getStringExtra("LID");
			//Log.i("DDDD", Lesson);
			if(hideBookmark.equalsIgnoreCase("1"))
			{
				layoutBookmark.setVisibility(View.GONE);
			}
			else
			{
				layoutBookmark.setVisibility(View.VISIBLE);
			}
//			Connector.dbhelper = new DBHelper(getActivity());
//			if(Connector.DevotionBookmarkcheckAlreadyExist(Lesson))
//			{
//				layoutBookmark.setVisibility(View.GONE);
//			}
//			Connector.dbhelper.close();
			}catch(Exception e)
			{
				e.printStackTrace();
			}
			//Log.e("TAG", json);
			bookmarkBtn= (Button) rootView.findViewById(R.id.bookmarkBtn);
			bookmarkBtn.setOnClickListener( new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Bookmark bookmark = new Bookmark();
					bookmark.setDid(Bdevotion.getLesseonid());
					bookmark.setTitle(Bdevotion.getTitle());
					bookmark.setContent(Bdevotion.getContent());
					bookmark.setDaily_guide_id(Bdevotion
							.getDaily_guide_id());
					bookmark.setDevotion(Bdevotion.getDevotion());
					Connector.addBookMark(getActivity(),bookmark,layoutBookmark);
				}
			});
				processdevotionJson(json);
//			} else {
//				linearContent.setVisibility(View.GONE);
//				lviewnodevotion.setVisibility(View.VISIBLE);
//				layoutBookmark.setVisibility(View.GONE);
//				txtdevotionstatus.setText(devotionalert);
//				//(new fetchLesson()).execute();
//			}
			return rootView;
		}
		
	
		
		
		
		
		
		//Process the JSon data obtain from the server Request
		// processes the Jsondata representing the devotion for the day.
		public void processdevotionJson(String result) {
			// TODO Auto-generated method stub
			if (result.equalsIgnoreCase("NOT_SET")) {
				// lviewnodevotion,linearLayout1,layoutdate,layoutmessage,layout1,layout2,layout3,layoutreflex
				linearContent.setVisibility(View.GONE);
				lviewnodevotion.setVisibility(View.VISIBLE);
				layoutBookmark.setVisibility(View.GONE);
				progressbarLoad.setVisibility(View.GONE);
				txtdevotionstatus.setText("Sorry No Lesson Found");

			} else {
				JSONObject object;

				try {
					object = new JSONObject(result);
					if (object != null) {
			
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
					     prayer=object.optString("prayer");
						 day_name= object.optString("day_name");
						 loadeddate= object.optString("loadeddate");
						 rawDate=object.optString("rawDate");
						txtfreadings.setText(readingplan + "");
						txtreflection.setText(msg);
						txtprayer.setText(prayer);
						String dates = object.optString("RD");
						txtdate.setText(dates);
						txtreading.setText(verse);
						txtTitle.setText(Title);

							layoutprayer.setVisibility(View.VISIBLE);
							lviewnodevotion.setVisibility(View.GONE);
							// linearLayout1.setVisibility(View.GONE);
							layoutdate.setVisibility(View.VISIBLE);
							layoutmessage.setVisibility(View.VISIBLE);
							layout1.setVisibility(View.VISIBLE);
							layout2.setVisibility(View.VISIBLE);
							layoutreflex.setVisibility(View.VISIBLE);
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
		
		
		void setupfloatingMenu1()
		{
			ImageView icon = new ImageView(getActivity()); // Create an icon
			icon.setImageResource( R.drawable.ic_launcher);

			  FloatingActionButton centerActionButton = new FloatingActionButton.Builder(getActivity())
	          .setTheme(FloatingActionButton.THEME_DARK)
	          .setContentView(icon)
	          .setPosition(FloatingActionButton.POSITION_RIGHT_CENTER)
	          .build();
			
			SubActionButton.Builder itemBuilder = new SubActionButton.Builder(getActivity());
			// repeat many times:
			ImageView itemIcon = new ImageView(getActivity());
			itemIcon.setImageResource(R.drawable.ic_nsore_info) ;
			SubActionButton btncomment = itemBuilder.setContentView(itemIcon).build();
			
			
			ImageView itemIcon2 = new ImageView(getActivity());
			itemIcon2.setImageResource(R.drawable.ic_nsore_share) ;
			SubActionButton btnshare = itemBuilder.setContentView(itemIcon2).build();
			
			ImageView itemIcon3 = new ImageView(getActivity());
			itemIcon3.setImageResource(R.drawable.ic_nsore_notes) ;
			SubActionButton btnnote = itemBuilder.setContentView(itemIcon3).build();
			
			//btncomment.setOnClickListener(l)
			
			FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(getActivity())
//			.setStartAngle(0) // A whole circle!
//	        .setEndAngle(180)
.setRadius(getResources().getDimensionPixelSize(R.dimen.radius_medium))
	        .addSubActionView(btncomment)
	        .addSubActionView(btnnote)
	         .addSubActionView(btnshare)
	        .attachTo(centerActionButton)
	        .build();
			
			
		}
	}
	
	
	
}
