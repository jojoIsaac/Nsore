package com.appsol.apps.devotional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.appsol.apps.projectcommunicate.classes.Connector;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link TestimonyDetailsFragment.OnFragmentInteractionListener} interface to
 * handle interaction events. Use the
 * {@link TestimonyDetailsFragment#newInstance} factory method to create an
 * instance of this fragment.
 * 
 */
public class TestimonyDetailsFragment extends Fragment  {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;
	TextView mTxtEmojicon;
	public static EditText mEditEmojicon;
	ImageView img;
	private OnFragmentInteractionListener mListener;
	private View rootView;

	
	
	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment TestimonyDetailsFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static TestimonyDetailsFragment newInstance(String param1,
			String param2) {
		TestimonyDetailsFragment fragment = new TestimonyDetailsFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}
	void restoreTitle()
	{
		MainActivity.mTitle="Testimony";
		getActivity().supportInvalidateOptionsMenu();
		MainActivity.restoreActionBar();
	}
	public TestimonyDetailsFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}
		restoreTitle();
	}
TextView name,content;
ImageView imgdp;
private DisplayImageOptions options;
private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

private static class AnimateFirstDisplayListener extends
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



Button likeTestimony,shareTestimony;
static String testimonyID;
static String deviceID="";
protected ImageLoader imageLoader = ImageLoader.getInstance();
private View frags;
private ImageView imgCamera,imgSpeak,imgText;
String like="0";
private TextWatcher textWatch;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		restoreTitle();
		
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.mobile)
		.showImageForEmptyUri(R.drawable.mobile)
		.showImageOnFail(R.drawable.mobile).cacheInMemory(true)
		.cacheOnDisk(true).considerExifParams(true)
		.displayer(new RoundedBitmapDisplayer(6)).build();
				rootView=inflater.inflate(R.layout.testimontydetails, container,
				false);
				name= (TextView) rootView.findViewById(R.id.txttitle);
				content=(TextView) rootView.findViewById(R.id.txttestimonycontent);
				imgdp=(ImageView) rootView.findViewById(R.id.thumbImage);
				
				Bundle data= getArguments();
				
				if(data!=null)
				{
					String dp= data.getString("DP");
					String url = Connector.imageURL + dp;
					imageLoader.displayImage(url, imgdp, options,
							animateFirstListener);
					name.setText(data.getString("N"));
					content.setText(data.getString("C"));
					testimonyID = data.getString("ID");
					deviceID=data.getString("DID");
					getActivity().setTitle(name.getText()+"'s Testimony");
					like = data.getString("L");
				}
				else
				{
					
				}
				 
				likeTestimony=(Button) rootView.findViewById(R.id.likeTestimony);
				shareTestimony=(Button) rootView.findViewById(R.id.commentTestimony);
				if(!like.equalsIgnoreCase("0"))
				{
					likeTestimony.setVisibility(View.GONE);
					
				}
				
				shareTestimony.setOnClickListener( new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
						Intent ints= new Intent(Intent.ACTION_SEND);
						ints.putExtra(Intent.EXTRA_TEXT, name.getText().toString()+
								"'s Testimony  "+"\n "+content.getText().toString()+"\n Shared from: "+Connector.parentURL);
						ints.setType("text/plain");
						startActivity(ints);
						
					}
				});
				
				
				
				
				likeTestimony.setOnClickListener( new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						(new likeTestimony()).execute();
						// TODO Auto-generated method stub
//						AlertDialog.Builder build= new Builder(getActivity());
//						
//						build.setMessage("Are you sure")
//						.setTitle("Like Testimony")
//						
//						.setPositiveButton("Yes",  new DialogInterface.OnClickListener() {
//							
//							@Override
//							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
								
//							}
//						})
//						
//						.setNegativeButton("No",  new DialogInterface.OnClickListener() {
//							
//							@Override
//							public void onClick(DialogInterface arg0, int arg1) {
//								// TODO Auto-generated method stub
//								
//							}
//						})
//						.setIcon(android.R.drawable.ic_menu_info_details)
//
//						.show();
//						
					}
				});
				return rootView;
	}
	private boolean exitWindow=false;
	private static int toggleEcons=-1;
	

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
@Override
public void onResume() {
	// TODO Auto-generated method stub
	restoreTitle();
	super.onResume();
}
	
	private static  class likeTestimony extends AsyncTask<Void, String, String> {
		ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			pd = new ProgressDialog(Connector.context);
			pd.setMessage("Please Wait ....");
			pd.setCancelable(false);
			pd.show();
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			pd.dismiss();
			System.out.println(result);
			super.onPostExecute(result);
		}
		
		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			// Get Church Testimonies
			ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("TID", testimonyID));
			param.add(new BasicNameValuePair("reason", "Like Testimony"));
			param.add(new BasicNameValuePair("USER_ID", Connector.getUserId()));
			param.add(new BasicNameValuePair("UN", Connector.getUserDetails().get(1)));
			param.add(new BasicNameValuePair("DID",deviceID ));
			String response = Connector.sendData(param, Connector.context);

			return response;
		}
	}
	
	
}
