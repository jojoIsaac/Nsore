package com.appsol.apps.devotional;

import static com.appsol.apps.projectcommunicate.classes.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.appsol.apps.projectcommunicate.classes.CommonUtilities.EXTRA_MESSAGE;
import static com.appsol.apps.projectcommunicate.classes.CommonUtilities.EXTRA_MESSAGE_TYPE;
import static com.appsol.apps.projectcommunicate.classes.CommonUtilities.SENDER_ID;

import hotchemi.android.rate.AppRate;
import hotchemi.android.rate.OnClickButtonListener;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appsol.apps.projectcommunicate.classes.ARater;
import com.appsol.apps.projectcommunicate.classes.Connector;
import com.appsol.apps.projectcommunicate.classes.CropSquareTransformation;
import com.appsol.apps.projectcommunicate.classes.Devotion;
import com.appsol.apps.projectcommunicate.classes.ServerUtilities;
import com.appsol.apps.projectcommunicate.classes.WakeLocker;
import com.appsol.apps.projectcommunicate.model.ChurchGroup;
import com.appsol.apps.projectcommunicate.model.DBHelper;
import com.appsol.apps.projectcommunicate.model.Notes;
import com.appsol.apps.projectcommunicate.model.NsoreDevotionalUser;
import com.appsol.apps.projectcommunicate.model.SubacribedEvents;
import com.appsol.login.Entry;
import com.google.android.gcm.GCMRegistrar;
import com.squareup.picasso.Picasso;
//
//import org.acra.ACRA;
//import org.acra.annotation.ReportsCrashes;
//Mint.initAndStartSession(MyActivity.this, "b56c60dd");
//@ReportsCrashes(formKey = "testuser", formUri = "https://nsoredevotional.com/mobile/AppReporter/submit.php"
//		)
public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,OnFragmentInteractionListener {
	public static Fragment fragment; 
    public static boolean fromMain=false;

	public static Bitmap churchLogo;
	private String filePath;
	private OnFragmentInteractionListener mcaller;
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		Connector.setdevotionDate("RESET");
		Connector.setDevotion("NOT_SET");
		super.finish();
	}
	//Mint.initAndStartSession(MyActivity.this, "b56c60dd");
	@Override
	protected void onResume() {
	  super.onResume();
	  try
	  {
	  boolean isLoggedin = Connector.getisLOggedIn();
		String accountID = Connector.getUserId();
		// Toast.makeText(this, accountID, Toast.LENGTH_LONG).show();
		if (isLoggedin && !accountID.trim().equalsIgnoreCase("0")) {

			
		} 
		else
		{
			Intent ints = new Intent(this, Entry.class);
			startActivity(ints);
			finish();
		}
	  } catch(Exception e)
	  {
		  
	  }
	  // Logs 'install' and 'app activate' App Events.
	  //AppEventsLogger.activateApp(this);
	  //AppEventsLogger.
	}
	
	
	
	@Override
	protected void onPause() {
	  super.onPause();

	  // Logs 'app deactivate' App Event.
	 // AppEventsLogger.deactivateApp(this);
	}
	
	
	/**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
	private String dpname;
	

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    public static CharSequence mTitle;

    //Few House keepings
    private void processNotifications(String newMessage,String type,Context context,Intent intent, int i)
    {
    	//Toast.makeText(Connector.context, i+"", Toast.LENGTH_LONG).show();
    	 if(type!=null)
         {
            if(type.equalsIgnoreCase("LESSON"))
            {
         	   Toast.makeText(getApplicationContext(), "New Devotion: " + newMessage+"-"+type, Toast.LENGTH_LONG).show();
                
            }
            else if(type.equalsIgnoreCase("ALERT"))
            {
            	NavigationDrawerFragment.adapter1.getItem(5).setCount("1");
				NavigationDrawerFragment.adapter1.getItem(5).setCounterVisibility(true);
				NavigationDrawerFragment.adapter1.notifyDataSetChanged();
         	   showNotification(newMessage, Connector.context);
            }
            
            else if(type.equalsIgnoreCase("TESTIMONY"))
            {
         	   processTestimony(newMessage);
            }
            
            else if(type.equalsIgnoreCase("TESTIMONY LIKE"))
            {
         	   processTestimonyLike(newMessage,context);
            }
            
            else if(type.equalsIgnoreCase("TESTIMONY Comment"))
            {
         	   
            }
            else if(type.equalsIgnoreCase("EVENT ALERT")){
         	   processEventAlert(newMessage,context);
            }
            else if(type.equalsIgnoreCase("Friend Request")){
          	   processFriendRequest(newMessage,context,i);
             }
            else if(type.equalsIgnoreCase("Accept Request"))
            {
            	processNewFriend(newMessage,context,i);
            }
            else if(type.equalsIgnoreCase("Devotion Comment"))
            {
            	processDevotionComments(newMessage,i);
            }
            
            else if(type.equalsIgnoreCase("Group Message Post"))
            {
            	processGroupPostAlert(newMessage,i);
            }
            else if(type.equalsIgnoreCase("Group Feed Comment"))
            {
            	processGroupPostCommentAlert(newMessage,i);
            }
            
           
            else if(type.equalsIgnoreCase("User Church Changed"))
            {
         	   //newMessage
         	   String alert="";
            	try {
        			JSONObject alertobject= new JSONObject(newMessage);
        			alert= alertobject.optString("other-param");
        		} catch (JSONException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}
         	   //String churchid= intent.getExtras().getString("other-param");
         	   handleChurchChange(newMessage,context,alert);
            }
            else if(type.equalsIgnoreCase("User Profile Picture Changed"))
            {
         	   //newMessage
         	   String dpname="";
         	  try {
      			JSONObject alertobject= new JSONObject(newMessage);
      			dpname= alertobject.optString("other-param");
      		} catch (JSONException e) {
      			// TODO Auto-generated catch block
      			e.printStackTrace();
      		}
         	   handleDpChange(newMessage,context,dpname); 
            }
            
         }
         else
         {
         	Log.e("MESAAGE_TYPE", "The Gcm type has not been set");
         }
    }
    
    
    
    private void processGroupPostAlert(String newMessage,Integer i) {
		// TODO Auto-generated method stub
    	if(i<2)
    	{
    		 String dpname="";
        	  try {
     			JSONObject alertobject= new JSONObject(newMessage),feedObj;
     			dpname= alertobject.optString("devotion_details");
     			feedObj= new JSONObject(dpname);
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
     			
// Start the Church Group Activity
				  Intent intent = new Intent(Connector.context, ChurchGroupsActivity.class);
					intent.putExtra("ID",item.getGroupID());
					intent.putExtra("name",item.getGroupName());
					intent.putExtra("type",item.getGroupType());
					intent.putExtra("membersCount",item.getMembersCount());
					intent.putExtra("logo", item.getGrouplogo());
					intent.putExtra("status",item.getMembershipstatus());
					intent.putExtra("Clogo", item.getchurchlogo());
					intent.putExtra("Json", item.getJson());
					intent.putExtra("churchname", "@"+item.getchurchname());
					Connector.context.startActivity(intent);
     			 NotificationManager mNotificationManager =
    			         (NotificationManager) Connector.context.getSystemService(Context.NOTIFICATION_SERVICE);
    			 mNotificationManager.cancel(0);
     			//Toast.makeText(Connector.context, dpname, Toast.LENGTH_LONG).show();
     		} catch (JSONException e) {
     			// TODO Auto-generated catch block
     			e.printStackTrace();
     		}
    		
    	}
    	
		
	}
    
   
    
    
    private void processNewFriend(String newMessage,Context context,Integer i){
    	//Toast.makeText(context, i+"",Toast.LENGTH_LONG).show();
    	if(i<2)
    	{
    	
    		try
        	{
        		NsoreDevotionalUser  item = new  NsoreDevotionalUser();
                /*
                 name memberID comment_id timeStamp comment profilePic
                 * 
                 */
        		JSONObject alertobject= new JSONObject(newMessage),feedObj;
     			dpname= alertobject.optString("User_details");
     			feedObj= new JSONObject(dpname);
                item.setName(feedObj.getString("name"));
                item.setMemberID(feedObj.getString("memberID"));
             
                item.setEmail(feedObj.getString("email"));
                item.setPhone_number(feedObj.getString("phone"));
                //item.setCurrent(false);
    			item.setProfilePic(feedObj.getString("profilePic"));
    			item.setIsConnected("1");
    			item.setFriendsCount(feedObj.getString("friends")+"");
    			item.setMutualFriends(feedObj.optString("mutualFriends",feedObj.optInt("mutualFriends", 0)+""));
    			item.setMemberSince(feedObj.getString("date_joined"));
    			item.setUserCover(feedObj.getString("cover"));
    			item.setChurchname(feedObj.getString("churchName"));
                item.setChurchID(feedObj.getString("CID"));
                item.setNewFiend(true);
                Connector.handleUserProfile(item, context, false);
                NotificationManager mNotificationManager =
    			         (NotificationManager) Connector.context.getSystemService(Context.NOTIFICATION_SERVICE);
    			 mNotificationManager.cancel(0);
        	}
        	catch(Exception e)
        	{
        		
        	}
    	}
    	
    }
    
    
    
    
    
    
    private void processFriendRequest(String newMessage,Context context,Integer i){
    	//Toast.makeText(context, i+"",Toast.LENGTH_LONG).show();
    	if(i<2)
    	{
    	
    		try
        	{
        		NsoreDevotionalUser  item = new  NsoreDevotionalUser();
                /*
                 name memberID comment_id timeStamp comment profilePic
                 * 
                 */
        		JSONObject alertobject= new JSONObject(newMessage),feedObj;
     			dpname= alertobject.optString("User_details");
     			feedObj= new JSONObject(dpname);
                item.setName(feedObj.getString("name"));
                item.setMemberID(feedObj.getString("memberID"));
             
                item.setEmail(feedObj.getString("email"));
                item.setPhone_number(feedObj.getString("phone"));
                //item.setCurrent(false);
    			item.setProfilePic(feedObj.getString("profilePic"));
    			item.setIsConnected(feedObj.getInt("isConnected")+"");
    			item.setFriendsCount(feedObj.getString("friends")+"");
    			item.setMutualFriends(feedObj.optString("mutualFriends",feedObj.optInt("mutualFriends", 0)+""));
    			item.setMemberSince(feedObj.getString("date_joined"));
    			item.setUserCover(feedObj.getString("cover"));
    			item.setChurchname(feedObj.getString("churchName"));
                item.setChurchID(feedObj.getString("CID"));
                Connector.handleUserProfile(item, context, false);
                NotificationManager mNotificationManager =
    			         (NotificationManager) Connector.context.getSystemService(Context.NOTIFICATION_SERVICE);
    			 mNotificationManager.cancel(0);
        	}
        	catch(Exception e)
        	{
        		
        	}
    	}
    	
    }
    private void processDevotionComments(String newMessage,Integer i) {
		// TODO Auto-generated method stub
    	if(i<2)
    	{
    		 String dpname="";
        	  try {
     			JSONObject alertobject= new JSONObject(newMessage),data;
     			dpname= alertobject.optString("devotion_details");
     			data= new JSONObject(dpname);
     			/*
     			 * 
     			 * $dataItem['rawDate']=$row['rawDate'];
					  $dataItem['title']=trim($row['T']);
					   $dataItem['day_name']=$row['DW'];
					   $dataItem['loadeddate']=$row['DD'];
					    $dataItem['LID']=$row['LID'];
     			 * 
     			 */
     			Devotion devotion = new Devotion();
     			devotion.setLesseonid(data.getString("LID"));
     			devotion.setNormalDevotionDate(data.getString("loadeddate"));
     			devotion.setDevotionday(data.getString("day_name"));
     			devotion.setTitle(data.getString("title"));
     			Connector.handleComment(devotion);
     			 NotificationManager mNotificationManager =
    			         (NotificationManager) Connector.context.getSystemService(Context.NOTIFICATION_SERVICE);
    			 mNotificationManager.cancel(0);
     			//Toast.makeText(Connector.context, dpname, Toast.LENGTH_LONG).show();
     		} catch (JSONException e) {
     			// TODO Auto-generated catch block
     			e.printStackTrace();
     		}
    		
    	}
    	
		
	}
	public static void showNotification(String message,Context context)
    {
    	
    	AlertDialog.Builder build= new Builder(context);
    	
    	//build.set
    	
    	
    	
    	 dialog= new Dialog(Connector.context);
    	LayoutInflater layinflate=((Activity) context).getLayoutInflater();
    	View rootView= layinflate.inflate(R.layout.fragment_announcement_details,null);
    	
    	dialog.setTitle("Announcement");
    
    	dialog.setCancelable(true);
    	TextView txtAnnouncement =(TextView) rootView.findViewById(R.id.txtdetails);
    	TextView title=(TextView) rootView.findViewById(R.id.txtTitle),txtDateadded=(TextView)rootView.findViewById(R.id.txtDateadded);
    	txtDateadded.setVisibility(View.GONE);
    	txtAnnouncement.setText(message);
    	//Button btnClose= (Button) rootView.findViewById(R.id.btnClose);
    	//ImageButton imgprofile=(ImageButton) rootView.findViewById(R.id.imgprofile);
    	//imgprofile.setVisibility(View.GONE);
    	//btnClose.setVisibility(View.GONE);
    	
    	try{
    		JSONObject content = new JSONObject(message);
    		if(content!=null)
    		{
    			String titlem= content.optString("title", "Church Announcement");
    			String messages= content.optString("message", "God Love's us all. Stay blessed");
    			txtAnnouncement.setText(messages);
    			title.setText(titlem);
    		}
    	}
    	catch(Exception e)
    	{
    		
    	}
    	
    	
    	dialog.setContentView(rootView);
    		dialog.setOnDismissListener( new OnDismissListener() {
    			
    			@Override
    			public void onDismiss(DialogInterface arg0) {
    				// TODO Auto-generated method stub
    				 NotificationManager mNotificationManager =
    		         (NotificationManager) Connector.context.getSystemService(Context.NOTIFICATION_SERVICE);
    		          mNotificationManager.cancel(0);
//    		          NavigationDrawerFragment.adapter1.getItem(5).setCount("");
//    					NavigationDrawerFragment.adapter1.getItem(5).setCounterVisibility(false);
//    					NavigationDrawerFragment.adapter1.notifyDataSetChanged();
    			
    			}
    		});
    	 dialog.show();
//    	 NotificationManager mNotificationManager =
//    	         (NotificationManager) Connector.context.getSystemService(Context.NOTIFICATION_SERVICE);
//    	 mNotificationManager.cancel(0);
    }

    
    @Override
    protected void onDestroy() {
        if (mRegisterTask != null) {
            mRegisterTask.cancel(true);
        }
        try {
            unregisterReceiver(mHandleMessageReceiver);
            GCMRegistrar.onDestroy(this);
        } catch (Exception e) {
            Log.e("UnRegister Receiver Error", "> " + e.getMessage());
        }
        fragment=null;
        super.onDestroy();
    }
    private final BroadcastReceiver mHandleMessageReceiver
     = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
            String type=intent.getExtras().getString(EXTRA_MESSAGE_TYPE);
            // Waking up mobile if it is sleeping
            WakeLocker.acquire(getApplicationContext());
             
            /**
             * Take appropriate action on this message
             * depending upon your app requirement
             * For now i am just displaying it on the screen
             * */
             
            // Showing received message
           processNotifications(newMessage, type, context, intent,2);
          
            // Releasing wake lock
            WakeLocker.release();
        }

    	
    };
    private ImageView imgdp;
	private AsyncTask<Void, Void, Void> mRegisterTask;
	//private DisplayImageOptions options;
	private Boolean isLoggedin;

	
	public static void checkActiveEvent()
	{
		List<SubacribedEvents> e= new ArrayList<SubacribedEvents>();
		Connector.dbhelper = new DBHelper(Connector.context);
		//Connector.openDB(getActivity());
		e= SubacribedEvents.getNotes(Connector.dbhelper);
		Connector.dbhelper.close();
		
		int size=e.size();
		//Toast.makeText(Connector.context, size+"", Toast.LENGTH_LONG).show();
		if(size>0)
		{
		NavigationDrawerFragment.adapter1.getItem(4).setCount(""+size);
		NavigationDrawerFragment.adapter1.getItem(4).setCounterVisibility(true);
		NavigationDrawerFragment.adapter1.notifyDataSetChanged();
		}
	}
	void AppRater()
	{
		 AppRate.with(this)
	     .setInstallDays(0) // default 10, 0 means install day.
	     .setLaunchTimes(3) // default 10
	     .setRemindInterval(2) // default 1
	     .setShowNeutralButton(true) // default true
	     .setDebug(false) // default false
	     .setOnClickButtonListener(new OnClickButtonListener() { // callback listener.
	         @Override
	         public void onClickButton(int which) {
	             Log.d(MainActivity.class.getName(), Integer.toString(which));
	         }
	     })
	     .monitor();

	 // Show a dialog if meets conditions
	 AppRate.showRateDialogIfMeetsConditions(this);
	}
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2980b9")));
//        getActionBar().setIcon(
//                   new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        Connector.myPrefs= getSharedPreferences(Connector.prefernceName, Context.MODE_PRIVATE);
        Connector.context=this;
       // ARater.registerEvent(this);
        
        
        
        //ACRA.init(getApplication());
        isLoggedin= Connector.getisLOggedIn();
		String accountID= Connector.getUserId();
        
        if(!isLoggedin || accountID.equalsIgnoreCase("0"))
		{
        	 NotificationManager mNotificationManager =
    		         (NotificationManager) Connector.context.getSystemService(Context.NOTIFICATION_SERVICE);
    		          mNotificationManager.cancel(0);
        	Intent ints= new Intent(this,Entry.class);
			startActivity(ints);
			finish();
		}
        
        
        setContentView(R.layout.activity_main);
        AppRater();
        checkActiveEvent();
