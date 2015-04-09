package com.appsol.apps.projectcommunicate.adapter;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.appsol.apps.devotional.AppController;
import com.appsol.apps.devotional.R;
import com.appsol.apps.projectcommunicate.classes.CircularNetworkImageView;
import com.appsol.apps.projectcommunicate.classes.Connector;
import com.appsol.apps.projectcommunicate.classes.Devotion;
import com.appsol.apps.projectcommunicate.classes.FeedImageView;
import com.appsol.apps.projectcommunicate.model.ChurchGroup;
import com.appsol.apps.projectcommunicate.model.ChurchGroupFeeds;
import com.appsol.apps.projectcommunicate.model.DevotionComment;
import com.appsol.apps.projectcommunicate.model.Testimony;
import com.appsol.volley.classes.ExtHttpClientStack;
import com.appsol.volley.classes.SslHttpClient;
import com.appsol.volley.classes.Volley;
import com.shamanland.facebook.likebutton.FacebookLikeBox;
public class GroupDataFeedListAdapter extends BaseAdapter {  
    private Activity activity;
    private LayoutInflater inflater;

    private List<ChurchGroupFeeds> feedItems;
    Context context;
    ImageLoader imageLoader =com.appsol.volley.classes.ImageCacheManager.getInstance().getImageLoader();
    
	
 
    public GroupDataFeedListAdapter(Activity activity, List<ChurchGroupFeeds> feedItems) {
        this.activity = activity;
        context=activity.getBaseContext();
        this.feedItems = feedItems;
    }
 
    @Override
    public int getCount() {
        return feedItems.size();
    }
 
    @Override
    public Object getItem(int location) {
        return feedItems.get(location);
    }
 
    @Override
    public long getItemId(int position) {
        return position;
    }
 
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
 
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.groupdatafeed_item, null);
 
       if (imageLoader == null)
            imageLoader = com.appsol.volley.classes.ImageCacheManager.getInstance().getImageLoader();
            
        TextView like_statement=(TextView) convertView.findViewById(R.id.like_statement);
        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView timestamp = (TextView) convertView
                .findViewById(R.id.timestamp);
        TextView statusMsg = (TextView) convertView
                .findViewById(R.id.txtStatusMsg);
        //TextView url = (TextView) convertView.findViewById(R.id.txtUrl);
        CircularNetworkImageView profilePic = (CircularNetworkImageView) convertView
                .findViewById(R.id.profilePic);
        FeedImageView feedImageView = (FeedImageView) convertView
                .findViewById(R.id.feedImage1);
       // TextView txttitle= (TextView) convertView.findViewById(R.id.txttitle);
//        FeedImageView feedImageView = (FeedImageView) convertView
//                .findViewById(R.id.feedImage1);
        TextView txtchurchname=(TextView) convertView.findViewById(R.id.txtchurchname);
        

        final ChurchGroupFeeds item = feedItems.get(position);
 
        name.setText(item.getName());
        name.setOnClickListener( new OnClickListener() {
        	
        	@Override
        	public void onClick(View v) {
        		// TODO Auto-generated method stub
        		showProfile(item);
        	}
        });
 
        // Converting timestamp into x ago format
//        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
//                Long.parseLong(item.getTimeStamp()),
//                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
        timestamp.setText(" "+item.getDate_sent());
 
        
//        if (!TextUtils.isEmpty(item.getContent())) {
//        	txttitle.setText(item.getTitle().toUpperCase());
//            statusMsg.setVisibility(View.VISIBLE);
//        } else {
//            // status is empty, remove from view
//        	txttitle.setVisibility(View.GONE);
//        }
        //like_statement
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
        
       // Log.e("IMAGEURL", url);
     
        
        
       // profilePic.setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.mobile));

        if(!TextUtils.isEmpty(item.getProfilePic())&&!url.equalsIgnoreCase(Connector.imageURL))
		{
        	  profilePic.setImageUrl(Connector.imageURL+item.getProfilePic(), imageLoader);
		}
 
else
{
	 //profilePic.setImageUrl(Connector.ChurchLogoURL+item.getchurchlogo(), imageLoader);
profilePic.setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.mobile));
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
        final Button willAttend = (Button) convertView.findViewById(R.id.willAttend);
        final Button shareTestimony=(Button)convertView.findViewById(R.id.shareTestimony);
        final FacebookLikeBox fbox= (FacebookLikeBox) convertView.findViewById(R.id.likebox);
        final Button comment =(Button) (convertView.findViewById(R.id.btncomment));
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
        
        return convertView;
    }
    protected void handleComment(ChurchGroupFeeds item) {
		// TODO Auto-generated method stub
		Connector.handleGroupFeedComment(item);
	}

    protected void showProfile(ChurchGroupFeeds item) {
  		// TODO Auto-generated method stub
    	//Toast.makeText(context, item.getUserCover(), Toast.LENGTH_LONG).show();
  		Connector.handleUserProfile(item, context, false);
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
	               	
	               notifyDataSetChanged();
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
	
	

	   
	   
	   
	   
    
}