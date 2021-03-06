package com.appsol.apps.devotional;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.StringRequest;
import com.appsol.apps.devotional.DevotionalHistoryFeedActivity.EndlessScrollListener;
import com.appsol.apps.projectcommunicate.adapter.ChurchGroupFeedListAdapter;
import com.appsol.apps.projectcommunicate.adapter.DevotionFeedListAdapter;
import com.appsol.apps.projectcommunicate.adapter.UsersFeedListAdapter;
import com.appsol.apps.projectcommunicate.classes.Connector;
import com.appsol.apps.projectcommunicate.classes.Devotion;
import com.appsol.apps.projectcommunicate.model.ChurchGroup;
import com.appsol.apps.projectcommunicate.model.DevotionComment;
import com.appsol.apps.projectcommunicate.model.NsoreDevotionalUser;
import com.appsol.volley.classes.ExtHttpClientStack;
import com.appsol.volley.classes.SslHttpClient;
import com.appsol.volley.classes.Volley;
//import com.android.volley.toolbox.Volley;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Toast;

public class ChurchGroupList extends Fragment {
     public Integer justClicked=199;
	public ChurchGroupList() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		
		outState.putString("CID", CID);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(requestCode==199 && resultCode==Activity.RESULT_OK)
		{
//       listAdapter.getItem(1).setIsConnected("-1");
//       listAdapter.notifyDataSetChanged();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	private static final String TAG = ChurchGroupList.class
			.getSimpleName();
	private View rootView;
	private ListView listView;
	private ChurchGroupFeedListAdapter listAdapter;
	private List<ChurchGroup> feedItems;
	private String URL_FEED = "http://api.androidhive.info/feed/feed.json";
	static Map<String, String> params;
	private boolean mHasData = false;
	private boolean mInError = false;
	private  String CID="";
	Integer startAT = 0;
	
	private static ProgressBar progressbarLoad;
	
	@Override
	public void onViewStateRestored(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewStateRestored(savedInstanceState);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(savedInstanceState!=null)
		{
			CID = savedInstanceState.getString("CID");
		}
		if (getArguments() != null && savedInstanceState==null) {
			CID= getArguments().getString("CID");
		}
		
		
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		feedItems= new ArrayList<ChurchGroup>();
		listAdapter = new ChurchGroupFeedListAdapter(getActivity(),android.R.layout.simple_list_item_1, feedItems);
		if(listView!=null)
		{
			listView.setAdapter(listAdapter);
			listView.setOnScrollListener(new EndlessScrollListener());
		}
		justClicked=0;
		loadpage();
		//listView.setOnScrollListener(new EndlessScrollListener());
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
					params.put("MID", CID);
					params.put("UID", Connector.getUserId());
					params.put("reason", "Load Church Groups");
					return params;
				}
			};
			InputStream keyStore = getResources().openRawResource(R.raw.appsol);

			RequestQueue queue = Volley.newRequestQueue(getActivity(),
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
					params.put("MID", CID);
					params.put("UID", Connector.getUserId());
					params.put("reason", "Load Church Groups");
					if(startIndex==0)// Reload the page 
					{
						params.put("index",  "0");
					
					}
					else{
						params.put("index", startIndex + "");
					}
					

					return params;
				}
			};
			InputStream keyStore = getResources().openRawResource(R.raw.appsol);

			RequestQueue queue = Volley.newRequestQueue(getActivity(),
					new ExtHttpClientStack(new SslHttpClient(keyStore, "qwerty")));
			// Adding request to volley request queue
			queue.add(jsonReq);
		}
		
		private Response.Listener<String> createMyReqSuccessListener() {
			return new Response.Listener<String>() {

				@Override
				public void onResponse(String response) {
					if (!response.equalsIgnoreCase("No Testimony Shared.")) {
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
					showErrorDialog(Connector.HandleVolleyerror(error, getActivity()));
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

			AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
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
					loadpage2(0);
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
	    	if (!TextUtils.isEmpty(response)&&!response.trim()
					.contains("No Group Found") && !response.trim().contains("{\"Friends\":\"No Friend Found\"}")) {

				JSONObject objects;

				
	        try {

				objects = new JSONObject(response.trim());
				if (objects != null) {
					JSONArray feedArray  = objects.getJSONArray("groups");

					if (feedArray  != null) {
	        
	            //JSONArray feedArray = response.getJSONArray("feed");
	 
	 
	 justClicked=199;
	            for (int i = 0; i < feedArray.length(); i++) {
	                JSONObject feedObj = (JSONObject) feedArray.get(i);
	 
	             ChurchGroup  item = new  ChurchGroup();
	              /*
	              {"id":"2",
	              "name":"Nsore Prayer Tower",
	              "date":"2015-02-19 19:59:53",
	              "slogan":"We pray ",
	              "description":"Prayer is the master key",
	              "logo":"","type":"opened",
	              "memberCount":"1",
	              "church":"Nsore Devotional Official Community",
	              "church-logo":"1412478079.png",
	              "branch":"NA","since":"1 hr ago",
	              "status":"accepted"},
	               * 
	               */
	            item.setGroupType(feedObj.getString("type"));
	             item.setGroupID(feedObj.getString("id"));
	             
	              item.setGroupName(feedObj.getString("name"));
	              item.setDescription(feedObj.getString("description"));
	           
	              item.setSlogan(feedObj.getString("slogan"));
	              item.setDateCreated(feedObj.getString("date"));
	              //item.setCurrent(false);
				 // item.setProfilePic(feedObj.getString("profilePic"));
				  item.setMembershipstatus(feedObj.getString("status")+"");
				  //item.setFriendsCount(feedObj.getString("friends")+"");
				  item.setMembersCount(feedObj.optString("memberCount",feedObj.optInt("memberCount", 0)+""));
				  item.setMemberSince(feedObj.getString("since"));
				  item.setGrouplogo(feedObj.getString("logo"));
				  item.setchurchname(feedObj.getString("church"));
	              item.setchurchid(feedObj.optInt("churchID",0));
	              item.setchurchlogo(feedObj.optString("church-logo", ""));
				  item.setJson(feedObj.toString());
	                 
	             //startAT = 
	              
	              listAdapter.add(item);
	            }
	 
	            // notify data changes to list adapater
	            
	            startAT= feedItems.size()+ 1;
	            listAdapter.notifyDataSetChanged();
	            progressbarLoad.setVisibility(View.GONE);
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
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		rootView= inflater.inflate(R.layout.activity_facebook_feed, container,
				false);
		intiCompoments(rootView);
	return	rootView;
	}

	private void intiCompoments(View rootView) {
		// TODO Auto-generated method stub
		progressbarLoad = (ProgressBar) rootView
				.findViewById(R.id.progressbarLoad);
		URL_FEED = "https://nsoredevotional.com/mobile/devotionHandler.php";
		listView = (ListView) rootView.findViewById(R.id.list);
		feedItems = new ArrayList<ChurchGroup>();

		listAdapter = new ChurchGroupFeedListAdapter(getActivity(),android.R.layout.simple_list_item_1, feedItems);
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
