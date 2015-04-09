package com.appsol.apps.devotional;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.appsol.apps.projectcommunicate.classes.Connector;
import com.appsol.apps.projectcommunicate.model.ChurchEvents;
import com.appsol.apps.projectcommunicate.model.DBHelper;
import com.appsol.apps.projectcommunicate.model.SubacribedEvents;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class EventDetails extends ActionBarActivity {

	private static Fragment fragment;
	private static FragmentManager fragmentmanger;

	static String eventID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_details);
		
//		if (savedInstanceState == null) {
//			getSupportFragmentManager().beginTransaction()
//					.add(R.id.container, new PlaceholderFragment()).commit();
//		}
		
		
		Connector.context = this;
		String reason= getIntent().getStringExtra("reason");
		fragmentmanger= getSupportFragmentManager();
		if(reason.equalsIgnoreCase("Show Upcoming Event"))
		{
			String eventID=getIntent().getStringExtra("EventID");
			String event_title=getIntent().getStringExtra("Title");
			String details=getIntent().getStringExtra("Detail");
			String start_date=getIntent().getStringExtra("Start_date");
			String starttime=getIntent().getStringExtra("Start_Time");
			String end_date=getIntent().getStringExtra("EndDate");
			String end_time=getIntent().getStringExtra("EndTime");
			String banner=getIntent().getStringExtra("banner");
			String location=getIntent().getStringExtra("Location");
			String type= getIntent().getStringExtra("Type");
			String isPast=getIntent().getStringExtra("isPast");
			String isGoing = getIntent().getStringExtra("isGoing");
			String json= getIntent().getStringExtra("JSON");
			//	data.putString("isPast", selectedDevotion.getIspast());
			fragment = new PlaceholderFragment();
			
			Bundle data = new Bundle();
			data.putString("EventID", eventID);
			data.putString("banner", banner);
			data.putString("Location", location);
			data.putString("Detail", details);
			data.putString("EndDate", end_date);
			data.putString("Start_Time", starttime);
			data.putString("EndTime", end_time);
			data.putString("Type", type);
			data.putString("Start_date",start_date);
			data.putString("Title",event_title);
			data.putString("isPast", isPast);
			data.putString("isGoing",isGoing);
			data.putString("JSON", json);
			fragment.setArguments(data);
			
			
			
//			SubacribedEvents sevent= new SubacribedEvents();
//			sevent.setEventID(eventID);
//			sevent.setEventJson("yeysyeye");
//			sevent.setTitle(event_title);
//			sevent.setDate_string(Connector.getdate("MMM d"));
//			Connector.dbhelper = new DBHelper(this);
//			Connector.dbhelper.addEvent(sevent);
//			Connector.dbhelper.close();
		
		}
		else
		{
			fragment= new EventListFragment();
			Bundle data = new Bundle();
			data.putString("pos", getIntent().getExtras().getString("id"));
			data.putString("post", getIntent().getExtras().getString("ids"));
			data.putString("posts", getIntent().getExtras().getString("idss").toString());
			data.putString("year", getIntent().getExtras().getString("Year"));
			fragment.setArguments(data);
		}
		fragmentmanger.beginTransaction().add(R.id.container, fragment).commit();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.event_details, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}
		protected static ImageLoader imageLoader = ImageLoader.getInstance();
		//private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
TextView txtTitle,txtEventTime,txtlocation,txteventtype,txtdetails;

