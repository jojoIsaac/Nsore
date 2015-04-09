package com.appsol.apps.devotional;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.StringRequest;
import com.appsol.apps.projectcommunicate.adapter.ChurchDetailsPagerAdapter;
import com.appsol.apps.projectcommunicate.adapter.DevotionPagerAdapter;
import com.appsol.apps.projectcommunicate.adapter.UserprofileDetailsPagerAdapter;
import com.appsol.apps.projectcommunicate.classes.CircularNetworkImageView;
import com.appsol.apps.projectcommunicate.classes.Connector;
import com.appsol.apps.projectcommunicate.classes.ImageHelper;
import com.appsol.apps.projectcommunicate.model.NsoreDevotionalUser;
import com.appsol.volley.classes.ExtHttpClientStack;
import com.appsol.volley.classes.SslHttpClient;
import com.appsol.volley.classes.Volley;
//import com.android.volley.toolbox.Volley;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass. Use the
 * {@link NsoreUserDetailsActivity#newInstance} factory method to create an
 * instance of this fragment.
 * 
 */
public class NsoreUserDetailsActivity extends ActionBarActivity {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;
	private TextView txtChurchname;
	private CircularNetworkImageView profilepix;
	private String filePath;
	private String imagename;
	public  NsoreDevotionalUser nsoreUser;
	private boolean mInError;
	
	

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment ChurchDetailsFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static NsoreUserDetailsActivity newInstance(String param1, String param2) {
		NsoreUserDetailsActivity fragment = new NsoreUserDetailsActivity();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		//fragment.setArguments(args);
		return fragment;
	}

	public NsoreUserDetailsActivity() {
		// Required empty public constructor
	}
@Override
protected void onActivityResult(int arg0, int arg1, Intent arg2) {
	// TODO Auto-generated method stub
	super.onActivityResult(arg0, arg1, arg2);
}

@Override
protected void onResume() {
	// TODO Auto-generated method stub
	super.onResume();
	Connector.myPrefs= getSharedPreferences(Connector.prefernceName, Context.MODE_PRIVATE);


}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
			Connector.context= this;
		//View view = LayoutInflater.inflate(R.layout.view_pager_userprofile_ui,null,false);
		setContentView(R.layout.view_pager_userprofile_ui);
		
		setUpUI();
		
		
		
		
	}

	
	Button btnconnect;
	private void setUpUI() {
		// TODO Auto-generated method stub
		ImageLoader imageLoader = com.appsol.volley.classes.ImageCacheManager.getInstance().getImageLoader();
		Intent commentIntent= getIntent();
		nsoreUser = new NsoreDevotionalUser();
		
		
		if(commentIntent!=null)
		{
		nsoreUser.setMemberID(commentIntent.getExtras().getString("UID"));
		nsoreUser.setName(commentIntent.getExtras().getString("name"));
		nsoreUser.setChurchname(commentIntent.getExtras().getString("churchname"));
		nsoreUser.setChurchID(commentIntent.getExtras().getString("churchid"));
		nsoreUser.setDeviceID(commentIntent.getExtras().getString("userdevice"));
		nsoreUser.setMemberSince(commentIntent.getExtras().getString("membersince"));
		nsoreUser.setIsConnected(commentIntent.getExtras().getString("connected"));
		nsoreUser.setProfilePic(commentIntent.getExtras().getString("userdp"));
		nsoreUser.setUserCover(commentIntent.getExtras().getString("cover"));
		nsoreUser.setMutualFriends(commentIntent.getExtras().getString("mutual"));
		nsoreUser.setAboutUser(commentIntent.getExtras().getString("about"));
		setTitle(nsoreUser.getName());
		}
		
		
ViewPager mPager = (ViewPager) findViewById(R.id.viewPager);
		
		
		
		mPager.setAdapter(new UserprofileDetailsPagerAdapter(getSupportFragmentManager(),nsoreUser));
		final LinearLayout layout_cover_photo= (LinearLayout) findViewById(R.id.layout_cover_photo);
		txtChurchname= (TextView)findViewById(R.id.txtuserfullname);
		txtChurchname.setText(nsoreUser.getName());
		  profilepix=(CircularNetworkImageView) findViewById(R.id.userprofile);
		  btnconnect= (Button) findViewById(R.id.btnconnect);
		  /*
		  This function searches if two users are connected
		 -10: The two Ids Provided are the same so we avoid check
		  1: The users are Connected
		   2: This if the other user is yet to accept request
		  -1: If no request has been sent
		*/
		  
		  if(nsoreUser.getIsConnected().equalsIgnoreCase("1"))
		  {
			  btnconnect.setText("In connection");
			  Drawable img = getResources().getDrawable( R.drawable.ic_nsore_heart );
			  btnconnect.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
			  nsoreUser.setOperationReason("Edit Connection");
		  }
		  else if(nsoreUser.getIsConnected().equalsIgnoreCase("-1"))
		  {
			    btnconnect.setText("Send Request");
			    Drawable img = getResources().getDrawable( R.drawable.ic_nsore_add );
			    btnconnect.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
			    nsoreUser.setOperationReason("Send Request");
		  }
		  else if(nsoreUser.getIsConnected().equalsIgnoreCase("2"))
		  {
			  btnconnect.setText("Cancel Request");
			  Drawable img =getResources().getDrawable( R.drawable.ic_nsore_cancel );
			  btnconnect.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
			  nsoreUser.setOperationReason("Cancel Request");
		  }
		  else if(nsoreUser.getIsConnected().equalsIgnoreCase("-2"))
		  {
			  btnconnect.setText("Accept Request");
			  Drawable img = getResources().getDrawable( R.drawable.ic_nsore_tick );
			  btnconnect.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
			  nsoreUser.setOperationReason("Accept Request");
		  }
		  else
		  {
			  btnconnect.setText("Send Request");
			  Drawable img = getResources().getDrawable( R.drawable.ic_nsore_add );
			 btnconnect.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
			  nsoreUser.setOperationReason("Send Request");
		  }
		  
		  
		
		  btnconnect.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				handleServerRequest(nsoreUser);
				
			}
		});
			
		   String url = Connector.imageURL + nsoreUser.getProfilePic(); 
		   if (imageLoader == null)
	            imageLoader = com.appsol.volley.classes.ImageCacheManager.getInstance().getImageLoader();
		   profilepix.setImageBitmap(BitmapFactory.decodeResource(Connector.context.getResources(), R.drawable.mobile));
	        if(!url.equalsIgnoreCase(Connector.imageURL))
			{
	        	profilepix.setImageUrl(Connector.imageURL+nsoreUser.getProfilePic(), imageLoader);
			}
	 
	else
		profilepix.setImageBitmap(BitmapFactory.decodeResource(Connector.context.getResources(), R.drawable.mobile));
	     
	        
	        filePath= Environment.getExternalStorageDirectory().getPath();
	        //Set some vital information
	        
	        
	        final String urlCover = Connector.member_cover_photosURL + nsoreUser.getUserCover();     
	   //layout_cover_photo= (LinearLayout) findViewById(R.id.layout_cover_photo);  
	   
	  
	      //layout_cover_photo
			
			//imageLoader.get(requestUrl, imageListener, maxWidth, maxHeight)
		imageLoader.get(urlCover,  new ImageListener() {
			
			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@TargetApi(Build.VERSION_CODES.JELLY_BEAN) @Override
			public void onResponse(ImageContainer arg0, boolean arg1) {
				// TODO Auto-generated method stub
				ImageContainer dayt= arg0;
				
				//Toast.makeText(NsoreUserDetailsActivity.this, urlCover, Toast.LENGTH_LONG).show();
				
				if(dayt!=null)
				{
					Bitmap data= dayt.getBitmap();
					if(data!=null)
					{
						 Drawable img = new BitmapDrawable(getResources(),data);
						 layout_cover_photo.setBackground(img);
						 //BitmapDrawable img= data;
					}
					 
				}
			}
		}, layout_cover_photo.getWidth(), 100);
	}


	
	
	
	
	private void handleServerRequest(final NsoreDevotionalUser user) {
		
		String URL_FEED = "https://nsoredevotional.com/mobile/devotionHandler.php";
		StringRequest jsonReq = new StringRequest(Method.POST, URL_FEED,
				createMyReqSuccessListener(user), createMyReqErrorListener()) {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				params.put("CID", Connector.getChurchID());
				params.put("MID", user.getMemberID());
				params.put("UID", Connector.getUserId());
				params.put("reason", user.getOperationReason());
				
				return params;
			}
		};
		InputStream keyStore = Connector.context.getResources().openRawResource(R.raw.appsol);

		RequestQueue queue = Volley.newRequestQueue(Connector.context,
				new ExtHttpClientStack(new SslHttpClient(keyStore, "qwerty")));
		// Adding request to volley request queue
		queue.add(jsonReq);
	}
	
	private Response.Listener<String> createMyReqSuccessListener(final NsoreDevotionalUser user) {
		return new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				if (!response.equalsIgnoreCase("No Testimony Shared.")) {
					//user.setServerResponse(response);
					//Toast.makeText(Connector.context, response, Toast.LENGTH_LONG).show();
					if(response.trim().contains("Request Cancelled"))
					{
						  //holder.btnConnect.setText("Send Request");
						    user.setOperationReason("Send Request");
						    btnconnect.setText("Send Request");
						    Drawable img =getResources().getDrawable( R.drawable.ic_nsore_add );
							  btnconnect.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
						    user.setIsConnected("-1");
						  
					}
					else if(response.trim().contains("Friend Accepted"))
					{
						  //holder.btnConnect.setText("Send Request");
						    user.setOperationReason("Edit Connection");
						    btnconnect.setText("Edit Connection");
						    Drawable img =getResources().getDrawable( R.drawable.ic_nsore_heart );
							  btnconnect.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
						    user.setIsConnected("1");
						  
					} 
					else if(response.trim().contains("Request Sent"))
					{
						  user.setOperationReason("Cancel Request");
						  Drawable img =getResources().getDrawable( R.drawable.ic_nsore_cancel );
						  btnconnect.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
						  btnconnect.setText("Cancel Request");
						  user.setIsConnected("2");
						  
					}
				}
			}
		};
	}

	private Response.ErrorListener createMyReqErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				showErrorDialog(error.getMessage());
				Log.d("RES", error.getMessage());
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
				//handleServerRequest(nsoreUser);
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
	
	

}
