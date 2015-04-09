package com.appsol.apps.projectcommunicate.adapter;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.opengl.Visibility;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.appsol.apps.devotional.AppController;
import com.appsol.apps.devotional.R;
import com.appsol.apps.projectcommunicate.classes.Connector;
import com.appsol.apps.projectcommunicate.classes.FeedImageView;
import com.appsol.apps.projectcommunicate.model.ChurchEvents;
import com.appsol.apps.projectcommunicate.model.DBHelper;
import com.appsol.apps.projectcommunicate.model.SubacribedEvents;
import com.appsol.apps.projectcommunicate.model.Testimony;
import com.appsol.volley.classes.ExtHttpClientStack;
import com.appsol.volley.classes.SslHttpClient;
import com.appsol.volley.classes.Volley;

public class EventFeedListAdapter extends BaseAdapter {  
    private Activity activity;
    private LayoutInflater inflater;
    private List<ChurchEvents> feedItems;
    Context context;
    ImageLoader imageLoader =  com.appsol.volley.classes.ImageCacheManager.getInstance().getImageLoader();
 
    public EventFeedListAdapter(Activity activity, List<ChurchEvents> feedItems) {
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
            convertView = inflater.inflate(R.layout.eventfeed_item, null);
 
        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
 
        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView timestamp = (TextView) convertView
                .findViewById(R.id.timestamp);
        TextView statusMsg = (TextView) convertView
                .findViewById(R.id.txtStatusMsg);
        TextView url = (TextView) convertView.findViewById(R.id.txtUrl);
        NetworkImageView profilePic = (NetworkImageView) convertView
                .findViewById(R.id.profilePic);
        TextView txttitle= (TextView) convertView.findViewById(R.id.txttitle);
        FeedImageView feedImageView = (FeedImageView) convertView
                .findViewById(R.id.feedImage1);
        final TextView like_statement=(TextView) convertView.findViewById(R.id.like_statement);
        LinearLayout layout_share_like= (LinearLayout) convertView.findViewById(R.id.layout_share_like);
        

        final ChurchEvents item = feedItems.get(position);
 
        name.setText(item.getChurchname());

        timestamp.setText(item.getStart_date());
 
        
        if (!TextUtils.isEmpty(item.getDetail())) {
        	txttitle.setText(item.getTitle().toUpperCase());
            statusMsg.setVisibility(View.VISIBLE);
        } else {
            // status is empty, remove from view
        	txttitle.setVisibility(View.GONE);
        }
    	Integer likes =Integer.parseInt(item.getUsersAttending().trim());
        if(!item.getIsGoing().trim().equalsIgnoreCase("0"))
        {
       
       	if(likes>3)
       	{
       		like_statement.setText("You and "+ (likes-1) +" users  attending");
       	}
       	else if(likes>1)
       	{
       		like_statement.setText("You and "+ (likes-1) +" other user attending");
       	}
       	else
       	{
       		like_statement.setText("You will attend");
       	}
       	like_statement.setVisibility(View.VISIBLE);
        }
        else
        {
        	if(likes>1)
        	{
        	like_statement.setText( (likes) +" users attending ");
        	like_statement.setVisibility(View.VISIBLE);
        	}
        	else if(likes==1)
        	{
        		like_statement.setText( (likes) +" user  attending ");
        		like_statement.setVisibility(View.VISIBLE);
        	}
//        	else
//        	{
//        		like_statement.setVisibility(View.GONE);
//        	}
        }
       
        
        
        // Chcek for empty status message
        if(item.getIspast().trim().equalsIgnoreCase("1"))
		{
			layout_share_like.setVisibility(View.VISIBLE);
		}
        else
        {
        	layout_share_like.setVisibility(View.GONE);
        }
        
        if (!TextUtils.isEmpty(item.getDetail())) {
        	int length= item.getDetail().length();
        	
        	if(length<=300)
            statusMsg.setText(item.getDetail());
        	else 
        		statusMsg.setText(item.getDetail().subSequence(0, 299)+" ...");
            statusMsg.setVisibility(View.VISIBLE);
        } else {
            // status is empty, remove from view
            statusMsg.setVisibility(View.GONE);
        }
 
        // Checking for null feed url
        if (item.getUrl() != null) {
            url.setText(Html.fromHtml("<a href=\"" + item.getUrl() + "\">"
                    + item.getUrl() + "</a> "));
 
            // Making url clickable
            url.setMovementMethod(LinkMovementMethod.getInstance());
            url.setVisibility(View.VISIBLE);
        } else {
            // url is null, remove from the view
            url.setVisibility(View.GONE);
        }
 
        

        profilePic.setImageUrl(Connector.ChurchLogoURL+item.getChurchLogo(), imageLoader);
 
        
        
        
        
        // Feed image
        if (item.getBanner() != null) {
            feedImageView.setImageUrl(Connector.eventBannerURL+item.getBanner(), imageLoader);
            feedImageView.setVisibility(View.VISIBLE);
            feedImageView
                    .setResponseObserver(new FeedImageView.ResponseObserver() {
                        @Override
                        public void onError() {
                        }
 
                        @Override
                        public void onSuccess() {
                        }
                    });
        } else {
            feedImageView.setVisibility(View.GONE);
        }
 final Button shareEvent =(Button)  convertView.findViewById(R.id.shareEvent);
 shareEvent.setOnClickListener( new OnClickListener() {
		
		private String messageToShare;

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			messageToShare="I would like to invite you to an upcoming event by the "+Connector.getChurchName()+"\n" +
					"Titled: "+ item.getTitle()+"\nDate: "+item.getStart_date() + " \nTime: " +
							""+ item.getStart_Time()+"\nLocation: "+item.getLocation()+" \nShared from Nsore Devotional";
			Intent ints= new Intent(Intent.ACTION_SEND);
			ints.putExtra(Intent.EXTRA_TEXT, messageToShare);
			ints.setType("text/plain");
			
			Connector.context.startActivity(ints);
		}
	});
        //Handle the attendance button
        final Button willAttend = (Button) convertView.findViewById(R.id.willAttend);
        
        
        if(item.getIsGoing().trim().equalsIgnoreCase("1"))
		{
			willAttend.setText("Cancel Request");
			if(likes>3)
	       	{
	       		like_statement.setText("You and "+ (likes-1) +" users  attending");
	       	}
	       	else if(likes >1)
	       	{
	       		like_statement.setText("You and "+ (likes-1) +" other user attending");
	       	}
	       	else
	       	{
	       		like_statement.setText("You will attend");
	       	}
	       	like_statement.setVisibility(View.VISIBLE);
		}
        else
        {
        	willAttend.setText("Will Attend");
        	
        	if(likes>1)
        	{
        	like_statement.setText( (likes) +" users attending ");
        	like_statement.setVisibility(View.VISIBLE);
        	}
        	else if(likes==1)
        	{
        		like_statement.setText( (likes) +" user  attending ");
        		like_statement.setVisibility(View.VISIBLE);
        	}
        	else
        		
        	{
        		like_statement.setVisibility(View.GONE);
        	}
        	
        }
        
