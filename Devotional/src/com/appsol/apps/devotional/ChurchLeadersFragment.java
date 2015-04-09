package com.appsol.apps.devotional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.appsol.apps.projectcommunicate.classes.CircularNetworkImageView;
import com.appsol.apps.projectcommunicate.classes.Connector;
import com.appsol.apps.projectcommunicate.model.ChurchLeader;
import com.appsol.apps.projectcommunicate.model.Testimony;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass. Use the
 * {@link ChurchLeadersFragment#newInstance} factory method to create an
 * instance of this fragment.
 * 
 */
public class ChurchLeadersFragment extends Fragment {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;
	private View rootView;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment ChurchLeadersFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static ChurchLeadersFragment newInstance(String param1, String param2) {
		ChurchLeadersFragment fragment = new ChurchLeadersFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}

	public ChurchLeadersFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		rootView= inflater.inflate(R.layout.fragment_church_leaders, container,
				false);
		
		setUpUI(rootView);
		return rootView;
	}
static ProgressBar progressBar;
//private DisplayImageOptions options;
//protected ImageLoader imageLoader = ImageLoader.getInstance();
private static ItemAdapter ChurchLeaderAdapter;
List<ChurchLeader>ChurchLeaderlist;
private ListView lstleaders;
	private void setUpUI(View rootView) {
		// TODO Auto-generated method stub
		progressBar=(ProgressBar) rootView.findViewById(R.id.progressbar);
		ChurchLeaderlist = new ArrayList<ChurchLeader>();
		lstleaders = (ListView) rootView.findViewById(R.id.lstLeaders);
		ChurchLeaderAdapter = new ItemAdapter(getActivity(),
				android.R.layout.simple_list_item_1, ChurchLeaderlist);
		lstleaders.setAdapter(ChurchLeaderAdapter);
//		options = new DisplayImageOptions.Builder()
//				.showImageOnLoading(R.drawable.mobile)
//				.showImageForEmptyUri(R.drawable.mobile)
//				.showImageOnFail(R.drawable.mobile).cacheInMemory(true)
//				.cacheOnDisk(true).considerExifParams(true)
//				.displayer(new RoundedBitmapDisplayer(6)).build();
		lstleaders.setOnItemClickListener( new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				ChurchLeader leader = ChurchLeaderAdapter.getItem(position);
				if(leader!=null)
				{
					Intent intent= new Intent(getActivity(), ChurchLeaderDetails.class);
					intent.putExtra("N", leader.getLeader_title()+" "+leader.getLeader_name());
					intent.putExtra("P",leader.getPosition());
					intent.putExtra("PF", leader.getProfile());
					intent.putExtra("DP", leader.getLeader_photo());
					
					
					
				startActivity(intent);
				}
			}
		});
		
		
		(new fetchChurchLeaders()).execute();
	}
	
	
	
	///get Church Leaders
	
	////Set up some house stufss
	private static class ViewHolder {
		TextView title;
		TextView content;
		 CircularNetworkImageView image,thumbup1;
//		 NetworkImageView thumbup;
		TextView txtPosition;
		TextView txtname;
		
	}

	class ItemAdapter extends ArrayAdapter<ChurchLeader> {

		List<ChurchLeader> testimoniess;
		ImageLoader imageLoader = com.appsol.volley.classes.ImageCacheManager.getInstance().getImageLoader();
	    
		public ItemAdapter(Context context, int resource,
				List<ChurchLeader> objects) {
			super(context, resource, objects);
			testimoniess = objects;
			// TODO Auto-generated constructor stub
		}

		//private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

		@Override
		public int getCount() {
			return testimoniess.size();
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View view = convertView;
			final ViewHolder holder;

			ChurchLeader testimony = getItem(position);

			if (convertView == null) {
				view = getActivity().getLayoutInflater().inflate(
						R.layout.churchleaderlistitem, parent, false);
				holder = new ViewHolder();
				holder.title = (TextView) view.findViewById(R.id.txttitle);
				holder.image = ( CircularNetworkImageView) view.findViewById(R.id.thumbImage);
				//holder.thumbup=(ImageView) view.findViewById(R.id.testimonylikeICo);
				holder.txtPosition = (TextView) view
						.findViewById(R.id.txtPosition);
		holder.txtname=(TextView) view.findViewById(R.id.txtname);
//				holder.txtlikes= (TextView) view.findViewById(R.id.txtlikes);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			
			  if (imageLoader == null)
		            imageLoader = com.appsol.volley.classes.ImageCacheManager.getInstance().getImageLoader();
		            
		 
			
			if (testimony != null) {
				holder.title.setText(testimony.getLeader_title());
				//holder.content.setText(testimony.getContent());
				String url = Connector.leadersImages + testimony.getLeader_photo();
				//String likes= testimony.getLikes();
				
					holder.txtname.setText(testimony.getLeader_name());
				
				holder.txtPosition.setText(testimony.getPosition());
				holder.image.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.mobile));
				 
				if(!url.equalsIgnoreCase(Connector.leadersImages))
				{
//					imageLoader.displayImage(url, holder.image, options,
//							animateFirstListener);
					
					
				holder.image.setImageUrl(url, imageLoader);
						}
				 
			else
					holder.image.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.mobile));
				 
				        
					
				}
				

			

			return view;
		}
	}

	

	private static  class fetchChurchLeaders extends AsyncTask<Void, Integer, String> {
		ProgressDialog pd;

		private int myProgressCount;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			if(progressBar!=null)
				progressBar.setProgress(0);
		     myProgressCount = 0;
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			
			if(progressBar!=null)
				progressBar.setVisibility(View.VISIBLE);
				progressBar.setProgress(values[0]);
			super.onProgressUpdate(values);
		}
		
		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			// Get Church Testimonies
			ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("CID", Connector.getChurchID()));
			param.add(new BasicNameValuePair("reason", "get Church Leaders"));
			param.add(new BasicNameValuePair("USER_ID", Connector.getUserId()));
			String response = null;
			response =Connector.sendData(param, Connector.context);
			   while (response == null) {
             myProgressCount++;
             /**
              * Runs on the UI thread after publishProgress(Progress...) is
              * invoked. The specified values are the values passed to
              * publishProgress(Progress...).
              *
              * Parameters values The values indicating progress.
              */

             publishProgress(myProgressCount);
             SystemClock.sleep(100);
       }
			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			if(progressBar!=null)
			{
				progressBar.setProgress(0);
				progressBar.setVisibility(View.GONE);
			}
			//pd.dismiss();
			Log.e("ERR", result);
			
			if(!result.equalsIgnoreCase("NOT_SET"))
			{
				try {
					JSONObject object = new JSONObject(result);

					if (object != null) {
						JSONArray jarray = object.getJSONArray("Church-Leaders");

						if (jarray != null) {
							ChurchLeaderAdapter.clear();
							for (int i = 0; i < jarray.length(); i++) {
								JSONObject objects = jarray.optJSONObject(i);
								
						
								 String leader_name= objects.optString("leader_name");
								 String leader_title= objects.optString("leader_title");
								 String position= objects.optString("position");
								 String leader_photo= objects.optString("leader_photo");
								 String profile= objects.optString("profile");
								 String church_branch_leader_id= objects.optString("church_branch_leader_id");
								 
								 ChurchLeader churchleader= new ChurchLeader();
								 churchleader.setId(church_branch_leader_id);
								 churchleader.setLeader_name(leader_name);
								 churchleader.setLeader_photo(leader_photo);
								 churchleader.setLeader_title(leader_title);
								 churchleader.setPosition(position);
								 churchleader.setProfile(profile);
								 ChurchLeaderAdapter.add(churchleader);
								
								
							}
						}
					}
					ChurchLeaderAdapter.notifyDataSetChanged();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
			

			super.onPostExecute(result);
		}

	}

}
