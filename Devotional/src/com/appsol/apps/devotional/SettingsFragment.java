package com.appsol.apps.devotional;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.appsol.apps.devotional.FaceBookSignActivity.FragmentParent;
import com.appsol.apps.projectcommunicate.classes.Connector;
import com.appsol.apps.projectcommunicate.model.DBHelper;
import com.appsol.login.Entry;
import com.facebook.Request;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link SettingsFragment.OnFragmentInteractionListener} interface to handle
 * interaction events. Use the {@link SettingsFragment#newInstance} factory
 * method to create an instance of this fragment.
 * 
 */
public class SettingsFragment extends Fragment implements OnFragmentInteractionListener{
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;

	private OnFragmentInteractionListener mListener;
	private String[] navMenuTitles;
	public  UiLifecycleHelper uiHelper;

	public  FragmentManager fmgr= getFragmentManager();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		uiHelper = new UiLifecycleHelper(getActivity(), callback);
		uiHelper.onCreate(savedInstanceState);
		
	}

	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};


	
	

	
	
	
	

	
	
	
	
	
	class myAdapter extends ArrayAdapter<String> 
	{

		public myAdapter(Context context, int resource, String[] objects) {
			super(context, resource, objects);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View row = LayoutInflater.from(getActivity()).inflate(R.layout.settings_item, parent,false);
			
			if(row!=null)
			{
				TextView txt= (TextView) row.findViewById(R.id.title);
				txt.setText(getItem(position));
				if(position %2 ==1){
					//	v.setBackgroundResource((R.drawable.row1)); 
						row.setBackgroundColor(getContext().getResources().getColor(R.color.row1));
					} else {
					//	v.setBackgroundResource(R.drawable.row2); 
						row.setBackgroundColor(getContext().getResources().getColor(R.color.row2));
					}
				
				return row;
			}
			
			return super.getView(position, convertView, parent);
		}
	}

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment SettingsFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static SettingsFragment newInstance(String param1, String param2) {
		SettingsFragment fragment = new SettingsFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}
	void restoreTitle()
	{
		MainActivity.mTitle="Settings";
		getActivity().supportInvalidateOptionsMenu();
		MainActivity.restoreActionBar();
	}
	@Override
	public void onResume() {
		restoreTitle();
	    super.onResume();
	    Session session = Session.getActiveSession();
		if (session != null && (session.isOpened() || session.isClosed())) {
			onSessionStateChange(session, session.getState(), null);
		}
		uiHelper.onResume();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    //uiHelper.onActivityResult(requestCode, resultCode, data);
	    uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onPause() {
	    super.onPause();
	   uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
	    super.onDestroy();
	    uiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	   uiHelper.onSaveInstanceState(outState);
	}
	
	
	public SettingsFragment() {
		// Required empty public constructor
	}

	
	Button btnChurch;
	//private Facebook mFacebook;

	protected void changeChurch() {
		// TODO Auto-generated method stub
		
		AlertDialog.Builder build=  new Builder(getActivity());
		build.setTitle("Join Church")
		.setMessage("After joining another church you will be required to log in. \nDo you want to proceed?")
		.setPositiveButton("Yes", new  DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				ChooseChurch.logoutIfNew=true;
				Intent ints = new Intent(getActivity(), ChooseChurch.class);
				startActivity(ints);
				
			}
		})
		.setNegativeButton("No", new  DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
		})
		.show();
		
		
	}
	//Functions to respond to user clicks
	protected void visitSupportWebSite() {
		// TODO Auto-generated method stub
		Intent ints=  new Intent(Intent.ACTION_VIEW, Uri.parse("http://nsore.appsolinfosystems.com/mobile/apps.php?reason=Support WebSite"));
		startActivity(ints);
	}

	protected void visitSupport() {
		// TODO Auto-generated method stub
		Intent ints=  new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:projects@appsolinfosystems.com?subject=Support Service"));
		startActivity(ints);
	}

	protected void forgotPassowrd() {
		// TODO Auto-generated method stub
		//dialog = new Dialog(getActivity());
		List<String> user = Connector.getUserDetails();
		String email = user.get(2);
		
		if(!email.equalsIgnoreCase("") && email.length()>0)
		{
			RetrivePassword(email);
		}
		else
		{
			final View rootview= getActivity().getLayoutInflater().inflate(R.layout.lost_password,null);
			AlertDialog.Builder build= new Builder(getActivity());
			build.setView(rootview)
			.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
	EditText edtemail = (EditText) rootview.findViewById(R.id.edtemail);
	RetrivePassword(edtemail.getText().toString());
			}
			}
			)
			.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					
				}
			})
			.show();
		}
		
		
		//dialog.setContentView(rootview);
	
		
	}

	protected void RetrivePassword(final String string) {
		// TODO Auto-generated method stub
		AsyncTask<Void, Integer, String> emailpassword = new AsyncTask<Void, Integer, String>() {
			
			ProgressDialog pdg;
		      @Override
		    protected void onPreExecute() {
		    	// TODO Auto-generated method stub
		    	  pdg= new ProgressDialog(getActivity());
		    	  pdg.setMessage("Please wait ...");
		    	  pdg.setCancelable(false);
		    	  pdg.show();
		    	  
		    	super.onPreExecute();
		    }
			
			@Override
			protected String doInBackground(Void... arg0) {
				ArrayList<NameValuePair> param= new ArrayList<NameValuePair>();
				param.add( new BasicNameValuePair("reason","Resend Password"));
				param.add( new BasicNameValuePair("USER_ID",Connector.getUserId()));
				param.add( new BasicNameValuePair("email",string));
				//List<String> user = Connector.getUserDetails();
				//txt_email.setText(user.get(2));
				String response= Connector.sendData(param, getActivity());
				
					return response;
			}
			
			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
			
				pdg.dismiss();
				try {
				
					Toast.makeText(getActivity(), "Please an email has been sent to you. \n Visit it for further instructions", 
							 Toast.LENGTH_LONG).show();
				if(result.indexOf("Resetting Your Nsore Devotion Password")>-1)
				{
					Toast.makeText(getActivity(), "Please an email has been sent to you. \n Visit it for further instructions", 
							 Toast.LENGTH_LONG).show();
				}
				
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				super.onPostExecute(result);
			}
			
		};
		
		emailpassword.execute();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		restoreTitle()
		;
		navMenuTitles = getResources().getStringArray(R.array.settings_page_menus);
		getActivity().setTitle("Settings");
		View rootview= inflater.inflate(R.layout.fragment_settings, container, false);
		ListView lstItems= (ListView) rootview.findViewById(R.id.lstSettings);
		myAdapter mm= new myAdapter(getActivity(), android.R.layout.simple_list_item_1, navMenuTitles);
		UserDetailsFragment.isNew=false;
		lstItems.setAdapter(mm);
		lstItems.setOnItemClickListener( new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub'
				//FragmentManager fmanager= getActivity().getSupportFragmentManager();
			switch (position) {
			case 0:
				Intent ints1= new Intent(getActivity(), SubscriptionActivity.class);
				startActivity(ints1);
				break;
case 1:
	changeChurch();
				break;
				
case 2:
	
		break;
case 3:
	changePassword();
	break;
case 4:
	forgotPassowrd();
	break;


case 5: 
	Intent ints= new Intent(getActivity(), ExpandableActivity.class);
	startActivity(ints);
break;
case 6:
	showAboutPage();
	break;
case 7:
	ClearAllData();
		break;
			default:
				break;
			}
				getActivity().supportInvalidateOptionsMenu();
			}
		});
		btnChurch=(Button) rootview.findViewById(R.id.btnChurch);
		btnChurch.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				
				AlertDialog.Builder build=  new Builder(getActivity());
				build.setTitle("Log Out")
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setMessage("Are you sure?")
				.setPositiveButton("Yes", new  DialogInterface.OnClickListener() {
					
					private Session session;

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						// TODO Auto-generated method stub
						
						session = Session.getActiveSession();
						if (session != null && (session.isOpened() || session.isClosed())) {
							onSessionStateChange(session, session.getState(), null);
						}
						
						 Connector.setisLoggedIn(Connector.context, false);
										Intent ints= new Intent(getActivity(), Entry.class);
										startActivity(ints);
										getActivity().finish();
					}
				})
				.setNegativeButton("No", new  DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						
					}
				})
				.show();

				
				
				
			}
		});

		return rootview;
	}
	
	public static void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
		if (state.isOpened()) {
			
		session.closeAndClearTokenInformation();
		} else if (state.isClosed()) {
			
		}
	}
	
	protected void showAboutPage() {
		Intent ints= new Intent(getActivity(), AboutActivity.class);
		startActivity(ints);
	}

	protected void showHelppage() {
		// TODO Auto-generated method stub

	}
	private static String password="";
	
	protected void changePassword() {
		// TODO Auto-generated method stub
		dialog= new Dialog(getActivity());
		LayoutInflater layinflate= getActivity().getLayoutInflater();
		View rootView= layinflate.inflate(R.layout.chanagepassword,null);
		dialog.setContentView(rootView);
		dialog.setTitle("Change Password");
		final EditText txtcurpassword= (EditText) rootView.findViewById(R.id.txtcurpassword);
		final EditText txtpassword= (EditText) rootView.findViewById(R.id.txtpassword);
		final EditText txtCpassword= (EditText) rootView.findViewById(R.id.txtCpassword);
		final Button btnchangepass =(Button) rootView.findViewById(R.id.btnchangepass);
		
		btnchangepass.setOnClickListener( new OnClickListener() {
			
			

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String message="";
				if(txtcurpassword.getText().toString().length()==0)
				{
					message="Please provide your current password";
					
				}
				
				
				else if(txtpassword.getText().toString().length()< 5)
				{
					message="Please the minimum Password length is 5";
				}
				
				else if(txtCpassword.getText().toString().length()< 5)
				{
					message="Please the minimum Password length is 5";
				}
				

				else if(!txtCpassword.getText().toString().equalsIgnoreCase(txtpassword.getText().toString()))
				{
					message="Please the new passwords don\'t match";
				}
				
				
				if(message.replace(" ", "").length()==0)
				{
					AlertDialog.Builder b= new Builder(getActivity());
					password= txtCpassword.getText().toString();
					(new changePAssword()).execute();
				}
				else
				{
					Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
				}
			}
		});
		Button btncancel =(Button) rootView.findViewById(R.id.btncancel);
		btncancel.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		dialog.show();
		
	}
	
	
	protected void ClearAllData() {
		// TODO Auto-generated method stub
		
		AlertDialog.Builder build=  new Builder(getActivity());
		build.setTitle("Clear Saved Data")
		.setIcon(android.R.drawable.ic_dialog_alert)
		.setMessage("All Bookmarks and notes will be lost. Do you want to proceed?")
		.setPositiveButton("Yes", new  DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
			Connector.dbhelper = new DBHelper(getActivity());
			Connector.dbhelper.deleteAlldata();
			Connector.dbhelper.close();
			Toast.makeText(getActivity(), "All Bookmarks and notes deleted", Toast.LENGTH_LONG).show();
			}
		})
		.setNegativeButton("No", new  DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
		})
		.show();
	}

	
	
	
