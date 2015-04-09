package com.appsol.apps.devotional;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.appsol.apps.projectcommunicate.classes.CircularNetworkImageView;
import com.appsol.apps.projectcommunicate.classes.ConnectionDetector;
import com.appsol.apps.projectcommunicate.classes.Connector;
import com.appsol.apps.projectcommunicate.classes.CropSquareTransformation;
import com.appsol.login.Entry;
import com.appsol.volley.classes.ExtHttpClientStack;
import com.appsol.volley.classes.SslHttpClient;
import com.appsol.volley.classes.Volley;
//import com.captechconsulting.captechbuzz.model.images.ImageCacheManager;
import com.squareup.picasso.Picasso;
//import android.graphics.pdf.PdfDocument;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link UserDetailsFragment.OnFragmentInteractionListener} interface to handle
 * interaction events. Use the {@link UserDetailsFragment#newInstance} factory
 * method to create an instance of this fragment.
 * 
 */
public class UserDetailsFragment extends Fragment implements OnFragmentInteractionListener{
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";
	static CircularNetworkImageView imgprofile;
	private static String filePath;
	public static Integer  RESULTS_CODE=1000;

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;
   public static TextView txt_name,txt_email,txt_username,txtphone,txttime;
   public static Button btneditprofile,btnchangepassword;
   static boolean isNew=false;
//   public static isFec
	private OnFragmentInteractionListener mListener;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment UserDetailsFragment.
	 */
	 // TODO: Rename and change types and number of parameters
	
	
	static ImageLoader imageLoader = com.appsol.volley.classes.ImageCacheManager.getInstance().getImageLoader();
	
	public static void reloadImag()
	{
	String imagename=Connector.getUserdp();
		
		File files = new File(filePath+ "/"+Connector.AppFolder+"/userDP/"+imagename);
		if(files.exists() && files.isFile())
		{
		
//			Picasso.with(Connector.context)
//			  .load(files.getAbsoluteFile())
//			  .resize(250, 250)
//			  .centerCrop()
//			  .transform(new CropSquareTransformation())
//			  .into(imgprofile);
			
			//imgprofile.setImageUrl(filePath+ "/"+Connector.AppFolder+"/userDP/"+imagename, imageLoader);
			imgprofile.setImageBitmap(BitmapFactory.decodeFile(filePath+ "/"+Connector.AppFolder+"/userDP/"+imagename));

		}
	}
	
	
	public static UserDetailsFragment newInstance(String param1, String param2) {
		UserDetailsFragment fragment = new UserDetailsFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}
@Override
public void onResume() {
	// TODO Auto-generated method stub
	loadpage() ;
	reloadImag();
	super.onResume();
}
	public UserDetailsFragment() {
		// Required empty public constructor
	}
	void restoreTitle()
	{
		MainActivity.mTitle="Profile";
		getActivity().supportInvalidateOptionsMenu();
		MainActivity.restoreActionBar();
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}
		restoreTitle();
		//setHasOptionsMenu(true);
	}
	ViewGroup vg;
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		//inflater.inflate(R.menu.userprofilemenu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if(item.getItemId()==R.id.mn_edit)
		{
			
		}
		return super.onOptionsItemSelected(item);
	}
	TextView txt_events,txt_requests;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		restoreTitle();
		vg= container;
		View rootView =inflater.inflate(R.layout.fragment_user_details, container,
				false);
		//getActivity().setTitle("Profile");
		imgprofile = (CircularNetworkImageView) rootView.findViewById(R.id.imgprofile);
		
		/*
		 * filePath= Environment.getExternalStorageDirectory().getPath();
		String imagename="";
		
		File files = new File(filePath+ "/"+Connector.AppFolder+"/userDP/");
		 */
		filePath= Environment.getExternalStorageDirectory().getPath();
		final String imagename=Connector.getUserdp();
		
		File files = new File(filePath+ "/"+Connector.AppFolder+"/userDP/"+imagename);
		if(files.exists() && files.isFile())
		{
		
//			Picasso.with(getActivity())
//			  .load(files.getAbsoluteFile())
//			  .resize(250, 250)
//			  .centerCrop()
//			  .transform(new CropSquareTransformation())
//			  .into(imgprofile);
			
			//imgprofile.setImageUrl(filePath+ "/"+Connector.AppFolder+"/userDP/"+imagename, imageLoader);	
			imgprofile.setImageBitmap(BitmapFactory.decodeFile(filePath+ "/"+Connector.AppFolder+"/userDP/"+imagename));

		}
		else
		{
			
			if(ConnectionDetector.isConnectingToInternet(getActivity()))
			{
				
				AsyncTask<Void, Integer, Bitmap> loadImage= new AsyncTask<Void, Integer, Bitmap>(){

					@Override
					protected Bitmap doInBackground(Void... params) {
						// TODO Auto-generated method stub
						Bitmap data= Connector.download_Image(Connector.imageURL+imagename,
								
								
								filePath+ "/"+Connector.AppFolder+"/userDP/"+imagename);
						return data;
					}
					
					@Override
					protected void onPostExecute(Bitmap result) {
						// TODO Auto-generated method stub
						if(result!=null)
						{
//							  Picasso.with(getActivity())
//							  .load(filePath+ "/"+Connector.AppFolder+"/userDP/"+imagename)
//							  .resize(250, 250)
//							  .centerCrop()
//							  .transform(new CropSquareTransformation())
//							  .error(R.drawable.mobile)
//							  .into(imgprofile);
							imgprofile.setImageBitmap(BitmapFactory.decodeFile(filePath+ "/"+Connector.AppFolder+"/userDP/"+imagename));

							  //imgprofile.setImageUrl(filePath+ "/"+Connector.AppFolder+"/userDP/"+imagename, imageLoader);
						}
						super.onPostExecute(result);
					}
					
				};
				loadImage.execute();

			}
			
		
		}
		imgprofile.setOnClickListener( new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				// TODO Auto-generated method stub
				Intent ints = new Intent(getActivity(), UserprofileImageActivity.class);
				startActivityForResult(ints, RESULTS_CODE);
			}
		});
		txt_name=(TextView)rootView.findViewById(R.id.txt_name);
		txt_email=(TextView) rootView.findViewById(R.id.txt_email);
		txtphone=(TextView) rootView.findViewById(R.id.txt_phone);
		txt_username=(TextView) rootView.findViewById(R.id.txt_username);
		txt_requests=(TextView) rootView.findViewById(R.id.txt_requests);
		//txttime
