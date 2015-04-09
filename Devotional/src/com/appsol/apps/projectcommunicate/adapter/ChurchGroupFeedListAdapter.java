package com.appsol.apps.projectcommunicate.adapter;

import github.ankushsachdeva.emojicon.EmojiconEditText;
import github.ankushsachdeva.emojicon.EmojiconGridView.OnEmojiconClickedListener;
import github.ankushsachdeva.emojicon.EmojiconsPopup;
import github.ankushsachdeva.emojicon.EmojiconsPopup.OnEmojiconBackspaceClickedListener;
import github.ankushsachdeva.emojicon.EmojiconsPopup.OnSoftKeyboardOpenCloseListener;
import github.ankushsachdeva.emojicon.emoji.Emojicon;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.appsol.apps.devotional.ChurchGroupsActivity;
import com.appsol.apps.devotional.DevotionDetailActivity;
import com.appsol.apps.devotional.R;
import com.appsol.apps.projectcommunicate.classes.CircularNetworkImageView;
import com.appsol.apps.projectcommunicate.classes.Connector;
import com.appsol.apps.projectcommunicate.classes.Devotion;
import com.appsol.apps.projectcommunicate.model.Bookmark;
import com.appsol.apps.projectcommunicate.model.Church;
import com.appsol.apps.projectcommunicate.model.ChurchGroup;
import com.appsol.apps.projectcommunicate.model.NsoreDevotionalUser;
import com.appsol.volley.classes.ExtHttpClientStack;
import com.appsol.volley.classes.SslHttpClient;
import com.appsol.volley.classes.Volley;
import com.google.gson.JsonObject;

public class ChurchGroupFeedListAdapter extends ArrayAdapter<ChurchGroup> {
	private Activity activity;
	private LayoutInflater inflater;
	private List<ChurchGroup> feedItems;
	Context context;
	ImageLoader imageLoader =  com.appsol.volley.classes.ImageCacheManager.getInstance().getImageLoader();
	private boolean mInError;
    private boolean isMine;
	public ChurchGroupFeedListAdapter(Activity activity,Integer resource, List<ChurchGroup> feedItems) {
		super(activity, resource, feedItems);
		this.activity = activity;
		context = activity;
		this.feedItems = feedItems;
		
	}
	
	public ChurchGroupFeedListAdapter(Activity activity,Integer resource, List<ChurchGroup> feedItems,boolean isMine) {
		super(activity, resource, feedItems);
		this.activity = activity;
		context = activity;
		this.feedItems = feedItems;
		this.isMine= isMine;
	}

	@Override
	public int getCount() {
		return feedItems.size();
	}