//Toast.makeText(this, n, Toast.LENGTH_LONG).show();
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        
        filePath= Environment.getExternalStorageDirectory().getPath();
		String imagename="";
        //Settup the application folder
        File files = new File(filePath+ "/"+Connector.AppFolder+"/userDP/");
    	
		if(!files.exists())
		{
			
			files.mkdirs();
			//Log.d("D",files.getAbsolutePath());
		}
		
		//Perform the GCM setting up
		GCMProcessing();
		 String message= getIntent().getStringExtra("MESSAGE");
		 String type= getIntent().getStringExtra("TYPE");

			 try
			 {
				 processNotifications(message, type, Connector.context, getIntent(),0);
			 }
			 catch(Exception e)
			 {
				 
			 }

			 
		GCMStuffs();
//		options = new DisplayImageOptions.Builder()
//		.showImageOnLoading(R.drawable.usericos)
//		.showImageForEmptyUri(R.drawable.ic_empty)
//		.showImageOnFail(R.drawable.ic_error).cacheInMemory(true)
//		.cacheOnDisk(true).considerExifParams(true)
//		.displayer(new RoundedBitmapDisplayer(6)).build();
		if (savedInstanceState == null) {
			fragment= new AllDevotions();
           getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
		//changeFragment(1,0);
		}
    }

    //The GCM Method
    void GCMProcessing()
    {
    	//GCMRegistrar.unregister(this);
    // Make sure the device has the proper dependencies.
    GCMRegistrar.checkDevice(this);

    // Make sure the manifest was properly set - comment out this line
    // while developing the app, then uncomment it when it's ready.
    GCMRegistrar.checkManifest(this);
    //registerReceiver(mHandleMessageReceiver, new IntentFilter(
//        DISPLAY_MESSAGE_ACTION));

    // Get GCM registration id

    final String regId = GCMRegistrar.getRegistrationId(this);

    // Check if regid already presents
    if (regId.equals("")) {
    // Registration is not present, register now with GCM          
    GCMRegistrar.register(this, SENDER_ID);
    } else {
    // Device is already registered on GCM
    if (GCMRegistrar.isRegisteredOnServer(this)) {
        // Skips registration.             
       // Toast.makeText(getApplicationContext(), "Already registered with GCM", Toast.LENGTH_LONG).show();
    } else {
        // Try to register again, but not in the UI thread.
        // It's also necessary to cancel the thread onDestroy(),
        // hence the use of AsyncTask instead of a raw thread.
        final Context context = this;
        mRegisterTask = new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                // Register on our server
                // On server creates a new user
                ServerUtilities.register(context, Connector.getUserId(), Connector.getUserDetails().get(3).toString(), regId);
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                mRegisterTask = null;
            }

        };
        mRegisterTask.execute(null, null, null);
    }
    }


    }



    void GCMStuffs()
    {
    	registerReceiver(mHandleMessageReceiver, new IntentFilter(
                DISPLAY_MESSAGE_ACTION));
    }
    
    
    
    
    
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
     changeFragment(1, position);
    }
    
    
    @Override
	public void onNavigationDrawerItemSelected2(int position) {
		// TODO Auto-generated method stub
    	 changeFragment(2, position);
	}

    
    void changeFragment(int listID,int position)
    {
    	Fragment currentFragment=null;
    	
    	if(fragment!=null)
    	{
    		currentFragment= fragment;
    	}
    	if(listID==1)
    	{
    		//If the Item is from the upper listView
    		switch (position) {
			case 0:
				fragment= new AllDevotions();
				mTitle="Devotional";
				break;

			case 1:
				fragment= new DevotionalBookmarks();
				mTitle="Bookmarks";
				break;

			case 2:
				fragment= new NotesFragment();
				mTitle="Notes";
				break;
			case 3:
				fragment= new ChurchTestimonyFeedActivity();
				mTitle="Testimonies";
				break;
				
			case 4:
				fragment= new ChurchEventFeedActivity();
				mTitle="Events";
				break;
			case 5:
				NavigationDrawerFragment.adapter1.getItem(5).setCount("");
				NavigationDrawerFragment.adapter1.getItem(5).setCounterVisibility(false);
				NavigationDrawerFragment.adapter1.notifyDataSetChanged();
		
				
				fragment= new AnnounceFragment();
				mTitle="Announcements";
				break;
			case 6:
				//fragment= new FAQFragment();
				fragment= new PrayerRequest();
				mTitle="Prayer Request";
				break;

			case 8:
				fragment= new UserDetailsFragment();
				mTitle="Profile";
				break;
			
			case 9:
				fragment= new ChurchDetailsFragment();
				mTitle="Church Profile";
				break;
			case 10:
				fragment= new ChurchGroupList();
				mTitle="Groups";
				
				break;
			case 11:
				//fragment= new PlaceholderFragment();
				//mTitle="Share Nsore App";
				Intent ints= new Intent(this, ShareActivity.class);
				startActivity(ints);
				break;
			case 12:
				fragment= new SettingsFragment();
				mTitle="Settings";
				break;
			default:
				break;
			}
    	}
    
    	
    	if (MainActivity.fragment != null) {
    		FragmentManager fragmentManager =getSupportFragmentManager();
	        
    		if(currentFragment!=null)
    		{
    			fragmentManager.beginTransaction().replace(((ViewGroup)(currentFragment.getView().getParent())).getId(), fragment).addToBackStack(null).commit();

    		}
    		else
    		{
    			fragmentManager.beginTransaction()
				.replace(R.id.container, MainActivity.fragment).addToBackStack(null).commit();
    		}
    		///transaction.replace(((ViewGroup)(currentFragment.getView().getParent())).getId(), fragment); 
			

			// update selected item and title, then close the drawer
//			mDrawerList.setItemChecked(position, true);
//			mDrawerList.setSelection(position);
//			mTitles=navMenuTitles[position];
//			setTitle(mTitles);
			
			supportInvalidateOptionsMenu();
			
		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
    }
    
    
    
    
    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }
    public static void restoreActionBar() {
    	
    }