Dialog dialog;
//LoginButton LoginButton;
Button btn_twitter;
private String TAG="FACEBOOK";
	protected void showSocialAppsChoose() {
		// TODO Auto-generated method stub
		dialog= new Dialog(getActivity());
		LayoutInflater layinflate= getActivity().getLayoutInflater();
		//View rootView= layinflate.inflate(R.layout.connect_friends,null);
		//dialog.setContentView(rootView);
		dialog.setTitle("Choose Social Network");
//		LoginButton=(LoginButton) rootView.findViewById(R.id.authButton);
//		LoginButton.setFragment(this);
		//LoginButton.setReadPermissions(Arrays.asList("user_likes", "user_status"));
//		btn_twitter=(Button) rootView.findViewById(R.id.btn_twitter);
//		 
//		btn_twitter.setOnClickListener( new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				Session.NewPermissionsRequest newPermissionsRequest = new Session
//					      .NewPermissionsRequest(getActivity(), Arrays.asList("publish_actions"));
//					    session.requestNewPublishPermissions(newPermissionsRequest);
//			}
//		});
		
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
	

	
	
	@Override
	public void onFragmentInteraction(Uri uri) {
		// TODO Auto-generated method stub
		
	}

	

	@Override
	public void onFragmentInteraction(Uri uri, Integer Code) {
		// TODO Auto-generated method stub
		
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
	
	
}
