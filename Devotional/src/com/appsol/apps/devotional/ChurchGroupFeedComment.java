package com.appsol.apps.devotional;


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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.appsol.apps.devotional.DevotionalHistoryFeedActivity.EndlessScrollListener;
import com.appsol.apps.projectcommunicate.adapter.DevotionCommentFeedListAdapter;
import com.appsol.apps.projectcommunicate.adapter.DevotionFeedListAdapter;
import com.appsol.apps.projectcommunicate.adapter.GroupCommentFeedListAdapter;
import com.appsol.apps.projectcommunicate.classes.CircularNetworkImageView;
import com.appsol.apps.projectcommunicate.classes.Connector;
import com.appsol.apps.projectcommunicate.classes.Devotion;
import com.appsol.apps.projectcommunicate.model.ChurchGroupFeeds;
import com.appsol.apps.projectcommunicate.model.DevotionComment;
import com.appsol.apps.projectcommunicate.model.GroupFeedComment;
import com.appsol.volley.classes.ExtHttpClientStack;
import com.appsol.volley.classes.SslHttpClient;
//import com.android.volley.toolbox.Volley;
import com.appsol.volley.classes.Volley;

public class ChurchGroupFeedComment extends ActionBarActivity {
	private static String URL_FEED = "https://nsoredevotional.com/mobile/devotionHandler.php";
   
    private GroupCommentFeedListAdapter listAdapter;
    private List<GroupFeedComment> feedItems;
	 private static final String TAG = ChurchGroupFeedComment.class.getSimpleName();
	 ImageLoader imageLoader =  com.appsol.volley.classes.ImageCacheManager.getInstance().getImageLoader();
		
	 
	 static Map<String, String> params;
	 private boolean mHasData = false;
	    private boolean mInError = false;
	    Integer startAT=0;
		private View rootView;

		private TextView timestamp;

		private EmojiconEditText emojiconEditText;
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
		
		
		private void loadpage2()
	    {
	    	 final int startIndex = 1 + feedItems.size();
	    	 //progressbarLoad.setVisibility(View.VISIBLE);
	    	 //https://nsoredevotional.com/mobile/eventsFetch.php?reason=Get%20Devotion%20lesson%20Comments&LID=118&index=1&MID=4
		        	URL_FEED= "https://nsoredevotional.com/mobile/devotionHandler.php";
		        	 StringRequest jsonReq = new  StringRequest(Method.POST,
		                    URL_FEED, createMyReqSuccessListener(),createMyReqErrorListener())
		        	    {
		 		        @Override
		 		        protected Map<String, String> getParams() {
		 		       Map<String, String> params = new HashMap<String, String>();
		 		        	 params.put("LID", devotionID);
			 		            params.put("MID", Connector.getUserId());
				 		        	params.put("reason", "Get Group Feed Comments");
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
	 
		 private Response.Listener<String> createMyReqSuccessListener() {
		        return new Response.Listener<String>() {
		        	
		        	
		        	
		        	
		        	
		            @Override
		            public void onResponse(String response) {
		            	
		            	if(feedItems.size()<=0){
		            	Toast.makeText(Connector.context, response, Toast.LENGTH_LONG).show();
		            	}
		            	if(!response.trim().equalsIgnoreCase("No Comment Found."))
	        			{
		            		parseJsonFeed(response);
	        			}
		            }
		        };
		 }
		
		 private Response.ErrorListener createMyReqErrorListener() {
				return new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						progressbarLoad.setVisibility(View.GONE);
						if(error!=null)
						{
						showErrorDialog(Connector.HandleVolleyerror(error, ChurchGroupFeedComment.this),1);
						if(error.getMessage()!=null)
						Log.d("RES", error.getMessage());
						}
						else
						{
							
							showErrorDialog("An error occured. Please check your internet Connection",1);
						}
						
					}
				};
			}

			private void showErrorDialog(String error,final Integer instance) {
				mInError = true;
				//Toast.makeText(DevotionDiscussions.this,instance.toString(), Toast.LENGTH_LONG).show();
				
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
						if(instance<2)
						{
						loadpage2();
						}
						else
						{
							String newText = emojiconEditText.getText().toString();
							
//							mAdapter.add(newText);
//							mAdapter.notifyDataSetChanged();
							if(!TextUtils.isEmpty(newText))
							{
								//Toast.makeText(DevotionDiscussions.this,newText, Toast.LENGTH_LONG).show();
								
							saveDataOnServer(newText);
							}
							
							
							
						}
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
			public void onSaveInstanceState(Bundle outState) {
				// TODO Auto-generated method stub
				super.onSaveInstanceState(outState);
				
				outState.putString("UID", devotionID);
			}
			
		   
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_chat_ui_withuserdp);
		Connector.context= this;
		if(savedInstanceState!=null)
		{
			devotionID = savedInstanceState.getString("UID");
		}
		
		
		setUpUI(savedInstanceState);
	}
//
	/*
	 * We pass the devotion title and other details to this class through intent the UI to 
	 */
	