//    public static void restoreActionBar() {
//        ActionBar actionBar = ((ActionBarActivity) Connector.context).getSupportActionBar();
//        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
//        actionBar.setDisplayShowTitleEnabled(true);
//        actionBar.setTitle(mTitle);
//    }
@Override
public boolean onPrepareOptionsMenu(Menu menu) {
	// TODO Auto-generated method stub
	
	if (!mNavigationDrawerFragment.isDrawerOpen()) {
        // Only show items in the action bar relevant to this screen
        // if the drawer is not showing. Otherwise, let the drawer
        // decide what to show in the action bar.
        //getMenuInflater().inflate(R.menu.main, menu);
		Connector.context=this;
        restoreActionBar();
        return true;
    }
	return super.onPrepareOptionsMenu(menu);
}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            //getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
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

 
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(KeyEvent.KEYCODE_MENU==keyCode)
		{
			NavigationDrawerFragment.manipulateDrawer();
			//Toast.makeText(this, "Menu Clicked", Toast.LENGTH_LONG).show();
			
			//mDrawerToggle.onOptionsItemSelected(mitem);
			return true;
		}
		else if(KeyEvent.KEYCODE_BACK ==keyCode)
		{
			fragment=null;
			onBackPressed();
		}
		return super.onKeyUp(keyCode, event);

}

	@Override
	public void onFragmentInteraction(Uri uri) {
		// TODO Auto-generated method stub
		
	}

	
	
	//GCM Operation handlers

