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
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.appsol.apps.devotional.PrayerRequest.createAccount;
import com.appsol.apps.projectcommunicate.adapter.GroupDataFeedListAdapter;
import com.appsol.apps.projectcommunicate.classes.CircularNetworkImageView;
import com.appsol.apps.projectcommunicate.classes.Connector;
import com.appsol.apps.projectcommunicate.model.ChurchGroup;
import com.appsol.apps.projectcommunicate.model.ChurchGroupFeeds;
import com.appsol.volley.classes.ExtHttpClientStack;
import com.appsol.volley.classes.SslHttpClient;
import com.appsol.volley.classes.Volley;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

public class ChurchGroupsActivity1 extends ActionBarActivity {
	private String churchGroupName, logo, id, type, members, status,Clogo,churchname;
	private ImageLoader imageLoader;
	private ChurchGroup churchgroup;
	private Button btngroupmember;
	Button btntoggle_status ;
    private ListView listView;
    private GroupDataFeedListAdapter listAdapter;
    private List<ChurchGroupFeeds> feedItems;
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
			 		        	 params.put("GID", id);
				 		         params.put("UID", Connector.getUserId());
					 		     params.put("reason", "Load Group Feeds");
			 		             params.put("index", startIndex+"");

			 		            return params;
			 		        }} 
			        	 ;
			            InputStream keyStore = getResources().openRawResource(R.raw.appsol);
			            
			            
			            RequestQueue queue = Volley.newRequestQueue(this,
		                        new ExtHttpClientStack(new SslHttpClient(keyStore, "qwerty")));
			            // Adding request to volley request queue
			            queue.add(jsonReq);
			        }
		
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		/*
		 * intent.putExtra("ID",item.getGroupID());
		 * intent.putExtra("name",item.getGroupName());
		 * intent.putExtra("type",item.getGroupType());
		 * intent.putExtra("membersCount",item.getMembersCount());
		 * 
		 * intent.putExtra("name",item.getMembershipstatus());
		 */
		outState.putString("GID", id);
		outState.putString("GN", churchGroupName);
		outState.putString("logo", logo);
		outState.putString("type", type);
		outState.putString("MC", members);
		outState.putString("status", status);
		outState.putString("Clogo", Clogo);
		outState.putString("churchname", churchname);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_church_groups_wall);
		//setupfloatingMenu1();
		
		Connector.context= this;
		Connector.myPrefs= getSharedPreferences(Connector.prefernceName, Context.MODE_PRIVATE);
		imageLoader = com.appsol.volley.classes.ImageCacheManager.getInstance()
				.getImageLoader();
		churchgroup= new ChurchGroup();
		churchGroupName = getIntent().getStringExtra("name");
		id = getIntent().getStringExtra("ID");
		status = getIntent().getStringExtra("status");
		churchname = getIntent().getStringExtra("churchname");
		Clogo =  getIntent().getStringExtra("Clogo");
		logo = getIntent().getStringExtra("logo");
		type = getIntent().getStringExtra("type");
		members = getIntent().getStringExtra("membersCount");
		if (savedInstanceState != null) {
			churchGroupName = savedInstanceState.getString("GN");
			id = savedInstanceState.getString("GID");
			status = savedInstanceState.getString("status");
			Clogo = savedInstanceState.getString("Clogo");
			logo = savedInstanceState.getString("logo");
			type = savedInstanceState.getString("type");
			members = savedInstanceState.getString("MC");
			churchname=savedInstanceState.getString("churchname");
		} else {
			churchGroupName = getIntent().getStringExtra("name");
			id = getIntent().getStringExtra("ID");
			status = getIntent().getStringExtra("status");
			Clogo =  getIntent().getStringExtra("Clogo");
			logo = getIntent().getStringExtra("logo");
			type = getIntent().getStringExtra("type");
			churchname = getIntent().getStringExtra("churchname");
			members = getIntent().getStringExtra("membersCount");
		}
		 
		churchgroup.setGroupID(id);
		churchgroup.setGroupType(type);
		churchgroup.setMembersCount(members);
		btngroupmember = (Button) findViewById(R.id.btngroupmember);
	 btntoggle_status = (Button) findViewById(R.id.btntoggle_status);
		TextView txtgroupname = (TextView) findViewById(R.id.txtgroupname),txtchurchname =(TextView)  findViewById(R.id.txtchurchname);
		txtgroupname.setText(churchGroupName);
		txtchurchname.setText(""+churchname);
		final String membersString = " Members(" + members + ")";
		btngroupmember.setText(membersString);
		setTitle(churchGroupName);
		// Set logo
		CircularNetworkImageView profilePic = (CircularNetworkImageView) findViewById(R.id.churchlogo);
		String grouplogo = logo;

		String url = "";

		if (TextUtils.isEmpty(logo.trim())) {
			url = Connector.ChurchLogoURL + Clogo;
		} else {
			url = Connector.churchGrouplogo + logo;
		}
		profilePic.setImageUrl(null, imageLoader);
		// holder.
		// profilePic.setImageBitmap(BitmapFactory.decodeResource(activity.getResources(),
		// R.drawable.mobile));
		if (!url.equalsIgnoreCase(Connector.imageURL)
				&& !url.equalsIgnoreCase(Connector.churchGrouplogo)
				&& !url.equalsIgnoreCase(Connector.ChurchLogoURL)) {
			if (imageLoader == null)
				imageLoader = com.appsol.volley.classes.ImageCacheManager
						.getInstance().getImageLoader();
			profilePic.setImageUrl(url, imageLoader);
		}

		else
			profilePic.setImageBitmap(BitmapFactory.decodeResource(
					getResources(), R.drawable.mobile));
		ToggleButtonState(btntoggle_status,status);
		btngroupmember.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					//Integer mCount = Integer.parseInt(members);
					if (!	churchgroup.getMembersCount().trim().equalsIgnoreCase("0")) {
						Intent intent = new Intent(ChurchGroupsActivity1.this,
								NsoreGroupMembers.class);
						intent.putExtra("GID", id);
						intent.putExtra("name", churchGroupName);
						intent.putExtra("type", type);
						intent.putExtra("membersCount", members);

						// intent.putExtra("name",item.getMembershipstatus());
						startActivity(intent);
					}
					else
					{
						Toast.makeText(ChurchGroupsActivity1.this,churchgroup.getMembersCount(), Toast.LENGTH_LONG).show();
					}

				} catch (Exception e) {
					Toast.makeText(ChurchGroupsActivity1.this,e.getMessage(), Toast.LENGTH_LONG).show();
				}

			}
		});
		
		btntoggle_status.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(churchgroup.getOperationReason().equalsIgnoreCase("Leave Group") || churchgroup.getOperationReason().equalsIgnoreCase("Cancel Request"))
				{
					AlertDialog.Builder b= new Builder(ChurchGroupsActivity1.this);
					b.setMessage(churchgroup.getOperationReason())
					
				
					.setNegativeButton("No",new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
						
							
						}
					})
					.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							 handleServerRequest(churchgroup,btntoggle_status);
						}
					})
					.show();
				}
				else
				{
					 handleServerRequest(churchgroup,btntoggle_status);
				}
			}
		});
		
        Connector.context=this;
      
       
       progressbarLoad=(ProgressBar)findViewById(R.id.progressbarLoad);
