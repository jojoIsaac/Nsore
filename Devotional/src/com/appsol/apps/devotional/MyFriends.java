package com.appsol.apps.devotional;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.appsol.apps.projectcommunicate.adapter.UsersFeedListAdapter;
import com.appsol.apps.projectcommunicate.classes.Connector;
import com.appsol.apps.projectcommunicate.model.NsoreDevotionalUser;
import com.appsol.volley.classes.ExtHttpClientStack;
import com.appsol.volley.classes.SslHttpClient;
import com.appsol.volley.classes.Volley;
//import com.android.volley.toolbox.Volley;

public class MyFriends extends ActionBarActivity {

	public MyFriends() {
		// TODO Auto-generated constructor stub
	}
	
	public Integer justClicked=199;
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		
		outState.putString("UID", UID);
	}
	

	private static final String TAG = FriendsList.class
			.getSimpleName();
	private View rootView;
	private ListView listView;
	private UsersFeedListAdapter listAdapter;
	private List<NsoreDevotionalUser> feedItems;
	private String URL_FEED = "http://api.androidhive.info/feed/feed.json";
	static Map<String, String> params;
	private boolean mHasData = false;
	private boolean mInError = false;
	private boolean isSearching=false;
	private  String UID;
	Integer startAT = 0;
	
	private static ProgressBar progressbarLoad;
	
	private TextWatcher filterTextWatcher = new TextWatcher() {
		 
@Override
public void afterTextChanged(Editable s) {
// TODO Auto-generated method stub
 
}
 
@Override
public void beforeTextChanged(CharSequence s, int start, int count,
int after) {
// TODO Auto-generated method stub
 
}
 
@Override
public void onTextChanged(CharSequence s, int start, int before,
int count) {
	
		feedItems= new ArrayList<NsoreDevotionalUser>();
		listAdapter = new UsersFeedListAdapter(MyFriends.this,android.R.layout.simple_list_item_1, feedItems,true);
		if(listView!=null)
		{
			listView.setAdapter(listAdapter);
			listView.setOnScrollListener(new EndlessScrollListener());
		}
		justClicked=0;
		loadpage2(0) ;
		listView.setOnScrollListener(new EndlessScrollListener());
		
	
	
}
};



	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myfriends_layout);
		Connector.myPrefs= getSharedPreferences(Connector.prefernceName, Context.MODE_PRIVATE);
        Connector.context= this;
		if(savedInstanceState!=null)
		{
			UID = savedInstanceState.getString("UID");
		}
		else
		{
			UID = Connector.getUserId();
		}
		intiCompoments();

	}
	String title="";
	private EditText inputSearch;
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		
		feedItems= new ArrayList<NsoreDevotionalUser>();
		listAdapter = new UsersFeedListAdapter(this,android.R.layout.simple_list_item_1, feedItems,true);
		if(listView!=null)
		{
			listView.setAdapter(listAdapter);
			listView.setOnScrollListener(new EndlessScrollListener());
		}
		justClicked=0;
		loadpage();
		listView.setOnScrollListener(new EndlessScrollListener());
		super.onResume();
	}
	private void loadpage() {
		justClicked=0;
			
			progressbarLoad.setVisibility(View.VISIBLE);
			URL_FEED = "https://nsoredevotional.com/mobile/devotionHandler.php";
			StringRequest jsonReq = new StringRequest(Method.POST, URL_FEED,
					createMyReqSuccessListener(), createMyReqErrorListener()) {
				@Override
				protected Map<String, String> getParams() {
					Map<String, String> params = new HashMap<String, String>();
					params.put("CID", Connector.getChurchID());
					params.put("MID", UID);
					params.put("UID", Connector.getUserId());
					
					String data= inputSearch.getText().toString();
					if(!TextUtils.isEmpty(data))
					{
						params.put("reason", "Search User");
						params.put("data", data);
						params.put("index",  "1");
					}
					else
					{
						params.put("reason", "Load My Connections");
					
					}
					return params;
				}
			};
			InputStream keyStore = getResources().openRawResource(R.raw.appsol);

			RequestQueue queue = Volley.newRequestQueue(this,
					new ExtHttpClientStack(new SslHttpClient(keyStore, "qwerty")));
			// Adding request to volley request queue
			queue.add(jsonReq);
		}
	
	// Class to handle endless scrollings
		public class EndlessScrollListener implements OnScrollListener {
			// how many entries earlier to start loading next page
			private int visibleThreshold = 5;
			private int currentPage = 0;
			private int previousTotal = 0;
			private boolean loading = true;
			

			public EndlessScrollListener() {
			}

			public EndlessScrollListener(int visibleThreshold) {
				this.visibleThreshold = visibleThreshold;
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (loading) {
					if (totalItemCount > previousTotal) {
						loading = false;
						previousTotal = totalItemCount;
						currentPage++;
					}
				}
				if (!loading
						&& (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
					// I load the next page of gigs using a background task,
					// but you can call any function here.

					loadpage2(0);
					justClicked=199;
					loading = true;
				}
			}

			
			
			
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			public int getCurrentPage() {
				return currentPage;
			}
		}
	
		private void loadpage2(final int r) {
		 int start=0;
			if(r==1)// Reload the page 
			{
				 start = 0;
			
			}
			else{
				start = 1 + listAdapter.getCount();
			}
			final int startIndex = start;
			
			progressbarLoad.setVisibility(View.VISIBLE);
			URL_FEED = "https://nsoredevotional.com/mobile/devotionHandler.php";
			StringRequest jsonReq = new StringRequest(Method.POST, URL_FEED,
					createMyReqSuccessListener(), createMyReqErrorListener()) {
				@Override
				protected Map<String, String> getParams() {
					Map<String, String> params = new HashMap<String, String>();
					params.put("CID", Connector.getChurchID());
					params.put("MID", UID);
					params.put("UID", Connector.getUserId());
					params.put("reason", "Load My Connections");
					if(startIndex==0)// Reload the page 
					{
						params.put("index",  "1");
					
					}
					else{
						params.put("index", startIndex + "");
					}
					
					String data= inputSearch.getText().toString();
					if(!TextUtils.isEmpty(data))
					{
						params.put("reason", "Search User");
						params.put("data", data);
						
					}
					else
					{
						params.put("reason", "Load My Connections");
					
					}

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
					if (!response.equalsIgnoreCase("No Testimony Shared.")) {
						//Toast.makeText(MyFriends.this, response, Toast.LENGTH_LONG).show();
						parseJsonFeed(response);
					}
				}
			};
		}

		private Response.ErrorListener createMyReqErrorListener() {
			return new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					progressbarLoad.setVisibility(View.GONE);
					if(error!=null)
					{
					showErrorDialog(Connector.HandleVolleyerror(error, MyFriends.this));
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
					loadpage2(2);
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
		
		
		
		   /**
	     * Parsing json reponse and passing the data to feed view list adapter
	     * */
	    private void parseJsonFeed(String response) {
	    	//Toast.makeText(this, response, Toast.LENGTH_LONG).show();
	    	progressbarLoad.setVisibility(View.GONE);
	    	if (!response.trim()
					.contains("No Comment Found.") && !response.trim().contains("{\"Friends\":\"No Friend Found\"}")) {

				JSONObject objects;

				
				 Integer processing= Connector.processUsersListJson(listAdapter, response);
			      if(processing>0)
			      {
			    	  progressbarLoad.setVisibility(View.GONE);
			      }
	    	}
	    	else
	    	{
	    		//Toast.makeText(Connector.context, response, Toast.LENGTH_LONG).show();
	    	}
	    }
	
	

	private void intiCompoments() {
		// TODO Auto-generated method stub
		title= getIntent().getStringExtra("Title");
		
		if(!title.contains("(0)"))
		{
			setTitle(title.trim());
		}
		else
		{
			setTitle("Friends");
		}
		progressbarLoad = (ProgressBar) findViewById(R.id.progressbarLoad);
		URL_FEED = "https://nsoredevotional.com/mobile/devotionHandler.php";
		listView = (ListView) findViewById(R.id.list);
		feedItems = new ArrayList<NsoreDevotionalUser>();

		listAdapter = new UsersFeedListAdapter(this,android.R.layout.simple_list_item_1, feedItems,true);
		inputSearch = (EditText) findViewById(R.id.inputSearch);
		inputSearch.addTextChangedListener(filterTextWatcher);
listView.setOnItemClickListener( new OnItemClickListener() {

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		// TODO Auto-generated method stub
	
	}
});
		
justClicked=0;
		listView.setAdapter(listAdapter);
		listView.setOnScrollListener(new EndlessScrollListener());
		
		
	}
	
	

	

}