//		txttime=(TextView) rootView.findViewById(R.id.txttime);
//		txttime.setText(Connector.getRegTime());
		
		List<String> user = Connector.getUserDetails();
		txt_email.setText(user.get(2));
		txt_username.setText(user.get(1));
		txtphone.setText(user.get(3));
		txt_name.setText(user.get(0));
		txt_events= (TextView) rootView.findViewById(R.id.txt_events);
		txt_requests=(TextView) rootView.findViewById(R.id.txt_requests);
		txt_requests.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String value= txt_requests.getText().toString();
				
				if(!value.equalsIgnoreCase(" Requests (0)") && !value.equalsIgnoreCase(" Requests")  )
				{
				 Intent ints = new Intent(getActivity(), MyRequests.class);
				 ints.putExtra("Title", txt_requests.getText());
				 startActivity(ints);
				}
			}
		});
		txt_events.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 Intent ints = new Intent(getActivity(), MyFriends.class);
				 ints.putExtra("Title", txt_events.getText());
				 startActivity(ints);
			}
		});
		
		
		// Inflate the layout for this fragment
//		if(!isNew)
//		{
//		 ( new fetchDetails()).execute();
//		}
//		else
//		{
			
			txt_email.setText(user.get(2));
			txt_username.setText(user.get(1));
			txtphone.setText(user.get(3));
			txt_name.setText(user.get(0));
		//}
		txt_username.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				changeProfile();
			}
		});

		return rootView;
	}

	protected void loadEvents() {
		// TODO Auto-generated method stub
		Intent ints = new Intent(getActivity(), MyEventsActivity.class);
		startActivity(ints);
	}
	Dialog dialog;
	String password="";
	EditText txtname,txt_phone,txtemail,txtusername;
	private boolean mInError;
	void changeProfile()
	{
		dialog= new Dialog(getActivity());
		LayoutInflater layinflate= getActivity().getLayoutInflater();
		View rootView= layinflate.inflate(R.layout.usernamechange,null);
		dialog.setContentView(rootView);
	dialog.setTitle("Change Display/Username");
		
		txtusername= (EditText) rootView.findViewById(R.id.txtusername);
		List<String> user = Connector.getUserDetails();
		
		txtusername.setText(user.get(1));
		final Button btnchangepass =(Button) rootView.findViewById(R.id.btnregister);
		
		btnchangepass.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String message="";
				 if(txtusername.getText().toString().length()==0)
				{
					message="Please Provide your user/display name";
				}
				
				
				if(message.replace(" ", "").length()==0)
				{
					AlertDialog.Builder b= new Builder(getActivity());
					
					(new changeProfile()).execute();
				}
				else
				{
					Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
				}
			}
		});

		dialog.show();
		
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
	class fetchDetails extends AsyncTask<Void, Integer, String>
	{
		ProgressDialog pdg;
      @Override
    protected void onPreExecute() {
    	// TODO Auto-generated method stub
    	  pdg= new ProgressDialog(getActivity());
    	  pdg.setMessage("Please wait ...");
    	  pdg.show();
    	  
    	super.onPreExecute();
    }
		
		@Override
		protected String doInBackground(Void... arg0) {
		ArrayList<NameValuePair> param= new ArrayList<NameValuePair>();
		param.add( new BasicNameValuePair("reason","Get User Details"));
		param.add( new BasicNameValuePair("USER_ID",Connector.getUserId()));
		String response= Connector.sendData(param, getActivity());
		
			return response;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			//Toast.makeText(getActivity(), Connector.getUserId(), Toast.LENGTH_LONG).show();
			pdg.dismiss();
			processJson(result);
			super.onPostExecute(result);
		}
	}
	@Override
	public void onFragmentInteraction(Uri uri) {
		// TODO Auto-generated method stub
		
	}

	public void processJson(String result) {
		// TODO Auto-generated method stub
		//Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
		try {
			JSONObject object = new JSONObject(result);
			if(object!=null)
			{
				
			
			
			txt_email.setText(object.optString("email",""));
			txt_username.setText(object.optString("username",""));
			txtphone.setText(object.optString("phone_number",""));
			String name=object.optString("first_name","") +" "+ object.optString("last_name","") +" "+ object.optString("othernames","");
			txt_name.setText(name);
			String fcount= object.optString("FCount", "0");
			txt_events.setText(" Friends ("+fcount+")");
			String rcount=object.optString("RCount", "0");
			txt_requests.setText(" Requests ("+rcount+")");
			Connector.setUserInfo(name, object.optString("email",""), object.optString("phone_number",""), object.optString("username",""));
			String dp=object.optString("userdp","");
			if(!dp.equalsIgnoreCase(""))
			{
				Connector.setUserdp(dp);
			}
			
			
			isNew=true;
			}
			//Connector.setUserInfo(name, email, phone_number, username);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void loadpage() {
		
			//String URL_FEED = Connector.URL ;
			StringRequest jsonReq = new StringRequest(Method.POST, Connector.URL,
					createMyReqSuccessListener(), createMyReqErrorListener()) {
				@Override
				protected Map<String, String> getParams() {
					Map<String, String> params = new HashMap<String, String>();
					params.put("CID", Connector.getChurchID());
					params.put("USER_ID",Connector.getUserId());
					params.put("UID", Connector.getUserId());
					
					
						params.put("reason","Get User Details");
//						param.add( new BasicNameValuePair("reason","Get User Details"));
						//param.add( new BasicNameValuePair("USER_ID",Connector.getUserId()));
					
					//}
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
					//Toast.makeText(MyFriends.this, response, Toast.LENGTH_LONG).show();
					processJson(response);
				}
			}
		};
	}

	private Response.ErrorListener createMyReqErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				//progressbarLoad.setVisibility(View.GONE);
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
class changeProfile extends AsyncTask<Void, Integer, String>
{
	ProgressDialog pdg;
      @Override
    protected void onPreExecute() {
    	// TODO Auto-generated method stub
    	  pdg= new ProgressDialog(getActivity());
    	  pdg.setMessage("Please wait ...");
    	  pdg.show();
    	  
    	super.onPreExecute();
    }
		