public static void handleDpChange(String newMessage, final Context context,
		final String dpname) {
	// TODO Auto-generated method stub
	
	// TODO Auto-generated method stub
		//Toast.makeText(this, churchid, Toast.LENGTH_LONG).show();
		 dialog= new Dialog(Connector.context);
			LayoutInflater layinflate=((Activity) context).getLayoutInflater();
			View rootView= layinflate.inflate(R.layout.fragment_announcement,null);
			dialog.setContentView(rootView);
			dialog.setTitle("Profile Picture");
			dialog.setCancelable(false);
			TextView txtAnnouncement =(TextView) rootView.findViewById(R.id.txtAnnouncement);
			txtAnnouncement.setText("Loading New Profile Picture");
			ImageButton imgprofile=(ImageButton) rootView.findViewById(R.id.imgprofile);
			//
			Picasso.with(Connector.context)
			  .load(Connector.imageURL+dpname)
			  .resize(400, 400)
			  .centerCrop()
			  .transform(new CropSquareTransformation())
			  .into(imgprofile);
			
			//Bitmap map = imgprofile.get
			//imgprofile.setVisibility(View.GONE);
			
			Button btnClose= (Button) rootView.findViewById(R.id.btnClose);
	   btnClose.setOnClickListener( new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			
			
			
			
			AsyncTask<Void, Integer, Bitmap> fetchChurchDetails =  new AsyncTask<Void, Integer, Bitmap>() {
				
				ProgressDialog pdialog;
				private String oldimage;
				@Override
				protected void onPreExecute() {
					// TODO Auto-generated method stub
					pdialog= new ProgressDialog(Connector.context);
					pdialog.setMessage("Please Wait");
					pdialog.setCancelable(false);
					pdialog.show();
					super.onPreExecute();
				}
				@Override
				protected Bitmap doInBackground(Void... arg0) {
					// TODO Auto-generated method stub
					Bitmap data= Connector.download_Image(Connector.imageURL+dpname, Connector.AppFolder+"/userDP/"+dpname);
					return data;
				}
				@Override
				protected void onPostExecute(Bitmap result) {
					// TODO Auto-generated method stub
					String filePath= Environment.getExternalStorageDirectory().getPath();
					String imagepath = filePath+ "/"+Connector.AppFolder+"/userDP/"+dpname;
					pdialog.cancel();
					if(result!=null)
					{
						Toast.makeText(context, "Downloaded", Toast.LENGTH_LONG).show();
						oldimage= Connector.getUserdp();
						Connector.setUserdp(dpname);
						
						
						String filename = "pippo.jpg";
						File sd = Environment.getExternalStorageDirectory();
						File dest = new File(imagepath);

						Bitmap bitmap = (Bitmap)result;
						try {
						     FileOutputStream out = new FileOutputStream(dest);
						     bitmap.compress(Bitmap.CompressFormat.JPEG,90, out);
						     out.flush();
						     out.close();
						} catch (Exception e) {
						     e.printStackTrace();
						}
						
					}
					 dialog.cancel();
					
					super.onPostExecute(result);
				}
			};
			fetchChurchDetails.execute();
			
			;
			
		
			
			
			
			
			 NotificationManager mNotificationManager =
			         (NotificationManager) Connector.context.getSystemService(Context.NOTIFICATION_SERVICE);
			 mNotificationManager.cancel(0);
		}
	});
				dialog.setOnDismissListener( new OnDismissListener() {
					
					@Override
					public void onDismiss(DialogInterface arg0) {
						// TODO Auto-generated method stub
						 NotificationManager mNotificationManager =
				         (NotificationManager) Connector.context.getSystemService(Context.NOTIFICATION_SERVICE);
				 mNotificationManager.cancel(0);
					}
				});
				
			 dialog.show();
	
	
	
	
	
}