	///
	 private static String devotionTitle="";
	    private static String devotionDate="";
	    private static String devotionID="-1";
	    private static String devotionDay="";
	    private static String devotionRawDate="";
	    private static String username="",userdp="";
	    
	    /*
	     * commentIntent.putExtra("title", feeds.getTitle());
		commentIntent.putExtra("feedid", feeds.getFeedID());
		commentIntent.putExtra("content",feeds.getDescription());
		commentIntent.putExtra("date", feeds.getDate_sent());
		//commentIntent.putExtra("rawdate", feeds.getRawDate());
		commentIntent.putExtra("title", feeds.getTitle());
		commentIntent.putExtra("UID", feeds.getMemberID());
		commentIntent.putExtra("name", feeds.getName());
		commentIntent.putExtra("churchname", feeds.getChurchname());
		commentIntent.putExtra("churchid", feeds.getChurchID());
		commentIntent.putExtra("userdevice", feeds.getDeviceID());
		commentIntent.putExtra("membersince", feeds.getMemberSince());
		commentIntent.putExtra("connected", feeds.getIsConnected());
		commentIntent.putExtra("userdp", feeds.getProfilePic());
		commentIntent.putExtra("cover", feeds.getUserCover());
		commentIntent.putExtra("mutual", feeds.getMutualFriends());

		commentIntent.putExtra("about",feeds.getAboutUser());
	     */
	private void setUpUI(Bundle savedInstanceState) {
		// Retrieve the data passed to the intent
		devotionDate= getIntent().getExtras().getString("date");
		devotionRawDate= getIntent().getStringExtra("date");
		devotionID= getIntent().getExtras().getString("feedid");
		devotionTitle= getIntent().getExtras().getString("name");
		devotionDay= getIntent().getExtras().getString("day");
		userdp= getIntent().getExtras().getString("userdp");
		if (getIntent().getExtras().getString("feedid")!= null && savedInstanceState==null) {
			devotionID= getIntent().getExtras().getString("feedid");
		}
		//Set the Devotion Details
	
		TextView txtdevotionTitle = (TextView) findViewById(R.id.txtdevotionTitle)
				;
		timestamp=(TextView) findViewById(R.id.timestamp);
		//final Devotion item = feedItems.get(position);
		txtdevotionTitle.setText(devotionTitle.trim());
		// name.setText(item.getTitle());
		timestamp.setText(devotionRawDate);

		CircularNetworkImageView profilePic = (CircularNetworkImageView) findViewById(R.id.profilePic);
        profilePic.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.mobile));

        String url = Connector.imageURL + userdp; 
        if(!TextUtils.isEmpty(userdp)&&!url.equalsIgnoreCase(Connector.imageURL))
		{
        	  profilePic.setImageUrl(Connector.imageURL+userdp, imageLoader);
		}
 
