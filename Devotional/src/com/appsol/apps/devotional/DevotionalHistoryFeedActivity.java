package com.appsol.apps.devotional;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.appsol.apps.projectcommunicate.adapter.DevotionFeedListAdapter;
import com.appsol.apps.projectcommunicate.adapter.TestimonyFeedListAdapter;
import com.appsol.apps.projectcommunicate.classes.Connector;
import com.appsol.apps.projectcommunicate.classes.Devotion;
import com.appsol.apps.projectcommunicate.model.Bookmark;
import com.appsol.apps.projectcommunicate.model.ChurchEvents;
import com.appsol.apps.projectcommunicate.model.Testimony;
import com.appsol.volley.classes.ExtHttpClientStack;
import com.appsol.volley.classes.SslHttpClient;
import com.appsol.volley.classes.Volley;


public class DevotionalHistoryFeedActivity extends Fragment {

	void restoreTitle() {
		MainActivity.mTitle = "Devotional";
		getActivity().supportInvalidateOptionsMenu();
		MainActivity.restoreActionBar();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		restoreTitle();
		super.onResume();
	}

	private static final String TAG = DevotionalHistoryFeedActivity.class
			.getSimpleName();
	private ListView listView;
	private DevotionFeedListAdapter listAdapter;
	private List<Devotion> feedItems;
	private String URL_FEED = "http://api.androidhive.info/feed/feed.json";
	static Map<String, String> params;
	private boolean mHasData = false;
	private boolean mInError = false;
	Integer startAT = 0;
	private View rootView;
	private static ProgressBar progressbarLoad;

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