        willAttend.setOnClickListener( new OnClickListener() {
     	
     	@Override
     	public void onClick(View v) {
     		// TODO Auto-generated method stub
     		if(willAttend.getText().toString().equalsIgnoreCase("Cancel Request"))
     		{
     			HandleUserOptionToAttend(item,"Cancel Request",willAttend,like_statement);
     		}
     		else
     		  HandleUserOptionToAttend(item,"Will Attend Program",willAttend,like_statement);
     	}
     });
        
        return convertView;
    }
 
    
	protected void HandleUserOptionToAttend(final ChurchEvents event,final String willAttend,final Button willAttendBtn, TextView like_statement) {
		// TODO Auto-generated method stub
		AlertDialog.Builder build = new Builder(this.activity);
		
		
				build.setTitle("Program Attendance")
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						//(new WillAttendProgram()).execute(string);
						handleProgramAttend(event, willAttend, willAttendBtn);
					}
				})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					}
				});
				if( willAttend.equalsIgnoreCase("Will Attend Program"))
				{
					build.setMessage("Are you going for the program? \n Prior to the event you will be notified.\n " +
							"Do you want to continue?");
				}
				else
				{
					build.setMessage("Are you sure of this request?");
				}
				
				build.show()
				;
	}
	
	
	
	void handleProgramAttend(final ChurchEvents event, final String  willAttend,final Button willAttendBtn)
	{
		
	//This function will handle when a user click on the like button. 
	// If the user already likes the testimony, we assumes click it again renders it not-liked.
	        	 StringRequest jsonReq = new  StringRequest(Method.POST,
	                   Connector.URL, createMyReqSuccessListener(event, willAttendBtn),createMyReqErrorListener())
	        	    {
	 		        @Override
	 		        protected Map<String, String> getParams() {
	 		       Map<String, String> params = new HashMap<String, String>();    
	 		         
	 		           
	 		        	  params.put("reason",  willAttend);
	 		           
	 					
	 		        	 params.put("UID", Connector.getUserId());
	 		        	params.put("EID", event.getEventID());
	 		        	params.put("CID", Connector
	 								.getChurchID());
	 		            
	 		            return params;
	 		        }}
	        	 ;
	            InputStream keyStore = Connector.context.getResources().openRawResource(R.raw.appsol);
	            
	            
	            RequestQueue queue = Volley.newRequestQueue(Connector.context,
                        new ExtHttpClientStack(new SslHttpClient(keyStore, "qwerty")));
	            // Adding request to volley request queue
	            queue.add(jsonReq);
	        }
	
	
	 private Response.Listener<String> createMyReqSuccessListener(final ChurchEvents testimony,final Button willAttend) {
	        return new Response.Listener<String>() {
	        	
	        	
	        	
	        	
	        	
	            @Override
	            public void onResponse(String response) {
	            	
	            	
	            	//Integer likes= Integer.parseInt(testimony.getLikes());
	            	//
	            	Toast.makeText(Connector.context, response.trim(), Toast.LENGTH_LONG).show();
	            	if(response.trim().equalsIgnoreCase("Record saved. You will be notified with other details of this program later."))
					{
						
						SubacribedEvents sevent= new SubacribedEvents();
						sevent.setEventID(testimony.getEventID());
						sevent.setEventJson(testimony.getJson());
						sevent.setTitle(testimony.getTitle());
						sevent.setDate_string(testimony.getStart_date());
						Connector.dbhelper = new DBHelper(Connector.context);
						Connector.dbhelper.addEvent(sevent);
						Connector.dbhelper.close();
						//DBHelper db= new 
						willAttend.setText("Cancel Request");
						testimony.setIsGoing("1");
						
						
					}
					else
					{
						//deleteEvent
						Connector.dbhelper = new DBHelper(Connector.context);
						Connector.dbhelper.deleteEvent(testimony.getEventID());
						Connector.dbhelper.close();
						willAttend.setText("Will Attend");
						testimony.setIsGoing("0");
						
					}
	            	Integer likes= Integer.parseInt(testimony.getUsersAttending());
	            	if(!testimony.getIsGoing().equalsIgnoreCase("0"))
	 		           {
	 		        	 //testimony.setIsGoing("0");
	 		        	 
	 		        		 likes=likes+1;
	 		        	 
	 		        	 
	 		           }
	 		           else
	 		           {
	 		        	 // testimony.setIsGoing(("1"));
	 		        	   if(likes>0)
	 		        	 likes=likes-1;
	 		        	   else
	 		        		   likes=0;
	 		           }
	            	 testimony.setUsersAttending(likes.toString());
	               	
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