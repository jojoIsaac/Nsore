package com.appsol.apps.devotional;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.AdapterContextMenuInfo;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.appsol.apps.devotional.FriendsList.EndlessScrollListener;
import com.appsol.apps.devotional.SubscriptionActivity.churchDetails;
import com.appsol.apps.projectcommunicate.adapter.PrayerRequestListAdapter;
import com.appsol.apps.projectcommunicate.adapter.UsersFeedListAdapter;
import com.appsol.apps.projectcommunicate.classes.CircularNetworkImageView;
import com.appsol.apps.projectcommunicate.classes.Connector;
import com.appsol.apps.projectcommunicate.model.Church;
import com.appsol.apps.projectcommunicate.model.NsoreDevotionalUser;
import com.appsol.volley.classes.ExtHttpClientStack;
import com.appsol.volley.classes.SslHttpClient;
import com.appsol.volley.classes.Volley;

/**
 * A simple {@link Fragment} subclass. Use the {@link PrayerRequest#newInstance}
 * factory method to create an instance of this fragment.
 * 
 */
public class PrayerRequest extends Fragment {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;
	private View row;
	private static View rootView;
	private String URL_FEED;
	private boolean mInError;
	private int startAT;
	private ArrayList<String> churches;
	private ArrayList<String> churchID;
	private ArrayList<String> churchlogo;
	private ArrayList<Church> churchList;
	static Button btnCreateNew, btnnew;
	static TextView txt1;
	static TextView txt2;
	static ImageView img1;
	static ListView lstprayerrequest;
	static ProgressBar progressbarLoad;
	public static com.appsol.apps.projectcommunicate.model.PrayerRequest bmark;
	public static PrayerRequestListAdapter bdapter;
	public static List<com.appsol.apps.projectcommunicate.model.PrayerRequest> feedItems;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment PrayerRequest.
	 */
	// TODO: Rename and change types and number of parameters
	public static PrayerRequest newInstance(String param1, String param2) {
		PrayerRequest fragment = new PrayerRequest();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}

