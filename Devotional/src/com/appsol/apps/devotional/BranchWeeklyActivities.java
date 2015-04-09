package com.appsol.apps.devotional;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.StringRequest;
import com.appsol.apps.projectcommunicate.adapter.BranchActivityFeedListAdapter;
import com.appsol.apps.projectcommunicate.adapter.PrayerResponseListAdapter;
import com.appsol.apps.projectcommunicate.classes.Connector;
import com.appsol.apps.projectcommunicate.model.PrayerRequest.PrayerResponses;
import com.appsol.apps.projectcommunicate.model.church_branches.BranchActivity;
import com.appsol.apps.projectcommunicate.model.church_branches;
import com.appsol.volley.classes.ExtHttpClientStack;
import com.appsol.volley.classes.SslHttpClient;
import com.appsol.volley.classes.Volley;

import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class BranchWeeklyActivities extends ActionBarActivity {

	private String churchID="-1",BranchID="-1",BranchName="0",ChurchName="0";
	private boolean mInError;
	private ProgressBar progressbarLoad;
	private String URL_FEED;
	static ListView lstprayerrequest;
	
	
	public static church_branches.BranchActivity bmark;
	public static BranchActivityFeedListAdapter bdapter;
	public static List<church_branches.BranchActivity> feedItems;
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putString("BID",BranchID);
		//outState.putString("PR", PrayerRequest);
		outState.putString("BN", BranchName);
		outState.putString("CID", churchID);
		outState.putString("CN", ChurchName);
	}
	private Response.ErrorListener createMyReqErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				progressbarLoad.setVisibility(View.GONE);
				if(error!=null)
				{
				showErrorDialog(Connector.HandleVolleyerror(error, BranchWeeklyActivities.this));
				if(error.getMessage()!=null)
				Log.d("RES", error.getMessage());
				}
				else
				{
					
					showErrorDialog("An error occured. Please check your internet Connection");
				}
				
			}
		};
	}

	private void showErrorDialog(String error) {
		mInError = true;

		AlertDialog.Builder b = new AlertDialog.Builder(this);
		if(!TextUtils.isEmpty(error))
		{
			b.setMessage("Response : " + error);
		}
		else
		{
			b.setMessage("Response : "+"Unable to perform requested action" );
		}
		b.setPositiveButton("Try Again",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				loadpage();
			}
	
		}).
		setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
	
		}).
		show();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(savedInstanceState!=null)
		{
			churchID = savedInstanceState.getString("CID");
			ChurchName = savedInstanceState.getString("CN");
			BranchID= savedInstanceState.getString("BID");
			BranchName=savedInstanceState.getString("BN");
			//dateSent=savedInstanceState.getString("DS");
			
		}
		
		if ( savedInstanceState==null) {
			
			churchID = getIntent().getStringExtra("CID");
			ChurchName = getIntent().getStringExtra("CN");
			BranchID= getIntent().getStringExtra("BID");
			BranchName=getIntent().getStringExtra("BN");
		}
		
		//Toast.makeText(this, churchID, Toast.LENGTH_LONG).show();
		setContentView(R.layout.activity_branch_weekly_activities);
		lstprayerrequest = (ListView) findViewById(R.id.lv);
		progressbarLoad = (ProgressBar) findViewById(R.id.progressbarLoad);
		TextView txtrequestContent= (TextView) findViewById(R.id.txtrequestContent);
		//TextView timestamp=(TextView) findViewById(R.id.timestamp);
		TextView txtChurchName =(TextView) findViewById(R.id.txtChurchName);
		txtrequestContent.setText("Weekly Activities");
		setTitle(BranchName);
		//timestamp.setText(dateSent);
		txtChurchName.setText("@"+ChurchName);
		txtChurchName.setVisibility(View.GONE);
		feedItems= new ArrayList<BranchActivity>();
		bdapter = new BranchActivityFeedListAdapter(this,
				android.R.layout.simple_list_item_1,feedItems);
		lstprayerrequest.setAdapter(bdapter);
		
		loadpage();
	}

	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		//loadpage();
		super.onResume();
	}
	private void loadpage() {
		//justClicked=0;
			
			progressbarLoad.setVisibility(View.VISIBLE);
			URL_FEED = "https://nsoredevotional.com/mobile/devotionHandler.php";
			StringRequest jsonReq = new StringRequest(Method.POST, URL_FEED,
					createMyReqSuccessListener(), createMyReqErrorListener()) {
				@Override
				protected Map<String, String> getParams() {
					  Map<String, String> params = new HashMap<String, String>();
	 		            params.put("reason", "Get Branch Weekly Activity");
	 		           params.put("BID", BranchID);
	 		           params.put("CID", churchID);
					return params;
				}
			};
			InputStream keyStore = getResources().openRawResource(R.raw.appsol);

			RequestQueue queue = Volley.newRequestQueue(this,
					new ExtHttpClientStack(new SslHttpClient(keyStore, "qwerty")));
			// Adding request to volley request queue
			queue.add(jsonReq);
		}
	
	
	private Response.Listener<String> createMyReqSuccessListener() {
        return new Response.Listener<String>() {
        	
        	
        	
        	
        	
            @Override
            public void onResponse(String response) {
            	if(!response.equalsIgnoreCase("Sorry No Event Found For the Selected Month")&&!response.equalsIgnoreCase("ERR") && !response.equalsIgnoreCase("Saved"))
    			{
            		parseJsonFeed(response);
    			}
            	
            }
        };
 }

  
	
	
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.branch_weekly_activities, menu);
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
	
	
	
	
	  // Process the response
	   /**
	    * Parsing json reponse and passing the data to feed view list adapter
	    * */
	   private void parseJsonFeed(String response) {
	 	//Toast.makeText(this, response, Toast.LENGTH_LONG).show();
	   //	Log.e("RES",response);
	   	progressbarLoad.setVisibility(View.GONE);
	   	if (!response.trim()
					.contains("No Comment Found.") && !response.trim().contains("{\"WeeklyActivities\":\"No PrayerRequest\"}")) {
				JSONObject objects;
	       try {

				objects = new JSONObject(response.trim());
				if (objects != null) {
					JSONArray feedArray  = objects.getJSONArray("WeeklyActivities");

					if (feedArray  != null) {
	       
	           //JSONArray feedArray = response.getJSONArray("feed");



	           for (int i = 0; i < feedArray.length(); i++) {
	               JSONObject feedObj = (JSONObject) feedArray.get(i);

	            BranchActivity item = new  BranchActivity ();
	            /*
	             * {"id":"1","day":"monday","description":"","activity":"Mens Fellowship",
	             * "venue":"Church Auditorium",
	             * "time":"6pm","churchName":"Nsore Devotional Official Community","Logo":"1412478079.png"}
	             */
	             item.setID(feedObj.getString("id"));
	             item.setDay(feedObj.getString("day"));
	             item.setDescription(feedObj.getString("description"));
	             item.setActivity(feedObj.getString("activity"));
				 item.setVenue(feedObj.getString("venue")+"");
				 item.setTime(feedObj.getString("time"));
				 item.setchurchlogo(feedObj.getString("Logo"));
				 item.setActivity(feedObj.getString("activity"));
				 item.setchurchname(feedObj.getString("churchName"));
				// setTitle(item.getchurchname());
	             bdapter .add(item);
	           }

	           // notify data changes to list adapater
	           
	           //startAT= feedItems.size()+ 1;
	           bdapter .notifyDataSetChanged();
	           progressbarLoad.setVisibility(View.GONE);
//	          if(bdapter.getCount()>0)
//	          {
//	        	  toogleView(true);
//	          }
					}
					
				}
	       } catch (JSONException e) {
	           e.printStackTrace();
	       }
	   	}
	   	else
	   	{
	   		//Toast.makeText(Connector.context, response, Toast.LENGTH_LONG).show();
	   	}
	   }
	
	
	
	
	
	
	
	
	
	
	
}
