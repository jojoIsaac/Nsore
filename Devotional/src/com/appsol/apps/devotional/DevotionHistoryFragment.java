package com.appsol.apps.devotional;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.appsol.apps.projectcommunicate.adapter.DevotionListAdapter;
import com.appsol.apps.projectcommunicate.classes.Connector;
import com.appsol.apps.projectcommunicate.classes.Connector.GridMonthAdapter;
import com.appsol.apps.projectcommunicate.classes.Devotion;
import com.appsol.apps.projectcommunicate.model.Bookmark;
import com.appsol.apps.projectcommunicate.model.ChurchEvents;
import com.appsol.apps.projectcommunicate.model.DBHelper;

/**
 * A simple {@link Fragment} subclass. Use the
 * {@link DevotionHistoryFragment#newInstance} factory method to create an
 * instance of this fragment.
 * 
 */
public class DevotionHistoryFragment extends Fragment implements OnKeyListener  {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
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
	// TODO: Rename and change types and number of parameters
	public static DevotionHistoryFragment newInstance(String param1,
			String param2) {
		DevotionHistoryFragment fragment = new DevotionHistoryFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}

	public DevotionHistoryFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}
	}
	
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub

		getActivity().getMenuInflater().inflate(R.menu.history_list, menu);
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
	
	static TextView txteventTitle,txteventstartdate,txteventLocation;
	DevotionListAdapter devotions;
	ArrayList<Devotion> devotionList;
	static Devotion selectedDevotion;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		rootView= inflater.inflate(R.layout.fragment_devotion_history, container,
				false);
		
		
		setHasOptionsMenu(false);
		setUpUI(rootView);
		
		
		return rootView;
	}


	public static void toggleCalender()
	{
		if( layout_Month.getVisibility()!=View.GONE)
		{
			btnshow.setImageResource(R.drawable.uparrow);
			layout_Month.setVisibility(View.GONE);
		}
		
		
	}
	
	private void setUpUI(View rootView) {
		// TODO Auto-generated method stub
		 
		 progressbarLoad=(ProgressBar) rootView.findViewById(R.id.progressbarLoad);
		 if(progressbarLoad!=null)
		 {
			 progressbarLoad.setProgress(0);
		 }
		
		 layout_no_Bookmark= (LinearLayout) rootView.findViewById(R.id.layout_no_Bookmark);
		 layout_no_Bookmark.setVisibility(View.GONE);
		 gridView = (GridView) rootView.findViewById(R.id.grid_view);
		 layout_Month=(LinearLayout) rootView.findViewById(R.id.layout_Month);
		 layout_no_event=(LinearLayout) rootView.findViewById(R.id.layout_no_event);
		 TextView txtchurchname=(TextView) rootView.findViewById(R.id.txt1);
		 
		 
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
			
		 MonthAdapter = new Connector.GridMonthAdapter(getActivity());
		 gridView.setAdapter(MonthAdapter);
		 TextView txtYear= (TextView) rootView.findViewById(R.id.txtYear);
		 
		 txtYear.setText("Browse Devotions within the year "+Connector.getYear());
		 txtYear.setVisibility(View.GONE);
		 
		 btnshow=(ImageView) rootView.findViewById(R.id.btnshow);
		 btnshow.setImageResource(R.drawable.uparrow);
		 //Set up the ListView
		 devotionList= new ArrayList<Devotion>();
		 lstDevotions = (ListView) rootView.findViewById(R.id.lstMessages);
		 devotions= new DevotionListAdapter(getActivity(), android.R.layout.simple_list_item_1, devotionList);
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
						Intent intent= new Intent(getActivity(), DevotionDetailActivity.class);
						intent.putExtra("DEVOTION-JSON", devotion.getDevotionJson());
						intent.putExtra("LID",devotion.getLesseonid());
						intent.putExtra("DID",devotion.getDaily_guide_id());
						if(Bookmark.checkAlreadyBookedMarked(devotion.getLesseonid()))
						{
							intent.putExtra("HIDE-BOOOKMARK", "1");	
						}
						else
							intent.putExtra("HIDE-BOOOKMARK", "0");
						
						getActivity().startActivity(intent);
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
				
				Intent intent= new Intent(getActivity(), DevotionDetailActivity.class);
				intent.putExtra("DEVOTION-JSON", selectedDevotion.getDevotionJson());
				intent.putExtra("LID",selectedDevotion.getLesseonid());
				intent.putExtra("DID",selectedDevotion.getDaily_guide_id());
				Connector.dbhelper = new DBHelper(getActivity());
				if(Bookmark.checkAlreadyBookedMarked(selectedDevotion.getLesseonid()))
				{
					intent.putExtra("HIDE-BOOOKMARK", "1");	
				}
				else
					intent.putExtra("HIDE-BOOOKMARK", "0");
				
				Connector.dbhelper.close();
				getActivity().startActivity(intent);
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
			Connector.shareText(getActivity(), data);
			break;
		case R.id.mn_bookmark:
		{
			Bookmark bookmark = new Bookmark();
			bookmark.setDid(selectedDevotion.getLesseonid());
			bookmark.setTitle(selectedDevotion.getTitle());
			bookmark.setContent(selectedDevotion.getSummary());
			bookmark.setDaily_guide_id(selectedDevotion.getDaily_guide_id());
			bookmark.setDevotion(selectedDevotion.getDevotion());
			Connector.addBookMark(this.getActivity(),bookmark,new View(getActivity()));
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
				parameter.add(new BasicNameValuePair("CID", Connector
						.getChurchID()));
				 status = Connector.sendData(parameter, getActivity());
			}
			else
			{
				parameter.add(new BasicNameValuePair("reason","Get Devotion History"));
				parameter.add(new BasicNameValuePair("M", params[0]));
				parameter.add(new BasicNameValuePair("Y", params[1]));
				//parameter.add(new BasicNameValuePair("LOAD-STATUS", params[2]));
				parameter.add(new BasicNameValuePair("CID", Connector
						.getChurchID()));
				 status = Connector.sendData(parameter, getActivity());
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
		ProgressBar progressbar= new ProgressBar(getActivity());
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
				Toast.makeText(getActivity(), result, Toast.LENGTH_LONG)
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

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		
		if(KeyEvent.KEYCODE_BACK ==keyCode)
		{
			toggleCalender();
			return true;
		}
		return false;
	}
	

}
