package com.appsol.apps.devotional;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.android.volley.Cache;
import com.android.volley.Cache.Entry;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import com.appsol.apps.projectcommunicate.adapter.EventFeedListAdapter;
import com.appsol.apps.projectcommunicate.classes.Connector;
import com.appsol.apps.projectcommunicate.model.ChurchEvents;
import com.appsol.volley.classes.ExtHttpClientStack;
import com.appsol.volley.classes.SslHttpClient;
import com.appsol.volley.classes.Volley;


public class ChurchEventFeedActivity extends Fragment {

	 private static final String TAG = MainActivity.class.getSimpleName();
	    private ListView listView;
	    private EventFeedListAdapter listAdapter;
	    private List<ChurchEvents> feedItems;
	    private String URL_FEED = "http://api.androidhive.info/feed/feed.json";
		static Map<String, String> params;
		 private boolean mHasData = false;
		    private boolean mInError = false;
			private View rootView;
			private ProgressBar progressbarLoad;
		    
		    @Override
		    public void onResume() {
		    	// TODO Auto-generated method stub
		    	super.onResume();
		    	//loadpage2();
		    }
		    
			//Class to handle endless scrollings   
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
		        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
		            // I load the next page of gigs using a background task,
		            // but you can call any function here.
		        	
		        	 loadpage2();
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
		    
		    
		    
		    
		    private void loadpage2()
		    {
		    	 final int startIndex = 1 + feedItems.size();
		    	 progressbarLoad.setVisibility(View.VISIBLE);
		    	 URL_FEED ="https://nsoredevotional.com/mobile/eventsFetch.php?reason=Load%20%20Events%20modified&CID="+Connector.getChurchID()+"&index="+startIndex+"&MID="+Connector.getUserId();
			       
			        	//URL_FEED= "https://nsoredevotional.com/mobile/eventsFetch.php?reason=Load%20%20Events%20modified&CID=1&index="+startIndex+"&MID=4";
			        	 StringRequest jsonReq = new  StringRequest(Method.GET,
			                    URL_FEED, createMyReqSuccessListener(),createMyReqErrorListener())
			        	    {
			 		        @Override
			 		        protected Map<String, String> getParams() {
			 		           // Map<String, String> params = new HashMap<String, String>();
//			 		            params.put("reason", "Members Testimonies");
//			 		            params.put("CID", "1");
//			 		            params.put("USER_ID", "1");
			 		            params.put("index", startIndex+"");

			 		            return params;
			 		        }}
			        	 ;
			            InputStream keyStore = getResources().openRawResource(R.raw.appsol);
			            
			            
			            RequestQueue queue = Volley.newRequestQueue(getActivity(),
		                        new ExtHttpClientStack(new SslHttpClient(keyStore, "qwerty")));
			            // Adding request to volley request queue
			            queue.add(jsonReq);
			        }
		    	
		    //}
		    
