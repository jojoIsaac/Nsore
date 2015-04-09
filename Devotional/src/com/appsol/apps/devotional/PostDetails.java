package com.appsol.apps.devotional;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.appsol.apps.projectcommunicate.classes.CircularNetworkImageView;
import com.appsol.apps.projectcommunicate.classes.Connector;
import com.appsol.apps.projectcommunicate.classes.FeedImageView;
import com.appsol.apps.projectcommunicate.model.ChurchGroupFeeds;
import com.appsol.volley.classes.ExtHttpClientStack;
import com.appsol.volley.classes.SslHttpClient;
import com.appsol.volley.classes.Volley;
import com.shamanland.facebook.likebutton.FacebookLikeBox;

import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class PostDetails extends ActionBarActivity {
ImageLoader	imageLoader = com.appsol.volley.classes.ImageCacheManager.getInstance().getImageLoader();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.groupdatafeed_item);
	}
	 TextView like_statement;
	void updateLikes(ChurchGroupFeeds item)
	{
		  if(!item.getLikeFeed().equalsIgnoreCase("0"))
	         {
	        	Integer likes =Integer.parseInt(item.getLikes().trim());
	        	if(likes>3)
	        	{
	        		like_statement.setText("You and "+ (likes-1) +" users like this");
	        		item.setOperationReason("UnLike Group Feed");
	        	}
	        	else if(likes>1)
	        	{
	        		like_statement.setText("You and "+ (likes-1) +" other user like this");
	        		item.setOperationReason("UnLike Group Feed");
	        	}
	        	else
	        	{
	        		like_statement.setText("You like this");
	        		item.setOperationReason("UnLike Group Feed");
	        	}
	        	like_statement.setVisibility(View.VISIBLE);
	         }
	         else
	         {
	        	 item.setOperationReason("Like Group Feed");
	        	 like_statement.setVisibility(View.GONE);
	         }
	        
	}
	   public void getView() {
		   
	       
	 
	       if (imageLoader == null)
	            imageLoader = com.appsol.volley.classes.ImageCacheManager.getInstance().getImageLoader();
	            
	        like_statement=(TextView) findViewById(R.id.like_statement);
	        TextView name = (TextView) findViewById(R.id.name);
	        TextView timestamp = (TextView) findViewById(R.id.timestamp);
	        TextView statusMsg = (TextView) findViewById(R.id.txtStatusMsg);
	        //TextView url = (TextView) convertView.findViewById(R.id.txtUrl);
	        CircularNetworkImageView profilePic = (CircularNetworkImageView) findViewById(R.id.profilePic);
	        FeedImageView feedImageView = (FeedImageView) findViewById(R.id.feedImage1);
	       // TextView txttitle= (TextView) convertView.findViewById(R.id.txttitle);
//	        FeedImageView feedImageView = (FeedImageView) convertView
//	                .findViewById(R.id.feedImage1);
	        TextView txtchurchname=(TextView) findViewById(R.id.txtchurchname);
	        

	        final ChurchGroupFeeds item = new ChurchGroupFeeds();
	 
	        name.setText(item.getName());
	        name.setOnClickListener( new OnClickListener() {
	        	
	        	@Override
	        	public void onClick(View v) {
	        		// TODO Auto-generated method stub
	        		showProfile(item);
	        	}
	        });
	        timestamp.setText(" "+item.getDate_sent());
	 
	      
	        
	        
	        if(!TextUtils.isEmpty(item.getChurchname()))
	        {
	        	txtchurchname.setText(" @ "+item.getChurchname());
	        	txtchurchname.setVisibility(View.VISIBLE);
	        } else {
	            // status is empty, remove from view
	        	txtchurchname.setVisibility(View.GONE);
	        }
	        
	        
	        // Chcek for empty status message
	        if (!TextUtils.isEmpty(item.getDescription())) {
	        	int length= item.getDescription().length();
	        	
	        	if(length<=300)
	            statusMsg.setText(item.getDescription());
	        	else 
	        		statusMsg.setText(item.getDescription().subSequence(0, 299)+" ...");
	            statusMsg.setVisibility(View.VISIBLE);
	        } else {
	            // status is empty, remove from view
	            statusMsg.setVisibility(View.GONE);
	        }
	 
	        String url = Connector.imageURL + item.getProfilePic(); 
	    	        if(!TextUtils.isEmpty(item.getProfilePic())&&!url.equalsIgnoreCase(Connector.imageURL))
			{
	        	  profilePic.setImageUrl(Connector.imageURL+item.getProfilePic(), imageLoader);
			}
	 
	else
	{
		 //profilePic.setImageUrl(Connector.ChurchLogoURL+item.getchurchlogo(), imageLoader);
	profilePic.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.mobile));
	Log.e("IMAGEURL", url);
	}
	profilePic.setOnClickListener( new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//Toast.makeText(Connector.context, item.getAboutUser(), Toast.LENGTH_LONG).show();
			showProfile(item);
		}
	});


	//Feed image
	if (!TextUtils.isEmpty(item.getAttachment())&&item.getAttachment() != null) {
	    feedImageView.setImageUrl(Connector.groupFeedUrl+item.getAttachment(), imageLoader);
	    feedImageView.setVisibility(View.VISIBLE);
	    feedImageView
	            .setResponseObserver(new FeedImageView.ResponseObserver() {
	                @Override
	                public void onError() {
	                	Log.e("LOGG", "Not loaded");
	                }

	                @Override
	                public void onSuccess() {
	                }
	            });
	} else {
	    feedImageView.setVisibility(View.GONE);
	}

	 
	        //Handle the attendance button
	        final Button willAttend = (Button) findViewById(R.id.willAttend);
	        final Button shareTestimony=(Button)findViewById(R.id.shareTestimony);
	        final FacebookLikeBox fbox= (FacebookLikeBox)findViewById(R.id.likebox);
	        final Button comment =(Button) (findViewById(R.id.btncomment));
	        if(!item.getLikes().trim().equalsIgnoreCase("0"))
	        {
	        	fbox.setText(item.getLikes());
	        	fbox.setVisibility(View.VISIBLE);
	        }
	        else
	        {
	        	fbox.setText(item.getLikes());
	        	fbox.setVisibility(View.GONE);
	        }
	        

	        willAttend.setOnClickListener( new OnClickListener() {
	     	
	     	@Override
	     	public void onClick(View v) {
	     		// TODO Auto-generated method stub
	     		handleTestimonyLike(item);
	     	
	     	}
	     });
	        
	        shareTestimony.setOnClickListener( new OnClickListener() {
	    		
	    		@Override
	    		public void onClick(View v) {
	    			
	    			/*Intent ints= new Intent(Intent.ACTION_SEND);
	    			ints.putExtra(Intent.EXTRA_TEXT, item.getSenderName().toString()+
	    					"'s Testimony  "+"\n "+item.getDescription().toString()+"\n Shared from: "+Connector.parentURL);
	    			ints.setType("text/plain");
	    			Connector.context.startActivity(ints);*/
	    			
	    		}
	    	});
	    	if(item.getComment().equalsIgnoreCase("0"))
	    		comment.setText("Comment");
	    		else
	    			comment.setText(item.getComment());
	        comment.setOnClickListener( new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					handleComment(item);
				}
			});
	        
	        
	    }
	
	   protected void handleComment(ChurchGroupFeeds item) {
			// TODO Auto-generated method stub
			Connector.handleGroupFeedComment(item);
		}

	    protected void showProfile(ChurchGroupFeeds item) {
	  		// TODO Auto-generated method stub
	    	//Toast.makeText(context, item.getUserCover(), Toast.LENGTH_LONG).show();
	  		Connector.handleUserProfile(item, this, false);
	  	}
		void handleTestimonyLike(final ChurchGroupFeeds testimony)
		{
			
		//This function will handle when a user click on the like button. 
		// If the user already likes the testimony, we assumes click it again renders it not-liked.
			String URL_FEED = "https://nsoredevotional.com/mobile/devotionHandler.php";
		        	 StringRequest jsonReq = new  StringRequest(Method.POST,
		        			 URL_FEED , createMyReqSuccessListener(testimony),createMyReqErrorListener())
		        	    {
		 		        @Override
		 		        protected Map<String, String> getParams() {
		 		       Map<String, String> params = new HashMap<String, String>();    
		 		           params.put("FID", testimony.getFeedID());
		 		           if(!testimony.getLikeFeed().equalsIgnoreCase("0"))
		 		           {
		 		        	  params.put("reason", "UnLike Group Feed");
		 		           }
		 		           else
		 		           {
		 		        	  params.put("reason", "Like Group Feed");
		 		           }
		 					
		 					params.put("USER_ID", Connector.getUserId());
		 					params.put("UN", Connector.getUserDetails().get(1));
		 					//params.put("DID",testimony.getDeviceID() );
		 					String ismine= (testimony.getMemberID().equalsIgnoreCase(Connector.getUserId())? "Y":"N");
		 					params.put("IS-MINE",ismine);
		 		            
		 		            return params;
		 		        }}
		        	 ;
		            InputStream keyStore = Connector.context.getResources().openRawResource(R.raw.appsol);
		            
		            
		            RequestQueue queue = Volley.newRequestQueue(Connector.context,
	                        new ExtHttpClientStack(new SslHttpClient(keyStore, "qwerty")));
		            // Adding request to volley request queue
		            queue.add(jsonReq);
		        }
		
		
		 private Response.Listener<String> createMyReqSuccessListener(final ChurchGroupFeeds testimony) {
		        return new Response.Listener<String>() {
		        	
		        	
		        	
		        	
		        	
		            @Override
		            public void onResponse(String response) {
		            	
		            	
		            	Integer likes= Integer.parseInt(testimony.getLikes());
		            	if(!testimony.getLikeFeed().equalsIgnoreCase("0"))
		 		           {
		 		        	 testimony.setLikeFeed("0");
		 		        	 if(likes>0)
		 		        	 {
		 		        		 likes=likes-1;
		 		        	 }
		 		        	 
		 		           }
		 		           else
		 		           {
		 		        	  testimony.setLikeFeed("1");
		 		        	 likes=likes+1;
		 		           }
		            	 testimony.setLikes(likes.toString());
		               	
		              
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
						showErrorDialog(Connector.HandleVolleyerror(error, Connector.context));
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
				

				AlertDialog.Builder b = new AlertDialog.Builder(Connector.context);
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
						//loadpage2();
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		///getMenuInflater().inflate(R.menu.post_details, menu);
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