//		 if(progressbarLoad!=null)
//		 {
//			 progressbarLoad.setProgress(0);
//		 }
//       
        URL_FEED ="https://nsoredevotional.com/mobile/devotionHandler.php";
        listView = (ListView) findViewById(R.id.list);
 
        feedItems = new ArrayList<ChurchGroupFeeds >();
 
        listAdapter = new GroupDataFeedListAdapter(this, feedItems);
        listView.setAdapter(listAdapter);
        listView.setOnScrollListener(new EndlessScrollListener());
        	 StringRequest jsonReq = new  StringRequest(Method.POST,
                    URL_FEED, new Response.Listener<String>() {
 
                        private String TAG="ERROR";

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
//	 		            params.put("reason", "Members Testimonies");
	 		        params.put("GID", id);
	 		         params.put("UID", Connector.getUserId());
		 		     params.put("reason", "Load Group Feeds");
	 		           
	 		            return params;
	 		        }}
        	 ;
            InputStream keyStore = getResources().openRawResource(R.raw.appsol);
            
            
            RequestQueue queue = Volley.newRequestQueue(this,
                    new ExtHttpClientStack(new SslHttpClient(keyStore, "qwerty")));
            // Adding request to volley request queue
            queue.add(jsonReq);
       // }	
		
		
		
	
	}
	void changemembersCount()
	{
		 String membersString = " Members(" + churchgroup.getMembersCount() + ")";
			btngroupmember.setText(membersString);
	}
	
	void ToggleButtonState(Button btnConnect,String Mstatus)
	{
		churchgroup.setMembershipstatus(Mstatus);
		if(Mstatus.equalsIgnoreCase("accepted"))
		  {
			btnConnect.setText("Leave");
			  Drawable img = getResources().getDrawable( R.drawable.ic_nsore_heart);
			  btnConnect.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
			  setupfloatingMenu1();
			  churchgroup.setOperationReason("Leave Group");
		  }
		  else if(Mstatus.equalsIgnoreCase("NA") || TextUtils.isEmpty(Mstatus))
		  {
			 btnConnect.setText("Join");
			    Drawable img = getResources().getDrawable( R.drawable.ic_nsore_add );
				btnConnect.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
				 
				churchgroup.setOperationReason("Join Group");
		  }
		  else if(Mstatus.equalsIgnoreCase("pending"))
		  {
			  btnConnect.setText("Cancel Request");
			  Drawable img =getResources().getDrawable( R.drawable.ic_nsore_cancel );
			 btnConnect.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
			 churchgroup.setOperationReason("Cancel Group Request");
		  }
		 
		  else
		  {
			 btnConnect.setText("Join"); 
			 Drawable img =getResources().getDrawable( R.drawable.ic_nsore_cancel );
			 btnConnect.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
			 churchgroup.setOperationReason("Join Group");
		  }
		  
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.church_groups, menu);
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

	///Toggle membership
	
	
	