		@Override
		protected String doInBackground(Void... arg0) {
		ArrayList<NameValuePair> parameter= new ArrayList<NameValuePair>();
//		parameter.add(new BasicNameValuePair("N",txtname.getText().toString()));
//		parameter.add(new BasicNameValuePair("P",txt_phone.getText().toString()));
		
		parameter.add(new BasicNameValuePair("UN",txtusername.getText().toString()));
		//parameter.add(new BasicNameValuePair("E",txtemail.getText().toString()));
		parameter.add( new BasicNameValuePair("reason","Update Username"));
		parameter.add( new BasicNameValuePair("USER_ID",Connector.getUserId()));
		String response= Connector.sendData(parameter, getActivity());
		
			return response;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			pdg.dismiss();
			processJsonProfileChange(result);
			super.onPostExecute(result);
		}
}
public void processJsonProfileChange(String result) {
	// TODO Auto-generated method stub
	try {
		JSONObject object = new JSONObject(result);
		if(object!=null)
		{
			String status = object.getString("status");
			if(status.equalsIgnoreCase("Completed"))
			{
				Toast.makeText(getActivity(), "Operation successful",Toast.LENGTH_LONG).show();
				//Connector.setUserInfo( txtusername.getText().toString());
				Connector.setUserName(txtusername.getText().toString());
				List<String> user = Connector.getUserDetails();
				txt_email.setText(user.get(2));
				txt_username.setText(user.get(1));
				txtphone.setText(user.get(3));
				txt_name.setText(user.get(0));
				dialog.dismiss();
			}
			else
			{
				Toast.makeText(getActivity(), "Sorry Operation Failed",Toast.LENGTH_LONG).show();
			}
			
		}
	}
		catch (Exception e) {
			// TODO: handle exception
		}
		

}




	class changePAssword extends AsyncTask<Void, Integer, String>
	{
		ProgressDialog pdg;
	      @Override
	    protected void onPreExecute() {
	    	// TODO Auto-generated method stub
	    	  pdg= new ProgressDialog(getActivity());
	    	  pdg.setMessage("Please wait ...");
	    	  pdg.show();
	    	  
	    	super.onPreExecute();
	    }
			
			@Override
			protected String doInBackground(Void... arg0) {
			ArrayList<NameValuePair> param= new ArrayList<NameValuePair>();
			param.add( new BasicNameValuePair("PSWD",password));
			param.add( new BasicNameValuePair("reason","Change Password"));
			param.add( new BasicNameValuePair("USER_ID",Connector.getUserId()));
			String response= Connector.sendData(param, getActivity());
			
				return response;
			}
			
			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				pdg.dismiss();
				processJsonPasswordChange(result);
				super.onPostExecute(result);
			}
	}
	public void processJsonPasswordChange(String result) {
		// TODO Auto-generated method stub
		try {
			JSONObject object = new JSONObject(result);
			if(object!=null)
			{
				String status = object.getString("status");
				if(status.equalsIgnoreCase("Completed"))
				{
					Toast.makeText(getActivity(), "Operation successful",Toast.LENGTH_LONG).show();
					Connector.setisLoggedIn(Connector.context, false);
					Intent ints= new Intent(getActivity(), Entry.class);
					startActivity(ints);
					getActivity().finish();
				}
				else
				{
					Toast.makeText(getActivity(), "Sorry Operation Failed",Toast.LENGTH_LONG).show();
				}
				
			}
		}
			catch (Exception e) {
				// TODO: handle exception
			}
			
	
	}


	@Override
	public void onFragmentInteraction(Uri uri, Integer Code) {
		// TODO Auto-generated method stub
		
	}
	
	
}
