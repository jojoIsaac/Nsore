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

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.appsol.apps.projectcommunicate.classes.Connector;
import com.appsol.apps.projectcommunicate.model.Testimony;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link TestimoniesCommunityFragment.OnFragmentInteractionListener} interface
 * to handle interaction events. Use the
 * {@link TestimoniesCommunityFragment#newInstance} factory method to create an
 * instance of this fragment.
 * 
 */
public class TestimoniesCommunityFragment extends Fragment implements
		OnFragmentInteractionListener {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options;
	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;
	static ItemAdapter testimonies;
	protected static final String STATE_PAUSE_ON_SCROLL = "STATE_PAUSE_ON_SCROLL";
	protected static final String STATE_PAUSE_ON_FLING = "STATE_PAUSE_ON_FLING";
	protected boolean pauseOnScroll = false;
	protected boolean pauseOnFling = true;
	private OnFragmentInteractionListener mListener;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment TestimoniesCommunityFragment.
	 */
	// TODO: Rename and change types and number of parameters
	ListView lstTestimonies;
	private View rootView;

	public static TestimoniesCommunityFragment newInstance(String param1,
			String param2) {
		TestimoniesCommunityFragment fragment = new TestimoniesCommunityFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}

	List<Testimony> testimonylist;
	private static ProgressBar progressbarLoad;
	

	public TestimoniesCommunityFragment() {
		// Required empty public constructor
	}
public static void refreshList()
{
	(new fetchTestimonies()).execute();
}

public static void createTestimony()
{
	Intent ints= new Intent(Connector.context, ShareTestimony.class);
	Connector.context.startActivity(ints);
}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}
		restoreTitle();
		setHasOptionsMenu(true);
		
	}

	void restoreTitle()
	{
		MainActivity.mTitle="Testimonies";
		getActivity().supportInvalidateOptionsMenu();
		MainActivity.restoreActionBar();
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		restoreTitle();
		super.onResume();
	}

	private void applyScrollListener() {
		// lstTestimonies.setOnScrollListener(new
		// PauseOnScrollListener(imageLoader, pauseOnScroll, pauseOnFling));
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.mn_share_testimony:
		{
		     TestimoniesCommunityFragment.createTestimony();
			
		}
		break;
		
//		case R.id.mn_refresh_testimony:
//		{
//			
//				TestimoniesCommunityFragment.refreshList();
//			
//		}
//		break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
@Override
public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	// TODO Auto-generated method stub

	inflater.inflate(R.menu.testimony_menu, menu);
	super.onCreateOptionsMenu(menu, inflater);
}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		restoreTitle();
		getActivity().setTitle("Testimonies");
		getActivity().supportInvalidateOptionsMenu();
		// Inflate the layout for this fragment
		rootView = inflater.inflate(R.layout.fragment_testimonies_community,
				container, false);
		 progressbarLoad=(ProgressBar) rootView.findViewById(R.id.progressbarLoad);
		 if(progressbarLoad!=null)
		 {
			 progressbarLoad.setProgress(0);
		 }
		
		testimonylist = new ArrayList<Testimony>();
		lstTestimonies = (ListView) rootView.findViewById(R.id.lstTestimonies);
		testimonies = new ItemAdapter(getActivity(),
				android.R.layout.simple_list_item_1, testimonylist);
		lstTestimonies.setAdapter(testimonies);
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.mobile)
				.showImageForEmptyUri(R.drawable.mobile)
				.showImageOnFail(R.drawable.mobile).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.displayer(new RoundedBitmapDisplayer(6)).build();
		
		lstTestimonies.setOnItemClickListener( new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				Testimony testimony=testimonies.getItem(position);
				
				if(testimony!=null)
				{
					MainActivity.fragment = new TestimonyDetailsFragment();
					Bundle data= new Bundle();
					data.putString("DP",testimony.getUserdp());
					data.putString("N",testimony.getName());
					data.putString("C",testimony.getFullcontent());
					data.putString("ID", testimony.getTestimonyID());
					data.putString("L", testimony.getLikeTestimony());
					data.putString("DID",testimony.getDeviceID());
					MainActivity.fragment.setArguments(data);
					
					
					
					 FragmentManager fragmentManager =getActivity().getSupportFragmentManager();
						fragmentManager.beginTransaction()
								.replace(R.id.container, MainActivity.fragment).addToBackStack(null).commit();
//					FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//					fragmentManager.beginTransaction()
//							.replace(R.id.frame_container, MainActivity.fragment).commit();
					getActivity().supportInvalidateOptionsMenu();
				}
				
			}
		});
		try
		{
			(new fetchTestimonies()).execute();
		}
		catch(Exception e)
		{
			
		}
		return rootView;
	}

	// TODO: Rename method, update argument and hook method into UI event
	public void onButtonPressed(Uri uri) {
		if (mListener != null) {
			mListener.onFragmentInteraction(uri);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnFragmentInteractionListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated to
	 * the activity and potentially other fragments contained in that activity.
	 * <p>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */

	@Override
	public void onFragmentInteraction(Uri uri) {
		// TODO Auto-generated method stub

	}

	private static class ViewHolder {
		TextView title;
		TextView content;
		ImageView image,thumbup;
		TextView txtTime;
		TextView txtlikes;
		
	}

	class ItemAdapter extends ArrayAdapter<Testimony> {

		List<Testimony> testimoniess;

		public ItemAdapter(Context context, int resource,
				List<Testimony> objects) {
			super(context, resource, objects);
			testimoniess = objects;
			// TODO Auto-generated constructor stub
		}

		private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

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

			Testimony testimony = getItem(position);

			if (convertView == null) {
				view = getActivity().getLayoutInflater().inflate(
						R.layout.testimontyitems, parent, false);
				holder = new ViewHolder();
				holder.title = (TextView) view.findViewById(R.id.txttitle);
				holder.image = (ImageView) view.findViewById(R.id.thumbImage);
				holder.thumbup=(ImageView) view.findViewById(R.id.testimonylikeICo);
				holder.content = (TextView) view
						.findViewById(R.id.txttestimonycontent);
				holder.txtTime=(TextView) view.findViewById(R.id.txtTime);
				holder.txtlikes= (TextView) view.findViewById(R.id.txtlikes);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			if (testimony != null) {
				holder.title.setText(testimony.getName());
				holder.content.setText(testimony.getContent());
				String url = Connector.imageURL + testimony.getUserdp();
				String likes= testimony.getLikes().trim();
				Log.e("LIKES",testimony.getSenderid()+": s"+likes);
				if(!likes.equalsIgnoreCase("0"))
				{
					holder.txtlikes.setText(likes+ "");
					holder.thumbup.setVisibility(View.VISIBLE);
				}
				else
				{
					holder.txtlikes.setText("");
					holder.thumbup.setVisibility(View.GONE);
				}
				holder.txtTime.setText(testimony.getMessageTime());
				
				if(!url.equalsIgnoreCase(Connector.imageURL))
				{
					imageLoader.displayImage(url, holder.image, options,
							animateFirstListener);
				}
				else
				{
					holder.image.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.mobile));
				}
				

			}

			return view;
		}
	}

	public static class AnimateFirstDisplayListener extends
			SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}

	private static  class fetchTestimonies extends AsyncTask<Void, Integer, String> {
		ProgressDialog pd;
		private int myProgressCount;
//		@Override
//		protected void onPreExecute() {
//			// TODO Auto-generated method stub
//			pd = new ProgressDialog(Connector.context);
//			pd.setMessage("Please Wait ....");
//			pd.setCancelable(false);
//			pd.show();
//			super.onPreExecute();
//		}
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			if(progressbarLoad!=null)
			{
				 progressbarLoad.setProgress(0);
				 progressbarLoad.setVisibility(View.VISIBLE);
			}
			     myProgressCount = 0;
			    
			super.onPreExecute();
		}
		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			
			if(progressbarLoad!=null)
			   progressbarLoad.setProgress(values[0]);
			super.onProgressUpdate(values);
		}
		
		
		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			// Get Church Testimonies
			ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("CID", Connector.getChurchID()));
			param.add(new BasicNameValuePair("reason", "Get Church Testimonies"));
			param.add(new BasicNameValuePair("USER_ID", Connector.getUserId()));
			String response=null;
			response = Connector.sendData(param, Connector.context);
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
			//pd.dismiss();
			//Log.e("ERR", result);
			
			if(!result.equalsIgnoreCase("No Testimony Shared."))
			{
				try {
					JSONObject object = new JSONObject(result);

					if (object != null) {
						JSONArray jarray = object.getJSONArray("ChurchTestimonies");

						if (jarray != null) {
							testimonies.clear();
							for (int i = 0; i < jarray.length(); i++) {
								JSONObject objects = jarray.optJSONObject(i);
								// adapter.add(objects.optString("CN"));
								/*
								 * CONCAT(`first_name`,' ',`last_name`) as
								 * 'N',`userdp` as 'DP', `content` as 'C'
								 * ,SUBSTRING(`content`,1,60) as 'M'
								 */
								String name = objects.optString("N");
								String dp = objects.optString("DP");
								String content = objects.optString("C");
								String summary = objects.optString("M");
								String time = objects.optString("D");
								String likes = objects.optString("L");
								String testimonyID= objects.optString("TID");
								String userlike= objects.optString("Like");
								String senderID= objects.optString("SID");
								System.out.println(content);
								Testimony testimony = new Testimony();
								testimony.setContent(summary);
								testimony.setUserdp(dp);
								testimony.setFullcontent(content);
								testimony.setName(name);
								testimony.setLikes(likes);
								testimony.setMessageTime(time);
								testimony.setTestimonyID(testimonyID);
								testimony.setLikeTestimony(userlike);
								testimony.setSenderid(senderID);
								testimony.setDeviceID(objects.optString("DID",""));
								testimony.setIsConnected(objects.getInt("isConnected")+"");
								testimony.setFriendsCount(objects.getInt("connection")+"");
								testimony.setMutualFriends(objects.optString("mutualFriends",objects.optInt("mutualFriends", 0)+""));
								testimony.setMemberSince(objects.getString("date_joined"));
								testimonies.add(testimony);
							}
						}
					}
					testimonies.notifyDataSetChanged();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else
			{
				Toast.makeText(Connector.context, result, Toast.LENGTH_LONG).show();
			}
			if(progressbarLoad!=null)
			{
			progressbarLoad.setProgress(0);
			progressbarLoad.setVisibility(View.GONE);
			}
			
			

			super.onPostExecute(result);
		}

	}

	@Override
	public void onFragmentInteraction(Uri uri, Integer Code) {
		// TODO Auto-generated method stub
		
	}

}