private void handleServerRequest(final ChurchGroup user, Button btntoggle_status) {
		
		String URL_FEED = "https://nsoredevotional.com/mobile/devotionHandler.php";
		StringRequest jsonReq = new StringRequest(Method.POST, URL_FEED,
				createMyReqSuccessListener(user,btntoggle_status), createMyReqErrorListener()) {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				params.put("CID", Connector.getChurchID());
				params.put("GID", user.getGroupID());
				params.put("GS", user.getGroupType());
				params.put("UID", Connector.getUserId());
				params.put("reason", user.getOperationReason());
				params.put("branch", Connector.getBranch());
				return params;
			}
		};
		InputStream keyStore = getResources().openRawResource(R.raw.appsol);

		RequestQueue queue = Volley.newRequestQueue(this,
				new ExtHttpClientStack(new SslHttpClient(keyStore, "qwerty")));
		// Adding request to volley request queue
		queue.add(jsonReq);
	}

private Response.Listener<String> createMyReqSuccessListener(final ChurchGroup user, final Button btntoggle_status) {
	return new Response.Listener<String>() {

		@Override
		public void onResponse(String response) {
			
			try{
				JSONObject object = new JSONObject(response);
				if(object!=null)
				{
					//{"response":"saved","count":"4"}
				  String 	results=  object.optString("response","Err");
				  String count= object.optString("count", "0");
			
			if (!results.equalsIgnoreCase("No Testimony Shared.")) {
				//user.setServerResponse(response);
				//Toast.makeText(Connector.context, response, Toast.LENGTH_LONG).show();
				if(results.trim().contains("Waiting"))
				{
					  //holder.btnConnect.setText("Send Request");
					
					    user.setOperationReason("Cancel Request");
					    user.setMembershipstatus("pending");
					   
				}
				else if(results.trim().contains("saved") || results.trim().contains("Already a member"))
				{
					  //holder.btnConnect.setText("Send Request");
					    user.setOperationReason("Already Member");
					    user.setMembershipstatus("accepted");
					    user.setMembersCount((Integer.parseInt(user.getMembersCount())+1)+"");
					 
				} 
				
				else if(results.trim().contains("Request Sent"))
				{
					  user.setOperationReason("Cancel Request");
					  user.setMembershipstatus("pending");
					  
				}
				else if(results.trim().contains("Left"))
				{
					  user.setOperationReason("Cancel Request");
					  user.setMembershipstatus("NA");
					   
				}
				else 
				{
					if(user.getMembersCount().equalsIgnoreCase("0"))
					{
						user.setMembersCount("0");
					}
					else
					{
						// user.setMembersCount((Integer.parseInt(user.getMembersCount())-1)+"");
					}
					 user.setMembershipstatus("NA");
					  
				}
				ToggleButtonState(btntoggle_status, user.getMembershipstatus());
			}
			user.setMembersCount(count);
			changemembersCount();
			
				}
				
			}
				catch (Exception e) {
					// TODO: handle exception
				}
		}
	};
}

