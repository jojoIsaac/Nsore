package com.appsol.apps.projectcommunicate.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.appsol.apps.devotional.AppController;
import com.appsol.apps.devotional.R;
import com.appsol.apps.projectcommunicate.classes.CircularNetworkImageView;
import com.appsol.apps.projectcommunicate.classes.Connector;
import com.appsol.apps.projectcommunicate.model.DevotionComment;
import com.shamanland.facebook.likebutton.FacebookLikeBox;
public class DevotionCommentFeedListAdapter extends BaseAdapter {  
    private Activity activity;
    private LayoutInflater inflater;
    private List<DevotionComment> feedItems;
    Context context;
    ImageLoader imageLoader =  com.appsol.volley.classes.ImageCacheManager.getInstance().getImageLoader();
	
 
    public DevotionCommentFeedListAdapter(Activity activity, List<DevotionComment> feedItems) {
        this.activity = activity;
        context=activity.getBaseContext();
        this.feedItems = feedItems;
    }
 
    @Override
    public int getCount() {
        return feedItems.size();
    }
 
    @Override
    public DevotionComment getItem(int location) {
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
            convertView = inflater.inflate(R.layout.devotion_comments_feed_item, null);
 
        if (imageLoader == null)
            imageLoader =com.appsol.volley.classes.ImageCacheManager.getInstance().getImageLoader();
        	
        TextView like_statement=(TextView) convertView.findViewById(R.id.like_statement);
        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView timestamp = (TextView) convertView
                .findViewById(R.id.timestamp);
        TextView statusMsg = (TextView) convertView
                .findViewById(R.id.txtStatusMsg);
        //TextView url = (TextView) convertView.findViewById(R.id.txtUrl);
        CircularNetworkImageView profilePic = (CircularNetworkImageView) convertView
                .findViewById(R.id.profilePic);
       // TextView txttitle= (TextView) convertView.findViewById(R.id.txttitle);
//        FeedImageView feedImageView = (FeedImageView) convertView
//                .findViewById(R.id.feedImage1);
        TextView txtchurchname=(TextView) convertView.findViewById(R.id.txtchurchname);
        

        final DevotionComment item = feedItems.get(position);
 
        name.setText(item.getName());
        name.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showProfile(item);
			}
		});
        timestamp.setText(" "+item.getTimeStamp());

        //Hide all UIs not needed
        like_statement.setVisibility(View.GONE);
        txtchurchname.setVisibility(View.GONE);
    
        final Button willAttend = (Button) convertView.findViewById(R.id.willAttend);
        final Button shareTestimony=(Button)convertView.findViewById(R.id.shareTestimony);
        final FacebookLikeBox fbox= (FacebookLikeBox) convertView.findViewById(R.id.likebox);
        
        shareTestimony.setVisibility(View.GONE);
        willAttend.setVisibility(View.GONE);
        fbox.setVisibility(View.GONE);
       
        
        // Chcek for empty status message
        if (!TextUtils.isEmpty(item.getComment())) {
        	int length= item.getComment().length();
        	
        	if(length<=300)
            statusMsg.setText(item.getComment());
        	else 
        		statusMsg.setText(item.getComment().subSequence(0, 299)+" ...");
            statusMsg.setVisibility(View.VISIBLE);
        } else {
            // status is empty, remove from view
            statusMsg.setVisibility(View.GONE);
        }
        profilePic.setImageUrl(null, imageLoader);
        profilePic.setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.mobile));

        String url = Connector.imageURL + item.getProfilePic(); 
        if(!url.equalsIgnoreCase(Connector.imageURL))
		{
        	  profilePic.setImageUrl(Connector.imageURL+item.getProfilePic(), imageLoader);
		}
 
else
	profilePic.setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.mobile));

 profilePic.setOnClickListener( new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		showProfile(item);
	}
});
        

     
        
        return convertView;
    }
	   
	   
	   protected void showProfile(DevotionComment item) {
		// TODO Auto-generated method stub
		Connector.handleUserProfile(item, context, false);
	}

	public void remove(int location)
	   {
		   DevotionComment comment = getItem(location);
		   if(comment!=null)
		   {

			   this.feedItems .remove(location);
			   notifyDataSetChanged();
		   }
	   }
	   
	   
	   
	   
	   
	   
	   private int mDeviceHeight;
		private PopupWindow popWindow;
	   @SuppressLint("NewApi") 
	   public void onShowPopup(View v){
	    
	   LayoutInflater layoutInflater = (LayoutInflater)Connector.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    
	   // inflate the custom popup layout
	   final View inflatedView = layoutInflater.inflate(R.layout.fragment_chat_ui, null,false);
	   // find the ListView in the popup layout
	   ListView listView = (ListView)inflatedView.findViewById(R.id.lv);
	    
	   // get device size
	   Display display = activity.getWindowManager().getDefaultDisplay();
	   final Point size = new Point();
	   display.getSize(size);
	   mDeviceHeight = size.y;
	    
       // set height depends on the device size
	   popWindow = new PopupWindow(inflatedView, size.x - 50,size.y - 400, true );
	   // set a background drawable with rounders corners
	   popWindow.setBackgroundDrawable(Connector.context.getResources().getDrawable(R.drawable.fb_popup_bg));
	   // make it focusable to show the keyboard to enter in `EditText`
	   popWindow.setFocusable(true);
	   // make it outside touchable to dismiss the popup window
	   popWindow.setOutsideTouchable(true);
	    
	   // show the popup at bottom of the screen and set some margin at bottom ie,
	   popWindow.showAtLocation(v, Gravity.BOTTOM, 0,100);
	   }

	   
	   
	   
	   private void changeEmojiKeyboardIcon(ImageView iconToBeChanged, int drawableResourceId){
			iconToBeChanged.setImageResource(drawableResourceId);
		}
	   
	private void setSimpleList(ListView listView) {
		// TODO Auto-generated method stub
		
	}

	   
	   
	   
	   
	   
	   
    
}