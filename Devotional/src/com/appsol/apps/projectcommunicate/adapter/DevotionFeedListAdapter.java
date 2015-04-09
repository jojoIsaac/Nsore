package com.appsol.apps.projectcommunicate.adapter;

import github.ankushsachdeva.emojicon.EmojiconEditText;
import github.ankushsachdeva.emojicon.EmojiconsPopup;
import github.ankushsachdeva.emojicon.EmojiconGridView.OnEmojiconClickedListener;
import github.ankushsachdeva.emojicon.EmojiconsPopup.OnEmojiconBackspaceClickedListener;
import github.ankushsachdeva.emojicon.EmojiconsPopup.OnSoftKeyboardOpenCloseListener;
import github.ankushsachdeva.emojicon.emoji.Emojicon;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupWindow.OnDismissListener;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.appsol.apps.devotional.AppController;
import com.appsol.apps.devotional.DevotionDetailActivity;
import com.appsol.apps.devotional.DevotionDiscussions;
import com.appsol.apps.devotional.R;
import com.appsol.apps.devotional.UILApplication;
import com.appsol.apps.projectcommunicate.classes.Connector;
import com.appsol.apps.projectcommunicate.classes.Devotion;
import com.appsol.apps.projectcommunicate.model.Bookmark;
import com.appsol.apps.projectcommunicate.model.Testimony;
import com.appsol.volley.classes.ExtHttpClientStack;
import com.appsol.volley.classes.SslHttpClient;
import com.appsol.volley.classes.Volley;

import com.shamanland.facebook.likebutton.FacebookLikeBox;