public static void handleChurchChange(String newMessage, final Context context, final String churchid) {
	// TODO Auto-generated method stub
	//Toast.makeText(this, churchid, Toast.LENGTH_LONG).show();
	 dialog= new Dialog(Connector.context);
		LayoutInflater layinflate=((Activity) context).getLayoutInflater();
		View rootView= layinflate.inflate(R.layout.fragment_announcement,null);
		dialog.setContentView(rootView);
		dialog.setTitle("Church Announcement");
		dialog.setCancelable(false);
		TextView txtAnnouncement =(TextView) rootView.findViewById(R.id.txtAnnouncement);
		txtAnnouncement.setText("Your church has been Changed. Restarting Application.");
		ImageButton imgprofile=(ImageButton) rootView.findViewById(R.id.imgprofile);
		imgprofile.setVisibility(View.GONE);
		
		Button btnClose= (Button) rootView.findViewById(R.id.btnClose);
   btnClose.setOnClickListener( new View.OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		Connector.setisLoggedIn(context, false);
		Connector.setisLoggedIn(Connector.context, false);
		
		
		
		AsyncTask<Void, Integer, String> fetchChurchDetails =  new AsyncTask<Void, Integer, String>() {
			
			ProgressDialog pdialog;
			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				pdialog= new ProgressDialog(Connector.context);
				pdialog.setMessage("Please Wait");
				pdialog.setCancelable(false);
				pdialog.show();
				super.onPreExecute();
			}
			@Override
			protected String doInBackground(Void... arg0) {
				// TODO Auto-generated method stub
				ArrayList<NameValuePair> parameters= new ArrayList<NameValuePair>();
				Connector.setChurchID(context, churchid);
				if(!Connector.getChurchID().equalsIgnoreCase("NO_CHURCH_FOUND"))
				{
					parameters.add( new BasicNameValuePair("CID",Connector.getChurchID()));
					parameters.add( new BasicNameValuePair("reason","get Church Details"));
					String response="";
					response= Connector.sendData(parameters, Connector.context);
							

								// TODO: register the new account here.
								return response;
				}
					return "NOT_SET";
			}
			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				pdialog.cancel();
				Connector.setChurchJson(result);
				Connector.setisLoggedIn(context, false);
				Intent ints= new Intent(context, Entry.class);
				Connector.context.startActivity(ints);
				((Activity) Connector.context).finish();
				super.onPostExecute(result);
			}
		};
		fetchChurchDetails.execute();
		
		
	
		
		
		
		
		 NotificationManager mNotificationManager =
		         (NotificationManager) Connector.context.getSystemService(Context.NOTIFICATION_SERVICE);
		 mNotificationManager.cancel(0);
	}
});
			dialog.setOnDismissListener( new OnDismissListener() {
				
				@Override
				public void onDismiss(DialogInterface arg0) {
					// TODO Auto-generated method stub
					 NotificationManager mNotificationManager =
			         (NotificationManager) Connector.context.getSystemService(Context.NOTIFICATION_SERVICE);
			 mNotificationManager.cancel(0);
				}
			});
			
		 dialog.show();
	
}
//protected ImageLoader imageLoader = ImageLoader.getInstance();
private static Dialog dialog;
public static String mTitles;



