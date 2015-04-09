package com.appsol.apps.devotional;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.appsol.apps.projectcommunicate.adapter.TestimonyFeedListAdapter;
import com.appsol.apps.projectcommunicate.classes.Connector;
import com.appsol.apps.projectcommunicate.model.Testimony;
import com.appsol.volley.classes.ExtHttpClientStack;
import com.appsol.volley.classes.SslHttpClient;
import com.appsol.volley.classes.Volley;


public class ChurchTestimonyFeedActivity extends Fragment {

	
	void restoreTitle()
	{
		MainActivity.mTitle="Testimonies";
		getActivity().supportInvalidateOptionsMenu();
		MainActivity.restoreActionBar();
	}
	
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		restoreTitle();
		super.onResume();
	}
	
	
	 private static final String TAG = MainActivity.class.getSimpleName();
	    private ListView listView;
	    private TestimonyFeedListAdapter listAdapter;
	    private List<Testimony> feedItems;
	    private String URL_FEED = "http://api.androidhive.info/feed/feed.json";
		static Map<String, String> params;
		 private boolean mHasData = false;
		    private boolean mInError = false;
		    Integer startAT=0;
			private View rootView;
			private static ProgressBar progressbarLoad;
			
		    
		    
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
			        	URL_FEED = "https://nsoredevotional.com/mobile/eventsFetch.php";
			        	 StringRequest jsonReq = new  StringRequest(Method.POST,
			                    URL_FEED, createMyReqSuccessListener(),createMyReqErrorListener())
			        	    {
			 		        @Override
			 		        protected Map<String, String> getParams() {
			 		       Map<String, String> 
			 		             params = new HashMap<String, String>();
			 		        	 params.put("CID", Connector.getChurchID());
				 		         params.put("MID", Connector.getUserId());
					 		     params.put("reason", "Get Church Testimonies");
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
		          Map<String, String> params = new HashMap<String, String>();
//		            params.put("reason", "Members Testimonies");
//		            params.put("CID", "1");
//		            params.put("USER_ID", "1");
		          params.put("CID", Connector.getChurchID());
		            params.put("MID", Connector.getUserId());
		 		        	params.put("reason", "Get Church Testimonies");
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
			            	if(!response.equalsIgnoreCase("No Testimony Shared."))
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
			
		
			   
			  
			
			   @Override
				public void onCreate(Bundle savedInstanceState) {
					super.onCreate(savedInstanceState);
					
					restoreTitle();
					setHasOptionsMenu(true);
					
				}
				
				@Override
				public boolean onOptionsItemSelected(MenuItem item) {
					// TODO Auto-generated method stub
					switch (item.getItemId()) {
					case R.id.mn_share_testimony:
					{
					    createTestimony();
						
					}
					break;
					
					
					

					default:
						break;
					}
					return super.onOptionsItemSelected(item);
				}
				
				public static void createTestimony()
				{
					Intent ints= new Intent(Connector.context, ShareTestimony.class);
					Connector.context.startActivity(ints);
				}
			@Override
			public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
				// TODO Auto-generated method stub

				inflater.inflate(R.menu.testimony_menu, menu);
				super.onCreateOptionsMenu(menu, inflater);
			} 
			
	    @SuppressLint("NewApi")
		public View  onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
	       // super.onCreate(savedInstanceState);
	    	restoreTitle();
			getActivity().setTitle("Testimonies");
			getActivity().supportInvalidateOptionsMenu();
	        Connector.context=getActivity();
	       rootView= inflater.inflate(R.layout.activity_facebook_feed,container,
					false);
	       
	       progressbarLoad=(ProgressBar) rootView.findViewById(R.id.progressbarLoad);
//			 if(progressbarLoad!=null)
//			 {
//				 progressbarLoad.setProgress(0);
//			 }
//	       
	        URL_FEED ="https://nsoredevotional.com/mobile/eventsFetch.php";
	        listView = (ListView) rootView. findViewById(R.id.list);
	 
	        feedItems = new ArrayList<Testimony >();
	 
	        listAdapter = new TestimonyFeedListAdapter(getActivity(), feedItems);
	        listView.setAdapter(listAdapter);
	        listView.setOnScrollListener(new EndlessScrollListener());
	        	 StringRequest jsonReq = new  StringRequest(Method.POST,
	                    URL_FEED, new Response.Listener<String>() {
	 
	                        @Override  
	                        public void onResponse(String response) {
	                            VolleyLog.d(TAG, "Response: " + response.toString());
	                            if (response != null) {
	                                parseJsonFeed(response);
	                            }
	                        }
	                    },createMyReqErrorListener())
	        	 {
		 		        @Override
		 		        protected Map<String, String> getParams() {
		 		         Map<String, String> params = new HashMap<String, String>();
//		 		            params.put("reason", "Members Testimonies");
	 		            params.put("CID", Connector.getChurchID());
	 		            params.put("MID", Connector.getUserId());
		 		        	params.put("reason", "Get Church Testimonies");
		 		           
		 		            return params;
		 		        }}
	        	 ;
	            InputStream keyStore = getResources().openRawResource(R.raw.appsol);
	            
	            
	            RequestQueue queue = Volley.newRequestQueue(getActivity(),
                        new ExtHttpClientStack(new SslHttpClient(keyStore, "qwerty")));
	            // Adding request to volley request queue
	            queue.add(jsonReq);
	       // }
	 return  rootView;
	    }
	 
	    /**
	     * Parsing json reponse and passing the data to feed view list adapter
	     * */
	    private void parseJsonFeed(String response) {
	    	//Toast.makeText(this, response, Toast.LENGTH_LONG).show();
	    	progressbarLoad.setVisibility(View.GONE);
	    	if (!response
					.contains("No Testimony Shared.")) {

				JSONObject objects;

				
	        try {

				objects = new JSONObject(response);
				if (objects != null) {
					JSONArray feedArray  = objects.getJSONArray("ChurchTestimonies");

					if (feedArray  != null) {
	        
	            //JSONArray feedArray = response.getJSONArray("feed");
	 
	            for (int i = 0; i < feedArray.length(); i++) {
	                JSONObject feedObj = (JSONObject) feedArray.get(i);
	 
	              Testimony  item = new Testimony();
	              
	              item.setId(feedObj.getInt("id")+"");
	              item.setChurchname(feedObj.getString("churchName"));
	              item.setUserdp(feedObj.getString("profilePic"));
	              item.setName(feedObj.getString("name"));
	              item.setFullcontent(feedObj.getString("content"));
	              item.setLikes(feedObj.getString("likes"));
	              item.setLikeTestimony(feedObj.getString("Like"));
	              item.setMessageTime(feedObj.getString("timeStamp"));
	              item.setSenderid(feedObj.getString("userid"));
	              item.setDeviceID(feedObj.getString("deviceid"));
                  item.setJson(feedObj.toString());
                  item.setUserCover(feedObj.getString("cover"));
                  item.setAboutUser("about-user");
                  item.setContent(feedObj.getString("summary"));
                  item.setIsConnected(feedObj.getInt("isConnected")+"");
    			  item.setFriendsCount(feedObj.getInt("connection")+"");
    			  item.setMutualFriends(feedObj.optString("mutualFriends",feedObj.optInt("mutualFriends", 0)+""));
    			  item.setMemberSince(feedObj.getString("date_joined"));
                  
                 //startAT = 
                  
	                feedItems.add(item);
	            }
	 
	            // notify data changes to list adapater
	            
	            startAT= feedItems.size()+1;
	            listAdapter.notifyDataSetChanged();
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