			private void loadPage() {
//				progressbarLoad.setProgress(0);
//				progressbarLoad.setVisibility(View.GONE);
		       // RequestQueue queue = AppController.getInstance().getRequestQueue();
		 InputStream keyStore = getResources().openRawResource(R.raw.appsol);
		        
		        RequestQueue queue = Volley.newRequestQueue(getActivity(),
		                                 new ExtHttpClientStack(new SslHttpClient(keyStore, "qwerty")));
		        final int startIndex = 1 + feedItems.size();
		       
		        StringRequest myReq = new StringRequest(Method.POST,
		                                              
		        		Connector.URL
		        		,
		                 
		                                                createMyReqSuccessListener(),
		                                                createMyReqErrorListener())
		        {
		        @Override
		        protected Map<String, String> getParams() {
		           // Map<String, String> params = new HashMap<String, String>();
//		            params.put("reason", "Members Testimonies");
//		            params.put("CID", "1");
//		            params.put("USER_ID", "1");
		            params.put("index", startIndex+"");

		            return params;
		        }}

		        ;

		        queue.add(myReq);
		    }
			 private Response.Listener<String> createMyReqSuccessListener() {
			        return new Response.Listener<String>() {
			        	
			        	
			        	
			        	
			        	
			            @Override
			            public void onResponse(String response) {
			            	if(!response.equalsIgnoreCase("Sorry No Event Found For the Selected Month"))
		        			{
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
							loadpage2();
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
			
			   
			
	    @SuppressLint("NewApi")
	    @Override
		public View  onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
		       // super.onCreate(savedInstanceState);
		        
		        Connector.context=getActivity();
		       rootView= inflater.inflate(R.layout.activity_facebook_feed,container,
						false);
		        //URL_FEED ="https://nsoredevotional.com/mobile/eventsFetch.php";
		        listView = (ListView) rootView. findViewById(R.id.list);
	        URL_FEED ="https://nsoredevotional.com/mobile/eventsFetch.php?reason=Load%20%20Events%20modified&CID="+Connector.getChurchID()+"&index=1&MID="+Connector.getUserId();
	        //listView = (ListView) findViewById(R.id.list);
	        progressbarLoad=(ProgressBar) rootView.findViewById(R.id.progressbarLoad);
	        feedItems = new ArrayList<ChurchEvents >();
	 
	        listAdapter = new EventFeedListAdapter(getActivity(), feedItems);
	        listView.setAdapter(listAdapter);
	        listView.setOnScrollListener(new EndlessScrollListener());
	         
	        // These two lines not needed,
	        // just to get the look of facebook (changing background color & hiding the icon)
	       
	        // We first check for cached request
//	        Cache cache = AppController.getInstance().getRequestQueue().getCache();
//	        Entry entry = cache.get(URL_FEED);
//	        if (entry != null) {
//	            // fetch the data from cache
//	            try {
//	                String data = new String(entry.data, "UTF-8");
//	                try {
//	                    parseJsonFeed(data);
//	                } catch (Exception e) {
//	                    e.printStackTrace();
//	                }
//	            } catch (UnsupportedEncodingException e) {
//	                e.printStackTrace();
//	            }
//	 
//	        } 
//	        else {
	            // making fresh volley request and getting json
	        	 StringRequest jsonReq = new  StringRequest(Method.GET,
	                    URL_FEED, new Response.Listener<String>() {
	 
	                        @Override
	                        public void onResponse(String response) {
	                            VolleyLog.d(TAG, "Response: " + response.toString());
	                            if (response != null) {
	                                parseJsonFeed(response);
	                            }
	                        }
	                    },createMyReqErrorListener());
	            InputStream keyStore = getResources().openRawResource(R.raw.appsol);
	            
	            
	            RequestQueue queue = Volley.newRequestQueue(getActivity(),
                        new ExtHttpClientStack(new SslHttpClient(keyStore, "qwerty")));
	            // Adding request to volley request queue
	            queue.add(jsonReq);
	        //}
	 return rootView;
	    }
	 
	    /**
	     * Parsing json reponse and passing the data to feed view list adapter
	     * */
	    private void parseJsonFeed(String response) {
	    	progressbarLoad.setVisibility(View.GONE);
	    	if (!response.trim().
					equalsIgnoreCase("Sorry No Event Found")) {

				JSONObject objects;

				
	        try {
Log.e("DATASD", response);
				objects = new JSONObject(response);
				if (objects != null) {
					JSONArray feedArray  = objects.getJSONArray("Church-Events");

					if (feedArray  != null) {
	        
	            //JSONArray feedArray = response.getJSONArray("feed");
	 
	            for (int i = 0; i < feedArray.length(); i++) {
	                JSONObject feedObj = (JSONObject) feedArray.get(i);
	 
	              ChurchEvents item = new ChurchEvents();
	              item.setEventID(feedObj.getInt("id")+"");
	              item.setChurchname(feedObj.getString("churchName"));
	              item.setBanner(feedObj.getString("banner"));
	              item.setLocation(feedObj.getString("location"));
	              item.setDetail(feedObj.getString("description"));
	              item.setTitle(feedObj.getString("title"));
	              item.setChurchLogo(feedObj.getString("profilePic"));
	              item.setStartDate(feedObj.getString("start_date"));
	              item.setIspast(feedObj.getString("OPT"));
	              item.setIsGoing(feedObj.getString("isGoing"));
	              item.setStart_Time(feedObj.getString("start_time"));
	              item.setStart_date(feedObj.getString("start_date"));
	              item.setUsersAttending(feedObj.getInt("usersAttending")+"");
	              item.setJson(feedObj.toString(1));
	              //Log.e("ERRU", "id: "+ feedObj.getString("id")+"   isPast: "+ feedObj.getString("OPT") );
//	                item.setId(feedObj.getInt("id"));
//	                item.setName(feedObj.getString("name"));
//	 
//	                // Image might be null sometimes
//	                String image = feedObj.isNull("banner") ? null : feedObj
//	                        .getString("banner");
//	                item.setImge(image);
//	                item.setStatus(feedObj.getString("description"));
//	                item.setProfilePic(feedObj.getString("profilePic"));
//	                item.setTimeStamp(feedObj.getString("start_time"));
//	 
//	                // url might be null sometimes
//	                String feedUrl = feedObj.isNull("url") ? null : feedObj
//	                        .getString("url");
//	                item.setUrl(feedUrl);
	          
	                feedItems.add(item);
	            }
	 
	            // notify data changes to list adapater
	            listAdapter.notifyDataSetChanged();
					}
					
				}
	        } catch (JSONException e) {
	            e.printStackTrace();
	        }
	    	}
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