	@Override
	public ChurchGroup getItem(int location) {
		return feedItems.get(location);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	private static class ViewHolder {
		TextView name;
		TextView txtchurchname;
		CircularNetworkImageView profilePic;
		TextView txtMutualFriends,txttotal;
		TextView timestamp;
		Button btnConnect;
		
	}
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		final ViewHolder holder;
		final ChurchGroup item = feedItems.get(position);
		
		if (convertView == null) {
			view = activity.getLayoutInflater().inflate(
					R.layout.friends_list_feed_item, parent, false);
			holder = new ViewHolder();
			
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
			//holder.profilePic.setImageUrl(null, imageLoader);
		}
		
		holder.name=(TextView) view.findViewById(R.id.name);
		holder.txtchurchname=(TextView) view.findViewById(R.id.txtchurchname);
		holder.txtMutualFriends=(TextView) view.findViewById(R.id.txtMutualFriends);
		holder.profilePic = (CircularNetworkImageView) view
                .findViewById(R.id.profilePic);
		holder.timestamp =(TextView) view.findViewById(R.id.timestamp);
		holder.btnConnect =(Button) view.findViewById(R.id.btnConnect);
		holder.txttotal=(TextView) view.findViewById(R.id.txttotal);
		holder.profilePic.setImageUrl(null, imageLoader);
		if(item!=null)
		{
			holder.profilePic.setImageUrl(null, imageLoader);
			holder.name.setText(item.getGroupName());
			holder.txtchurchname.setText(item.getchurchname());
		
			
			if(item.getGroupType().equalsIgnoreCase("opened_to_other_churches")
					||item.getGroupType().equalsIgnoreCase("opened"))
			{
				holder.txtMutualFriends.setText("Type: Opened");
			}
			else
			{
				holder.txtMutualFriends.setText("Type: Closed");
			}
			holder.btnConnect.setVisibility(View.VISIBLE);
			holder.txttotal.setVisibility(View.VISIBLE);
			holder.txttotal.setText("Total Members: "+item.getMembersCount());
			String Mstatus= item.getMembershipstatus().trim();
			  if(Mstatus.equalsIgnoreCase("accepted"))
			  {
				  holder.btnConnect.setText("Leave");
				  Drawable img = getContext().getResources().getDrawable( R.drawable.ic_nsore_heart);
				  holder.btnConnect.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
				 
				  item.setOperationReason("Leave Group");
			  }
			  else if(Mstatus.equalsIgnoreCase("NA") || TextUtils.isEmpty(Mstatus))
			  {
				    holder.btnConnect.setText("Join");
				    Drawable img = getContext().getResources().getDrawable( R.drawable.ic_nsore_add );
					  holder.btnConnect.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
					 
				    item.setOperationReason("Join Group");
			  }
			  else if(Mstatus.equalsIgnoreCase("pending"))
			  {
				  holder.btnConnect.setText("Cancel Request");
				  Drawable img = getContext().getResources().getDrawable( R.drawable.ic_nsore_cancel );
				  holder.btnConnect.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
				  item.setOperationReason("Cancel Group Request");
			  }
			 
			  else
			  {
				  holder.btnConnect.setText("Join"); 
				  item.setOperationReason("Join Group");
				  Drawable img = getContext().getResources().getDrawable( R.drawable.ic_nsore_add );
				  holder.btnConnect.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
				 
			  }
			  
			
		
		
				holder.timestamp.setText("Created: "+item.getDateCreated());
				String grouplogo= item.getGrouplogo();
				
			  String url ="";
			  
			  if(TextUtils.isEmpty(grouplogo.trim()))
			  {
				 url = Connector.ChurchLogoURL + item.getchurchlogo(); 
			  }
			  else
			  {
				   url =  Connector.churchGrouplogo+item.getGrouplogo();
			  }
			  holder.profilePic.setImageUrl(null, imageLoader);
			  //holder. profilePic.setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.mobile));
		        if(!url.equalsIgnoreCase(Connector.imageURL) &&!url.equalsIgnoreCase(Connector.churchGrouplogo) && !url.equalsIgnoreCase(Connector.ChurchLogoURL))
				{
		        	if (imageLoader == null)
		    			imageLoader = com.appsol.volley.classes.ImageCacheManager.getInstance().getImageLoader();
		        	holder. profilePic.setImageUrl(  url, imageLoader);
				}
		 
		else
			holder.profilePic.setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.mobile));
		
		
		   holder.profilePic.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				handleComment(item);
			}
		});
		   holder.name.setOnClickListener( new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					handleComment(item);
				}
			});
		   holder.txtMutualFriends.setOnClickListener( new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					handleComment(item);
				}
			});
		  holder.txtchurchname.setOnClickListener( new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					handleComment(item);
				}
			});
		
		}
		
		holder.btnConnect.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(item.getOperationReason().equalsIgnoreCase("Leave Group") || item.getOperationReason().equalsIgnoreCase("Cancel Request"))
				{
					AlertDialog.Builder b= new Builder(activity);
					b.setMessage(item.getOperationReason())
					
				
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
							 handleServerRequest(item);
						}
					})
					.show();
				}
				else
				{
					 handleServerRequest(item);
				}
				
				
			}
		});
		
		
	
		

		 holder.btnConnect.setText("");

		return view ;
	}
	
	private void handleServerRequest(final ChurchGroup user) {
		
		String URL_FEED = "https://nsoredevotional.com/mobile/devotionHandler.php";
		StringRequest jsonReq = new StringRequest(Method.POST, URL_FEED,
				createMyReqSuccessListener(user), createMyReqErrorListener()) {
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
		InputStream keyStore = Connector.context.getResources().openRawResource(R.raw.appsol);

		RequestQueue queue = Volley.newRequestQueue(Connector.context,
				new ExtHttpClientStack(new SslHttpClient(keyStore, "qwerty")));
		// Adding request to volley request queue
		queue.add(jsonReq);
	}
	
	private Response.Listener<String> createMyReqSuccessListener(final ChurchGroup user) {
		return new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				
				
				if (!response.equalsIgnoreCase("No Testimony Shared.")) {
					
					try
					{
					JSONObject object = new JSONObject(response);
					if(object!=null)
					{
						//{"response":"saved","count":"4"}
					  String 	results=  object.optString("response","Err");
					  String count= object.optString("count", "0");
					  
					  
					  

						if(results.trim().contains("Waiting"))
						{
							  //holder.btnConnect.setText("Send Request");
							
							    user.setOperationReason("Cancel Request");
							    user.setMembershipstatus("pending");
							    notifyDataSetChanged();
						}
						else if(results.trim().contains("saved") || results.trim().contains("Already a member"))
						{
							  //holder.btnConnect.setText("Send Request");
							    user.setOperationReason("Already Member");
							    user.setMembershipstatus("accepted");
							    user.setMembersCount((Integer.parseInt(user.getMembersCount())+1)+"");
							    notifyDataSetChanged();
						} 
						else if(results.trim().contains("Request Sent"))
						{
							  user.setOperationReason("Cancel Request");
							  user.setMembershipstatus("pending");
							    notifyDataSetChanged();
						}
						else if(results.trim().contains("Left"))
						{
							  user.setOperationReason("Cancel Request");
							  user.setMembershipstatus("NA");
							    notifyDataSetChanged();
						}
						else 
						{
							if(user.getMembersCount().equalsIgnoreCase("0"))
							{
								user.setMembersCount("0");
							}
							else
							{
								 user.setMembersCount((Integer.parseInt(user.getMembersCount())-1)+"");
							}
							 user.setMembershipstatus("NA");
							    notifyDataSetChanged();
						}
						user.setMembersCount(count);
					}
					
					}
					catch(Exception e )
					{
						
					}
					
					
					
				}
			}
		};
	}

	private Response.ErrorListener createMyReqErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				
				if(error!=null)
				{
				showErrorDialog(Connector.HandleVolleyerror(error, context));
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

		AlertDialog.Builder b = new AlertDialog.Builder(context);
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
	protected void handleComment(ChurchGroup item) {
		// TODO Auto-generated method stub
		//Connector.handleUserProfile(item, Connector.context, false);
		
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
		
	}

	
	

	
	
	
	
	
	
	
	// call this method when required to show popup
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
	    
	    
	   // fill the data to the list items
	   setSimpleList(listView);
	   // Set other UI Components
	   final EmojiconEditText emojiconEditText = (EmojiconEditText) inflatedView.findViewById(R.id.emojicon_edit_text);
		final View rootViews = inflatedView.findViewById(R.id.root_view);
		final ImageView emojiButton = (ImageView) inflatedView.findViewById(R.id.emoji_btn);
		final ImageView submitButton = (ImageView) inflatedView.findViewById(R.id.submit_btn);

		// Give the topmost view of your activity layout hierarchy. This will be used to measure soft keyboard height
		final EmojiconsPopup popup = new EmojiconsPopup(rootViews, Connector.context);

		//Will automatically set size according to the soft keyboard size        
		popup.setSizeForSoftKeyboard();

		//Set on emojicon click listener
		popup.setOnEmojiconClickedListener(new OnEmojiconClickedListener() {

			@Override
			public void onEmojiconClicked(Emojicon emojicon) {
				emojiconEditText.append(emojicon.getEmoji());
			}
		});

		//Set on backspace click listener
		popup.setOnEmojiconBackspaceClickedListener(new OnEmojiconBackspaceClickedListener() {

			@Override
			public void onEmojiconBackspaceClicked(View v) {
				KeyEvent event = new KeyEvent(
						0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
				emojiconEditText.dispatchKeyEvent(event);
			}
		});

		//If the emoji popup is dismissed, change emojiButton to smiley icon
		popup.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				changeEmojiKeyboardIcon(emojiButton, R.drawable.smiley);
			}
		});
	

		//If the text keyboard closes, also dismiss the emoji popup
		popup.setOnSoftKeyboardOpenCloseListener(new OnSoftKeyboardOpenCloseListener() {

			@Override
			public void onKeyboardOpen(int keyBoardHeight) {

			}

			@Override
			public void onKeyboardClose() {
				if(popup.isShowing())
					popup.dismiss();
			}
		});

		//On emoji clicked, add it to edittext
		popup.setOnEmojiconClickedListener(new OnEmojiconClickedListener() {

			@Override
			public void onEmojiconClicked(Emojicon emojicon) {
				emojiconEditText.append(emojicon.getEmoji());
			}
		});

		//On backspace clicked, emulate the KEYCODE_DEL key event
		popup.setOnEmojiconBackspaceClickedListener(new OnEmojiconBackspaceClickedListener() {

			@Override
			public void onEmojiconBackspaceClicked(View v) {
				KeyEvent event = new KeyEvent(
						0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
				emojiconEditText.dispatchKeyEvent(event);
			}
		});
		
		// To toggle between text keyboard and emoji keyboard keyboard(Popup)
		emojiButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				//If popup is not showing => emoji keyboard is not visible, we need to show it
				if(!popup.isShowing()){
					
					//If keyboard is visible, simply show the emoji popup
					if(popup.isKeyBoardOpen()){
						popup.showAtBottom();
						changeEmojiKeyboardIcon(emojiButton, R.drawable.ic_action_keyboard);
					}
					
					//else, open the text keyboard first and immediately after that show the emoji popup
					else{
						emojiconEditText.setFocusableInTouchMode(true);
						emojiconEditText.requestFocus();
						popup.showAtBottomPending();
						final InputMethodManager inputMethodManager = (InputMethodManager) Connector.context.getSystemService(Context.INPUT_METHOD_SERVICE);
						inputMethodManager.showSoftInput(emojiconEditText, InputMethodManager.SHOW_IMPLICIT);
						changeEmojiKeyboardIcon(emojiButton, R.drawable.ic_action_keyboard);
					}
				}
				
				//If popup is showing, simply dismiss it to show the undelying text keyboard 
				else{
					popup.dismiss();
				}
			}
		});	

	    
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

	
	private class AppFilter<T> extends Filter {

		private ArrayList<T> sourceObjects;

		public AppFilter(List<T> objects) {
			sourceObjects = new ArrayList<T>();
			synchronized (this) {
				sourceObjects.addAll(objects);
			}
		}

		@Override
		protected FilterResults performFiltering(CharSequence chars) {
			String filterSeq = chars.toString().toLowerCase();
			FilterResults result = new FilterResults();
			if (filterSeq != null && filterSeq.length() > 0) {
				ArrayList<T> filter = new ArrayList<T>();

				for (T object : sourceObjects) {
					// the filtering itself:
					if (object.toString().toLowerCase().contains(filterSeq))
						filter.add(object);
				}
				result.count = filter.size();
				result.values = filter;
			} else {
				// add all objects
				synchronized (this) {
					result.values = sourceObjects;
					result.count = sourceObjects.size();
				}
			}
			return result;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			// NOTE: this function is *always* called from the UI thread.
			ArrayList<T> filtered = (ArrayList<T>) results.values;
			notifyDataSetChanged();
			clear();
			for (int i = 0, l = filtered.size(); i < l; i++)
				add((ChurchGroup) filtered.get(i));
			notifyDataSetInvalidated();
		}
	}
	
}