public class DevotionFeedListAdapter extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private List<Devotion> feedItems;
	Context context;
	ImageLoader imageLoader =  com.appsol.volley.classes.ImageCacheManager.getInstance().getImageLoader();

	public DevotionFeedListAdapter(Activity activity, List<Devotion> feedItems) {
		this.activity = activity;
		context = activity.getBaseContext();
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
			convertView = inflater.inflate(R.layout.devotionhistory_feed_item,
					null);

		if (imageLoader == null)
			imageLoader = AppController.getInstance().getImageLoader();
		LinearLayout lvDate;
		TextView like_statement = (TextView) convertView
				.findViewById(R.id.like_statement);
		TextView devotiontitle = (TextView) convertView
				.findViewById(R.id.devotiontitle);
		TextView timestamp = (TextView) convertView
				.findViewById(R.id.timestamp);
		TextView statusMsg = (TextView) convertView
				.findViewById(R.id.txtStatusMsg);

		TextView txtchurchname = (TextView) convertView
				.findViewById(R.id.txtchurchname), txtDate = (TextView) convertView
				.findViewById(R.id.txtdate), tvc = (TextView) convertView
				.findViewById(R.id.txtday);
		;
		lvDate = (LinearLayout) convertView.findViewById(R.id.lvDate);
		// Handle the attendance button
				final Button willAttend = (Button) convertView
						.findViewById(R.id.willAttend);
				final Button shareTestimony = (Button) convertView
						.findViewById(R.id.shareTestimony);
				final FacebookLikeBox fbox = (FacebookLikeBox) convertView
						.findViewById(R.id.likebox);
				
				final Button comment_devotion =(Button) convertView.findViewById(R.id.comment_devotion);
				
		final Devotion item = feedItems.get(position);
		devotiontitle.setText(item.getTitle().trim());
		// name.setText(item.getTitle());
		timestamp.setText(" "+item.getRawDate());

		txtDate.setText(item.getNormalDevotionDate());
		// TextView tvc=(TextView) row.findViewById(R.id.txtday);
		tvc.setText(item.getDevotionday());

		String verse = item.getVerse().replace(" ", "");

		devotiontitle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				handleUserClick(item);
			}
		});
		Integer likes = Integer.parseInt(item.getDevotionLikes().trim());
		Integer userlike = Integer.parseInt(item.getUserLike());
		if (!item.getDevotionLikes().equalsIgnoreCase("0")) {

			if (userlike > 0) {
				if (likes == 1) {
					like_statement.setText("You like this");
				} else if (likes == 2) {
					like_statement.setText("You and " + (likes - 1)
							+ " other user like this");
				} else if (likes > 2) {
					like_statement.setText("You and " + (likes - 1)
							+ " other users like this");
				}
				like_statement.setVisibility(View.VISIBLE);
				willAttend.setText("Unlike");
			} else {
				if (likes == 1) {
					like_statement.setText(1 + " user like this");
				} else {
					like_statement.setText((likes) + "  users like this");
				}
				willAttend.setText("Like");
				like_statement.setVisibility(View.GONE);
			}

			// like_statement.setVisibility(View.VISIBLE);
		} else {
			like_statement.setVisibility(View.GONE);
		}

		if (!TextUtils.isEmpty(item.getChurchName())) {
			txtchurchname.setText(" @ " + item.getChurchName());
			txtchurchname.setVisibility(View.VISIBLE);
		} else {
			// status is empty, remove from view
			txtchurchname.setVisibility(View.GONE);
		}

		// Chcek for empty status message
		if (!TextUtils.isEmpty(item.getVerse())) {
			int length = item.getVerse().length();

			if (length <= 300)
				statusMsg.setText(item.getVerse());
			else
				statusMsg.setText(item.getVerse().subSequence(0, 299) + " ...");
			statusMsg.setVisibility(View.VISIBLE);
		} else {
			// status is empty, remove from view
			statusMsg.setVisibility(View.GONE);
		}
		statusMsg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				handleUserClick(item);
			}
		});

		
		
		if(item.getComments().equalsIgnoreCase("0"))
		comment_devotion.setText("Comment");
		else
			comment_devotion.setText(item.getComments());

		if (!item.getDevotionLikes().trim().equalsIgnoreCase("0")) {
			fbox.setText(item.getDevotionLikes());
			fbox.setVisibility(View.VISIBLE);
		} else {
			fbox.setText(item.getUserLike());
			fbox.setVisibility(View.GONE);
		}

		willAttend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				handleTestimonyLike(item);

			}
		});

		shareTestimony.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String data = Connector.getChurchName() + "" + "\n "
						+ item.getTitle() + "\n" + "Memory Verse: "
						+ item.getVerse() + "\n"
						+ "- Register and access more at: " + ""
						+ Connector.parentURL;
				Connector.shareText(Connector.context, data);

			}
		});

		lvDate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				handleUserClick(item);
			}
		});

		/// Handle the showing of the pop up window
		comment_devotion.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				handleComment(item);
			}
		});
		return convertView;
	}

	protected void handleComment(Devotion item) {
		// TODO Auto-generated method stub
		Connector.handleComment(item);
	}

	void handleTestimonyLike(final Devotion testimony) {

		// This function will handle when a user click on the like button.
		// If the user already likes the testimony, we assumes click it again
		// renders it not-liked.
		StringRequest jsonReq = new StringRequest(Method.POST,
				"https://nsoredevotional.com/mobile/eventsFetch.php",
				createMyReqSuccessListener(testimony),
				createMyReqErrorListener()) {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				params.put("TID", testimony.getLesseonid());
				// Toast.makeText(context, testimony.getLesseonid(),
				// Toast.LENGTH_LONG).show()
				if (!testimony.getUserLike().equalsIgnoreCase("0")) {
					params.put("reason", "UnLike Devotional");
				} else {
					params.put("reason", "Like Devotional");
				}

				params.put("USER_ID", Connector.getUserId());
				params.put("UN", Connector.getUserDetails().get(1));
				params.put("DID", testimony.getLesseonid());

				return params;
			}
		};
		InputStream keyStore = Connector.context.getResources()
				.openRawResource(R.raw.appsol);

		RequestQueue queue = Volley.newRequestQueue(Connector.context,
				new ExtHttpClientStack(new SslHttpClient(keyStore, "qwerty")));
		// Adding request to volley request queue
		queue.add(jsonReq);
	}

	private Response.Listener<String> createMyReqSuccessListener(
			final Devotion testimony) {
		return new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {

				Integer likes = Integer.parseInt(testimony.getDevotionLikes());
				if (!testimony.getUserLike().equalsIgnoreCase("0")) {
					testimony.setUserLike("0");
					if (likes > 0) {
						likes = likes - 1;
					}

				} else {
					testimony.setUserLike("1");
					likes = likes + 1;
				}
				testimony.setDevotionLikes(likes.toString());

				notifyDataSetChanged();
			}
		};
	}

	private Response.ErrorListener createMyReqErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				
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

	void handleUserClick(Devotion devotion) {
		Intent intent = new Intent(Connector.context,
				DevotionDetailActivity.class);
		intent.putExtra("DEVOTION-JSON", devotion.getDevotionJson());
		intent.putExtra("LID", devotion.getLesseonid());
		intent.putExtra("DID", devotion.getDaily_guide_id());
		if (Bookmark.checkAlreadyBookedMarked(devotion.getLesseonid())) {
			intent.putExtra("HIDE-BOOOKMARK", "1");
		} else
			intent.putExtra("HIDE-BOOOKMARK", "0");
		intent.putExtra("From-New", "Yes");
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

	
}