static String messageToShare="";
Button shareEvent,willAttend;
LinearLayout layout_share_like;
ImageView banner;
String eventID="";
String event_title="";
private DisplayImageOptions options;
 String start_date;
 String eventJson;
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_event_details,
					container, false);
			
			getActivity().setTitle("Event Detail");
			
			//Initiate UI
			txtTitle = (TextView) rootView.findViewById(R.id.txtTitle);
			txtEventTime = (TextView) rootView.findViewById(R.id.txtEventTime);
			txtlocation = (TextView) rootView.findViewById(R.id.txtlocation);
			txteventtype = (TextView) rootView.findViewById(R.id.txteventtype);
			txtdetails = (TextView) rootView.findViewById(R.id.txtdetails);
			banner= (ImageView) rootView.findViewById(R.id.banner);
			willAttend=(Button) rootView.findViewById(R.id.willAttend);
			Bundle data= getArguments();
			eventID=data.getString("EventID");
		    
			 event_title=data.getString("Title");
			String details=data.getString("Detail");
			 start_date=data.getString("Start_date");
			final String starttime=data.getString("Start_Time");
			String end_date=data.getString("EndDate");
			String end_time=data.getString("EndTime");
			String banner=data.getString("banner");
			final String location=data.getString("Location");
			String type= data.getString("Type");
			String isGoing = data.getString("isGoing");
		  eventJson= data.getString("JSON");
		 // Toast.makeText(getActivity(), data.getString("JSON"), Toast.LENGTH_LONG).show();
			if(isGoing.equalsIgnoreCase("1"))
			{
				willAttend.setText("Cancel Request");
			}
			//data.putString("isPast", selectedDevotion.getIspast());
			String isPast=	data.getString("isPast");
			txteventtype.setText(type);
			txtdetails.setText(details);
			txtTitle.setText(event_title);
			txtEventTime.setText(start_date + " "+ starttime);
			txtdetails.setText(details);
			txtlocation.setText(location);
			layout_share_like=(LinearLayout) rootView.findViewById(R.id.layout_share_like);
			
			shareEvent= (Button) rootView.findViewById(R.id.shareEvent);
			if(isPast.equalsIgnoreCase("0"))
			{
				layout_share_like.setVisibility(View.GONE);
			}
			shareEvent.setOnClickListener( new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					messageToShare="I would like to invite you to an upcoming event by the "+Connector.getChurchName()+"\n" +
							"Titled: "+ event_title+"\nDate: "+start_date + " \nTime: " +
									""+ starttime+"\nLocation: "+location+" \nShared from Nsore Devotional";
					Intent ints= new Intent(Intent.ACTION_SEND);
					ints.putExtra(Intent.EXTRA_TEXT, messageToShare);
					ints.setType("text/plain");
					
					startActivity(ints);
				}
			});
			
			willAttend.setOnClickListener( new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(willAttend.getText().toString().equalsIgnoreCase("Cancel Request"))
					{
						 HandleUserOptionToAttend("Cancel Request");
					}
					else
					  HandleUserOptionToAttend("Will Attend Program");
				}
			});
			
			
			///Instantiate the Image loader