protected void processTestimonyLike(String newMessage,Context context) {
	 dialog= new Dialog(Connector.context);
		LayoutInflater layinflate=((Activity) context).getLayoutInflater();
		View rootView= layinflate.inflate(R.layout.fragment_announcements,null);
		dialog.setContentView(rootView);
		dialog.setTitle("Church Announcement");
		dialog.setCancelable(true);
		TextView txtAnnouncement =(TextView) rootView.findViewById(R.id.txtAnnouncement);
		txtAnnouncement.setText(newMessage);

			dialog.setOnDismissListener( new OnDismissListener() {
				
				@Override
				public void onDismiss(DialogInterface arg0) {
					// TODO Auto-generated method stub
					 NotificationManager mNotificationManager =
			         (NotificationManager) Connector.context.getSystemService(Context.NOTIFICATION_SERVICE);
			 mNotificationManager.cancel(0);
				}
			});
		 dialog.show();
}


protected void processEventAlert(String newMessage,Context context) {
	 dialog= new Dialog(Connector.context);
		LayoutInflater layinflate=((Activity) context).getLayoutInflater();
		View rootView= layinflate.inflate(R.layout.fragment_events,null);
		dialog.setContentView(rootView);
		dialog.setTitle("Church Event");
		dialog.setCancelable(true);
		TextView txtAnnouncement =(TextView) rootView.findViewById(R.id.txtAnnouncement);
		txtAnnouncement.setText(newMessage);

			dialog.setOnDismissListener( new OnDismissListener() {
				
				@Override
				public void onDismiss(DialogInterface arg0) {
					// TODO Auto-generated method stub
					 NotificationManager mNotificationManager =
			         (NotificationManager) Connector.context.getSystemService(Context.NOTIFICATION_SERVICE);
			 mNotificationManager.cancel(0);
				}
			});
		 dialog.show();
}


protected void processTestimony(String newMessage) {
	
}

@Override
public void onFragmentInteraction(Uri uri, Integer Code) {
	// TODO Auto-generated method stub
	
}





@Override
public void onActivityResult(int requestCode, int resultCode, Intent data) {
	// TODO Auto-generated method stub
	super.onActivityResult(requestCode, resultCode, data);

	 if (requestCode == NotesFragment.RESULTS_CODE && resultCode == Activity.RESULT_OK ) 
	{
		
//		 if(fragment instanceof NotesFragment)
//		 {
			 Connector.dbhelper = new DBHelper(this);
				//Connector.openDB(getActivity());
			 NotesFragment. bookmarks= Notes.getNotes(Connector.dbhelper);
				Connector.dbhelper.close();
				if( NotesFragment. bookmarks.size()>0)
				{
					 NotesFragment. bdapter.clear();
					
					for (Notes b : NotesFragment.bookmarks) {
						NotesFragment. bdapter.add(b);
					}
					NotesFragment. bdapter.notifyDataSetChanged();
					NotesFragment.toogleView(true);
					
				}
				else
				{
					NotesFragment.toogleView(false);
				} 
		// }
		
	}
}
	
}