	public PrayerRequest() {
		// Required empty public constructor
	}

	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		feedItems= new ArrayList<com.appsol.apps.projectcommunicate.model.PrayerRequest>();
		bdapter = new PrayerRequestListAdapter(getActivity(),
				android.R.layout.simple_list_item_1,feedItems);
		if(lstprayerrequest!=null)
		{
			lstprayerrequest.setAdapter(bdapter);
			lstprayerrequest.setOnScrollListener(new EndlessScrollListener());
			registerForContextMenu(lstprayerrequest);
		}
		//justClicked=0;
		loadpage();
		//lstprayerrequest.setOnScrollListener(new EndlessScrollListener());
		super.onResume();
	}
	private void loadpage() {
		//justClicked=0;
			
			progressbarLoad.setVisibility(View.VISIBLE);
			URL_FEED = "https://nsoredevotional.com/mobile/devotionHandler.php";
			StringRequest jsonReq = new StringRequest(Method.POST, URL_FEED,
					createMyReqSuccessListener(), createMyReqErrorListener()) {
				@Override
				protected Map<String, String> getParams() {
					  Map<String, String> params = new HashMap<String, String>();
	 		            params.put("reason", "Get My PrayerRequest");
	 		            params.put("MID", Connector.getUserId());
					return params;
				}
			};
			InputStream keyStore = getResources().openRawResource(R.raw.appsol);

			RequestQueue queue = Volley.newRequestQueue(getActivity(),
					new ExtHttpClientStack(new SslHttpClient(keyStore, "qwerty")));
			// Adding request to volley request queue
			queue.add(jsonReq);
		}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		AdapterContextMenuInfo info= (AdapterContextMenuInfo) item.getMenuInfo();
		final Integer position= info.position;
		final com.appsol.apps.projectcommunicate.model.PrayerRequest prayer= bdapter.getItem(position);
		if(prayer!=null && prayer.getChurch_member_prayer_request_id()!=null && !TextUtils.isEmpty(prayer.getChurch_member_prayer_request_id()))
		{
		switch (item.getItemId()) {
		case R.id.action_resend:
			
				onShowPopup(prayer.getPrayer_request());
			
			
			break;
		case R.id.action_delete:
			AlertDialog.Builder builder= new Builder(getActivity());
			builder.setMessage("Are you sure?")
			.setTitle("Delete Request")
			.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					deletRequest(prayer.getChurch_member_prayer_request_id());
				}
			})
			.setNegativeButton("No", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			})
			.show();
			
		break;
		
		case 2: ///This will show response from the church
			if(prayer!=null)
			{
				//onShowPopup(prayer.getPrayer_request());
				/*
				 * requestID= getArguments().getString("RID");
			PrayerRequest=getArguments().getString("PR");
			dateSent=getArguments().getString("DS");
			CID = getArguments().getString("CID");
				 */
				Bundle data= new Bundle();
				data.putString("RID", prayer.getChurch_member_prayer_request_id());
				data.putString("PR",prayer.getPrayer_request());
				data.putString("DS", prayer.getSent_at());
				data.putString("CID",prayer.getChurch_id());
				data.putString("CN",prayer.getChurchname());
				
				Fragment prayerResponses= new PrayerResponsesFragment();
				prayerResponses.setArguments(data);
				
				getActivity().getSupportFragmentManager().
				beginTransaction().replace(((ViewGroup)(getView().getParent())).getId(), prayerResponses).addToBackStack(null).commit();

			}
			break;

		default:
			break;
		}
		}
		return super.onContextItemSelected(item);
	}
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		getActivity().getMenuInflater().inflate(R.menu.global, menu);
		menu.setHeaderTitle("");
		AdapterContextMenuInfo info= (AdapterContextMenuInfo) menuInfo;
		final Integer position= info.position;
		com.appsol.apps.projectcommunicate.model.PrayerRequest prayer= bdapter.getItem(position);
		if(prayer!=null)
		{
			if(!prayer.getStatus().equalsIgnoreCase("pending"))
			   menu.add(1, 2, 10, "Responses");
		}
		super.onCreateContextMenu(menu, v, menuInfo);
	}
	
	void deletRequest(final String requestID)
	{
		URL_FEED = "https://nsoredevotional.com/mobile/devotionHandler.php";
		StringRequest jsonReq = new StringRequest(Method.POST, URL_FEED,
				createMyReqSuccessListener("Delete"), createMyReqErrorListener()) {
			@Override
			protected Map<String, String> getParams() {
				  Map<String, String> params = new HashMap<String, String>();
 		            params.put("reason", "Delete PrayerRequest");
 		            params.put("request", requestID);
				return params;
			}
		};
		InputStream keyStore = getResources().openRawResource(R.raw.appsol);

		RequestQueue queue = Volley.newRequestQueue(getActivity(),
				new ExtHttpClientStack(new SslHttpClient(keyStore, "qwerty")));
		// Adding request to volley request queue
		queue.add(jsonReq);
	}
	
	private Listener<String> createMyReqSuccessListener(String reason) {
		// TODO Auto-generated method stub
    return new Response.Listener<String>() {
        	
        	
        	
        	
        	
            @Override
            public void onResponse(String response) {
            	if(!response.trim().equalsIgnoreCase("ERR") && !response.trim().equalsIgnoreCase("Saved")&& response.trim().equalsIgnoreCase("Deleted"))
    			{
            		onResume();
    			}
            	
            }
        };
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}
		 Connector.myPrefs= getActivity().getSharedPreferences(Connector.prefernceName, Context.MODE_PRIVATE);
		setHasOptionsMenu(true);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		inflater.inflate(R.menu.notelistmenu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	

	public static void toogleView(boolean b) {
		if (b) {
			txt1.setVisibility(View.GONE);
			txt2.setVisibility(View.GONE);
			img1.setVisibility(View.GONE);
			btnCreateNew.setVisibility(View.GONE);
			//progressbarLoad.setVisibility(View.VISIBLE);
			lstprayerrequest.setVisibility(View.VISIBLE);
		} else {
			txt1.setVisibility(View.VISIBLE);
			txt2.setVisibility(View.VISIBLE);
			img1.setVisibility(View.VISIBLE);
			btnCreateNew.setVisibility(View.VISIBLE);
			progressbarLoad.setVisibility(View.GONE);
			lstprayerrequest.setVisibility(View.GONE);
		}
	}
@Override
public boolean onOptionsItemSelected(MenuItem item) {
	// TODO Auto-generated method stub
	if(item.getItemId()==R.id.mn_add_note)
	{
		onShowPopup("");
	}
	return super.onOptionsItemSelected(item);
}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		Connector.context=getActivity();
		rootView = inflater.inflate(R.layout.fragment_prayer_request,
				container, false);
		txt1 = (TextView) rootView.findViewById(R.id.txt1);
		txt2 = (TextView) rootView.findViewById(R.id.txtdevotionstatus);
		img1 = (ImageView) rootView.findViewById(R.id.imgeventBanner);
		btnCreateNew = (Button) rootView.findViewById(R.id.btnx);
		// btnnew=(Button) rootView.findViewById(R.id.btnnew);
		feedItems = new ArrayList<com.appsol.apps.projectcommunicate.model.PrayerRequest>();
		lstprayerrequest = (ListView) rootView.findViewById(R.id.list);
		progressbarLoad = (ProgressBar) rootView
				.findViewById(R.id.progressbarLoad);
		bdapter = new PrayerRequestListAdapter(getActivity(),
				android.R.layout.simple_list_item_1,feedItems);
		lstprayerrequest.setAdapter(bdapter);
		//lstprayerrequest.setOnScrollListener(new EndlessScrollListener());
		registerForContextMenu(lstprayerrequest);
		return rootView;
	}
	
	private void loadpage2()
    {
    	 final int startIndex = 1 + feedItems.size();
    	 progressbarLoad.setVisibility(View.VISIBLE);
    	 URL_FEED = "https://nsoredevotional.com/mobile/devotionHandler.php";
    	 StringRequest jsonReq = new  StringRequest(Method.POST,
	                    URL_FEED, createMyReqSuccessListener(),createMyReqErrorListener())
	        	    {
	 		        @Override
	 		        protected Map<String, String> getParams() {
	  Map<String, String> params = new HashMap<String, String>();
 		            params.put("reason", "Get My PrayerRequest");
//	 		            params.put("CID", "1");
 		            params.put("MID", Connector.getUserId());
	 		            params.put("index", startIndex+"");

	 		            return params;
	 		        }}
	        	 ;
	            InputStream keyStore = getResources().openRawResource(R.raw.appsol);
	            
	            
	            RequestQueue queue = Volley.newRequestQueue(getActivity(),
                        new ExtHttpClientStack(new SslHttpClient(keyStore, "qwerty")));
	            // Adding request to volley request queue
	            queue.add(jsonReq);
	        }
	
	
	private Response.Listener<String> createMyReqSuccessListener() {
        return new Response.Listener<String>() {
        	
        	
        	
        	
        	
            @Override
            public void onResponse(String response) {
            	if(!response.equalsIgnoreCase("Sorry No Event Found For the Selected Month")&&!response.equalsIgnoreCase("ERR") && !response.equalsIgnoreCase("Saved"))
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
				showErrorDialog(Connector.HandleVolleyerror(error, getActivity()));
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

		AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
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
				loadpage2();
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
	
   // Process the response
   /**
    * Parsing json reponse and passing the data to feed view list adapter
    * */
   private void parseJsonFeed(String response) {
   	//Toast.makeText(this, response, Toast.LENGTH_LONG).show();
   	progressbarLoad.setVisibility(View.GONE);
   	if (!response.trim()
				.contains("No Comment Found.") && !response.trim().contains("{\"PrayerRequest\":\"No PrayerRequest\"}")) {

			JSONObject objects;

			
       try {

			objects = new JSONObject(response.trim());
			if (objects != null) {
				JSONArray feedArray  = objects.getJSONArray("PrayerRequest");

				if (feedArray  != null) {
       
           //JSONArray feedArray = response.getJSONArray("feed");



           for (int i = 0; i < feedArray.length(); i++) {
               JSONObject feedObj = (JSONObject) feedArray.get(i);

            com.appsol.apps.projectcommunicate.model.PrayerRequest  item = new  com.appsol.apps.projectcommunicate.model.PrayerRequest();
             /*
             $dataItem['id']= $row['church_member_prayer_request_id'];
			  $dataItem['request']=$row['prayer_request'];
			  $dataItem['date']=ago($row['sent_at'], -1);
			  $dataItem['status']= $row['status'];
			  $dataItem['church_id']=$row['church_id']
			  $dataItem['churchName']=getchurchName($row['church_id');
              * 
              */
           
             item.setChurch_member_prayer_request_id(feedObj.getString("id"));
             item.setPrayer_request(feedObj.getString("request"));
             item.setStatus(feedObj.getString("status"));
             item.setSent_at(feedObj.getString("date"));
             //item.setCurrent(false);
			  item.setChurch_id(feedObj.getString("church_id"));
			  item.setChurchname(feedObj.getString("churchName")+"");
			 
			  item.setChurchname(feedObj.getString("churchName"));
            
             
             bdapter .add(item);
           }

           // notify data changes to list adapater
           
           startAT= feedItems.size()+ 1;
           bdapter .notifyDataSetChanged();
           progressbarLoad.setVisibility(View.GONE);
          if(bdapter.getCount()>0)
          {
        	  toogleView(true);
          }
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
   
   
   
   
	
// Class to handle endless scrollings
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
				if (!loading
						&& (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
					// I load the next page of gigs using a background task,
					// but you can call any function here.

					loadpage2();
					//justClicked=199;
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
		Spinner spins;
		ArrayAdapter<String> adapter;
		
		
		PopupWindow popWindow;
		   @SuppressLint("NewApi") 
		   private  void onShowPopup(String prayer){
			
			//ImageLoader imageLoader = com.appsol.volley.classes.ImageCacheManager.getInstance().getImageLoader();
			
		   LayoutInflater layoutInflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    
		   // inflate the custom popup layout
		   final View inflatedView = layoutInflater.inflate(R.layout.fragment_create_note, null,false);
		   final EditText body= (EditText) inflatedView.findViewById(R.id.body);
		  
		   body.setText(prayer);
		   ///
		   churches = new ArrayList<String>();
			churchID= new ArrayList<String>();
			churchlogo= new ArrayList<String>();
		   adapter= new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, churches);
				spins=(Spinner) inflatedView.findViewById(R.id.spnChurch);
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spins.setAdapter(adapter);
				
		   Button btnconnect =(Button) inflatedView.findViewById(R.id.btnsavenote);
		   Button btnCancel=(Button) inflatedView.findViewById(R.id.btnCancel);
		   
		   
		   btnconnect.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				// TODO Auto-generated method stub
				String URL_FEED = "https://nsoredevotional.com/mobile/devotionHandler.php";
				StringRequest jsonReq = new StringRequest(Method.POST, URL_FEED,
						createMyReqSuccessListeners(), createMyReqErrorListener()) {
					

					@Override
					protected Map<String, String> getParams() {
						 String spinsVal;
						 String id;
						Map<String, String> params = new HashMap<String, String>();
						 spinsVal= spins.getSelectedItem().toString();
						 id= churchID.get(spins.getSelectedItemPosition());
						 if(id!=null)
						params.put("CID", id);
						 else
							 params.put("CID", Connector.getChurchID());
						params.put("MID",Connector.getUserId());
						params.put("UID", Connector.getUserId());
						params.put("request", body.getText().toString());
						
						params.put("reason","Save PrayerRequest");
						
						return params;
					}
				};
				InputStream keyStore = Connector.context.getResources().openRawResource(R.raw.appsol);

				if(!TextUtils.isEmpty( body.getText().toString().trim()) && adapter.getCount()>0)
				{
					RequestQueue queue = Volley.newRequestQueue(Connector.context,
							new ExtHttpClientStack(new SslHttpClient(keyStore, "qwerty")));
					// Adding request to volley request queue
					queue.add(jsonReq);	
				}
				
				
			}

			private Listener<String> createMyReqSuccessListeners() {
				// TODO Auto-generated method stub
				  return new Response.Listener<String>() {
			        	
			        	
			        	
			        	
			        	
			            @Override
			            public void onResponse(String response) {
			            	Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
			            	if(response.trim().equalsIgnoreCase("Saved"))
			            	{
			            		if(popWindow!=null)
			            		{
			            			popWindow.dismiss();
			            			loadpage();
			            		}
			            	}
			            	
			            }
			        };
				
			}
		});
		// find the ListView in the popup layout
		   int mDeviceHeight;
		    
		   // get device size
		   Display display = getActivity().getWindowManager().getDefaultDisplay();
		   final Point size = new Point();
		   display.getSize(size);
		   mDeviceHeight = size.y;	    
		   // set height depends on the device size
		   popWindow = new PopupWindow(inflatedView, size.x - 50,size.y - 300, true );
		   
		   //Views
		   
		   
		 
		   // set a background drawable with rounders corners
		   popWindow.setBackgroundDrawable(Connector.context.getResources().getDrawable(R.drawable.flatlayout));
		   // make it focusable to show the keyboard to enter in `EditText`
		   popWindow.setFocusable(true);
		   // make it outside touchable to dismiss the popup window
		   popWindow.setOutsideTouchable(true);
		    
		   // show the popup at bottom of the screen and set some margin at bottom ie,
		   popWindow.showAtLocation(inflatedView, Gravity.TOP, 0,150);
	
			(new createAccount()).execute();
			btnCancel.setOnClickListener( new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					popWindow.dismiss();
				}
			});
		   }
		
		
		class createAccount extends AsyncTask<Void, Integer, String>
		{
			ProgressDialog pdg;
 @Override
protected void onPreExecute() {
	// TODO Auto-generated method stub
	 pdg= new ProgressDialog(getActivity());
	
	 pdg.setMessage("Please Wait. Fetching Church list");
	 pdg.setCancelable(false);
	 pdg.show();
	super.onPreExecute();
}
 
 @Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
	 pdg.cancel();
	 processJson(result);
				super.onPostExecute(result);
			}

private void processJson(String result) {
	// TODO Auto-generated method stub
	//Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
	try {
		churches = new ArrayList<String>();
		churchID= new ArrayList<String>();
		churchlogo= new ArrayList<String>();
		JSONObject object = new  JSONObject(result);
	
		if(object!=null)
		{
			JSONArray jarray= object.getJSONArray("Church");
			
			if(jarray!=null)
			{
				for(int i=0;i<jarray.length();i++)
				{
					JSONObject objects= jarray.optJSONObject(i);
					adapter.add(objects.optString("CN"));
					churchID.add(objects.optString("CID"));
					churchlogo.add(objects.optString("CL"));
				}
			}
		}
		adapter.notifyDataSetChanged();
		
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

@Override
protected String doInBackground(Void... arg0) {
	ArrayList<NameValuePair> parameter= new ArrayList<NameValuePair>();
	parameter.add(new BasicNameValuePair("CID",Connector.getChurchID()));
	parameter.add(new BasicNameValuePair("MID",Connector.getUserId()));
	parameter.add(new BasicNameValuePair("reason","Get My Pref Churches"));
	String status= Connector.sendData(parameter, getActivity());
	return status;
}
 
		}

}