	private void loadpage2() {
		final int startIndex = 1 + feedItems.size();
		progressbarLoad.setVisibility(View.VISIBLE);
		URL_FEED = "https://nsoredevotional.com/mobile/eventsFetch.php";
		StringRequest jsonReq = new StringRequest(Method.POST, URL_FEED,
				createMyReqSuccessListener(), createMyReqErrorListener()) {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				params.put("CID", Connector.getChurchID());
				params.put("MID", Connector.getUserId());
				params.put("reason", "Load Ordered Devotion History");
				params.put("index", startIndex + "");

				return params;
			}
		};
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
				if (!response.equalsIgnoreCase("No Testimony Shared.")) {
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		restoreTitle();
		setHasOptionsMenu(true);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.mn_share_testimony: {
			TestimoniesCommunityFragment.createTestimony();

		}
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub

		// inflater.inflate(R.menu.testimony_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@SuppressLint("NewApi")
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// super.onCreate(savedInstanceState);
		restoreTitle();
		getActivity().setTitle("Devotion");
		getActivity().supportInvalidateOptionsMenu();
		Connector.context = getActivity();
		rootView = inflater.inflate(R.layout.activity_facebook_feed, container,
				false);

		progressbarLoad = (ProgressBar) rootView
				.findViewById(R.id.progressbarLoad);
		// if(progressbarLoad!=null)
		// {
		// progressbarLoad.setProgress(0);
		// }
		//
		URL_FEED = "https://nsoredevotional.com/mobile/eventsFetch.php";
		listView = (ListView) rootView.findViewById(R.id.list);

		feedItems = new ArrayList<Devotion>();

		listAdapter = new DevotionFeedListAdapter(getActivity(), feedItems);
		listView.setAdapter(listAdapter);
		listView.setOnScrollListener(new EndlessScrollListener());

		listView.setOnItemClickListener(new OnItemClickListener() {

			// private ChurchEvents selectedDevotion;
			// //private PlaceholderFragment fragment;
			// private Object fragmentmanger;

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Devotion devotion = null;
				// TODO Auto-generated method stub
				devotion = (Devotion) listAdapter.getItem(position);

				if (devotion != null) {
					Toast.makeText(getActivity(), devotion.getDevotionJson(),
							Toast.LENGTH_LONG).show();

					Intent intent = new Intent(getActivity(),
							DevotionDetailActivity.class);
					intent.putExtra("DEVOTION-JSON", devotion.getDevotionJson());
					intent.putExtra("LID", devotion.getLesseonid());
					intent.putExtra("DID", devotion.getDaily_guide_id());
					if (Bookmark.checkAlreadyBookedMarked(devotion
							.getLesseonid())) {
						intent.putExtra("HIDE-BOOOKMARK", "1");
					} else
						intent.putExtra("HIDE-BOOOKMARK", "0");

					getActivity().startActivity(intent);
				}

			}
		});
		StringRequest jsonReq = new StringRequest(Method.POST, URL_FEED,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						VolleyLog.d(TAG, "Response: " + response.toString());
						if (response != null) {
							parseJsonFeed(response.trim());
						}
					}
				}, new Response.ErrorListener() {

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
				}) {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				// params.put("reason", "Members Testimonies");
				params.put("CID", Connector.getChurchID());
				params.put("MID", Connector.getUserId());
				params.put("reason", "Load Ordered Devotion History");

				return params;
			}
		};
		InputStream keyStore = getResources().openRawResource(R.raw.appsol);

		RequestQueue queue = Volley.newRequestQueue(getActivity(),
				new ExtHttpClientStack(new SslHttpClient(keyStore, "qwerty")));
		// Adding request to volley request queue
		jsonReq.setRetryPolicy(new DefaultRetryPolicy(5000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		queue.add(jsonReq);
		// }
		return rootView;
	}

	/**
	 * Parsing json reponse and passing the data to feed view list adapter
	 * */
	private void parseJsonFeed(String response) {
		// Toast.makeText(this, response, Toast.LENGTH_LONG).show();
		progressbarLoad.setVisibility(View.GONE);
		if (!response.trim().contains("Sorry No Devotions Found")) {

			JSONObject objects;

			try {

				objects = new JSONObject(response.trim());
				if (objects != null) {
					JSONArray feedArray = objects.getJSONArray("Devotion");

					if (feedArray != null) {

						// JSONArray feedArray = response.getJSONArray("feed");

						for (int i = 0; i < feedArray.length(); i++) {
							JSONObject feedObj = (JSONObject) feedArray.get(i);

							Devotion item = new Devotion();

							item.setId(feedObj.getInt("LID") + "");
							item.setChurchName(feedObj.getString("churchName"));
							item.setUserLike(feedObj.getString("UserLike"));
							item.setDevotionLikes(feedObj.getString("Likes"));
							item.setDevotionJson(feedObj.toString());
							item.setContent(feedObj.getString("Msg"));
							item.setCurrent(false);
							item.setRawDate(feedObj.getString("rawDate"));

							item.setContent(feedObj.getString("Msg"));
							item.setTitle(feedObj.getString("title"));
							item.setPrayer(feedObj.getString("prayer"));
							item.setVerse(feedObj.getString("verse"));
							item.setReadingPlan(feedObj
									.getString("readingplan"));
							item.setDevotiondate(feedObj.getString("D") + " "
									+ feedObj.getString("MN"));
							item.setDevotionday(feedObj.getString("day_name"));
							item.setNormalDevotionDate(feedObj
									.getString("loadeddate"));
							item.setSummary(feedObj.getString("summary"));
							item.setDevotion(feedObj.toString());
							item.setLesseonid(feedObj.getString("LID"));
							item.setDaily_guide_id(feedObj.getString("DID"));
							item.setComments(feedObj.getString("comments"));
							item.setCurrentDevotionID(feedObj
									.optString("CurrentID"));

							// startAT =

							feedItems.add(item);
						}

						// notify data changes to list adapater

						startAT = feedItems.size() + 1;
						listAdapter.notifyDataSetChanged();
					}

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			// Toast.makeText(Connector.context, response,
			// Toast.LENGTH_LONG).show();
		}
	}

}