else
	profilePic.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.mobile));

		
		final ListView  
		 listView = (ListView)  findViewById(R.id.lv);
		 
	        feedItems = new ArrayList<GroupFeedComment >();
	 
	        listAdapter = new GroupCommentFeedListAdapter(this, feedItems);
	        listView.setAdapter(listAdapter);
	        listView.setLongClickable(true);
	       registerForContextMenu(listView);
//	        listView.setOnItemLongClickListener( new OnItemLongClickListener() {
//
//				@Override
//				public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
//						int position, long arg3) {
//					// TODO Auto-generated method stub
//					
//					}
//					return true;
//				}
//			});
	       // registerForContextMenu(listView);
	        listView.setOnScrollListener(new EndlessScrollListener());
	emojiconEditText = (EmojiconEditText) findViewById(R.id.emojicon_edit_text);
		final View rootViews = findViewById(R.id.root_view);
		final ImageView emojiButton = (ImageView) findViewById(R.id.emoji_btn);
		final ImageView submitButton = (ImageView) findViewById(R.id.submit_btn);
		 progressbarLoad=(ProgressBar) findViewById(R.id.progressbarLoad);
		// progressbarLoad.setVisibility(View.GONE);
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

		//On submit, add the edittext text to listview and clear the edittext
		submitButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String newText = emojiconEditText.getText().toString();
				
