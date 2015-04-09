package com.appsol.apps.devotional;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.appsol.apps.devotional.DevotionHistoryFragment.LoadDevotion;
import com.appsol.apps.projectcommunicate.adapter.DevotionListAdapter;
import com.appsol.apps.projectcommunicate.classes.Connector;
import com.appsol.apps.projectcommunicate.classes.Devotion;
import com.appsol.apps.projectcommunicate.classes.Connector.GridMonthAdapter;
import com.appsol.apps.projectcommunicate.model.Bookmark;
import com.appsol.apps.projectcommunicate.model.ChurchEvents;
import com.appsol.apps.projectcommunicate.model.DBHelper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import android.support.v7.app.ActionBarActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;

public class SubscribedDevotionsActivity extends ActionBarActivity {

	
	static String churchID="";
	private String branchCount;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_devotion_history);
		 churchID= getIntent().getExtras().getString("DATA");
		// Toast.makeText(this, churchID, Toast.LENGTH_LONG).show();
		 branchCount = getIntent().getExtras().getString("BC");
		setUpUI();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.subscribed_devotions, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		
		return super.onOptionsItemSelected(item);
	}
	
	
	
	
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;
	private View rootView;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment DevotionHistoryFragment.
	 */


	
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub

	getMenuInflater().inflate(R.menu.history_list, menu);
		menu.setHeaderTitle("");
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	static LinearLayout layouthead;
	static ProgressBar progressbarLoad;

	Button btnReadMore;
	private static ImageView btnshow;
	private View frags;
	static LinearLayout layout_Month;
	LinearLayout layout_no_event,layout_no_Bookmark;
	ListView lstDevotions;
	//ItemAdapter devotions;
	ArrayList<ChurchEvents> eventsList;
	private GridView gridView;
	private GridMonthAdapter MonthAdapter;
	private static DisplayImageOptions options;
	static TextView txteventTitle,txteventstartdate,txteventLocation;
	DevotionListAdapter devotions;
	ArrayList<Devotion> devotionList;
	static Devotion selectedDevotion;
	
  

	public static void toggleCalender()
	{
		if( layout_Month.getVisibility()!=View.GONE)
		{
			btnshow.setImageResource(R.drawable.uparrow);
			layout_Month.setVisibility(View.GONE);
		}
		
		
	}
	
	private void setUpUI() {
		// TODO Auto-generated method stub
		 
		 progressbarLoad=(ProgressBar) findViewById(R.id.progressbarLoad);
		 if(progressbarLoad!=null)
		 {
			 progressbarLoad.setProgress(0);
		 }
		
		 layout_no_Bookmark= (LinearLayout) findViewById(R.id.layout_no_Bookmark);
		 layout_no_Bookmark.setVisibility(View.GONE);
		 gridView = (GridView) findViewById(R.id.grid_view);
		 layout_Month=(LinearLayout) findViewById(R.id.layout_Month);
		 layout_no_event=(LinearLayout) findViewById(R.id.layout_no_event);
		 TextView txtchurchname=(TextView)findViewById(R.id.txt1);
		 
		 
		 if(layout_Month!=null)
		 {
			 layout_Month.setVisibility(View.GONE);
		 }
		
		 
		 if( layout_no_event!=null)
		 {
			 layout_no_event.setVisibility(View.GONE);
			 txtchurchname.setText(Connector.getChurchName()+" Events");
			 
		 }
		
			//layout_Month.setVisibility(View.GONE);
			
		 MonthAdapter = new Connector.GridMonthAdapter(SubscribedDevotionsActivity.this);
		 gridView.setAdapter(MonthAdapter);
		 TextView txtYear= (TextView) findViewById(R.id.txtYear);
		 
		 txtYear.setText("Browse Devotions within the year "+Connector.getYear());
		 txtYear.setVisibility(View.GONE);
		 
		 btnshow=(ImageView) findViewById(R.id.btnshow);
		 btnshow.setImageResource(R.drawable.uparrow);
		 //Set up the ListView
		 devotionList= new ArrayList<Devotion>();
		 lstDevotions = (ListView) findViewById(R.id.lstMessages);
		 devotions= new DevotionListAdapter(SubscribedDevotionsActivity.this, android.R.layout.simple_list_item_1, devotionList);
		 lstDevotions.setAdapter(devotions);
		 registerForContextMenu(lstDevotions);
		 lstDevotions.setOnScrollListener( new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				//toggleCalender();
//				if( layout_Month.getVisibility()==View.GONE)
//				{
//					btnshow.setImageResource(R.drawable.downarrow);
//					layout_Month.setVisibility(View.VISIBLE);
//				}
//				else
//				{
//					btnshow.setImageResource(R.drawable.uparrow);
//					layout_Month.setVisibility(View.GONE);
//				}
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				//toggleCalender();
//				if( layout_Month.getVisibility()==View.GONE)
//				{
//					btnshow.setImageResource(R.drawable.downarrow);
//					layout_Month.setVisibility(View.VISIBLE);
//				}
//				else
//				{
//					btnshow.setImageResource(R.drawable.uparrow);
//					layout_Month.setVisibility(View.GONE);
//				}
			}
		});
		 
		 //Respond to clicks
		 //When the user clicks the Devotion
	lstDevotions.setOnItemClickListener( new OnItemClickListener() {

				

				private ChurchEvents selectedDevotion;
				//private PlaceholderFragment fragment;
				private Object fragmentmanger;

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int position,
						long arg3) {
					Devotion devotion = null;
					
					// TODO Auto-generated method stub
					devotion= devotions.getItem(position);
					if(devotion!=null)
					{
						Intent intent= new Intent(SubscribedDevotionsActivity.this, DevotionDetailActivity.class);
						intent.putExtra("DEVOTION-JSON", devotion.getDevotionJson());
						intent.putExtra("LID",devotion.getLesseonid());
						intent.putExtra("DID",devotion.getDaily_guide_id());
						if(Bookmark.checkAlreadyBookedMarked(devotion.getLesseonid()))
						{
							intent.putExtra("HIDE-BOOOKMARK", "1");	
						}
						else
							intent.putExtra("HIDE-BOOOKMARK", "0");
						
						SubscribedDevotionsActivity.this.startActivity(intent);
					}

				}
			});
		 //If the GridViewItem is Clicked
		  gridView.setOnItemClickListener(new OnItemClickListener() {
	            @Override
	            public void onItemClick(AdapterView<?> parent, View v,
	                    int position, long id) {
//	            	 i.putExtra("id", parent.getItemAtPosition(position).toString());
//		                i.putExtra("ids", String.valueOf(position));
//		                i.putExtra("idss", String.valueOf(gridView.getSelectedItemPosition()));
//		                i.putExtra("Year", Connector.getYear());
	            	 String param[]={String.valueOf(position+1),Connector.getYear(),"LOAD-DEVOTION"};
	        		 (new LoadDevotion()).execute(param);
	            }
	        });
		 btnshow.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if( layout_Month.getVisibility()==View.GONE)
				{
					btnshow.setImageResource(R.drawable.downarrow);
					layout_Month.setVisibility(View.VISIBLE);
				}
				else
				{
					btnshow.setImageResource(R.drawable.uparrow);
					layout_Month.setVisibility(View.GONE);
				}
				
				
				
			}
		});
		 
		 //At the page load, Fetch the Immediate 20 devotions
		 String param[]={Connector.getMonth(),Connector.getYear(),"INIT-LOAD"};
		 (new LoadDevotion()).execute(param);
		 
		 
	}
	

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		AdapterContextMenuInfo info= (AdapterContextMenuInfo) item.getMenuInfo();
		selectedDevotion= devotions.getItem(info.position);
	//fragmentmanger= getActivity().getSupportFragmentManager();
		//fragment=null;
		switch (item.getItemId()) {
		case R.id.mn_open:

			if(selectedDevotion!=null)
			{
				
				Intent intent= new Intent(SubscribedDevotionsActivity.this, DevotionDetailActivity.class);
				intent.putExtra("DEVOTION-JSON", selectedDevotion.getDevotionJson());
				intent.putExtra("LID",selectedDevotion.getLesseonid());
				intent.putExtra("DID",selectedDevotion.getDaily_guide_id());
				Connector.dbhelper = new DBHelper(SubscribedDevotionsActivity.this);
				if(Bookmark.checkAlreadyBookedMarked(selectedDevotion.getLesseonid()))
				{
					intent.putExtra("HIDE-BOOOKMARK", "1");	
				}
				else
					intent.putExtra("HIDE-BOOOKMARK", "0");
				
				Connector.dbhelper.close();
				SubscribedDevotionsActivity.this.startActivity(intent);
			}
			
			break;
		case R.id.mn_Share_devotion:
//			Intent ints= new Intent(Intent.ACTION_SEND);
//			ints.putExtra(Intent.EXTRA_TEXT, "Nsore Devotional Lesson  \nTitle:  "+ selectedDevotion.getTitle()+""+"\n http://nsore.appsolinfosystems.com/mobile/index.php?reason=devotion&lid="+selectedDevotion.getDid()
//					+"&did="+selectedDevotion.getDaily_guide_id());
//			ints.setType("text/plain");
//			startActivity(ints);
			String data=Connector.getChurchName()+"" +
					"\n "+selectedDevotion.getTitle()+"\n" +
						"Memory Verse: "+selectedDevotion.getVerse()+"\n" +
								"- Register and access more at: " +
								""+Connector.parentURL;
			Connector.shareText(SubscribedDevotionsActivity.this, data);
			break;
		case R.id.mn_bookmark:
		{
			Bookmark bookmark = new Bookmark();
			bookmark.setDid(selectedDevotion.getLesseonid());
			bookmark.setTitle(selectedDevotion.getTitle());
			bookmark.setContent(selectedDevotion.getSummary());
			bookmark.setDaily_guide_id(selectedDevotion.getDaily_guide_id());
			bookmark.setDevotion(selectedDevotion.getDevotion());
			Connector.addBookMark(SubscribedDevotionsActivity.this,bookmark,new View(SubscribedDevotionsActivity.this));
		}
			break;
		
		default:
			break;
		}

		return super.onContextItemSelected(item);
	}
	
	class LoadDevotion extends AsyncTask<String, Integer, String> {

		
		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			
			if(progressbarLoad!=null)
			   progressbarLoad.setProgress(values[0]);
			super.onProgressUpdate(values);
		}
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			ArrayList<NameValuePair> parameter = new ArrayList<NameValuePair>();
			String status=null;
			if(params[2].equalsIgnoreCase("INIT-LOAD"))
			{
				parameter.add(new BasicNameValuePair("reason","Load Ordered Devotion History"));
				
				//parameter.add(new BasicNameValuePair("LOAD-STATUS", params[2]));
				parameter.add(new BasicNameValuePair("CID", churchID));
				 status = Connector.sendData(parameter, SubscribedDevotionsActivity.this);
			}
			else
			{
				parameter.add(new BasicNameValuePair("reason","Get Devotion History"));
				parameter.add(new BasicNameValuePair("M", params[0]));
				parameter.add(new BasicNameValuePair("Y", params[1]));
				//parameter.add(new BasicNameValuePair("LOAD-STATUS", params[2]));
				parameter.add(new BasicNameValuePair("CID", churchID));
				 status = Connector.sendData(parameter, SubscribedDevotionsActivity.this);
			}
			   while (status == null) {
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
			
			return status;
		}

		ProgressDialog pdg;
	
		ProgressDialog progressDialog;
		ProgressBar progressbar= new ProgressBar(SubscribedDevotionsActivity.this);
		private int myProgressCount;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			if(progressbarLoad!=null)
				 progressbarLoad.setProgress(0);
			     myProgressCount = 0;
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			
			processJson(result);
			super.onPostExecute(result);
		}

		public void processJson(String result) {
			// TODO Auto-generated method stub
			// Toast.makeText(getActivity(), result,
			// Toast.LENGTH_LONG).show();

			if (!result
					.equalsIgnoreCase("Sorry No Devotions Found For the Selected Month") && !result.equalsIgnoreCase("Sorry No Devotions Found")) {

				JSONObject objects;
				devotions.clear();
				try {
					objects = new JSONObject(result);
					if (objects != null) {
						JSONArray jarray = objects.getJSONArray("Devotion");

						if (jarray != null) {
							for (int i = 0; i < jarray.length(); i++) {
								JSONObject object = jarray.optJSONObject(i);

								if (object != null) {
									// Toast.makeText(getActivity(),
									// "Hello world",Toast.LENGTH_LONG).show();
									// Log.e("Data",
									// object.optString("DID"));
									Devotion devotion = new Devotion();
									// String status =
									// object.getString("status");
									// Connector.setdevotionStatus(status);
									String DID = object.optString("DID");
									String LID = object.optString("LID");
									String Title = object.optString("T");
									String summary = object.optString("M");
									String msg = object.optString("Msg");
									String day = object.optString("D");
									String month = object.optString("MN");
									String readingplan = object
											.optString("RP");
									String verse = object.optString("V");
									String prayer = object.optString("P");
									String day_name= object.optString("DW");
									String loadeddate= object.optString("DD");
									String rawDate=object.optString("rawDate");
									Log.e("SERVER",""+Connector.getServerTime());
								    Log.e("DID",""+ rawDate);
								
									// Toast.makeText(getActivity(),
									// "Hello world",
									// Toast.LENGTH_LONG).show();
									// //
									//
								    
								    if(Connector.getServerTime().equalsIgnoreCase(rawDate))
								    {
								    	devotion.setCurrent(true);
								    }
								    else
								    devotion.setCurrent(false);
									devotion.setRawDate(rawDate);
									devotion.setDevotionJson(object.toString());
									devotion.setContent(msg);
									devotion.setTitle(Title);
									devotion.setPrayer(prayer);
									devotion.setVerse(verse);
									devotion.setReadingPlan(readingplan);
									devotion.setDevotiondate(day + " "
											+ month);
									devotion.setDevotionday(day_name);
									devotion.setNormalDevotionDate(loadeddate);
									devotion.setSummary(summary);
									devotion.setDevotion(object.toString());
									devotion.setLesseonid(LID);
									devotion.setDaily_guide_id(DID);
									
									devotion.setCurrentDevotionID(object.optString("CurrentID"));
									devotions.add(devotion);
                                    
									//
									// //
									// devotion.setDaily_guide_id(daily_guide_id)

								} else {
									Log.e("ERR", "invalid Object");
								}
							}
							// Toast.makeText(getActivity(),
							// devotions.getCount() + " 8494949449",
							// Toast.LENGTH_LONG).show();
							devotions.notifyDataSetChanged();
							
						} else {
							Log.e("ERR", "invalid Object");
						}

					}

				} catch (JSONException e) {

				}
			} else {
				Toast.makeText(SubscribedDevotionsActivity.this, result, Toast.LENGTH_LONG)
						.show();

				//getActivity().finish();
				//layout_no_list.setVisibility(View.VISIBLE);
				//layout_list.setVisibility(View.GONE);
				//layouthead.setVisibility(View.GONE);
			}
			
			if(progressbarLoad!=null)
			{
			progressbarLoad.setProgress(0);
			progressbarLoad.setVisibility(View.GONE);
			}
		}
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
