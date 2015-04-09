package com.appsol.apps.devotional;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import com.appsol.apps.projectcommunicate.classes.Connector;
import com.appsol.apps.projectcommunicate.model.Contacts;
import com.appsol.sharewithcontact.ui.ContactsListActivity;
import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class ShareActivity extends ActionBarActivity {

	
	private UiLifecycleHelper uiHelper;
	private static final String APP_ID = "1446908752238057";
	// Instance of Facebook Class
    private Facebook facebook;
    private AsyncFacebookRunner mAsyncRunner;
    String FILENAME = "AndroidSSO_data";
    private SharedPreferences mPrefs;
    private static String sharemessage="" +
			"Check out Nsore Devotional App for your \n" +
			"Android phone and tablets from Appsol Info. Systems \n" +
			"I am using Nsore Devotional and it helps me daily in \n" +
			"my life with God, keeps me in touch with the word, my Church etc.\n" +
			" Download it now from: \n "+Connector.parentURL;
    void twitterShare()
    {
    	
    	String url = "twitter://post?message=";

    	try {
    		String message="" +
    				"Check out Nsore Devotional App for your\n" +
    				"Android phone and tablets from Appsol Info. Systems\n" +
    				"Download it now from: \n "+Connector.parentURL;
    	    Intent i = new Intent(Intent.ACTION_VIEW);
    	    i.setData(Uri.parse(url + Uri.encode(message)));
    	    startActivity(i);
    	} catch (android.content.ActivityNotFoundException e) {
    	    Toast.makeText(this, "Can't send tweet!", Toast.LENGTH_LONG).show();
    	}
    }
    void normalShare()
    {
		Intent shareIntent = new Intent();
		shareIntent.setAction(Intent.ACTION_SEND);
		shareIntent.putExtra(Intent.EXTRA_TEXT, sharemessage);
		shareIntent.setType("text/*" );
		startActivity(shareIntent);
    }
    void sendEmail()
    {
    	Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto","", null));
    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Nsore Devotional");
    emailIntent.putExtra(Intent.EXTRA_TEXT, sharemessage);
    startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share);
		  uiHelper = new UiLifecycleHelper(this, null);
		    uiHelper.onCreate(savedInstanceState);
		    mPrefs = getPreferences(MODE_PRIVATE);
		    
		    
//		    try {
//		        PackageInfo info = getPackageManager().getPackageInfo(
//		                "com.appsol.apps.devotional", 
//		                PackageManager.GET_SIGNATURES);
//		        for (Signature signature : info.signatures) {
//		            MessageDigest md = MessageDigest.getInstance("SHA");
//		            md.update(signature.toByteArray());
//		            Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//		            }
//		    } catch (NameNotFoundException e) {
//
//		    } catch (NoSuchAlgorithmException e) {
//
//		    }
		    
		    
		    String access_token = mPrefs.getString("access_token", null);
		    long expires = mPrefs.getLong("access_expires", 0);
		    TextView imgfacebook= (TextView) findViewById(R.id.imgfacebook);
		    TextView imgAllShares=(TextView) findViewById(R.id.imgAllShares);
		    TextView imgemailShare=(TextView) findViewById(R.id.imgemailShare);
		    TextView imgTwitter=(TextView) findViewById(R.id.imgTwitter);
		    TextView imgContact=(TextView) findViewById(R.id.imgContact);
		    imgContact.setOnClickListener( new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent ints= new Intent(ShareActivity.this,ContactsListActivity.class);
					startActivity(ints);
				}
			});
		    imgTwitter.setOnClickListener( new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					twitterShare();
				}
			});
		    imgemailShare.setOnClickListener( new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					sendEmail();
				}
			});
		    imgAllShares.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					normalShare();
				}
			});
		    imgfacebook.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					faceBookShare();
				}
			});
	}

	void faceBookShare()
	{
		if (FacebookDialog.canPresentShareDialog(getApplicationContext(), 
                FacebookDialog.ShareDialogFeature.SHARE_DIALOG)) {
// Publish the post using the Share Dialog
FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(this)
.setLink("https://nsoredevotional.com")
 .setApplicationName("Nsore Devotional")
.setDescription("Nsore Devotional is a web and " +
	    		"mobile based application that connects people in the" +
	    		" christian community in Ghana." +
	    		"")
	    		.setPicture("https://nsoredevotional.com/church_logos/thumbsmini/1412478079.png")
	    		.setCaption("Share Nsore Devotional")
.build();
uiHelper.trackPendingDialogCall(shareDialog.present());

} else {
// Fallback. For example, publish the post using the Feed Dialog
	
	if (Session.getActiveSession() == null || !Session.getActiveSession().isOpened()) {
        Session.openActiveSession(ShareActivity.this, true, callback);
    } else {
        publishFeedDialog();
    }
	
	//publishFeedDialog();
}
	}
	
	 private Session.StatusCallback callback = new Session.StatusCallback() {
			
			@Override
			public void call(Session session, SessionState state, Exception exception) {
				
				if (state.isOpened() ) {
					publishFeedDialog();
				}
			}
		};
	private void publishFeedDialog() {
	    Bundle params = new Bundle();
	    params.putString("name", "Nsore Devotional");
	    params.putString("caption", "Share Nsore Devotional");
	    params.putString("description", "Nsore Devotional is a web and " +
	    		"mobile based application that connects people in the" +
	    		" christian community in Ghana." +
	    		"");
	    params.putString("link", "https://nsoredevotional.com");
	    params.putString("picture", "https://nsoredevotional.com/church_logos/thumbsmini/1412478079.png");

	    WebDialog feedDialog = (
	        new WebDialog.FeedDialogBuilder(this,
	            Session.getActiveSession(),
	            params))
	        .setOnCompleteListener(new OnCompleteListener() {

	            @Override
	            public void onComplete(Bundle values,FacebookException error) {
	                if (error == null) {
	                    // When the story is posted, echo the success
	                    // and the post Id.
	                    final String postId = values.getString("post_id");
	                    if (postId != null) {
	                        Toast.makeText(ShareActivity.this,
	                            "Posted story, id: "+postId,
	                            Toast.LENGTH_SHORT).show();
	                    } else {
	                        // User clicked the Cancel button
	                        Toast.makeText(ShareActivity.this.getApplicationContext(), 
	                            "Publish cancelled", 
	                            Toast.LENGTH_SHORT).show();
	                    }
	                } else if (error instanceof FacebookOperationCanceledException) {
	                    // User clicked the "x" button
	                    Toast.makeText(ShareActivity.this.getApplicationContext(), 
	                        "Publish cancelled", 
	                        Toast.LENGTH_SHORT).show();
	                } else {
	                    // Generic, ex: network error
	                    Toast.makeText(ShareActivity.this.getApplicationContext(), 
	                        "Error posting story", 
	                        Toast.LENGTH_SHORT).show();
	                }
	            }

				

	        })
	        .build();
	    feedDialog.show();
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);

	    uiHelper.onActivityResult(requestCode, resultCode, data, new FacebookDialog.Callback() {
	        @Override
	        public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
	            Log.e("Activity", String.format("Error: %s", error.toString()));
	        }

	        @Override
	        public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
	            Log.i("Activity", "Success!");
	        }
	    });
	}
	
	
	@Override
	protected void onResume() {
	    super.onResume();
	    uiHelper.onResume();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    uiHelper.onSaveInstanceState(outState);
	}

	@Override
	public void onPause() {
	    super.onPause();
	    uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
	    super.onDestroy();
	    uiHelper.onDestroy();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.share, menu);
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
	List<Contacts> contacts;
	 
	void readContacts()
	{
		Cursor cursor= getContentResolver().
				query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
						null, null, null, null);
		while (cursor.moveToNext()) {
	String name= cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
	String phone= cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
	Contacts c= new Contacts();
	c.setName(name);
	c.setPhone(phone);
	//adapter.add(c);
	System.out.print("Name: "+ name+" == Phone: "+phone);
		}
		cursor.close();
		//adapter.notifyDataSetChanged();
		
	}
	
	
}
