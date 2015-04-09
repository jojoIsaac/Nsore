package com.appsol.login;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.http.client.HttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.StringRequest;

import com.appsol.apps.devotional.ChooseChurch;
import com.appsol.apps.devotional.FaceBookSignActivity;

import com.appsol.apps.devotional.MainActivity;
import com.appsol.apps.devotional.R;

import com.appsol.apps.projectcommunicate.classes.ARater;
import com.appsol.apps.projectcommunicate.classes.ConnectionDetector;
import com.appsol.apps.projectcommunicate.classes.Connector;
import com.appsol.apps.projectcommunicate.classes.DownloadImagesTask;
import com.appsol.apps.projectcommunicate.classes.Connector.EmailFormatValidator;
import com.appsol.volley.classes.ExtHttpClientStack;
import com.appsol.volley.classes.SslHttpClient;
import com.appsol.volley.classes.Volley;

import com.facebook.Request;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.ActionBarActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import hotchemi.android.rate.AppRate;
import hotchemi.android.rate.OnClickButtonListener;

public class Entry extends ActionBarActivity {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;
	MyAdapter myadd;
	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ImageView imageView;
	ViewPager mViewPager;
	RelativeLayout mainlayout;
	Button signIn, signUp;
	// private String imagepath;
	static View rootView;
	private static int[] pics = { R.drawable.pic1, R.drawable.pic2,
			R.drawable.pic3 };
	static View signup_view;
	static TelephonyManager tm;
	public static final Integer FINISHED_BASIC = 200;
@Override
protected void onStart() {
	// TODO Auto-generated method stub
	super.onStart();
}
	static void checkInternetConnection(final Context context) {
		boolean isInternet = ConnectionDetector.isConnectingToInternet(context);
		if (!isInternet) {

			AlertDialog.Builder build = new Builder(context);
			build.setTitle("Nsore Devotional")
					.setMessage(
							"Active internet connection is required for the Application to run effectively.\n "
									+ "Please check your internet Connection.")
					.setPositiveButton("Close App",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									((Activity) context).finish();
								}
							})
					.setNegativeButton("Try again",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									Intent ints = new Intent(context,
											Entry.class);
									((Activity) context).startActivity(ints);
									((Activity) context).finish();

								}
							}).setCancelable(false).show();

			// Toast.makeText(this, "Please check your internet Connection.",
			// Toast.LENGTH_LONG).show();
		}
	}

	@Override
	protected void onActivityResult(int requestcode, int resultsCode,
			Intent arg2) {
		// TODO Auto-generated method stub

		if (resultsCode == Activity.RESULT_OK) {
			if (resultsCode == FINISHED_BASIC) {

			}
		}

		super.onActivityResult(requestcode, resultsCode, arg2);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getSupportActionBar().hide();
		Connector.myPrefs = getSharedPreferences(Connector.prefernceName,
				Context.MODE_PRIVATE);
		Connector.context = this;
	
		String filePath = Environment.getExternalStorageDirectory().getPath();

		File file = new File(filePath + "/" + Connector.AppFolder);

		if (!file.exists()) {

			file.mkdirs();
			// Log.d("D",file.getAbsolutePath());
		}
		setContentView(R.layout.activity_entry);
		
		// Perform some diagnoses
		// 1: check if user is logged in
		boolean isLoggedin = Connector.getisLOggedIn();
		String accountID = Connector.getUserId();
		// Toast.makeText(this, accountID, Toast.LENGTH_LONG).show();
		if (isLoggedin && !accountID.trim().equalsIgnoreCase("0")) {

			Intent ints = new Intent(this, MainActivity.class);
			startActivity(ints);
			finish();
		} else if (!isLoggedin && !accountID.trim().equalsIgnoreCase("0")) {
			// btncreateAccount.setVisibility(View.GONE);
			getSupportFragmentManager()
					.beginTransaction()
					.setCustomAnimations(R.anim.abc_slide_in_top, 0, 0,
							R.anim.abc_slide_out_bottom)
					.add(R.id.main_page, new FragmentLogin()).commit();

		} else {
			checkInternetConnection(this);
		}

		// 2: check internet Connection
		// boolean isInternet= ConnectionDetector.isConnectingToInternet(this);
		mainlayout = (RelativeLayout) findViewById(R.id.main_page);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());
		myadd = new MyAdapter(getSupportFragmentManager());
		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(myadd);
		mainlayout.setBackgroundResource(pics[0]);
		tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		imageView = (ImageView) findViewById(R.id.imageView2);
		imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		fadeInAndShowImage(imageView);
		signIn = (Button) findViewById(R.id.btn_sign_in);

		signIn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getSupportFragmentManager()
						.beginTransaction()
						.setCustomAnimations(R.anim.abc_slide_in_top, 0, 0,
								R.anim.abc_slide_out_bottom)
						.add(R.id.main_page, new FragmentLogin())
						.addToBackStack("animation").commit();

			}

		});
		fmgr = getSupportFragmentManager();

		signUp = (Button) findViewById(R.id.btn_sign_up);
		signUp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				getSupportFragmentManager()
						.beginTransaction()
						.setCustomAnimations(R.anim.abc_slide_in_bottom, 0, 0,
								R.anim.abc_slide_out_top)
						.add(R.id.main_page, new FragmentSignUp())
						.addToBackStack("animation").commit();

			}

		});

		signIn.setVisibility(View.GONE);
		signUp.setVisibility(View.GONE);

	}

	private static String GetCountryZipCode(String ssid) {
		Locale loc = new Locale("", ssid);

		return loc.getDisplayCountry().trim();
	}

	class MyAdapter extends FragmentStatePagerAdapter {

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

	private void showsignup() {
		getSupportFragmentManager()
				.beginTransaction()
				.setCustomAnimations(R.anim.abc_slide_in_bottom, 0, 0,
						R.anim.abc_slide_out_top)
				.add(R.id.main_page, new FragmentSignUp())
				.addToBackStack("animation").commit();

	}

	public static class FragmentLogin extends Fragment {
		private String URL_FEED;
		private boolean mInError;
		private String imagepath;
		private UiLifecycleHelper uiHelper;
		public static final String TAG = "LOGIN-TAG";

		public void onSessionStateChange(Session session, SessionState state,
				Exception exception) {
			if (state.isOpened()) {
				// Log.i(TAG, "Logged in...");
				// Request user data and show the results
				Request.newMeRequest(session, new Request.GraphUserCallback() {

					// callback after Graph API response with user object

					@Override
					public void onCompleted(GraphUser user,
							com.facebook.Response response) {
						// TODO Auto-generated method stub

						if (user != null) {
							handleFaceBookLogin(user);
						}

					}
				}).executeAsync();
			} else if (state.isClosed()) {
				// Log.i(TAG, "Logged out...");
			}
		}

		void handleFaceBookLogin(GraphUser user) {
			

			if (user != null)

			{
				
				String facebookUsedAlready= Connector.getFaceaccountusedalready();
				
				if(!TextUtils.isEmpty(facebookUsedAlready) && facebookUsedAlready.equalsIgnoreCase(user.getId()))
				{
					Connector.setisLoggedIn(Connector.context, true);
					Intent ints = new Intent(getActivity(), MainActivity.class);
					startActivity(ints);
					getActivity().finish();
				}
				else
				{
					Intent commentIntent = new Intent(getActivity(), FaceBookSignActivity.class);
					commentIntent.putExtra("Data", user.getInnerJSONObject().toString());
					commentIntent.putExtra("email", user.asMap().get("email").toString());
					startActivity(commentIntent);
					getActivity().finish();
				}
				
				
			
				

			}

		}

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

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			View v = inflater.inflate(R.layout.login_views, container, false);
			final AutoCompleteTextView email = (AutoCompleteTextView) v
					.findViewById(R.id.email);
			final EditText password = (EditText) v.findViewById(R.id.password);
			Button email_sign_in_button = (Button) v
					.findViewById(R.id.email_sign_in_button);
			LoginButton authButton = (LoginButton) v
					.findViewById(R.id.authButton);
			authButton.setReadPermissions(Arrays.asList("email",
					"public_profile"));

			authButton.setFragment(this);
			TextView forgotpassword = (TextView) v
					.findViewById(R.id.forgotpassword);

			email_sign_in_button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					if (!TextUtils.isEmpty(email.getText().toString().trim())
							&& !TextUtils.isEmpty(password.getText().toString()
									.trim()))
						normalUserLogin(email.getText().toString(), password
								.getText().toString());
					else {
						Toast.makeText(getActivity(),
								"All fields are required", Toast.LENGTH_LONG)
								.show();
					}
				}
			});

			checkInternetConnection(getActivity());

			return v;
		}

		private void normalUserLogin(final String UN, final String password) {

			URL_FEED = "https://nsoredevotional.com/mobile/eventsFetch.php";
			StringRequest jsonReq = new StringRequest(Method.POST, URL_FEED,
					createMyReqSuccessListener(), createMyReqErrorListener1()) {
				@Override
				protected Map<String, String> getParams() {
					Map<String, String> params = new HashMap<String, String>();
					params.put("UN", UN);
					params.put("PSWD", password);
					params.put("reason", "Normal Userlogin");
					// /params.put("index", startIndex + "");

					return params;
				}
			};
			InputStream keyStore = getResources().openRawResource(R.raw.appsol);
			RequestQueue queue = Volley.newRequestQueue(getActivity(),new ExtHttpClientStack(
					new SslHttpClient(keyStore, "qwerty"))
					);
			// Adding request to volley request queue
			queue.add(jsonReq);
		}

		private Response.Listener<String> createMyReqSuccessListener() {
			return new Response.Listener<String>() {

				@Override
				public void onResponse(String response) {
					if (!response.equalsIgnoreCase("NOT_SET")) {
						parseJsonFeed(response);
					}
				}
			};
		}

		private Response.ErrorListener createMyReqErrorListener1() {
			return new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					
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

		void downloadChurchLogo(String churchLogo) {
			String filePath = Environment.getExternalStorageDirectory()
					.getPath();

			imagepath = filePath + "/" + Connector.AppFolder + "/"
					+ DownloadImagesTask.churchLogo;

			File file = new File(filePath + "/" + Connector.AppFolder + "/"
					+ churchLogo);
			if (!file.exists() && !file.isFile()) {
				DownloadImagesTask.gotoMain = true;
				Connector.context = getActivity();
				DownloadImagesTask.churchLogo = churchLogo;
				DownloadImagesTask dimage = new DownloadImagesTask();
				dimage.execute(Connector.ChurchLogoURL + churchLogo);

			} else {
				Intent ints = new Intent(getActivity(), MainActivity.class);
				startActivity(ints);
				getActivity().finish();
			}
		}

		protected void parseJsonFeed(String response) {
			// TODO Auto-generated method stub
			try {
				JSONObject object = new JSONObject(response);
				if (object != null) {
					String status = object.optString("status");
					String churchID = object.optString("churchID");
					String DID = object.optString("DID");
					String LID = object.optString("LID");
					String Title = object.optString("T");
					String m = object.optString("M");
					String spinsVal = object.optString("MID");
					String churchname = object.optString("CN");
					String churchLogo = object.optString("CL");
					String BID = object.optString("BID");

					String date = object.optString("date",
							Connector.getdate("yyy-MM-dd"));

					if (status.equalsIgnoreCase("Success")) {
						Connector.setServerDate(date);
						Connector.myPrefs = getActivity().getSharedPreferences(
								Connector.prefernceName, Context.MODE_PRIVATE);
						Connector.message = m;
						Connector.title = Title;
						Connector.myPrefs = Connector.context
								.getSharedPreferences(Connector.prefernceName,
										Context.MODE_PRIVATE);

						Connector.setBranch(BID);

						if (!DID.equalsIgnoreCase("0")
								&& !DID.equalsIgnoreCase("")
								&& (DID.replace(" ", "").length() > 0)) {
							Connector.setDailyGuide(Connector.context, DID);
						} else {
							Connector.setDailyGuide(Connector.context,
									"NO_GUIDE_FOUND");
						}

						if (!LID.equalsIgnoreCase("0")
								&& !LID.equalsIgnoreCase("")
								&& (LID.replace(" ", "").length() > 0)) {
							Connector.setDailyGuideLesson(Connector.context,
									LID);
						} else {
							Connector.setDailyGuideLesson(Connector.context,
									"NO_LESSON");
						}
						Editor edt = Connector.myPrefs.edit();
						Connector.setChurchName(getActivity(), churchname);

						Connector.setIsSplashDone(true);
						edt.putString("DID", DID);
						edt.putString("LID", LID);
						edt.putBoolean("REG_USER", true);
						edt.putString("CHURCH_LOGO", churchLogo);
						edt.putString("CHURCH_ID", churchID);
						// edt.putString("CHURCH_NAME", spinsVal);
						edt.commit();
						downloadChurchLogo(churchLogo);
						Connector.LID = LID;
						Connector.DID = DID;
						Connector.setUserID(Connector.context, spinsVal);
						Connector.setisLoggedIn(Connector.context, true);
					} else {
						Connector.setisLoggedIn(Connector.context, false);
						Toast.makeText(getActivity(),
								"Invalid Email/Phone number or Password",
								Toast.LENGTH_LONG).show();
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	void showActionBar() {

		getSupportActionBar().show();
	}

	static FragmentManager fmgr;

	public static class FragmentSignUp extends Fragment {
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			signup_view = inflater.inflate(R.layout.signup_step1, container,
					false);
			Button next = (Button) signup_view.findViewById(R.id.next_button);
			final AutoCompleteTextView fname = (AutoCompleteTextView) signup_view
					.findViewById(R.id.fname);

			final AutoCompleteTextView oname = (AutoCompleteTextView) signup_view
					.findViewById(R.id.oname);

			final AutoCompleteTextView lname = (AutoCompleteTextView) signup_view
					.findViewById(R.id.lname);
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

	public static class FragmentSignStep2 extends Fragment {
		private boolean isEmailValid(String email) {
			// TODO: Replace this with your own logic
			EmailFormatValidator emailv = new EmailFormatValidator();
			return emailv.validate(email);
			// return email.contains("@");
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			signup_view = inflater.inflate(R.layout.signup_step2, container,
					false);

			phone = (EditText) signup_view.findViewById(R.id.phone);
			code = (EditText) signup_view.findViewById(R.id.txtccode);
			final AutoCompleteTextView email = (AutoCompleteTextView) signup_view
					.findViewById(R.id.email);
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

	public static class FragmentSignStep3 extends Fragment {
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
					params.put("UN", data.getString("UN"));
					params.put("FN", data.getString("FN"));
					params.put("LN", data.getString("LN"));
					params.put("ON", data.getString("ON"));
					params.put("P", data.getString("PN"));
					params.put("E", data.getString("EM"));
					params.put("PSWD", data.getString("PS"));
					params.put("reason", "Create Account");
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
					processJson(response, data);
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

					if (status.equalsIgnoreCase("Account Created")
							|| status
									.equalsIgnoreCase("Account Already exists")) {
						Connector.regID = id;
						Connector.setUserID(getActivity(), id);
						Connector.contact = data.getString("PS");
						AlertDialog.Builder b = new AlertDialog.Builder(
								getActivity());
						Connector.setRegTime();
						b.setMessage("Account Created")
								.setTitle("Status")
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

		void handleProceed(Bundle data) {

		}
	}

	public static class FragmentSignStep4 extends Fragment {
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			signup_view = inflater.inflate(R.layout.signup_step4, container,
					false);
			Button next = (Button) signup_view.findViewById(R.id.next_button);
			next.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					fmgr.beginTransaction()
							.setCustomAnimations(R.animator.slide_in_right, 0,
									0, R.animator.slide_out_left)
							.add(R.id.main_page, new FragmentSignStep5())
							.addToBackStack("animation").commit();

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

	public static class FragmentSignStep5 extends Fragment {
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			signup_view = inflater.inflate(R.layout.signup_step5, container,
					false);
			Button next = (Button) signup_view.findViewById(R.id.next_button);
			next.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO DO SOme Verfification
					// getActivity().dispatchKeyEvent(new
					// KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
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

	public static class FragmentA extends Fragment {
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View v = inflater.inflate(R.layout.welcome, container, false);

			return v;
		}
	}

	public static class FragmentB extends Fragment {
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View v = inflater.inflate(R.layout.slide2, container, false);

			return v;
		}
	}

	public static class FragmentC extends Fragment {
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View v = inflater.inflate(R.layout.slide3, container, false);

			return v;
		}
	}

	private void fadeInAndShowImage(final ImageView img) {
		Animation fadeIn = new AlphaAnimation(0, 1);
		fadeIn.setInterpolator(new AccelerateInterpolator());
		fadeIn.setDuration(2000);
		final Animation swingIn = AnimationUtils.loadAnimation(this,
				R.anim.swing);

		fadeIn.setAnimationListener(new Animation.AnimationListener() {
			public void onAnimationEnd(Animation animation) {
				img.setVisibility(View.VISIBLE);
				slideAnim(signIn, signUp);
				img.startAnimation(swingIn);
			}

			public void onAnimationRepeat(Animation animation) {
			}

			public void onAnimationStart(Animation animation) {
			}
		});

		img.startAnimation(fadeIn);
	}

	private void slideAnim(final Button btn, final Button btn2) {// btn.setEnabled(false);
		btn.setVisibility(View.VISIBLE);
		btn2.setVisibility(View.VISIBLE);
		Animation slideIn = AnimationUtils.loadAnimation(this,
				R.anim.abc_slide_in_bottom);
		slideIn.setDuration(1200);
		slideIn.setAnimationListener(new Animation.AnimationListener() {
			public void onAnimationEnd(Animation animation) {

			}

			public void onAnimationRepeat(Animation animation) {
			}

			public void onAnimationStart(Animation animation) {
			}
		});

		btn.startAnimation(slideIn);
		btn2.startAnimation(slideIn);

	}

	private void slideAnimB(final Button btn) {
		btn.setVisibility(View.VISIBLE);
		Animation slideIn = AnimationUtils.loadAnimation(this,
				R.anim.abc_slide_in_bottom);
		slideIn.setDuration(1200);
		slideIn.setAnimationListener(new Animation.AnimationListener() {
			public void onAnimationEnd(Animation animation) {
				// slideAnimB(signUp);
			}

			public void onAnimationRepeat(Animation animation) {
			}

			public void onAnimationStart(Animation animation) {
			}
		});

		btn.startAnimation(slideIn);

	}

	private void fadeOutAndHideImage(final ImageView img) {
		Animation fadeOut = new AlphaAnimation(1, 0);
		fadeOut.setInterpolator(new AccelerateInterpolator());
		fadeOut.setDuration(2000);

		fadeOut.setAnimationListener(new Animation.AnimationListener() {
			public void onAnimationEnd(Animation animation) {
				img.setVisibility(View.GONE);
			}

			public void onAnimationRepeat(Animation animation) {
			}

			public void onAnimationStart(Animation animation) {
			}
		});

		img.startAnimation(fadeOut);
	}

	public void showLogin() {

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.menu_entry, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		// noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
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

	/**
	 * A placeholder fragment containing a simple view.
	 */

	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		static int pos;
		static LinearLayout mainlayout;

		public static PlaceholderFragment newInstance(int sectionNumber,
				LinearLayout ly) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			pos = sectionNumber;
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			mainlayout = ly;
			return fragment;
		}

		public PlaceholderFragment() {

		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			rootView = inflater.inflate(R.layout.fragment_entry, container,
					false);

			// ImageView view = new ImageView(rootView.getContext());
			// view.setImageResource(pics[pos]);

			// ((ViewPager) container).addView(view, 0);

			return rootView;
		}
	}

}
