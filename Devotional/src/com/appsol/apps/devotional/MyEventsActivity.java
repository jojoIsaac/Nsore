package com.appsol.apps.devotional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.appsol.apps.projectcommunicate.classes.Connector;
import com.appsol.apps.projectcommunicate.classes.Connector.GridMonthAdapter;
import com.appsol.apps.projectcommunicate.model.ChurchEvents;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MyEventsActivity extends ActionBarActivity {
	public static ChurchEvents upcomingEvent;
static LinearLayout layouthead;
static ProgressBar progressbarLoad;

Button btnReadMore;
private static ImageView btnshow;
private View frags;
static LinearLayout layout_Month;
LinearLayout layout_no_event;
ListView lstDevotions;
ItemAdapter devotions;
ArrayList<ChurchEvents> eventsList;
private GridView gridView;
private GridMonthAdapter MonthAdapter;
private static DisplayImageOptions options;
static TextView txteventTitle,txteventstartdate,txteventLocation;
@Override
public void onResume() {
	// TODO Auto-generated method stub
	//Toast.makeText(getActivity(), "Again", Toast.LENGTH_LONG).show();
	 (new checkUpcomingEvent()).execute();
	super.onResume();
}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		setContentView(R.layout.eventspicker_layout);
		setTitle("My Events");
		
		
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.eventslogo)
		.showImageForEmptyUri(R.drawable.eventslogo)
		.showImageOnFail(R.drawable.eventslogo).cacheInMemory(true)
		.cacheOnDisk(true).considerExifParams(true)
		.displayer(new RoundedBitmapDisplayer(6)).build();
		 Connector.context=this;

		 
		 
		 progressbarLoad=(ProgressBar) findViewById(R.id.progressbarLoad);
		 if(progressbarLoad!=null)
		 {
			 progressbarLoad.setProgress(0);
		 }
		

		 gridView = (GridView) findViewById(R.id.grid_view);
		 layout_Month=(LinearLayout) findViewById(R.id.layout_Month);
		 layout_no_event=(LinearLayout) findViewById(R.id.layout_no_event);
		 TextView txtchurchname=(TextView) findViewById(R.id.txt1);
		 
		 
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
			
		 MonthAdapter = new GridMonthAdapter (this);
		 gridView.setAdapter(MonthAdapter);
		 TextView txtYear= (TextView) findViewById(R.id.txtYear);
		 
		 txtYear.setText("Browse events within the year "+Connector.getYear());
		 txtYear.setVisibility(View.GONE);
		   gridView.setOnItemClickListener(new OnItemClickListener() {
	            @Override
	            public void onItemClick(AdapterView<?> parent, View v,
	                    int position, long id) {
	 
	            	loadEvents(String.valueOf(position+1), Connector.getYear());
						
		                //startActivity(i);
	            	
	            }
	        });
		 upcomingEvent= new ChurchEvents();
		 btnshow=(ImageView) findViewById(R.id.btnshow);
		 btnshow.setImageResource(R.drawable.uparrow);
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
		 //Year: 
		 eventsList= new ArrayList<ChurchEvents>();
			devotions = new ItemAdapter(this,
					android.R.layout.simple_list_item_1, eventsList);
		 lstDevotions = (ListView) findViewById(R.id.lstMessages);
			lstDevotions.setAdapter(devotions);
			registerForContextMenu(lstDevotions);
			lstDevotions.setOnItemClickListener( new OnItemClickListener() {

				

				private ChurchEvents selectedDevotion;
				//private PlaceholderFragment fragment;
				private Object fragmentmanger;

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int position,
						long arg3) {
					// TODO Auto-generated method stub
					upcomingEvent= devotions.getItem(position);
					if(upcomingEvent!=null)
					{
						Intent intent= new Intent(MyEventsActivity.this, EventDetails.class);
						
						intent.putExtra("EventID", upcomingEvent.getEventID());
						intent.putExtra("banner", upcomingEvent.getBanner());
						intent.putExtra("Location", upcomingEvent.getLocation());
						intent.putExtra("Detail", upcomingEvent.getDetail());
						intent.putExtra("EndDate", upcomingEvent.getEndDate());
						intent.putExtra("Start_Time", upcomingEvent.getStart_Time());
						intent.putExtra("EndTime", upcomingEvent.getEndTime());
						intent.putExtra("Type", upcomingEvent.getType());
						intent.putExtra("Start_date", upcomingEvent.getStart_date());
						intent.putExtra("Title", upcomingEvent.getTitle());
						intent.putExtra("reason", "Show Upcoming Event");
						intent.putExtra("isPast", upcomingEvent.getIspast());
						intent.putExtra("isGoing", upcomingEvent.getIsGoing());
						intent.putExtra("JSON", upcomingEvent.getJson());
						startActivity(intent);
						
					}
				}
			});
		
		
		
		
	}
	public static void toggleCalender()
	{
		if( layout_Month.getVisibility()!=View.GONE)
		{
			btnshow.setImageResource(R.drawable.uparrow);
			layout_Month.setVisibility(View.GONE);
		}
		else
		{
			btnshow.setImageResource(R.drawable.downarrow);
			layout_Month.setVisibility(View.VISIBLE);
		}
		
		
	}
	
	
	
	class checkUpcomingEvent extends AsyncTask<String, Integer, String>
	{
ProgressDialog progressDialog;
ProgressBar progressbar= new ProgressBar(MyEventsActivity.this);
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
protected void onProgressUpdate(Integer... values) {
	// TODO Auto-generated method stub
	
	if(progressbarLoad!=null)
	   progressbarLoad.setProgress(values[0]);
	super.onProgressUpdate(values);
}
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String response=null;
			ArrayList<NameValuePair> parameter = new ArrayList<NameValuePair>();
			parameter.add(new BasicNameValuePair("reason",
					"Load All My Events"));
			parameter.add(new BasicNameValuePair("UID", Connector.getUserId()));
			parameter.add(new BasicNameValuePair("CID", Connector
					.getChurchID()));
			response = Connector.sendData(parameter, MyEventsActivity.this);
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
		
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			if(progressbarLoad!=null)
			{
			progressbarLoad.setProgress(0);
			progressbarLoad.setVisibility(View.GONE);
			}
			//
			
			processJson(result);
			super.onPostExecute(result);
		}
		
		
		private void processJson(String result) {
			// TODO Auto-generated method stub
			if(result.equalsIgnoreCase("Sorry No Event Found"))
			{
				 layout_Month.setVisibility(View.VISIBLE);
				 layout_no_event.setVisibility(View.VISIBLE);
				 Toast.makeText(MyEventsActivity.this, result, Toast.LENGTH_LONG).show();
			}
			else
			{
				
					devotions.clear();
				
				//Handle JSON Processing
				
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
									String day= object.optString("DW");
									String date= object.optString("DD");
									//String location=object.optString("location", "");
									
									
										upcomingEvent= new ChurchEvents();
										upcomingEvent.setDate_string(date);
										upcomingEvent.setDay_string(day);
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
										upcomingEvent.setJson(object.toString());
										devotions.add(upcomingEvent);
								

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
			}
		}


		
	}
	
	
	
	
	
	
	
	public static class ViewHolder {
		TextView title;
		TextView content;
		ImageView image;
		TextView txtTime;
		TextView txtlikes;
		
		//DateView
		LinearLayout lvDate;
		TextView txtDate;
		TextView tvc;
	}

	public static class ItemAdapter extends ArrayAdapter<ChurchEvents> {

		List<ChurchEvents> testimoniess;
    Context context;
		public ItemAdapter(Context context, int resource,
				List<ChurchEvents> objects) {
			super(context, resource, objects);
			testimoniess = objects;
			this.context= context;
			// TODO Auto-generated constructor stub
		}

		private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
		private Integer iCounter=0;

		@Override
		public int getCount() {
			return testimoniess.size();
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View view = convertView;
			final ViewHolder holder;

			ChurchEvents testimony = getItem(position);

			if (convertView == null) {
			view = ((Activity) context).getLayoutInflater().inflate(
						R.layout.eventitem, parent, false);
				
				
				holder = new ViewHolder();
				holder.title = (TextView) view.findViewById(R.id.title);
				//holder.image = (ImageView) view.findViewById(R.id.thumbImage);
				holder.content = (TextView) view
						.findViewById(R.id.txtContent);

				holder.lvDate =(LinearLayout) view.findViewById(R.id.lvDate);
				holder.txtDate= (TextView) view.findViewById(R.id.txtdate);
				holder.tvc=(TextView) view.findViewById(R.id.txtday);
				//holder.txtTime=(TextView) view.findViewById(R.id.txtDate);
				//holder.txtlikes= (TextView) view.findViewById(R.id.txtlikes);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			if (testimony != null) {
				holder.title.setText(testimony.getTitle());
				holder.content.setText(testimony.getEventsSummary());
				String url = Connector.eventBannerURL + testimony.getBanner();
				String likes= "0";

				
				
				iCounter=iCounter+1;
				int check=iCounter % 4;
				if((check)==0)
				{
					holder.lvDate.setBackgroundColor(Connector.context.getResources().getColor(R.color.DevotionBookmarkColor_A));

				}
				else if((check)==1)
				{
					holder.lvDate.setBackgroundColor(Connector.context.getResources().getColor(R.color.DevotionBookmarkColor_B));

				}
				else if((check)==2)
				{
					holder.lvDate.setBackgroundColor(Connector.context.getResources().getColor(R.color.DevotionBookmarkColor_C));

				}
				else if((check)==3)
				{
					holder.lvDate.setBackgroundColor(Connector.context.getResources().getColor(R.color.DevotionBookmarkColor_D));

				}
				holder.tvc.setText(testimony.getDay_string());
				holder.txtDate.setText(testimony.getDate_string());
				
				
				
				//if(!url.equalsIgnoreCase(Connector.eventBannerURL))
				
//				imageLoader.displayImage(url, holder.image, options,
//						animateFirstListener);

			}

			return view;
		}
	}

	public static class AnimateFirstDisplayListener extends
			SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}

	///Handle the GRID Item Click
	private void loadEvents(String  month2, String year2) {
		// TODO Auto-generated method stub
		String param[]={month2,year2};
		(new LoadDevotion()).execute(param);
	}
	
	
	

	class LoadDevotion extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			ArrayList<NameValuePair> parameter = new ArrayList<NameValuePair>();
			parameter.add(new BasicNameValuePair("reason",
					"Load Events for Month"));
			parameter.add(new BasicNameValuePair("M", params[0]));
			parameter.add(new BasicNameValuePair("Y", Connector.getYear()));
			parameter.add(new BasicNameValuePair("CID", Connector
					.getChurchID()));
			parameter.add(new BasicNameValuePair("UID", Connector.getUserId()));
			String response =null;
			
			response = Connector.sendData(parameter, MyEventsActivity.this);
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

		ProgressDialog pdg;
		private ChurchEvents upcomingEvent;

		ProgressDialog progressDialog;
		ProgressBar progressbar= new ProgressBar(MyEventsActivity.this);
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
			if(progressbarLoad!=null)
			{
			progressbarLoad.setProgress(0);
			progressbarLoad.setVisibility(View.GONE);
			}
			processJson(result);
			//
			super.onPostExecute(result);
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			
			if(progressbarLoad!=null)
			   progressbarLoad.setProgress(values[0]);
			super.onProgressUpdate(values);
		}

		public void processJson(String result) {
			// TODO Auto-generated method stub
//			 Toast.makeText(MyEventsActivity.this, result,
//			 Toast.LENGTH_LONG).show();

			if (!result
					.equalsIgnoreCase("Sorry No Event Found For the Selected Month")) {

				JSONObject objects;

				try {
					objects = new JSONObject(result);
					if (objects != null) {
						JSONArray jarray = objects.getJSONArray("Church-Events");

						if (jarray != null) {
							devotions.clear();
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
									
									String day= object.optString("DW");
									String date= object.optString("DD");
									//String location=object.optString("location", "");
									
									
										upcomingEvent= new ChurchEvents();
										upcomingEvent.setDate_string(date);
										upcomingEvent.setDay_string(day);
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
										devotions.add(upcomingEvent);
										upcomingEvent.setJson(object.toString());

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
				Toast.makeText(MyEventsActivity.this, result, Toast.LENGTH_LONG)
						.show();

				
				
			}
		}
	}


	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.my_events, menu);
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
}