private Response.ErrorListener createMyReqErrorListener() {
	return new Response.ErrorListener() {
		@Override
		public void onErrorResponse(VolleyError error) {
			//progressbarLoad.setVisibility(View.GONE);
			if(error!=null)
			{
			showErrorDialog(Connector.HandleVolleyerror(error, ChurchGroupsActivity1.this));
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
	//mInError = true;

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
			handleServerRequest(churchgroup, btntoggle_status);
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
Toast.makeText(this, response, Toast.LENGTH_LONG).show();
	progressbarLoad.setVisibility(View.GONE);
	if (!TextUtils.isEmpty(response)&&!response
			.contains("No Group Feed Found")) {

		JSONObject objects;

		
    try {

		objects = new JSONObject(response);
		if (objects != null) {
			JSONArray feedArray  = objects.getJSONArray("GroupFeed");

			if (feedArray  != null) {
    
        //JSONArray feedArray = response.getJSONArray("feed");

        for (int i = 0; i < feedArray.length(); i++) {
            JSONObject feedObj = (JSONObject) feedArray.get(i);

           ChurchGroupFeeds  item = new  ChurchGroupFeeds();
           String userData= feedObj.optString("user");
           if(!TextUtils.isEmpty(userData))
           {
        	   item =  processUsersListJson(userData, item); 
           }
          //Process the UserJsonObject
           
           
           
           
           //
           
           
           item.setAttachment(feedObj.getString("attachment"));
           item.setFeedID(feedObj.getString("feed_id"));
           item.setGroupID(feedObj.getString("group_id"));
           item.setTitle(feedObj.getString("title"));
           item.setDescription(feedObj.getString("description"));
           item.setStatus(feedObj.getString("status"));
           item.setDate_sent(feedObj.getString("date_sent"));
           item.setLikeFeed(feedObj.getString("userliike"));
           item.setLikes(feedObj.getString("likes"));
          item.setComment(feedObj.getString("comments"));
           
          
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


public static  ChurchGroupFeeds  processUsersListJson(String response, ChurchGroupFeeds item)
{

	  try {
		  
		  JSONObject objects = new JSONObject(response.trim());
			if (objects != null) {
			
      
          //JSONArray feedArray = response.getJSONArray("feed");


//justClicked=199;
         
              JSONObject feedObj = objects;

         item = new   ChurchGroupFeeds();
            /*
             name memberID comment_id timeStamp comment profilePic
             * 
             */
           
            item.setName(feedObj.getString("name"));
            item.setMemberID(feedObj.getString("memberID"));
         
              item.setEmail(feedObj.getString("email"));
              item.setPhone_number(feedObj.getString("phone"));
            //item.setCurrent(false);
              Log.e("IDS", feedObj.getString("profilePic"));
			  item.setProfilePic(feedObj.getString("profilePic"));
			  item.setIsConnected(feedObj.getInt("isConnected")+"");
			  item.setFriendsCount(feedObj.getString("friends")+"");
			  item.setMutualFriends(feedObj.optString("mutualFriends",feedObj.optInt("mutualFriends", 0)+""));
			  item.setMemberSince(feedObj.getString("date_joined"));
			  item.setUserCover(feedObj.getString("cover"));
			  item.setChurchname(feedObj.getString("churchName"));
              item.setChurchID(feedObj.getString("CID"));
              item.setAboutUser(feedObj.getString("about-user"));
				
               
           //startAT = 
            
           return  item;
      
				
			}
      } catch (JSONException e) {
          e.printStackTrace();
          return null;
      }
	  return item;
}

Context getActivity()
{
	return this;
}
void setupfloatingMenu1()
{
	ImageView icon = new ImageView(getActivity()); // Create an icon
	icon.setImageResource( R.drawable.ic_nsore_float_edit);

	  FloatingActionButton centerActionButton = new FloatingActionButton.Builder(getActivity())
      .setTheme(FloatingActionButton.THEME_LIGHT)
      .setContentView(icon)
      .setPosition(FloatingActionButton.POSITION_BOTTOM_RIGHT)
      .build();
	
	SubActionButton.Builder itemBuilder = new SubActionButton.Builder(getActivity());
	ImageView itemIcon3 = new ImageView(getActivity());
	itemIcon3.setImageResource(R.drawable.ic_nsore_notes) ;
	SubActionButton btnnote = itemBuilder.setContentView(itemIcon3).build();
	btnnote.setOnClickListener( new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			handleFloatButtons(v, 1);
		}
	});
	// repeat many times:
	ImageView itemIcon = new ImageView(getActivity());
	itemIcon.setImageResource(R.drawable.ic_nsore_picture) ;
	SubActionButton btncomment = itemBuilder.setContentView(itemIcon).build();
	//handleFloatButtons(btncomment, 2);
	btncomment.setOnClickListener( new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			handleFloatButtons(v, 2);
		}
	});
	
	
	
	ImageView itemIcon4= new ImageView(getActivity());
	itemIcon4.setImageResource(R.drawable.ic_nsore_help) ;
	SubActionButton btnSecrete = itemBuilder.setContentView(itemIcon4).build();
	btnSecrete.setOnClickListener( new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			handleFloatButtons(v, 3);
		}
	});
	//btncomment.setOnClickListener(l)
	
	FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(getActivity())
//	.setStartAngle(0) // A whole circle!
//    .setEndAngle(180)
.setRadius(getResources().getDimensionPixelSize(R.dimen.radius_small))
	   .addSubActionView(btnnote)
  .addSubActionView(btncomment)
 
     //.addSubActionView(btnshare)
        .addSubActionView(btnSecrete)
    .attachTo(centerActionButton)
    .build();
	
	
}


public void handleFloatButtons(View v, Integer value)
{
	switch (value) {
	case 1:
		Intent intent= new Intent(getActivity(), PosttoNsoreGroup.class);
		intent.putExtra("id",id);
		intent.putExtra("GN", churchGroupName);
		intent.putExtra("intent", "POST-TEXT");
		startActivity(intent);
		break;
    case 2:
		
		break;
    case 3:
	
	break;
    case 4:
	
	break;
	default:
		break;
	}
}


PopupWindow popWindow;
@SuppressLint("NewApi") 
private  void onShowPopup(){
	
	//ImageLoader imageLoader = com.appsol.volley.classes.ImageCacheManager.getInstance().getImageLoader();
	
LayoutInflater layoutInflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
// inflate the custom popup layout
final View inflatedView = layoutInflater.inflate(R.layout.fragment_postto_nsore_group, null,false);
final EditText body= (EditText) inflatedView.findViewById(R.id.body);

//body.setText(prayer);
///

		
Button btnconnect =(Button) inflatedView.findViewById(R.id.btnsavenote);
Button btnCancel=(Button) inflatedView.findViewById(R.id.btnCancel);


btnconnect.setOnClickListener( new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		
		// TODO Auto-generated method stub
		String URL_FEED = "https://nsoredevotional.com/mobile/devotionHandler.php";
		StringRequest jsonReq = new StringRequest(Method.POST, URL_FEED,
				createMyReqSuccessListeners(), createMyReqErrorListener()) {
			

			@Override
			protected Map<String, String> getParams() {
				 String spinsVal;
				
				Map<String, String> params = new HashMap<String, String>();
			
				 if(id!=null)
				params.put("GID", id);
				params.put("MID",Connector.getUserId());
				params.put("UID", Connector.getUserId());
				params.put("request", body.getText().toString());
				
				params.put("reason","Save Group Feed");
				
				return params;
			}
		};
		InputStream keyStore = Connector.context.getResources().openRawResource(R.raw.appsol);

		if(!TextUtils.isEmpty( body.getText().toString().trim()) )
		{
			RequestQueue queue = Volley.newRequestQueue(Connector.context,
					new ExtHttpClientStack(new SslHttpClient(keyStore, "qwerty")));
			// Adding request to volley request queue
			queue.add(jsonReq);	
		}
		
		
	}

	private Listener<String> createMyReqSuccessListeners() {
		// TODO Auto-generated method stub
		  return new Response.Listener<String>() {
	        	
	        	
	        	
	        	
	        	
	            @Override
	            public void onResponse(String response) {
	            	Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
	            	if(response.trim().equalsIgnoreCase("Saved"))
	            	{
	            		if(popWindow!=null)
	            		{
	            			popWindow.dismiss();
	            			//loadpage();
	            		}
	            	}
	            	
	            }
	        };
		
	}
});
// find the ListView in the popup layout
int mDeviceHeight;
 
// get device size
Display display = ((Activity) getActivity()).getWindowManager().getDefaultDisplay();
final Point size = new Point();
display.getSize(size);
mDeviceHeight = size.y;	    
// set height depends on the device size
popWindow = new PopupWindow(inflatedView, size.x - 50,size.y - 300, true );

//Views



// set a background drawable with rounders corners
popWindow.setBackgroundDrawable(Connector.context.getResources().getDrawable(R.drawable.flatlayout));
// make it focusable to show the keyboard to enter in `EditText`
popWindow.setFocusable(true);
// make it outside touchable to dismiss the popup window
popWindow.setOutsideTouchable(true);
 
// show the popup at bottom of the screen and set some margin at bottom ie,
popWindow.showAtLocation(inflatedView, Gravity.TOP, 0,150);

	
	btnCancel.setOnClickListener( new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			popWindow.dismiss();
		}
	});
}








}