//			options = new DisplayImageOptions.Builder()
//			.showImageOnLoading(R.drawable.eventslogo)
//			.showImageForEmptyUri(R.drawable.eventslogo)
//			.showImageOnFail(R.drawable.eventslogo).cacheInMemory(true)
//			.cacheOnDisk(true).considerExifParams(true)
//			.displayer(new RoundedBitmapDisplayer(6)).build();
//			imageLoader.displayImage(Connector.eventBannerURL+banner,this.banner, options,
//					animateFirstListener);
			return rootView;
		}
		protected void HandleUserOptionToAttend(final String string) {
			// TODO Auto-generated method stub
			AlertDialog.Builder build = new Builder(getActivity());
			
			
					build.setTitle("Program Attendance")
					.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							(new WillAttendProgram()).execute(string);
						}
					})
					.setNegativeButton("No", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
						}
					});
					if(string.equalsIgnoreCase("Will Attend Program"))
					{
						build.setMessage("Are you going for the program? \n Prior to the event you will be notified.\n " +
								"Do you want to continue?");
					}
					else
					{
						build.setMessage("Are you sure of this request?");
					}
					
					build.show()
					;
		}
		
		
		class WillAttendProgram extends AsyncTask<String, Void, String>
		{

			private ProgressDialog pdg;

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				ArrayList<NameValuePair> parameter = new ArrayList<NameValuePair>();
				parameter.add(new BasicNameValuePair("reason",
						params[0]));
				parameter.add(new BasicNameValuePair("UID", Connector.getUserId()));
				parameter.add(new BasicNameValuePair("EID", eventID));
				parameter.add(new BasicNameValuePair("CID", Connector
						.getChurchID()));
				String status = Connector.sendData(parameter, getActivity());
				return status;
			}
			
			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				pdg = new ProgressDialog(Connector.context);
				pdg.setTitle("Please Wait ");
				pdg.setMessage("Loading Events ....");
				pdg.setCancelable(false);
				pdg.show();
				super.onPreExecute();
			}
			
			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				pdg.cancel();
				Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
				if(result.equalsIgnoreCase("Record saved. You will be notified with other details of this program later."))
				{
					
					SubacribedEvents sevent= new SubacribedEvents();
					sevent.setEventID(eventID);
					sevent.setEventJson(eventJson);
					sevent.setTitle(event_title);
					sevent.setDate_string(start_date);
					Connector.dbhelper = new DBHelper(getActivity());
					Connector.dbhelper.addEvent(sevent);
					Connector.dbhelper.close();
					//DBHelper db= new 
					willAttend.setText("Cancel Request");
					
					
					
				}
				else
				{
					//deleteEvent
					Connector.dbhelper = new DBHelper(getActivity());
					Connector.dbhelper.deleteEvent(eventID);
					Connector.dbhelper.close();
					willAttend.setText("Will Attend");
					
					
				}
				super.onPostExecute(result);
			}
		}
	}
	
	
	
	public static class EventListFragment extends Fragment {

		public EventListFragment() {
		}
TextView txtTitle,txtEventTime,txtlocation,txteventtype,txtdetails;
static String eventID;
static String messageToShare="";
Button shareEvent;
private Integer month;
private String year;
TextView txtpageReason;
ListView lstDevotions;
//ItemAdapter devotions;
ArrayList<ChurchEvents> eventsList;
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_history_list,
					container, false);
			
			month = 1;
			year = Connector.getYear();
			try {
				month = Integer.parseInt(getArguments().getString("post")) + 1;
				year = getArguments().getString("year");
				loadEvents(month, year);

			} catch (Exception e) {

			}
			eventsList= new ArrayList<ChurchEvents>();
			//devotions = new ItemAdapter(getActivity(),
					//android.R.layout.simple_list_item_1, eventsList);
			txtpageReason=(TextView) rootView.findViewById(R.id.txtpageReason);
			txtpageReason.setText("Events Within the Selected Month");
			lstDevotions = (ListView) rootView.findViewById(R.id.lstDevotions);
			//lstDevotions.setAdapter(devotions);
			registerForContextMenu(lstDevotions);
			lstDevotions.setOnItemClickListener( new OnItemClickListener() {

				

				private ChurchEvents selectedDevotion;

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int position,
						long arg3) {
					// TODO Auto-generated method stub
					//selectedDevotion= devotions.getItem(position);
					if(selectedDevotion!=null)
					{
						fragment= new  PlaceholderFragment();
						
						Bundle data= new Bundle();
						// data.putString(key, value)

						data.putString("EventID", selectedDevotion.getEventID());
						data.putString("banner", selectedDevotion.getBanner());
						data.putString("Location", selectedDevotion.getLocation());
						data.putString("Detail", selectedDevotion.getDetail());
						data.putString("EndDate", selectedDevotion.getEndDate());
						data.putString("Start_Time", selectedDevotion.getStart_Time());
						data.putString("EndTime", selectedDevotion.getEndTime());
						data.putString("Type", selectedDevotion.getType());
						data.putString("Start_date", selectedDevotion.getStart_date());
						data.putString("Title", selectedDevotion.getTitle());
						data.putString("isPast", selectedDevotion.getIspast());
					data.putString("isGoing",selectedDevotion.getIsGoing());
						fragment.setArguments(data);
						fragmentmanger.beginTransaction()
						.replace(R.id.container, fragment)
						.addToBackStack(null).commit();
						
					}
				}
			});
			return rootView;
		}
		private void loadEvents(int month2, String year2) {
			// TODO Auto-generated method stub
			(new LoadDevotion()).execute();
		}
		
		
		

		class LoadDevotion extends AsyncTask<Void, Void, String> {

			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub
				ArrayList<NameValuePair> parameter = new ArrayList<NameValuePair>();
				parameter.add(new BasicNameValuePair("reason",
						"Load Events"));
				parameter.add(new BasicNameValuePair("M", month.toString()));
				parameter.add(new BasicNameValuePair("Y", year.toString()));
				parameter.add(new BasicNameValuePair("CID", Connector
						.getChurchID()));
				parameter.add(new BasicNameValuePair("UID", Connector.getUserId()));
				String status = Connector.sendData(parameter, getActivity());
				return status;
			}

			ProgressDialog pdg;
			private ChurchEvents upcomingEvent;

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				pdg = new ProgressDialog(Connector.context);
				pdg.setTitle("Please Wait ");
				pdg.setMessage("Loading Events ....");
				pdg.setCancelable(false);
				pdg.show();
				super.onPreExecute();
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				pdg.cancel();
				Log.e("ITEMS", result);
				processJson(result);
				super.onPostExecute(result);
			}

			public void processJson(String result) {
				// TODO Auto-generated method stub
				// Toast.makeText(getActivity(), result,
				// Toast.LENGTH_LONG).show();

				if (!result
						.equalsIgnoreCase("Sorry No Event Found For the Selected Month")) {

					JSONObject objects;

					try {
						objects = new JSONObject(result);
						if (objects != null) {
							JSONArray jarray = objects.getJSONArray("Church-Events");

							if (jarray != null) {
								for (int i = 0; i < jarray.length(); i++) {
									JSONObject object = jarray.optJSONObject(i);

									if (object != null) {
										String eventID=object.optString("church_branch_event_id", "0");
										String event_title=object.optString("event_title", "");
										String details=object.optString("details", "");
										String start_date=object.optString("start_date", "");
										String starttime=object.optString("start_time");
										String end_date=object.optString("start_time", "");
										String end_time=object.optString("end_time", "");
										String banner=object.optString("banner", "");
										String location=object.optString("location", "");
										
										
											upcomingEvent= new ChurchEvents();
											upcomingEvent.setBanner(banner);
											upcomingEvent.setLocation(location);
											upcomingEvent.setDetail(details);
											upcomingEvent.setEndDate(end_date);
											upcomingEvent.setStart_Time(starttime);
											upcomingEvent.setEndTime(end_time);
											upcomingEvent.setEventID(eventID);
											upcomingEvent.setStartDate(start_date);
											upcomingEvent.setType(object.optString("type"));
											upcomingEvent.setTitle(event_title);
											String ispast=object.optString("OPT");
											upcomingEvent.setIspast(ispast);
											String summary=object.optString("M");
											upcomingEvent.setEventsSummary(summary);
											String isGoing=object.optString("isGoing","");
											upcomingEvent.setIsGoing(isGoing);
											//devotions.add(upcomingEvent);
									

									} else {
										Log.e("ERR", "invalid Object");
									}
								}
								// Toast.makeText(getActivity(),
								// devotions.getCount() + " 8494949449",
								// Toast.LENGTH_LONG).show();
								//devotions.notifyDataSetChanged();
							} else {
								Log.e("ERR", "invalid Object");
							}

						}

					} catch (JSONException e) {

					}
				} else {
					Toast.makeText(getActivity(), result, Toast.LENGTH_LONG)
							.show();

					getActivity().finish();
					
				}
			}
		}
		//Load Events
	}
}
