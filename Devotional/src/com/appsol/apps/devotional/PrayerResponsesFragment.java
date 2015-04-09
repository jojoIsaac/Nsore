package com.appsol.apps.devotional;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.StringRequest;
import com.appsol.apps.devotional.PrayerRequest.EndlessScrollListener;
import com.appsol.apps.projectcommunicate.adapter.PrayerRequestListAdapter;
import com.appsol.apps.projectcommunicate.adapter.PrayerResponseListAdapter;
import com.appsol.apps.projectcommunicate.classes.Connector;
import com.appsol.apps.projectcommunicate.model.PrayerRequest.PrayerResponses;
import com.appsol.volley.classes.ExtHttpClientStack;
import com.appsol.volley.classes.SslHttpClient;
import com.appsol.volley.classes.Volley;

import android.os.Build;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;

import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.AbsListView.OnScrollListener;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass. Use the
 * {@link PrayerResponsesFragment#newInstance} factory method to create an
 * instance of this fragment.
 * 
 */
public class PrayerResponsesFragment extends Fragment {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";
//TODO:
	private ProgressBar progressbarLoad;
	static ListView lstprayerrequest;
	private View row;
	private static View rootView;
	private String URL_FEED;
	private boolean mInError;
	private int startAT;
	public static com.appsol.apps.projectcommunicate.model.PrayerRequest.PrayerResponses bmark;
	public static PrayerResponseListAdapter bdapter;
	public static List<PrayerResponses> feedItems;
	//static ProgressBar progressbarLoad;
	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;
	private String CID="",CN="";

	
	
	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment PrayerResponsesFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static PrayerResponsesFragment newInstance(String param1,
			String param2) {
		PrayerResponsesFragment fragment = new PrayerResponsesFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}
public static String requestID="", PrayerRequest="", dateSent="";
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putString("RID", requestID);
		outState.putString("PR", PrayerRequest);
		outState.putString("DS", dateSent);
		outState.putString("CID", CID);
		outState.putString("CN", CN);
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		feedItems= new ArrayList<com.appsol.apps.projectcommunicate.model.PrayerRequest.PrayerResponses>();
		bdapter = new PrayerResponseListAdapter(getActivity(),
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
	
	public PrayerResponsesFragment() {
		// Required empty public constructor
	}
 @Override
		public void onViewStateRestored(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewStateRestored(savedInstanceState);
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(savedInstanceState!=null)
		{
			CID = savedInstanceState.getString("CID");
			CN = savedInstanceState.getString("CN");
			requestID= savedInstanceState.getString("RID");
			PrayerRequest=savedInstanceState.getString("PR");
			dateSent=savedInstanceState.getString("DS");
			
		}
		if (getArguments() != null && savedInstanceState==null) {
			
			requestID= getArguments().getString("RID");
			PrayerRequest=getArguments().getString("PR");
			dateSent=getArguments().getString("DS");
			CID = getArguments().getString("CID");
			CN= getArguments().getString("CN");
		}
		
		
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
	 		            params.put("reason", "Get My PrayerRequest Response");
	 		           params.put("RID", requestID);
	 		           params.put("CID", CID);
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		Connector.context=getActivity();
		rootView = inflater.inflate(R.layout.prayer_responses, container,
				false);
		lstprayerrequest = (ListView) rootView.findViewById(R.id.lv);
		progressbarLoad = (ProgressBar) rootView
				.findViewById(R.id.progressbarLoad);
		TextView txtrequestContent= (TextView) rootView.findViewById(R.id.txtrequestContent);
		TextView timestamp=(TextView) rootView.findViewById(R.id.timestamp);
		TextView txtChurchName =(TextView) rootView.findViewById(R.id.txtChurchName);
		txtrequestContent.setText(PrayerRequest);
		timestamp.setText(dateSent);
		txtChurchName.setText(CN);
		//LinearLayout lvDate = (LinearLayout) rootView.findViewById(R.id.lvDate);
		
		feedItems= new ArrayList<com.appsol.apps.projectcommunicate.model.PrayerRequest.PrayerResponses>();
		bdapter = new PrayerResponseListAdapter(getActivity(),
				android.R.layout.simple_list_item_1,feedItems);
		
			lstprayerrequest.setAdapter(bdapter);
			lstprayerrequest.setOnScrollListener(new EndlessScrollListener());
		
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
 		            params.put("reason", "Get My PrayerRequest Response");
//	 		            params.put("CID", "1");
 		            /*
 		             *  $request=$_POST['RID'];
   $CID=$_POST['CID'];
 		             */
 		            params.put("RID", requestID);
 		           params.put("CID", CID);
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
   //	Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
   //	Log.e("RES",response);
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

            com.appsol.apps.projectcommunicate.model.PrayerRequest.PrayerResponses  item = new  com.appsol.apps.projectcommunicate.model.PrayerRequest.PrayerResponses ();
            
             item.setChurch_member_prayer_request_id(feedObj.getString("id"));
             item.setResponse(feedObj.getString("response"));
             item.setResponded_by(feedObj.getString("responded_by"));
             item.setResponded_at(feedObj.getString("date"));
			 item.setChurchname(feedObj.getString("churchName")+"");
			 
			 item.setChurchname(feedObj.getString("churchName"));
             bdapter .add(item);
           }

           // notify data changes to list adapater
           
           startAT= feedItems.size()+ 1;
           bdapter .notifyDataSetChanged();
           progressbarLoad.setVisibility(View.GONE);
//          if(bdapter.getCount()>0)
//          {
//        	  toogleView(true);
//          }
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

}