//				mAdapter.add(newText);
//				mAdapter.notifyDataSetChanged();
				if(!TextUtils.isEmpty(newText))
				{
				saveDataOnServer(newText);
				}

			}
		});
		
		StringRequest jsonReq = new  StringRequest(Method.POST,
                URL_FEED, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        VolleyLog.d(TAG, "Response: " + response.toString());
                        if (response != null) {
                            parseJsonFeed(response);
                        }
                    }
                }, createMyReqErrorListener())
    	 {
 		        @Override
 		        protected Map<String, String> getParams() {
 		         Map<String, String> params = new HashMap<String, String>();
// 		            params.put("reason", "Members Testimonies");
		            params.put("LID", devotionID);
		            params.put("MID", Connector.getUserId());
 		        	params.put("reason", "Get Group Feed Comments");
 		           
 		            return params;
 		        }}
    	 ;
        InputStream keyStore = getResources().openRawResource(R.raw.appsol);
        
        
        RequestQueue queue = Volley.newRequestQueue(this,
                new ExtHttpClientStack(new SslHttpClient(keyStore, "qwerty")));
        // Adding request to volley request queue
        queue.add(jsonReq);
		
		

	}
	private static String lastComment="";
	protected void saveDataOnServer(final String newText) {

		
		StringRequest jsonReq = new StringRequest(Method.POST,
              "https://nsoredevotional.com/mobile/devotionHandler.php", new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        VolleyLog.d(TAG, "Response: " + response.trim().toString());
                        if (response != null) {
                           // parseJsonFeed(response);
                        	if(response.trim().contains("Saved"))
                        	{
                        		loadpage2();
                        		emojiconEditText.getText().clear();
                        		lastComment="";
                        	}
                        }
                    }
                },  createMyReqErrorListener(2))
		 {
		        @Override
		        protected Map<String, String> getParams() {
		         Map<String, String> params = new HashMap<String, String>();
		            params.put("reason", "Save Feed Comment");
	            params.put("data", newText);
	            params.put("UN", Connector.getUserDetails().get(1));
		            params.put("topicid", devotionID);
		            params.put("senderid", Connector.getUserId());
		            lastComment= newText;
		            return params;
		        }}
		;

        // Adding request to volley request queue
       // AppController.getInstance().addToRequestQueue(jsonReq);
		 InputStream keyStore = getResources().openRawResource(R.raw.appsol);
            
			RequestQueue queue = Volley.newRequestQueue(Connector.context,
                    new ExtHttpClientStack(new SslHttpClient(keyStore, "qwerty")));
			  queue.add(jsonReq);
	}

	private ErrorListener createMyReqErrorListener(final int i) {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				progressbarLoad.setVisibility(View.GONE);
				if(error!=null)
				{
				showErrorDialog(Connector.HandleVolleyerror(error, ChurchGroupFeedComment.this),i);
				if(error.getMessage()!=null)
				Log.d("RES", error.getMessage());
				}
				else
				{
					
					showErrorDialog("An error occured. Please check your internet Connection",i);
				}
				
			}
		};
	}

	private void changeEmojiKeyboardIcon(ImageView iconToBeChanged, int drawableResourceId){
		iconToBeChanged.setImageResource(drawableResourceId);
	}

	
	
	   /**
     * Parsing json reponse and passing the data to feed view list adapter
     * */
    private void parseJsonFeed(String response) {
    //Toast.makeText(this, response, Toast.LENGTH_LONG).show();
    	progressbarLoad.setVisibility(View.GONE);
    	if (!response.trim()
				.contains("No Comment Found.")) {

			JSONObject objects;

			
        try {

			objects = new JSONObject(response.trim());
			if (objects != null) {
				JSONArray feedArray  = objects.getJSONArray("FeedComment");

				if (feedArray  != null) {
        
            //JSONArray feedArray = response.getJSONArray("feed");
					/*
					 * $dataItem['member_id'] = $row['member_id'];
			//echo $userID;
			$dataItem['user']=getUserDetails($dataItem['member_id'],$sss);
			$dataItem['comment_id'] = $row['comment_id'];
			$dataItem['date_sent'] = $row['date_sent'];
			$dataItem['comment'] = $row['comment'];
			$dataItem['group_post_id']= $row['group_post_id'];
					 */
 
            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);
 
              GroupFeedComment  item = new GroupFeedComment();
              String sender= feedObj.getString("member_id");
              
              if(!sender.equalsIgnoreCase("0") &&!TextUtils.isEmpty(sender))
              {
           	   
              }
              String userData= feedObj.optString("user");
              if(!TextUtils.isEmpty(userData) && userData!=null)
              {
           	   item =  processUsersListJson(userData, item); 
           	   //item.setComment(feedObj.getString("attachment"));
                  item.setComment_id(feedObj.getString("comment_id"));
                 item.setTimeStamp(feedObj.getString("date_sent"));
               item.setGroup_post_id(feedObj.getString("group_post_id"));
//                  item.setDescription(feedObj.getString("description"));
//                  item.setStatus(feedObj.getString("status"));
//                  item.setDate_sent(feedObj.getString("date_sent"));
//                  item.setLikeFeed(feedObj.getString("userliike"));
//                  item.setLikes(feedObj.getString("likes"));
                 item.setComment(feedObj.getString("comments"));
                  
                
                //startAT = 
                 
                   feedItems.add(item);
              }
              
              
              /*
               name memberID comment_id timeStamp comment profilePic
               * 
               */
              /*
              item.setGroup_post_id(feedObj.getInt("lessonID")+"");
              item.setName(feedObj.getString("name"));
              item.setMemberID(feedObj.getString("memberID"));
              item.setComment_id(feedObj.getString("comment_id"));
              item.setTimeStamp(feedObj.getString("timeStamp"));
              item.setComment(feedObj.getString("comment"));
              item.setEmail(feedObj.getString("email"));
              item.setPhone_number(feedObj.getString("phone"));
              item.setChurchname(feedObj.getString("churchName"));
              item.setChurchID(feedObj.getString("CID"));
              //item.setCurrent(false);
			  item.setProfilePic(feedObj.getString("profilePic"));
			  item.setIsConnected(feedObj.getInt("isConnected")+"");
			  item.setFriendsCount(feedObj.getInt("connection")+"");
			  item.setMutualFriends(feedObj.optString("mutualFriends",feedObj.optInt("mutualFriends", 0)+""));
			  item.setMemberSince(feedObj.getString("date_joined"));
			  item.setUserCover(feedObj.getString("cover"));
				*/
				
                 
             //startAT = 
              
                //feedItems.add(item);
            }
 
            // notify data changes to list adapater
            
            startAT= feedItems.size()+ 1;
            listAdapter.notifyDataSetChanged();
            progressbarLoad.setVisibility(View.GONE);
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
	
	
    public static  GroupFeedComment  processUsersListJson(String response, GroupFeedComment item)
    {

    	  try {
    		  
    		  JSONObject objects = new JSONObject(response.trim());
    			if (objects != null) {
    			
          
              //JSONArray feedArray = response.getJSONArray("feed");


    //justClicked=199;
             
                  JSONObject feedObj = objects;

             item = new   GroupFeedComment();
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
	
	
	
	
	
	
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		GroupFeedComment devotion = null;
		if (v.getId() == R.id.lv) {
		    ListView lv = (ListView) v;
		    AdapterView.AdapterContextMenuInfo acmi = (AdapterContextMenuInfo) menuInfo;
		 devotion = (GroupFeedComment) lv.getItemAtPosition(acmi.position);
		    
		
		
		// TODO Auto-generated method stub
//		devotion= (DevotionComment) listAdapter.getItem(position);
		
		if(devotion!=null)
		{
			
			
			if(devotion.getMemberID().equalsIgnoreCase(Connector.getUserId()))
			{
				//Toast.makeText(Connector.context, devotion.getMemberID(), Toast.LENGTH_LONG).show();
				 menu.setHeaderTitle("Choose");
				getMenuInflater().inflate(R.menu.devotion_discussions, menu);
				
			}
			else
			{
				
			}
		}
		
		}
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if(item.getItemId()==R.id.mn_delete_comment)
		{
			AdapterView.AdapterContextMenuInfo info= (AdapterContextMenuInfo) item.getMenuInfo();
			
			//Toast.makeText(Connector.context, info.position+"", Toast.LENGTH_LONG).show();
			//DevotionComment devotion= listAdapter.getItem(info.position);
	GroupFeedComment devotion = null;
//		// TODO Auto-generated method stub
	Integer position= info.position;
		devotion= (GroupFeedComment) listAdapter.getItem(position);
		if(devotion!=null)
		{
			deleteFromServer(devotion, position);
		}
		
		}
		return super.onContextItemSelected(item);
	}
    void deleteFromServer(final GroupFeedComment comment,final Integer position)
    {
    	StringRequest jsonReq = new StringRequest(Method.POST,
                "https://nsoredevotional.com/mobile/devotionHandler.php", new Response.Listener<String>() {

                      @Override
                      public void onResponse(String response) {
                          VolleyLog.d(TAG, "Response: " + response.trim().toString());
                          if (response != null) {
                             // parseJsonFeed(response);
                          	if(response.trim().equalsIgnoreCase("Deleted"))
                          	{
                          		//loadpage2();
                          		listAdapter.remove(position);
                          	}
                          	
                          }
                      }
                  }, new Response.ErrorListener() {

                      @Override
                      public void onErrorResponse(VolleyError error) {
                         showErrorDialog("",1);
                      }
                  })
  		 {
  		        @Override
  		        protected Map<String, String> getParams() {
  		         Map<String, String> params = new HashMap<String, String>();
  		            params.put("reason", "Delete Feed Comment");
  	           
  		            params.put("topicid", comment.getComment_id());
  		            params.put("senderid", Connector.getUserId());

  		            return params;
  		        }}
  		;

          // Adding request to volley request queue
         // AppController.getInstance().addToRequestQueue(jsonReq);
  		 InputStream keyStore = getResources().openRawResource(R.raw.appsol);
              
  			RequestQueue queue = Volley.newRequestQueue(Connector.context,
                      new ExtHttpClientStack(new SslHttpClient(keyStore, "qwerty")));
  			  queue.add(jsonReq);
    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		
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
