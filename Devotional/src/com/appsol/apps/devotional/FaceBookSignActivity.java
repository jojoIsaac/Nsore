package com.appsol.apps.devotional;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.appsol.apps.projectcommunicate.classes.Connector;
import com.appsol.apps.projectcommunicate.classes.Connector.EmailFormatValidator;
import com.appsol.login.Entry.FragmentA;
import com.appsol.login.Entry.FragmentB;
import com.appsol.login.Entry.FragmentC;
import com.appsol.login.FragmentCountries;
import com.appsol.volley.classes.ExtHttpClientStack;
import com.appsol.volley.classes.SslHttpClient;
import com.appsol.volley.classes.Volley;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;

public class FaceBookSignActivity extends ActionBarActivity {
	
	public static GraphUser facebookUser;
	
	
	
	
	
	
	public static int FINISHED_BASIC=98;
	public static UiLifecycleHelper uiHelper;
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		TextView tv;

		@Override
		public Fragment getItem(int pos) {
			// mainlayout.setBackgroundResource(pics[pos]);
			switch (pos) {
			case 0:
				// return PlaceholderFragment.newInstance(0,mainlayout);

			case 1:
				// return SecondFragment.newInstance("welcome",mainlayout);
			case 2:
				// return ThirdFragment.newInstance("",mainlayout);

			default:

				return null;

			}
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:

				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
	}
	
	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			FragmentParent.onSessionStateChange(session, state, exception);
		}
	};
	public static class FragmentParent extends Fragment
	{
		
		public static String firstname;
		public static String lastname;
		public static String othername;
		public static String email;
		public static String facebookID;
		public static void processFacebookJson(String data,String emails)
		{
			try {
				JSONObject object = new JSONObject(data);
				if (object != null) {
					firstname= object.getString("first_name");
					
					lastname= object.getString("last_name");
					
					othername= object.getString("middle_name");
					email= emails;
					facebookID= object.getString("id");
					//firstname= object.getString("first_name");
					
				}
			}catch(Exception e)
			{
				
			}
		}
		public  FragmentManager fmgr= getFragmentManager();
		@Override
		public void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			
		}

		private Session.StatusCallback callback = new Session.StatusCallback() {
			@Override
			public void call(Session session, SessionState state,
					Exception exception) {
				onSessionStateChange(session, state, exception);
			}
		};

		@Override
		public void onResume() {
			super.onResume();
			Session session = Session.getActiveSession();
			if (session != null && (session.isOpened() || session.isClosed())) {
				onSessionStateChange(session, session.getState(), null);
			}
			uiHelper.onResume();
		}

		@Override
		public void onActivityResult(int requestCode, int resultCode,
				Intent data) {
			super.onActivityResult(requestCode, resultCode, data);
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
		
		
		public static void onSessionStateChange(Session session, SessionState state,
				Exception exception) {
			if (state.isOpened()) {
				// Log.i(TAG, "Logged in...");
				// Request user data and show the results
			
				com.facebook.Request.newMeRequest(session, new com.facebook.Request.GraphUserCallback() {

					// callback after Graph API response with user object

					@Override
					public void onCompleted(GraphUser user,
							com.facebook.Response response) {
						// TODO Auto-generated method stub

						if (user != null) {
							//handleFaceBookLogin(user);
							facebookUser = user;
						}

					}
				}).executeAsync();
			} else if (state.isClosed()) {
				// Log.i(TAG, "Logged out...");
			}
		}
	}
	
	
	public class MyAdapter extends FragmentStatePagerAdapter {

		public MyAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int i) {
			Fragment fragment = null;
			if (i == 0) {
				fragment = new FragmentA();
			}
			if (i == 1) {
				fragment = new FragmentB();
			}
			if (i == 2) {
				fragment = new FragmentC();
			}
			return fragment;
		}

		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			if (object instanceof FragmentA) {
				view.setTag(2);
			}
			if (object instanceof FragmentB) {
				view.setTag(1);
			}
			if (object instanceof FragmentC) {
				view.setTag(0);
			}
			return super.isViewFromObject(view, object);
		}
	}
	
	Session session;
	SectionsPagerAdapter mSectionsPagerAdapter;
	MyAdapter myadd;
	public static View signup_view;
	private RelativeLayout mainlayout;
	private ViewPager mViewPager;
	private  int[] pics = { R.drawable.pic1, R.drawable.pic2,
		R.drawable.pic3 };
	private static TelephonyManager tm;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().hide();
		Connector.myPrefs = getSharedPreferences(Connector.prefernceName,
				Context.MODE_PRIVATE);
		Connector.context = this;
		setContentView(R.layout.activity_face_book_sign);
		mainlayout = (RelativeLayout) findViewById(R.id.main_page);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
		myadd = new MyAdapter(getSupportFragmentManager());
		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(myadd);
		mainlayout.setBackgroundResource(pics[0]);
		tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);
	session = Session.getActiveSession();
	
	FragmentParent.processFacebookJson(getIntent().getStringExtra("Data"),getIntent().getStringExtra("email"));
	
	if (session != null && (session.isOpened() || session.isClosed())) {
		FragmentParent.onSessionStateChange(session, session.getState(), null);
		getSupportFragmentManager()
		.beginTransaction()
		.setCustomAnimations(R.anim.abc_slide_in_bottom, 0, 0,
				R.anim.abc_slide_out_top)
		.add(R.id.main_page, new FragmentSignUp())
		.commit();
	}
		
		

	}

	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.face_book_sign, menu);
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
	
	/// Bring in the fragment to control the user opting to link his/her facebook
	public static class FragmentSignUp extends FragmentParent {
		

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			fmgr= getFragmentManager();
			signup_view = inflater.inflate(R.layout.signup_step1, container,
					false);
			Button next = (Button) signup_view.findViewById(R.id.next_button);
			final AutoCompleteTextView fname = (AutoCompleteTextView) signup_view
					.findViewById(R.id.fname);

			final AutoCompleteTextView oname = (AutoCompleteTextView) signup_view
					.findViewById(R.id.oname);

			final AutoCompleteTextView lname = (AutoCompleteTextView) signup_view
					.findViewById(R.id.lname);
			
			if(facebookUser!=null)
			{
			fname.setText(facebookUser.getFirstName());
			oname.setText(facebookUser.getMiddleName());
			lname.setText(facebookUser.getLastName());
			}
			else
			{
				fname.setText(firstname);
				oname.setText(othername);
				lname.setText(lastname);
			}
			
			
			next.setOnClickListener(new View.OnClickListener() {
				

				@Override
				public void onClick(View v) {
					if (fname.getText().toString().length() == 0
							&& lname.getText().toString().length() == 0
							&& oname.getText().toString().length() == 0) {
						Toast.makeText(getActivity(),
								"Please Provide your Name", Toast.LENGTH_LONG)
								.show();

					} else {
						Fragment fragment = new FragmentSignStep2();
						Bundle data = new Bundle();
						data.putString("FN", fname.getText().toString());
						data.putString("ON", oname.getText().toString());
						data.putString("LN", lname.getText().toString());

						fragment.setArguments(data);
						fmgr.beginTransaction()
								.setCustomAnimations(R.animator.slide_in_right,
										0, 0, R.animator.slide_out_left)
								.add(R.id.main_page, fragment)
								.addToBackStack("animation").commit();
					}

				}
			});

			return signup_view;
		}
	}
	
	
	
	static TextView countries_tv;
	static ImageView countries_iv;
	static EditText phone = null;
	static EditText code = null;
	static String ccode = "";
	static String[] resourceList;

	public static void updateCountry(String countryCode) {

		int i = 0;
		int j = 0;
		String[] k = null;
		for (String s : resourceList) {
			i++;
			k = s.split(",");
			if ((k[1]).trim().toLowerCase().equals(countryCode)) {
				j = i - 1;
				break;
			}

		}

	}

	public static class FragmentSignStep2 extends FragmentParent {
		private static String GetCountryZipCode(String ssid) {
			Locale loc = new Locale("", ssid);

			return loc.getDisplayCountry().trim();
		}
		private boolean isEmailValid(String email) {
			// TODO: Replace this with your own logic
			EmailFormatValidator emailv = new EmailFormatValidator();
			return emailv.validate(email);
			// return email.contains("@");
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			fmgr= getFragmentManager();
			signup_view = inflater.inflate(R.layout.signup_step2, container,
					false);

			phone = (EditText) signup_view.findViewById(R.id.phone);
			code = (EditText) signup_view.findViewById(R.id.txtccode);
			final AutoCompleteTextView email = (AutoCompleteTextView) signup_view
					.findViewById(R.id.email);
			
			if(facebookUser!=null)
			{
				email.setText(facebookUser.asMap().get("email").toString());
			}
			code.setKeyListener(null);

			LinearLayout countries_layout = (LinearLayout) signup_view
					.findViewById(R.id.countries_linear_layout);

			countries_tv = (TextView) signup_view
					.findViewById(R.id.txtViewCountryName);
			countries_iv = (ImageView) signup_view
					.findViewById(R.id.imgViewFlag);
			resourceList = getActivity().getResources().getStringArray(
					R.array.CountryCodes);

			String countryCode = tm.getSimCountryIso();
			int i = 0;
			int j = 0;
			String[] k = null;
			for (String s : resourceList) {
				i++;
				k = s.split(",");
				if ((k[1]).trim().toLowerCase().equals(countryCode)) {
					j = i - 1;
					break;
				}

			}
			countries_layout.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// textView.setText("it works");
					Intent intent = new Intent(getActivity(),
							FragmentCountries.class);
					startActivity(intent);

				}
			});
			// textView.setText(k[1]);
			String[] g = resourceList[j].split(",");
			countries_tv.setText(GetCountryZipCode(g[1]).trim() + " (+" + k[0]
					+ ")");
			code.setText("+" + k[0]);
			// / textView.setText(countryCode);
			String pngName = g[1].trim().toLowerCase();
			if (i != 1 && j == 0) {
				countries_tv.setText("Ghana" + " (+" + "233" + ")");
				// textView.setText(countryCode);
				code.setText("+233");
				pngName = "gh";

			}
			countries_iv.setImageResource(getActivity().getResources()
					.getIdentifier("drawable/" + pngName, null,
							getActivity().getPackageName()));
			ccode = k[0];

			Button next = (Button) signup_view.findViewById(R.id.next_button);
			next.setOnClickListener(new View.OnClickListener() {
				//private FragmentManager fmgr;

				@Override
				public void onClick(View v) {

					if (!TextUtils.isEmpty(email.getText().toString())
							&& !TextUtils.isEmpty(phone.getText().toString())) {

						if (isEmailValid(email.getText().toString())) {
							Fragment fragment = new FragmentSignStep3();
							Bundle data = new Bundle();
							data = getArguments();
							data.putString("PN", code.getText().toString()
									+ phone.getText().toString());
							data.putString("EM", email.getText().toString());
							data.putString("code", code.getText().toString());
							fragment.setArguments(data);
							fmgr.beginTransaction()
									.setCustomAnimations(
											R.animator.slide_in_right, 0, 0,
											R.animator.slide_out_left)
									.add(R.id.main_page, fragment)
									.addToBackStack("animation").commit();
						} else {
							Toast.makeText(getActivity(),
									"Invalid Email provided", Toast.LENGTH_LONG)
									.show();
							email.requestFocus();
						}

					} else {
						Toast.makeText(getActivity(),
								"All fields are required", Toast.LENGTH_LONG)
								.show();
					}

				}
			});

			Button prev = (Button) signup_view.findViewById(R.id.prev_button);
			prev.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					getActivity().onBackPressed();
				}
			});
			return signup_view;
		}
	}

	public static class FragmentSignStep3 extends FragmentParent {
		private ProgressDialog pdg;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			signup_view = inflater.inflate(R.layout.signup_step3, container,
					false);
			Button next = (Button) signup_view.findViewById(R.id.next_button);
			final AutoCompleteTextView uname = (AutoCompleteTextView) signup_view
					.findViewById(R.id.uname);
			final AutoCompleteTextView pass = (AutoCompleteTextView) signup_view
					.findViewById(R.id.pass);
			final AutoCompleteTextView confirm_pass = (AutoCompleteTextView) signup_view
					.findViewById(R.id.confirm_pass);
			next.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					if (!TextUtils.isEmpty(uname.getText().toString())
							&& !TextUtils.isEmpty(confirm_pass.getText()
									.toString())
							&& !TextUtils.isEmpty(pass.getText().toString())) {
						if (pass.getText().toString().trim().length() >= 5) {
							if (confirm_pass
									.getText()
									.toString()
									.equalsIgnoreCase(pass.getText().toString())) {

								Bundle data = new Bundle();
								data = getArguments();
								data.putString("PS", pass.getText().toString());
								data.putString("UN", uname.getText().toString());
								// Fragment fragment= new FragmentSignStep4();
								// fragment.setArguments(data);
								saveUserDataOnweb(data);
								// Toast.makeText(getActivity(),
								// data.getString("FN"),
								// Toast.LENGTH_LONG).show();
								// fmgr.beginTransaction().setCustomAnimations(
								// R.animator.slide_in_right,0,0,
								// R.animator.slide_out_left
								// ).add( R.id.main_page, fragment
								// ).addToBackStack("animation").commit();
							} else {
								Toast.makeText(getActivity(),
										"Your Passwords don't Match",
										Toast.LENGTH_LONG).show();
							}
						} else {
							Toast.makeText(getActivity(),
									"The minimum password lenght is 5",
									Toast.LENGTH_LONG).show();
						}

					} else {
						Toast.makeText(getActivity(),
								"All fields are required. ", Toast.LENGTH_LONG)
								.show();
					}

				}
			});
			Button prev = (Button) signup_view.findViewById(R.id.prev_button);
			prev.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					getActivity().onBackPressed();
				}
			});

			return signup_view;
		}

		private void saveUserDataOnweb(final Bundle data) {
			// TODO Auto-generated method stub
			pdg = new ProgressDialog(getActivity());
			pdg.setTitle("Account Creation");
			pdg.setMessage("Please Wait ....");
			pdg.setCancelable(false);
			pdg.show();
			String URL_FEED = "https://nsoredevotional.com/mobile/operations.php";
			StringRequest jsonReq = new StringRequest(Method.POST, URL_FEED,
					createMyReqSuccessListener(data),
					createMyReqErrorListener()) {
				@Override
				protected Map<String, String> getParams() {
					Map<String, String> params = new HashMap<String, String>();
					if(facebookUser!=null)
					{
						params.put("UN", data.getString("UN"));
						params.put("FN", data.getString("FN"));
						params.put("LN", data.getString("LN"));
						params.put("ON", data.getString("ON"));
						params.put("P", data.getString("PN"));
						params.put("E", data.getString("EM"));
						params.put("PSWD", data.getString("PS"));
						params.put("FID",facebookUser.getId());
						params.put("reason", "Create Account with facebook");
					}
					
					// /params.put("index", startIndex + "");

					return params;
				}
			};
			InputStream keyStore = getResources().openRawResource(R.raw.appsol);

			RequestQueue queue = Volley.newRequestQueue(getActivity(),
					new ExtHttpClientStack(
							new SslHttpClient(keyStore, "qwerty")));
			// Adding request to volley request queue
			queue.add(jsonReq);
		}

		private Response.Listener<String> createMyReqSuccessListener(
				final Bundle data) {
			return new Response.Listener<String>() {

				@Override
				public void onResponse(String response) {
					if(!response.trim().equalsIgnoreCase("Invalid"))
					   processJson(response, data);
					else
					{
						
					}
				}
			};
		}

		void processJson(String s, Bundle data) {
			// Log.i("D",s);
			try {
				JSONObject object = new JSONObject(s);

				if (object != null) {

					String status = object.optString("message");
					String id = object.optString("id");

					if (status.equalsIgnoreCase("FAccount Created")
							|| status
									.equalsIgnoreCase("FAccount Already exists")) {
						Connector.regID = id;
						Connector.setUserID(getActivity(), id);
						Connector.contact = data.getString("PS");
						
						
						
						AlertDialog.Builder b = new AlertDialog.Builder(
								getActivity());
						Connector.setRegTime();
						b.setMessage("Account Created")
								.setTitle("Status")
								.setCancelable(false)
								.setPositiveButton("Proceed",
										new DialogInterface.OnClickListener() {

											

											@Override
											public void onClick(
													DialogInterface arg0,
													int arg1) {
												// TODO Auto-generated method
												// stub
												Intent ints = new Intent(
														getActivity(),
														ChooseChurch.class);
												Connector.setfacebookAlreadyLoggedIn(facebookUser.getId());
												startActivityForResult(ints,
														FINISHED_BASIC);
												getActivity().finish();
											}
										}).setCancelable(false).show();
					} else {

					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		private Response.ErrorListener createMyReqErrorListener() {
			return new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					showErrorDialog(error.getMessage());
					Log.d("RES", error.getMessage());
				}
			};
		}

		private void showErrorDialog(String error) {
			Boolean mInError = true;

			AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
			b.setMessage("Error occured: " + error);
			b.show();
		}

		void handleProceed(Bundle data) {

		}
	}


	
	
	
	
}
